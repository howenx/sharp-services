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

    @Inject
    private CartService cartService;

    @Inject
    private DetailMid detailMid;

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
            Logger.error("server exception:" + ex.getLocalizedMessage());
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

}
