package middle;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Throwables;
import domain.*;
import enums.SkuTypeEnum;
import org.springframework.beans.BeanUtils;
import play.Logger;
import play.libs.Json;
import service.CartService;
import service.IdService;
import service.PromotionService;
import service.ThemeService;
import util.GenCouponCode;
import util.SysParCom;

import javax.inject.Inject;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 中间事务处理层
 * Created by howen on 16/1/25.
 */
public class DetailMid {

    @Inject
    private CartService cartService;
    @Inject
    private PromotionService promotionService;
    @Inject
    private ThemeService themeService;

    @Inject
    private IdService idService;


    //将Json串转换成List
    public final static ObjectMapper mapper = new ObjectMapper();

    /**
     * 获取商品详情
     *
     * @param skuType   类型
     * @param itemId    itemId
     * @param skuTypeId skuTypeId
     * @param userId    userId
     * @return map
     */
    public Map<String, Object> getDetail(String skuType, Long skuTypeId, Long itemId, Long userId) {

        Map<String, Object> map = new HashMap<>();
        try {
            map.put("main", getItem(itemId));
            map.put("stock", getStock(skuType, itemId, skuTypeId, userId));
            map.put("push", getPushSku(skuType));
            Map<String, Object> commentMap = getComment(itemId, skuType, skuTypeId);
            if (commentMap != null) map.put("comment", commentMap);
            return map;
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.error("getItemDetail:" + Throwables.getStackTraceAsString(ex));
            return null;
        }
    }

    private Map<String, Object> getComment(Long itemId, String skuType, Long skuTypeId) {
        Map<String, Object> map = new HashMap<>();

        if (skuType.equals("item")) {
            Inventory inventory = new Inventory();
            inventory.setItemId(itemId);
            List<Inventory> inventoryList = themeService.getInvBy(inventory);


            final Integer[] fullGradeCount = {0};
            final Integer[] invAllRemarkCount = {0};


            for (Inventory inventory1 : inventoryList) {
                Remark remark = new Remark();
                remark.setSkuType("item");
                remark.setSkuTypeId(inventory1.getId());
                List<Remark> remarkList = cartService.selectRemark(remark);
                if (remarkList != null && remarkList.size() > 0) {
                    invAllRemarkCount[0] += remarkList.size();
                    remarkList.forEach(re -> {
                        if (re.getGrade() != 1 && re.getGrade() != 2) {
                            fullGradeCount[0] += 1;
                        }
                    });
                }
            }

            if (invAllRemarkCount[0] != 0) {
                NumberFormat numberFormat = NumberFormat.getInstance();
                numberFormat.setMaximumFractionDigits(2);
                String result = numberFormat.format(fullGradeCount[0].floatValue() / invAllRemarkCount[0].floatValue() * 100);
                map.put("remarkCount", invAllRemarkCount[0]);
                map.put("remarkRate", result);
                return map;
            } else return null;

        } else {
            Remark remark = new Remark();
            remark.setSkuType(skuType);
            remark.setSkuTypeId(skuTypeId);
            List<Remark> remarkList = cartService.selectRemark(remark);
            if (remarkList != null && remarkList.size() > 0) {
                map.put("remarkCount", remarkList.size());
                final Integer[] fullGradeCount = {0};

                remarkList.forEach(re -> {
                    if (re.getGrade() != 1 && re.getGrade() != 2) {
                        fullGradeCount[0] = +1;
                    }
                });
                NumberFormat numberFormat = NumberFormat.getInstance();
                numberFormat.setMaximumFractionDigits(2);
                String result = numberFormat.format(fullGradeCount[0].floatValue() / (float) remarkList.size() * 100);
                map.put("remarkRate", result);
                return map;
            } else return null;
        }
    }

    /**
     * 获取商品信息
     *
     * @param itemId 商品ID
     * @return item
     * @throws IOException
     */
    private Item getItem(Long itemId) throws IOException {

        Item item = new Item();
        item.setId(itemId);
        Optional<Item> itemOptional = Optional.ofNullable(themeService.getItemBy(item));
        if (itemOptional.isPresent()) {
            item = itemOptional.get();
            //将Json字符串转成list
            if (item.getItemDetailImgs() != null) {
                List<List<String>> listList = mapper.readValue(item.getItemDetailImgs(), mapper.getTypeFactory().constructCollectionType(List.class, List.class));

                //商品详情全是图片
                List<String> detailsList = new ArrayList<>();
                listList.forEach(l -> l.forEach(detailsList::add));

                //商品详情是html
                StringBuilder stringBuilder;
                stringBuilder = new StringBuilder("<!DOCTYPE HTML><html><meta charset='UTF-8'><title>Image Canvas</title></head><style>body {margin: 0px;} p {width: 100%;line-height: 24px;font-size: 12px;text-align: left;margin: 0px auto 0 auto;padding: 0;}p img{float: none;margin: 0;padding: 0;border: 0;vertical-align: top;}</style><body><p>");

                //使用Java8 Stream写法,增加图片地址前缀
                detailsList.stream().map((s) -> {
                    stringBuilder.append("<img width=\"100%\" src=\"").append(SysParCom.IMAGE_URL).append(s).append("\">");
                    return SysParCom.IMAGE_URL + s;
                }).collect(Collectors.toList());

                stringBuilder.append("</p></body></html>");

                item.setItemDetailImgs(stringBuilder.toString());

            } else if (item.getItemDetail() != null) {

                //商品详情是html
                StringBuilder stringBuilder;
                stringBuilder = new StringBuilder("<!DOCTYPE HTML><html><meta charset='UTF-8'><title>Image Canvas</title></head><style>body {margin: 0px;} p {width: 100%;line-height: 24px;font-size: 12px;text-align: left;margin: 0px auto 0 auto;padding: 0;}p img{float: none;margin: 0;padding: 0;border: 0;vertical-align: top;}</style><body><p>");

                stringBuilder.append(item.getItemDetail());

                stringBuilder.append("</p></body></html>");

                item.setItemDetailImgs(stringBuilder.toString());
            }
            return item;
        } else return null;
    }

    /**
     * 获取商品下所有库存的信息
     *
     * @param skuType   类型
     * @param itemId    itemId
     * @param skuTypeId skuTypeId
     * @param userId    userId
     * @return list
     */
    private Object getStock(String skuType, Long itemId, Long skuTypeId, Long userId) {
        SkuTypeEnum skuTypeEnum = SkuTypeEnum.getSkuTypeEnum(skuType);
        if (null == skuTypeEnum) {
            return null;
        }
        switch (skuTypeEnum) {
            case ITEM:
                return getItemStock(skuType, itemId, skuTypeId, userId);
            case VARY:
                return getSingleStock(skuType, itemId, skuTypeId, userId);
            case CUSTOMIZE:
                return getSingleStock(skuType, itemId, skuTypeId, userId);
            case PIN:  //拼购
                return getPinStock(skuType, itemId, skuTypeId, userId);
        }
        return null;
    }

    private List<Inventory> getSingleStock(String skuType, Long itemId, Long skuTypeId, Long userId) {

        List<Inventory> inventories = new ArrayList<>();

        Inventory inventory = new Inventory();

        SkuVo skuVo = new SkuVo();
        skuVo.setSkuType(skuType);
        skuVo.setSkuTypeId(skuTypeId);
        List<SkuVo> skuVos = themeService.getAllSkus(skuVo);
        if (skuVos.size() > 0) {
            skuVo = skuVos.get(0);
            inventory.setId(skuVo.getInvId());
            BeanUtils.copyProperties(skuVo, inventory);
            inventory.setInvUrl(SysParCom.DEPLOY_URL + "/comm/detail/" + skuType + "/" + itemId + "/" + skuTypeId);


            inventory.setInvImg(getInvImg(inventory.getInvImg()));

            inventory.setOrMasterInv(true);

            //预览图
            JsonNode js = Json.parse(inventory.getItemPreviewImgs());
            if (js.isArray()) {
                for (JsonNode j : js) {
                    if (j.has("url")) {
                        ((ObjectNode) j).put("url", SysParCom.IMAGE_URL + j.get("url").asText());
                    }
                }
            }

            inventory.setItemPreviewImgs(js.toString());
            inventory.setSkuType(skuType);
            inventory.setSkuTypeId(skuTypeId);
            inventory.setSoldAmount(skuVo.getSkuTypeSoldAmount());
            inventory.setItemPrice(skuVo.getSkuTypePrice());
            inventory.setState(skuVo.getSkuTypeStatus());
            inventory.setItemDiscount(skuVo.getSkuTypeDiscount());
            inventory.setCollectId(getCollectInfo(skuType, skuVo.getInvId(), skuTypeId, userId));

            inventories.add(inventory);
            return inventories;
        } else return null;
    }

    /**
     * @param skuType
     * @param itemId
     * @param skuTypeId 当前选中的主商品ID
     * @param userId
     * @return
     */
    private List<Inventory> getItemStock(String skuType, Long itemId, Long skuTypeId, Long userId) {
        Inventory inventory = new Inventory();
        inventory.setItemId(itemId);
        return themeService.getInvBy(inventory).stream().map(l -> {

            l.setInvUrl(SysParCom.DEPLOY_URL + "/comm/detail/" + skuType + "/" + itemId + "/" + l.getId());

            l.setInvImg(getInvImg(l.getInvImg()));

            //判断是否是当前需要显示的sku
            if (!skuTypeId.equals(-1L) && !l.getId().equals(skuTypeId)) {
                l.setOrMasterInv(false);
            } else if (l.getId().equals(skuTypeId)) {
                l.setOrMasterInv(true);
            }

            //预览图
            JsonNode js = Json.parse(l.getItemPreviewImgs());
            if (js.isArray()) {
                for (JsonNode j : js) {
                    if (j.has("url")) {
                        ((ObjectNode) j).put("url", SysParCom.IMAGE_URL + j.get("url").asText());
                    }
                }
            }
            l.setItemPreviewImgs(js.toString());

            l.setSkuType(skuType);
            l.setSkuTypeId(l.getId());
            l.setCollectId(getCollectInfo(skuType, l.getId(), l.getSkuTypeId(), userId));
            return l;
        }).collect(Collectors.toList());

    }

    /**
     * 转换图片,拼接URL前缀
     *
     * @param invImg invImg
     * @return invImg
     */
    private String getInvImg(String invImg) {
        //SKU图片
        if (invImg.contains("url")) {
            JsonNode jsonNode_InvImg = Json.parse(invImg);
            if (jsonNode_InvImg.has("url")) {
                ((ObjectNode) jsonNode_InvImg).put("url", SysParCom.IMAGE_URL + jsonNode_InvImg.get("url").asText());
                return Json.stringify(jsonNode_InvImg);
            } else return SysParCom.IMAGE_URL + invImg;
        } else return SysParCom.IMAGE_URL + invImg;
    }

    /**
     * 获取收藏信息
     *
     * @param skuType   skuType
     * @param skuId     skuId
     * @param skuTypeId skuTypeId
     * @param userId    userId
     * @return CollectId
     */
    private Long getCollectInfo(String skuType, Long skuId, Long skuTypeId, Long userId) {
        if (!userId.equals(-1L)) {
            //用户收藏信息
            Collect collect = new Collect();
            collect.setUserId(userId);
            collect.setSkuId(skuId);
            collect.setSkuType(skuType);
            collect.setSkuTypeId(skuTypeId);
            try {
                Logger.info("=getCollectInfo=userId=" + userId + ",skuId=" + skuId + ",skuType=" + skuType + ",skuTypeId==" + skuTypeId);
                Optional<List<Collect>> collectList = Optional.ofNullable(cartService.selectCollect(collect));
                if (collectList.isPresent() && collectList.get().size() > 0) {
                    return collectList.get().get(0).getCollectId();
                }
                return 0L;
            } catch (Exception ex) {
                Logger.error("cartService.selectCollect exception " + ex.getMessage());
                return 0L;
            }
        } else return 0L;
    }

    /**
     * 获取拼购库存详情
     *
     * @param skuType   skuType
     * @param itemId    itemId
     * @param skuTypeId skuTypeId
     * @param userId    userId
     * @return PinInvDetail
     */
    private PinInvDetail getPinStock(String skuType, Long itemId, Long skuTypeId, Long userId) {

        PinInvDetail pinInvDetail = new PinInvDetail();


        SkuVo skuVo = new SkuVo();
        skuVo.setSkuType(skuType);
        skuVo.setSkuTypeId(skuTypeId);
        List<SkuVo> skuVos = themeService.getAllSkus(skuVo);

        if (skuVos.size() > 0) {

            skuVo = skuVos.get(0);
            //预览图
            JsonNode js = Json.parse(skuVo.getItemPreviewImgs());
            if (js.isArray()) {
                for (JsonNode j : js) {
                    if (j.has("url")) {
                        ((ObjectNode) j).put("url", SysParCom.IMAGE_URL + j.get("url").asText());
                    }
                }
            }
            skuVo.setItemPreviewImgs(js.toString());
            BeanUtils.copyProperties(skuVo, pinInvDetail);
            pinInvDetail.setInvImg(getInvImg(pinInvDetail.getInvImg()));

            pinInvDetail.setInvPrice(skuVo.getItemPrice());//商品原价
            pinInvDetail.setSkuType(skuType);//商品类型拼购商品
            pinInvDetail.setSkuTypeId(skuTypeId);//拼购ID

        }

        PinSku pinSku = promotionService.getPinSkuById(skuTypeId);


        if (pinSku.getShareUrl() != null && !pinSku.getShareUrl().equals(""))
            pinInvDetail.setShareUrl(pinSku.getShareUrl()); //分享短连接
        pinInvDetail.setId(skuVo.getId());                              //库存ID
        pinInvDetail.setStatus(pinSku.getStatus());                     //状态
        pinInvDetail.setPinTitle(pinSku.getPinTitle());                 //拼购商品标题
        pinInvDetail.setStartAt(pinSku.getStartAt());                   //开始时间
        pinInvDetail.setEndAt(pinSku.getEndAt());                       //结束时间
        pinInvDetail.setRestrictAmount(pinSku.getRestrictAmount());     //每个ID限购数量
        pinInvDetail.setFloorPrice(pinSku.getFloorPrice());             //拼购最低价
        pinInvDetail.setPinDiscount(pinSku.getPinDiscount());           //拼购最低折扣

        List<PinTieredPrice> pinTieredPrices = promotionService.getTieredPriceByPinId(skuTypeId);

        pinTieredPrices = pinTieredPrices.stream().map(pinTieredPrice -> {
            if (pinTieredPrice.getMasterCouponClass() != null && !Objects.equals(pinTieredPrice.getMasterCouponClass(), "")) {
                pinTieredPrice.setMasterCouponClassName(GenCouponCode.CouponClassCode.getName(Integer.valueOf(pinTieredPrice.getMasterCouponClass())));
            }
            if (pinTieredPrice.getMemberCouponClass() != null && !Objects.equals(pinTieredPrice.getMemberCouponClass(), "")) {
                pinTieredPrice.setMemberCouponClassName(GenCouponCode.CouponClassCode.getName(Integer.valueOf(pinTieredPrice.getMemberCouponClass())));
            }
            return pinTieredPrice;
        }).collect(Collectors.toList());

        pinInvDetail.setPinTieredPrices(pinTieredPrices);

        pinInvDetail.setPinRedirectUrl(SysParCom.DEPLOY_URL + "/comm/detail/" + skuType + "/" + itemId + "/" + skuTypeId);
        pinInvDetail.setCollectId(getCollectInfo(skuType, skuVo.getId(), skuTypeId, userId));

        return pinInvDetail;
    }

    /**
     * 获取推荐拼购商品
     *
     * @return list
     * @throws Exception
     */
    public List<ThemeItem> getPushSku(String skuType) throws Exception {
        List<ThemeItem> themeItems = new ArrayList<>();
        SkuVo skuVo = new SkuVo();
        skuVo.setSkuTypeStatus("Y");
        if (skuType.equals("pin")) skuVo.setSkuType(skuType);
        else skuVo.setSkuType("item");
        List<SkuVo> skuVos = themeService.getAllSkus(skuVo);

        for (int i = 0; i < (skuVos.size() > 6 ? 6 : skuVos.size()); i++) {

            ThemeItem themeItem = new ThemeItem();
            skuVo = skuVos.get(i);

            themeItem.setCollectCount(skuVo.getCollectCount().intValue());
            themeItem.setItemDiscount(skuVo.getSkuTypeDiscount());
            JsonNode jsonNodeInvImg = Json.parse(skuVo.getSkuTypeImg());
            if (jsonNodeInvImg.has("url")) {
                ((ObjectNode) jsonNodeInvImg).put("url", SysParCom.IMAGE_URL + jsonNodeInvImg.get("url").asText());
                themeItem.setItemImg(Json.stringify(jsonNodeInvImg));
            }
            themeItem.setItemPrice(skuVo.getSkuTypePrice());
            themeItem.setItemSoldAmount(skuVo.getSkuTypeSoldAmount());
            themeItem.setItemSrcPrice(skuVo.getItemSrcPrice());
            themeItem.setItemTitle(skuVo.getSkuTypeTitle());
            themeItem.setItemUrl(SysParCom.DEPLOY_URL + "/comm/detail/" + skuVo.getSkuType() + "/" + skuVo.getItemId() + "/" + skuVo.getSkuTypeId());

            themeItem.setItemType(skuVo.getSkuType());
            themeItem.setState(skuVo.getSkuTypeStatus());//商品状态
            themeItem.setStartAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(skuVo.getSkuTypeStartAt()));
            themeItem.setEndAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(skuVo.getSkuTypeEndAt()));
            themeItems.add(themeItem);
        }
        return themeItems;
    }


    /**
     * 处理评价结果
     *
     * @param remarkList remarkList
     * @param img        img
     * @return List<Remark>
     */
    public List<Remark> dealRemark(List<Remark> remarkList, Boolean img) {
        List<Remark> imgRemarks = new ArrayList<>();

        for (Remark re : remarkList) {
            try {
                ID id = idService.getID(re.getUserId());
                if (id != null) {
                    re.setUserImg(SysParCom.IMAGE_URL + id.getPhotoUrl());
                    if (id.getNickname().length() < 4) {
                        re.setUserName(id.getNickname().charAt(0) + "**" + id.getNickname().charAt(id.getNickname().length() - 1));
                    } else
                        re.setUserName(id.getNickname().charAt(0) + id.getNickname().substring(1, id.getNickname().length() - 2).replaceAll("\\S", "*") + id.getNickname().charAt(id.getNickname().length() - 1));
                } else {
                    re.setUserImg(SysParCom.IMAGE_URL + "users/photo/default.png");
                    re.setUserName("HMM**" + new Random().nextInt(1000));
                }
                //增加购买时间,规格
                Order order = new Order();
                order.setOrderId(re.getOrderId());
                List<Order> orders = cartService.selectOrder(order);
                if (orders.size() == 1) {
                    order = orders.get(0);
                    re.setBuyAt(order.getOrderCreateAt());
                    OrderLine orderLine = new OrderLine();
                    orderLine.setOrderId(order.getOrderId());
                    orderLine.setSkuTypeId(re.getSkuTypeId());
                    orderLine.setSkuType(re.getSkuType());
                    List<OrderLine> orderLines = cartService.selectOrderLine(orderLine);
                    if (orderLines.size() == 1) {
                        orderLine = orderLines.get(0);
                        re.setSize(orderLine.getSkuColor() + orderLine.getSkuSize());
                    }
                }

                if (re.getPicture() != null) {
                    List<String> remarkPics = mapper.readValue(re.getPicture(), mapper.getTypeFactory().constructCollectionType(List.class, String.class));
                    if (remarkPics.size() > 0) {
                        if (img) {
                            for (String pic : remarkPics) {
                                re.setPicture(SysParCom.IMAGE_URL + pic);
                                Remark imgRemark = new Remark();
                                BeanUtils.copyProperties(re, imgRemark);
                                imgRemarks.add(imgRemark);
                            }
                        } else {
                            remarkPics = remarkPics.stream().map(pic -> SysParCom.IMAGE_URL + pic).collect(Collectors.toList());
                            re.setPicture(Json.toJson(remarkPics).toString());
                            imgRemarks.add(re);
                        }
                    }
                } else imgRemarks.add(re);
            } catch (Exception e) {
                e.printStackTrace();
                Logger.error("删除评价:" + Throwables.getStackTraceAsString(e));
            }
        }
        return imgRemarks;
    }
}
