package middle;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.Application;
import domain.*;
import play.Logger;
import play.libs.Json;
import service.CartService;
import service.PromotionService;
import service.ThemeService;
import util.GenCouponCode;

import javax.inject.Singleton;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 中间事务处理层
 * Created by howen on 16/1/25.
 */
@Singleton
public class DetailMid {

    private CartService cartService;
    private PromotionService promotionService;
    private ThemeService themeService;


    //将Json串转换成List
    final static ObjectMapper mapper = new ObjectMapper();

    public DetailMid(ThemeService themeService, CartService cartService, PromotionService promotionService) {
        this.themeService = themeService;
        this.cartService = cartService;
        this.promotionService = promotionService;
    }


    /**
     * 获取商品详情
     *
     * @param itemId 商品id
     * @param skuId  库存ID
     * @param varyId 多样化价格id
     * @param userId 用户userId,未登录为-1
     * @return map
     */
    public Map<String, Object> getDetail(Long itemId, Long skuId, Long varyId, Long subjectId,Long userId) {

        Map<String, Object> map = new HashMap<>();
        try {
            map.put("main", getItem(itemId));
            map.put("stock", getStock(itemId, skuId, varyId, subjectId,userId));
            map.put("push",getPushSku(null));
            return map;
        } catch (Exception ex) {
            Logger.error("getItemDetail: " + ex.getMessage());
            ex.printStackTrace();
            return null;
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
                    stringBuilder.append("<img width=\"100%\" src=\"").append(Application.IMAGE_URL).append(s).append("\">");
                    return Application.IMAGE_URL + s;
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
     * @param itemId 商品ID
     * @param skuId  库存ID
     * @param varyId 多样化价格ID
     * @return list
     */
    private List<Inventory> getStock(Long itemId, Long skuId, Long varyId, Long subjectId,Long userId) {

        Inventory inventory = new Inventory();
        inventory.setItemId(itemId);

        //遍历库存list 对其进行相应的处理
        return themeService.getInvBy(inventory).stream().map(l -> {

            l.setInvUrl(Application.DEPLOY_URL + "/comm/detail/" + itemId + "/" + l.getId());


            //SKU图片
            if (l.getInvImg().contains("url")) {
                JsonNode jsonNode_InvImg = Json.parse(l.getInvImg());
                if (jsonNode_InvImg.has("url")) {
                    ((ObjectNode) jsonNode_InvImg).put("url", Application.IMAGE_URL + jsonNode_InvImg.get("url").asText());
                    l.setInvImg(Json.stringify(jsonNode_InvImg));
                }
            } else l.setInvImg(Application.IMAGE_URL + l.getInvImg());


            //判断是否是当前需要显示的sku
            if (!skuId.equals(((Integer) (-1)).longValue()) && !l.getId().equals(skuId)) {
                l.setOrMasterInv(false);
            } else if (l.getId().equals(skuId)) {
                l.setOrMasterInv(true);
            }

            //预览图
            JsonNode js = Json.parse(l.getItemPreviewImgs());
            if (js.isArray()) {
                for (JsonNode j : js) {
                    if (j.has("url")) {
                        ((ObjectNode) j).put("url", Application.IMAGE_URL + j.get("url").asText());
                    }
                }
            }
            l.setItemPreviewImgs(js.toString());

            l.setSkuType("item");
            l.setSkuTypeId(l.getId());

            //是否是多样化价格产品 //vary
            if (!varyId.equals(((Integer) (-1)).longValue())) {
                VaryPrice varyPrice = new VaryPrice();
                varyPrice.setId(varyId);
                List<VaryPrice> varyPriceList = themeService.getVaryPriceBy(varyPrice);
                if (varyPriceList.size() > 0) {
                    varyPrice = varyPriceList.get(0);
                    l.setSoldAmount(varyPrice.getSoldAmount());
                    l.setItemPrice(varyPrice.getPrice());
                    l.setState(varyPrice.getStatus());
                    l.setInvUrl(Application.DEPLOY_URL + "/comm/detail/" + itemId + "/" + l.getId() + "/" + varyId);
                    l.setSkuType("vary");
                    l.setSkuTypeId(varyId);
                }
            }

            //是否自定义价格 customs
            if (!subjectId.equals(((Integer) (-1)).longValue())) {
                SubjectPrice subjectPrice = themeService.getSbjPriceById(subjectId);
                l.setItemPrice(subjectPrice.getPrice());
                l.setItemDiscount(subjectPrice.getDiscount());
                l.setInvUrl(Application.DEPLOY_URL + "/comm/subject/detail/" + itemId + "/" + l.getId() + "/" + subjectId);
                l.setSkuType("customize");
                l.setSkuTypeId(subjectId);
            }
            l.setCollectId(0L);

            if (!userId.equals(((Integer) (-1)).longValue())) {
                //用户收藏信息
                Collect collect = new Collect();
                collect.setUserId(userId);
                collect.setSkuId(l.getId());
                collect.setSkuType(l.getSkuType());
                collect.setSkuTypeId(l.getSkuTypeId());
                try{
                    Optional<List<Collect>> collectList = Optional.ofNullable(cartService.selectCollect(collect));
                    if (collectList.isPresent()&&collectList.get().size()>0) {
                        l.setCollectId(collectList.get().get(0).getCollectId());
                        Logger.info("item collect userId="+userId+",collectId="+l.getCollectId()+",skuId="+collect.getSkuId()+",skuType="+collect.getSkuType()+",skuTypeId="+collect.getSkuTypeId());
                    }else{
                        Logger.info("item not collect userId="+userId+",collectId="+l.getCollectId()+",skuId="+collect.getSkuId()
                                +",skuType="+collect.getSkuType()+",skuTypeId="+collect.getSkuTypeId()+",collectList.isPresent()="+collectList.isPresent());
                    }
                }catch (Exception ex){
                    Logger.error("cartService.selectCollect exception "+ ex.getMessage());
                }
            }

            return l;
        }).collect(Collectors.toList());

    }

    /**
     * 获取拼购商品详情
     *
     * @param itemId 商品ID
     * @param skuId  库存ID
     * @param pinId  拼购ID
     * @return map
     */
    public Map<String, Object> getPinDetail(Long itemId, Long skuId, Long pinId,Long userId) {

        Map<String, Object> map = new HashMap<>();
        try {
            map.put("main", getItem(itemId));
            map.put("stock", getPinInvDetail(itemId, skuId, pinId,userId));
            map.put("push",getPushSku("pin"));
            return map;
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.error("getItemDetail: " + ex.getMessage());
            return null;
        }
    }

    /**
     * 获取拼购库存详情
     *
     * @param itemId 商品ID
     * @param skuId  库存ID
     * @param pinId  拼购ID
     * @return PinInvDetail
     */
    private PinInvDetail getPinInvDetail(Long itemId, Long skuId, Long pinId,Long userId) {

        PinInvDetail pinInvDetail = new PinInvDetail();

        Inventory inventory = new Inventory();
        inventory.setId(skuId);
        List<Inventory> inventoryList = themeService.getInvBy(inventory);

        if (inventoryList.size() > 0) {

            inventory = inventoryList.get(0);

            pinInvDetail.setInvArea(inventory.getInvArea());//库存区域区分：'B'保税区仓库发货，‘Z’韩国直邮
            pinInvDetail.setRestAmount(inventory.getRestAmount());//商品余量

            JsonNode js = Json.parse(inventory.getItemPreviewImgs());
            if (js.isArray()) {
                for (JsonNode j : js) {
                    if (j.has("url")) {
                        ((ObjectNode) j).put("url", Application.IMAGE_URL + j.get("url").asText());
                    }
                }
            }
            pinInvDetail.setItemPreviewImgs(js.toString());//sku预览图

            pinInvDetail.setInvWeight(inventory.getInvWeight());//商品重量单位g
            pinInvDetail.setInvCustoms(inventory.getInvCustoms());//报关单位

            pinInvDetail.setPostalTaxRate(inventory.getPostalTaxRate());//税率,百分比
            pinInvDetail.setPostalStandard(inventory.getPostalStandard());//关税收费标准
            pinInvDetail.setInvAreaNm(inventory.getInvAreaNm());//仓储地名称
            pinInvDetail.setCollectCount(inventory.getCollectCount());//收藏数
            pinInvDetail.setBrowseCount(inventory.getBrowseCount());//浏览次数
            pinInvDetail.setSoldAmount(inventory.getSoldAmount());//已售出数量
            pinInvDetail.setInvPrice(inventory.getItemPrice());//商品原价
            pinInvDetail.setSkuType("pin");//商品类型拼购商品
            pinInvDetail.setSkuTypeId(pinId);//拼购ID


            JsonNode js_invImg = Json.parse(inventory.getInvImg());
            if (js_invImg.has("url")) {
                ((ObjectNode) js_invImg).put("url", Application.IMAGE_URL + js_invImg.get("url").asText());
            }
            pinInvDetail.setInvImg(js_invImg.toString());
        }

        PinSku pinSku = promotionService.getPinSkuById(pinId);

        pinInvDetail.setId(skuId);                                   //库存ID
        if (pinSku.getShareUrl() != null && !pinSku.getShareUrl().equals("")) pinInvDetail.setShareUrl(pinSku.getShareUrl()); //分享短连接
        pinInvDetail.setStatus(pinSku.getStatus());                     //状态
        pinInvDetail.setPinTitle(pinSku.getPinTitle());                 //拼购商品标题
        pinInvDetail.setStartAt(pinSku.getStartAt());                   //开始时间
        pinInvDetail.setEndAt(pinSku.getEndAt());                       //结束时间
        pinInvDetail.setRestrictAmount(pinSku.getRestrictAmount());     //每个ID限购数量
        pinInvDetail.setFloorPrice(pinSku.getFloorPrice());             //拼购最低价
        pinInvDetail.setPinDiscount(pinSku.getPinDiscount());           //拼购最低折扣

        List<PinTieredPrice> pinTieredPrices = promotionService.getTieredPriceByPinId(pinId);

        pinTieredPrices = pinTieredPrices.stream().map(pinTieredPrice -> {
            if (pinTieredPrice.getMasterCouponClass() != null && !Objects.equals(pinTieredPrice.getMasterCouponClass(), "")) {
                pinTieredPrice.setMasterCouponClassName(GenCouponCode.CouponClassCode.getName(Integer.valueOf(pinTieredPrice.getMasterCouponClass())));
            }
            if (pinTieredPrice.getMemberCouponClass() != null && !Objects.equals(pinTieredPrice.getMemberCouponClass(), "")) {
                pinTieredPrice.setMemberCouponClassName(GenCouponCode.CouponClassCode.getName(Integer.valueOf(pinTieredPrice.getMemberCouponClass())));
            }
            return pinTieredPrice;
        }).collect(Collectors.toList());

        pinInvDetail.setPinTieredPrices(pinTieredPrices);       //设置价格

        pinInvDetail.setPinRedirectUrl(Application.DEPLOY_URL + "/comm/pin/detail/" + itemId + "/" + skuId + "/" + pinId);
        pinInvDetail.setCollectId(0L);
        if (!userId.equals(((Integer) (-1)).longValue())) {
            //用户收藏信息
            Collect collect = new Collect();
            collect.setUserId(userId);
            collect.setSkuId(pinInvDetail.getId());
            collect.setSkuType(pinInvDetail.getSkuType());
            collect.setSkuTypeId(pinInvDetail.getSkuTypeId());
            try{
                Optional<List<Collect>> collectList = Optional.ofNullable(cartService.selectCollect(collect));
                if (collectList.isPresent()&&collectList.get().size()>0) {
                    pinInvDetail.setCollectId(collectList.get().get(0).getCollectId());
                    Logger.info("pin collect userId="+userId+",collectId="+pinInvDetail.getCollectId());
                }
            }catch (Exception ex){
                Logger.error("cartService.selectCollect exception"+ ex.getMessage());
            }
        }
        return pinInvDetail;
    }

    /**
     * 获取推荐拼购商品
     * @return list
     * @throws Exception
     */
    public List<ThemeItem> getPushSku(String skuType) throws Exception {
        List<ThemeItem> themeItems = new ArrayList<>();
        SkuVo skuVo  = new SkuVo();
        skuVo.setSkuTypeStatus("Y");
        if (skuType!=null) skuVo.setSkuType(skuType);
        List<SkuVo> skuVos = themeService.getAllSkus(skuVo);

        for (int i = 0; i < (skuVos.size() > 6 ? 6 : skuVos.size()); i++) {

            ThemeItem themeItem  =new ThemeItem();
            SkuVo pin = skuVos.get(i);

            themeItem.setCollectCount(pin.getCollectCount().intValue());
            themeItem.setItemDiscount(pin.getSkuTypeDiscount());
            JsonNode jsonNodeInvImg = Json.parse(pin.getSkuTypeImg());
            if (jsonNodeInvImg.has("url")) {
                ((ObjectNode) jsonNodeInvImg).put("url", Application.IMAGE_URL + jsonNodeInvImg.get("url").asText());
                themeItem.setItemImg(Json.stringify(jsonNodeInvImg));
            }
            themeItem.setItemPrice(pin.getSkuTypePrice());
            themeItem.setItemSoldAmount(pin.getSkuTypeSoldAmount());
            themeItem.setItemSrcPrice(pin.getItemSrcPrice());
            themeItem.setItemTitle(pin.getSkuTypeTitle());
            switch (pin.getSkuType()){
                case "item": themeItem.setItemUrl(Application.DEPLOY_URL + "/comm/detail/" + pin.getItemId() + "/" + pin.getInvId());break;
                case "vary": themeItem.setItemUrl(Application.DEPLOY_URL + "/comm/detail/" + pin.getItemId() + "/" + pin.getInvId() + "/" + pin.getSkuTypeId());break;
                case "pin": themeItem.setItemUrl(Application.DEPLOY_URL + "/comm/pin/detail/" + pin.getItemId() + "/" + pin.getInvId()  + "/" + pin.getSkuTypeId());break;
                case "customize": themeItem.setItemUrl(Application.DEPLOY_URL + "/comm/subject/detail/" + pin.getItemId() + "/" + pin.getInvId() + "/" + pin.getSkuTypeId());break;
            }
            themeItem.setItemType(pin.getSkuType());
            themeItem.setState(pin.getSkuTypeStatus());//商品状态
            themeItem.setStartAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(pin.getSkuTypeStartAt()));
            themeItem.setEndAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(pin.getSkuTypeEndAt()));
            themeItems.add(themeItem);
        }
        return themeItems;
    }
}
