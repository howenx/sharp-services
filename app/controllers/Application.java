package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import domain.Cart;
import domain.Message;
import domain.Slider;
import domain.Theme;
import net.spy.memcached.MemcachedClient;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import service.CartService;
import service.ThemeService;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Application extends Controller {

    //每页固定的取数
    public static final int PAGE_SIZE = Integer.valueOf(play.Play.application().configuration().getString("theme.page.size"));

    //图片服务器url
    public static final String IMAGE_URL = play.Play.application().configuration().getString("image.server.url");

    //发布服务器url
    public static final String DEPLOY_URL = play.Play.application().configuration().getString("deploy.server.url");

    @Inject
    private ThemeService themeService;

    @Inject
    private CartService cartService;

    @Inject
    private MemcachedClient cache;


    /**
     * 获取主页
     *
     * @param pageNum 页码
     * @return 主页JSON
     */
    public Result getIndex(int pageNum) {
        ObjectNode result = Json.newObject();
        if (pageNum > 0) {

            //计算从第几条开始取数据
            int offset = (pageNum - 1) * PAGE_SIZE;

            Optional<List<Theme>> listOptional = themeService.getThemes(PAGE_SIZE, offset);


            if (listOptional.isPresent()) {
                List<Theme> themeList = listOptional.get().stream().map(l -> {
                    if (l.getThemeImg().contains("url")) {
                        JsonNode jsonNode1 = Json.parse(l.getThemeImg());
                        if (jsonNode1.has("url")) {
                            ((ObjectNode) jsonNode1).put("url", IMAGE_URL + jsonNode1.get("url").asText());
                            l.setThemeImg(Json.stringify(jsonNode1));
                        }
                    } else l.setThemeImg(IMAGE_URL + l.getThemeImg());


                    if (l.getType().equals("ordinary")) {
                        l.setThemeUrl(DEPLOY_URL + "/topic/list/" + l.getId());
                    }
                    else {
                        l.setThemeUrl(l.getH5Link());
                    }

                    return l;
                }).collect(Collectors.toList());

                int page_count = 0;
                if (themeList.size() > 0) {
                    page_count = themeList.get(0).getThemeNum() / PAGE_SIZE + 1;
                }

                if (pageNum == 1) {
                    Optional<List<Slider>> listOptionalSlider = themeService.getSlider();

                    if (listOptionalSlider.isPresent()) {
                        //slider取出链接
                        List<Slider> sliderImgList = listOptionalSlider.get().stream().map(s -> {
                            if (s.getImg().contains("url")) {
                                JsonNode jsonNode2 = Json.parse(s.getImg());
                                if (jsonNode2.has("url")) {
                                    ((ObjectNode) jsonNode2).put("url", IMAGE_URL + jsonNode2.get("url").asText());
                                    s.setUrl(Json.stringify(jsonNode2));
                                }
                            }
                            s.setItemTarget(DEPLOY_URL + s.getItemTarget());
                            return s;
                        }).collect(Collectors.toList());

                        result.putPOJO("message", Json.toJson(new Message(Message.ErrorCode.getName(Message.ErrorCode.SUCCESS.getIndex()), Message.ErrorCode.SUCCESS.getIndex())));
                        result.putPOJO("slider", Json.toJson(sliderImgList));
                        result.putPOJO("theme", Json.toJson(themeList));
                        result.putPOJO("page_count", page_count);
                        return ok(result);

                    } else {
                        result.putPOJO("message", Json.toJson(new Message(Message.ErrorCode.getName(Message.ErrorCode.SLIDER_NULL_EXCEPTION.getIndex()), Message.ErrorCode.SLIDER_NULL_EXCEPTION.getIndex())));
                        return ok(result);
                    }
                } else {
                    result.putPOJO("message", Json.toJson(new Message(Message.ErrorCode.getName(Message.ErrorCode.SUCCESS.getIndex()), Message.ErrorCode.SUCCESS.getIndex())));
                    result.putPOJO("theme", Json.toJson(themeList));
                    result.putPOJO("page_count", page_count);
                    return ok(result);
                }

            } else {
                result.putPOJO("message", Json.toJson(new Message(Message.ErrorCode.getName(Message.ErrorCode.THEME_NULL_EXCEPTION.getIndex()), Message.ErrorCode.THEME_NULL_EXCEPTION.getIndex())));
                return ok(result);
            }
        } else {
            result.putPOJO("message", Json.toJson(new Message(Message.ErrorCode.getName(Message.ErrorCode.BAD_PARAMETER.getIndex()), Message.ErrorCode.BAD_PARAMETER.getIndex())));
            return badRequest(result);
        }
    }

    /**
     * 获取购物车数量
     *
     * @return result
     */
    public Result getCartAmount() {
        Optional<String> header = Optional.ofNullable(request().getHeader("id-token"));
        Map<String, Object> map = new HashMap<>();
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
                        map.put("cartNum", cartService.getCartByUserSku(cart).get(0).getCartNum());
                    }
                }
            }
            map.put("message", Json.toJson(new Message(Message.ErrorCode.getName(Message.ErrorCode.SUCCESS.getIndex()), Message.ErrorCode.SUCCESS.getIndex())));
            return ok(Json.toJson(map));
        } catch (Exception ex) {
            Logger.error("server exception:" + ex.getMessage());
            map.put("message", Json.toJson(new Message(Message.ErrorCode.getName(Message.ErrorCode.SERVER_EXCEPTION.getIndex()), Message.ErrorCode.SERVER_EXCEPTION.getIndex())));
            return ok(Json.toJson(map));
        }
    }
}
