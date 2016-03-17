package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import domain.Cart;
import domain.Message;
import middle.ThemeListMid;
import net.spy.memcached.MemcachedClient;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import service.CartService;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * 二级页面数据
 * Created by howen on 16/1/25.
 */
public class ThemeListCtrl extends Controller {

    @Inject
    private CartService cartService;

    @Inject
    private ThemeListMid themeListMid;

    @Inject
    private MemcachedClient cache;


    /**
     * 获取主题列表
     *
     * @param themeId 主题ID
     * @return 返回主题列表JSON
     */
    public Result getThemeList(Long themeId) {
        ObjectNode result = Json.newObject();
        Optional<String> header = Optional.ofNullable(request().getHeader("id-token"));
        try {
            if (header.isPresent()) {
                Optional<String> token = Optional.ofNullable(cache.get(header.get()).toString());
                if (token.isPresent()) {
                    JsonNode userJson = Json.parse(token.get());
                    Long userId = Long.valueOf(userJson.findValue("id").asText());
                    Cart cart = new Cart();
                    cart.setUserId(userId);
                    Optional<List<Cart>> cartList = Optional.ofNullable(cartService.getCartByUserSku(cart));
                    if (cartList.isPresent() && cartList.get().size() > 0) {
                        result.putPOJO("cartNum", cartService.getCartByUserSku(cart).get(0).getCartNum());
                    }
                }
            }
            result.putPOJO("message", Json.toJson(new Message(Message.ErrorCode.getName(Message.ErrorCode.SUCCESS.getIndex()), Message.ErrorCode.SUCCESS.getIndex())));
            result.putPOJO("themeList", Json.toJson(themeListMid.getThemeList(themeId)));
            return ok(result);
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.error("server exception:" + ex.getMessage());
            result.putPOJO("message", Json.toJson(new Message(Message.ErrorCode.getName(Message.ErrorCode.SERVER_EXCEPTION.getIndex()), Message.ErrorCode.SERVER_EXCEPTION.getIndex())));
            return ok(result);
        }
    }
}
