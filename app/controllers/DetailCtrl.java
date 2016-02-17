package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import domain.Cart;
import domain.Message;
import middle.DetailMid;
import net.spy.memcached.MemcachedClient;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import service.CartService;
import service.PromotionService;
import service.ThemeService;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 商品详情页
 * Created by howen on 16/1/25.
 */
public class DetailCtrl extends Controller {

    private ThemeService themeService;

    private CartService cartService;

    private PromotionService promotionService;

    private DetailMid detailMid;

    private MemcachedClient cache;

    @Inject
    public DetailCtrl(ThemeService themeService, CartService cartService, PromotionService promotionService, MemcachedClient cache) {
        this.cache = cache;
        this.themeService = themeService;
        this.cartService = cartService;
        this.promotionService = promotionService;
        detailMid = new DetailMid(themeService, cartService, promotionService);
    }

    /**
     * 获取商品详情
     *
     * @param itemId     商品ID
     * @param skuId  库存ID
     * @param varyId 多样化价格ID
     * @return json
     */
    public Result getItemDetail(Integer subjectFlag, Long itemId, Long skuId, Long varyId) {

        Optional<String> header = Optional.ofNullable(request().getHeader("id-token"));
        //组合结果集
        Map<String, Object> map = new HashMap<>();
        try {
            if (subjectFlag == 1) {
                map = detailMid.getDetail(itemId, skuId, varyId, -1L);
            } else map = detailMid.getDetail(itemId, skuId, -1L, varyId);

            if (header.isPresent()) {
                Optional<String> token = Optional.ofNullable(cache.get(header.get()).toString());
                if (token.isPresent()) {
                    JsonNode userJson = Json.parse(token.get());
                    Long userId = Long.valueOf(userJson.findValue("id").asText());
                    Cart cart = new Cart();
                    cart.setUserId(userId);
                    Optional<List<Cart>> cartList = Optional.ofNullable(cartService.getCartByUserSku(cart));
                    if (cartList.isPresent() && cartList.get().size() > 0) {
                        map.put("cartNum", cartService.getCartByUserSku(cart).get(0).getCartNum());
                    }
                }
            }
            map.put("message", Json.toJson(new Message(Message.ErrorCode.getName(Message.ErrorCode.SUCCESS.getIndex()), Message.ErrorCode.SUCCESS.getIndex())));
            return ok(Json.toJson(map));
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.error("server exception:" + ex.getLocalizedMessage());
            map.put("message", Json.toJson(new Message(Message.ErrorCode.getName(Message.ErrorCode.SERVER_EXCEPTION.getIndex()), Message.ErrorCode.SERVER_EXCEPTION.getIndex())));
            return ok(Json.toJson(map));
        }
    }

    /**
     * 获取拼购详情页
     *
     * @param id    商品ID
     * @param skuId 库存ID
     * @param pinId 拼购库存ID
     * @return json
     */
    public Result getPinDetail(Long id, Long skuId, Long pinId) {
        Optional<String> header = Optional.ofNullable(request().getHeader("id-token"));
        //组合结果集
        Map<String, Object> map = new HashMap<>();
        try {
            map = detailMid.getPinDetail(id, skuId, pinId);
            if (header.isPresent()) {
                Optional<String> token = Optional.ofNullable(cache.get(header.get()).toString());
                if (token.isPresent()) {
                    JsonNode userJson = Json.parse(token.get());
                    Long userId = Long.valueOf(userJson.findValue("id").asText());
                    Cart cart = new Cart();
                    cart.setUserId(userId);
                    Optional<List<Cart>> cartList = Optional.ofNullable(cartService.getCartByUserSku(cart));
                    if (cartList.isPresent() && cartList.get().size() > 0) {
                        map.put("cartNum", cartService.getCartByUserSku(cart).get(0).getCartNum());
                    }
                }
            }
            map.put("message", Json.toJson(new Message(Message.ErrorCode.getName(Message.ErrorCode.SUCCESS.getIndex()), Message.ErrorCode.SUCCESS.getIndex())));
            return ok(Json.toJson(map));
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.error("server exception:" + ex.getLocalizedMessage());
            map.put("message", Json.toJson(new Message(Message.ErrorCode.getName(Message.ErrorCode.SERVER_EXCEPTION.getIndex()), Message.ErrorCode.SERVER_EXCEPTION.getIndex())));
            return ok(Json.toJson(map));
        }
    }

}
