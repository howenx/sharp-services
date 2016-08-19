package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Throwables;
import domain.*;
import middle.DetailMid;
import util.SysParCom;
import net.spy.memcached.MemcachedClient;
import org.apache.commons.codec.digest.DigestUtils;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import service.CartService;
import service.ThemeService;
import util.cache.MemcachedConfiguration;

import javax.inject.Inject;
import java.util.*;

/**
 * 商品详情页
 * Created by howen on 16/1/25.
 */
public class DetailCtrl extends Controller {

    @Inject
    private CartService cartService;

    @Inject
    private DetailMid detailMid;

    @Inject
    private ThemeService themeService;

    @Inject
    private MemcachedClient cache;


    /**
     * 获取商品详情
     *
     * @param skuType   skuType
     * @param itemId    itemId
     * @param skuTypeId skuTypeId
     * @return json
     */
    public Result getItemDetail(String skuType, Long itemId, Long skuTypeId) {

        //组合结果集
        Map<String, Object> map = new HashMap<>();
        try {
            map.putAll(getCartNum(request().getHeader("id-token")));
            map = detailMid.getDetail(skuType, skuTypeId, itemId, (Long) map.get("userId"));
            map.put("message", Json.toJson(new Message(Message.ErrorCode.getName(Message.ErrorCode.SUCCESS.getIndex()), Message.ErrorCode.SUCCESS.getIndex())));
            return ok(Json.toJson(map));
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.error("server exception:" + Throwables.getStackTraceAsString(ex));
            map.put("message", Json.toJson(new Message(Message.ErrorCode.getName(Message.ErrorCode.SERVER_EXCEPTION.getIndex()), Message.ErrorCode.SERVER_EXCEPTION.getIndex())));
            return ok(Json.toJson(map));
        }
    }

    private Map<String, Object> getCartNum(String headerToken) throws Exception {
        Map<String, Object> map = new HashMap<>();
        Long userId = -1L;
        Optional<String> header = Optional.ofNullable(headerToken);
        if (header.isPresent()) {
            Optional<Object> token = Optional.ofNullable(cache.get(header.get()));
            if (token.isPresent()) {
                JsonNode userJson = Json.parse(token.get().toString());
                userId = Long.valueOf(userJson.findValue("id").asText());
                Cart cart = new Cart();
                cart.setUserId(userId);
                Optional<List<Cart>> cartList = Optional.ofNullable(cartService.getCartByUserSku(cart));
                if (cartList.isPresent() && cartList.get().size() > 0) {
                    map.put("cartNum", cartList.get().get(0).getCartNum());
                }
            }
        }
        map.put("userId", userId);
        return map;
    }

    /**
     * 全部评价
     *
     * @param skuType   skuType
     * @param skuTypeId skuTypeId
     * @param pageNum   pageNum
     * @return Result
     */
    @SuppressWarnings("unchecked")
    public Result getSkuRemark(Integer grade, String skuType, Long skuTypeId, Integer pageNum) {

        ObjectNode result = Json.newObject();

        Integer page_size = grade == 6 ? SysParCom.IMG_PAGE_SIZE : SysParCom.PAGE_SIZE;

        if (pageNum > 0) {
            //计算从第几条开始取数据
            int offset = (pageNum - 1) * page_size;

            if (skuType.equals("item")) {

                List<Remark> itemRemarks = new ArrayList<>();

                Optional<Object> cacheRemark = Optional.ofNullable(cache.get(DigestUtils.md5Hex(grade + skuType + skuTypeId + pageNum)));
                if (cacheRemark.isPresent() && cacheRemark.get() != null) {
                    itemRemarks = (List<Remark>) cacheRemark.get();
                    Logger.info("Cache Hit Ratio [getSkuRemark]: " + DigestUtils.md5Hex(grade + skuType + skuTypeId + pageNum));
                } else {
                    SkuVo skuVo = new SkuVo();
                    skuVo.setSkuType(skuType);
                    skuVo.setSkuTypeId(skuTypeId);
                    List<SkuVo> skuVos = themeService.getAllSkus(skuVo);
                    if (skuVos.size() == 1) {
                        skuVo = skuVos.get(0);
                        Inventory inventory = new Inventory();
                        inventory.setItemId(skuVo.getItemId());
                        List<Inventory> inventoryList = themeService.getInvBy(inventory);

                        for (Inventory inventory1 : inventoryList) {
                            Remark rk = new Remark();
                            rk.setSkuType("item");
                            rk.setSkuTypeId(inventory1.getId());

                            if (grade == 6) { //请求的是所有晒图的结果
                                rk.setPicture("not null");
                                List<Remark> remarkList = cartService.selectRemark(rk);
                                if (remarkList != null && remarkList.size() > 0) {
                                    itemRemarks.addAll(detailMid.dealRemark(rk,remarkList, true));
                                }
                            } else {//请求的是好评或者差评或者全部的结果
                                if (grade != 0) rk.setGrade(grade);
                                List<Remark> remarkList = cartService.selectRemark(rk);
                                if (remarkList != null && remarkList.size() > 0) {
                                    itemRemarks.addAll(detailMid.dealRemark(rk,remarkList, false));
                                }
                            }
                        }
                        if (!itemRemarks.isEmpty())
                            cache.set(DigestUtils.md5Hex(grade + skuType + skuTypeId + pageNum), MemcachedConfiguration.expiration, itemRemarks);
                    } else {
                        result.putPOJO("message", Json.toJson(new Message(Message.ErrorCode.getName(Message.ErrorCode.NOT_FOUND_SKU.getIndex()), Message.ErrorCode.NOT_FOUND_SKU.getIndex())));
                        return ok(result);
                    }
                }
                int page_count = 0;
                if (itemRemarks.size() > 0) {
                    page_count = itemRemarks.size() % page_size == 0 ? itemRemarks.size() / page_size : itemRemarks.size() / page_size + 1;
                }
                if (pageNum > page_count) {
                    result.putPOJO("message", Json.toJson(new Message(Message.ErrorCode.getName(Message.ErrorCode.SUCCESS.getIndex()), Message.ErrorCode.SUCCESS.getIndex())));
                    return ok(result);
                } else {
                    List<Remark> pageRemarkItems;
                    if (itemRemarks.size() < page_size) {
                        if (pageNum == 1) {
                            pageRemarkItems = new ArrayList<>(itemRemarks);
                        } else {
                            pageRemarkItems = new ArrayList<>();
                        }
                    } else {
                        if (itemRemarks.size() < offset + page_size) {
                            if (offset > itemRemarks.size()) {
                                pageRemarkItems = new ArrayList<>();
                            } else pageRemarkItems = new ArrayList<>(itemRemarks.subList(offset, itemRemarks.size()));
                        } else
                            pageRemarkItems = new ArrayList<>(itemRemarks.subList(offset, offset + page_size));
                    }

                    result.putPOJO("message", Json.toJson(new Message(Message.ErrorCode.getName(Message.ErrorCode.SUCCESS.getIndex()), Message.ErrorCode.SUCCESS.getIndex())));
                    if (pageRemarkItems.size() > 0) result.putPOJO("remarkList", Json.toJson(pageRemarkItems));
                    result.putPOJO("page_count", page_count);
                    result.putPOJO("count_num", itemRemarks.size());
                    return ok(result);
                }
            } else {
                Remark remark = new Remark();
                remark.setPageSize(page_size);
                remark.setOffset(offset);
                remark.setSkuTypeId(skuTypeId);
                remark.setSkuType(skuType);

                if (grade != 0 && grade != 6) {
                    remark.setGrade(grade);
                } else if (grade == 6) {
                    remark.setPicture("not null");
                }

                List<Remark> remarkList = cartService.selectRemarkPaging(remark);

                if (remarkList.size() > 0) {

                    if (grade == 6) {
                        remarkList = detailMid.dealRemark(remark,remarkList, true);
                        result.putPOJO("count_num", remarkList.size());
                    } else {
                        remarkList = detailMid.dealRemark(remark,remarkList, false);
                        result.putPOJO("count_num", remarkList.get(0).getCountNum());
                    }

                    int page_count = 0;

                    if (remarkList.size() > 0) {
                        page_count = remarkList.get(0).getCountNum() % page_size == 0 ? remarkList.get(0).getCountNum() / page_size : remarkList.get(0).getCountNum() / page_size + 1;
                    }

                    result.putPOJO("message", Json.toJson(new Message(Message.ErrorCode.getName(Message.ErrorCode.SUCCESS.getIndex()), Message.ErrorCode.SUCCESS.getIndex())));
                    if (remarkList.size() > 0) result.putPOJO("remarkList", Json.toJson(remarkList));
                    result.putPOJO("page_count", page_count);

                    return ok(result);
                } else {
                    result.putPOJO("message", Json.toJson(new Message(Message.ErrorCode.getName(Message.ErrorCode.SUCCESS.getIndex()), Message.ErrorCode.SUCCESS.getIndex())));
                    result.putPOJO("page_count", 0);
                    return ok(result);
                }
            }
        } else {
            result.putPOJO("message", Json.toJson(new Message(Message.ErrorCode.getName(Message.ErrorCode.BAD_PARAMETER.getIndex()), Message.ErrorCode.BAD_PARAMETER.getIndex())));
            return badRequest(result);
        }
    }


}
