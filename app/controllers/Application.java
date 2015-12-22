package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import domain.Cart;
import domain.Message;
import domain.Slider;
import domain.Theme;
import filters.UserAuth;
import net.spy.memcached.MemcachedClient;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import service.CartService;
import service.ThemeService;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Application extends Controller {

    //每页固定的取数
    public static final int PAGE_SIZE = 200;

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

        //计算从第几条开始取数据
        int offset = (pageNum - 1) * PAGE_SIZE;

        Optional<List<Theme>> listOptional = themeService.getThemes(PAGE_SIZE, offset);

        //组合结果集
        ObjectNode result = Json.newObject();

        if (listOptional.isPresent()) {
            List<Theme> themeList = listOptional.get().stream().map(l -> {
                l.setThemeImg(IMAGE_URL + l.getThemeImg());
                l.setThemeUrl(DEPLOY_URL + "/topic/list/" + l.getId());
                return l;
            }).collect(Collectors.toList());

            Optional<List<Slider>> listOptionalSlider = themeService.getSlider();

            if (listOptionalSlider.isPresent()) {
                //slider取出链接
                List<Map> sliderImgList = listOptionalSlider.get().stream().map(l -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("url", IMAGE_URL + l.getImg());
                    map.put("itemTarget", DEPLOY_URL + l.getItemTarget());

                    Pattern p=Pattern.compile("\\d+");

                    if (l.getTargetType().equals("D")) {
                        Matcher m=p.matcher(l.getItemTarget());
                        while(m.find()) {
                            map.put("itemTargetAndroid", DEPLOY_URL +"/comm/detail/web/"+ m.group());
                        }
                    }
                    map.put("targetType", l.getTargetType());
                    return map;
                }).collect(Collectors.toList());

                try {
                    result.putPOJO("message", Json.toJson(new Message(Message.ErrorCode.getName(Message.ErrorCode.SUCCESS.getIndex()), Message.ErrorCode.SUCCESS.getIndex())));
                    result.putPOJO("slider", Json.toJson(sliderImgList));
                    result.putPOJO("theme", Json.toJson(themeList));
                    return ok(result);
                } catch (Exception ex) {
                    Logger.error("server exception:" + ex.toString());
                    result.putPOJO("message", Json.toJson(new Message(Message.ErrorCode.getName(Message.ErrorCode.SERVER_EXCEPTION.getIndex()), Message.ErrorCode.SERVER_EXCEPTION.getIndex())));
                    return ok(result);
                }
            } else {
                result.putPOJO("message", Json.toJson(new Message(Message.ErrorCode.getName(Message.ErrorCode.SLIDER_NULL_EXCEPTION.getIndex()), Message.ErrorCode.SLIDER_NULL_EXCEPTION.getIndex())));
                return ok(result);
            }
        } else {
            result.putPOJO("message", Json.toJson(new Message(Message.ErrorCode.getName(Message.ErrorCode.THEME_NULL_EXCEPTION.getIndex()), Message.ErrorCode.THEME_NULL_EXCEPTION.getIndex())));
            return ok(result);
        }

    }

    /**
     * 获取主题列表
     *
     * @param themeId 主题ID
     * @return 返回主题列表JSON
     */
    public Result getThemeList(Long themeId) {
        Optional<String> header = Optional.ofNullable(request().getHeader("id-token"));
        //组合结果集
        ObjectNode result = Json.newObject();
        try {

            if (header.isPresent()) {
                Optional<String> token = Optional.ofNullable(cache.get(header.get()).toString());
                if (token.isPresent()) {
                    JsonNode userJson = Json.parse(token.get());
                    Long userId = Long.valueOf(userJson.findValue("id").asText());
                    Cart cart = new Cart();
                    cart.setUserId(userId);
                    result.putPOJO("cartNum",cartService.getCartByUserSku(cart).size());
                }
            }

            Optional<JsonNode> listOptional = themeService.getThemeList(themeId);
            if (listOptional.isPresent()) {
                result.putPOJO("message", Json.toJson(new Message(Message.ErrorCode.getName(Message.ErrorCode.SUCCESS.getIndex()), Message.ErrorCode.SUCCESS.getIndex())));
                result.putPOJO("themeList", themeService.getThemeList(themeId).get());
                return ok(result);
            } else {
                result.putPOJO("message", Json.toJson(new Message(Message.ErrorCode.getName(Message.ErrorCode.THEME_LIST_NULL_EXCEPTION.getIndex()), Message.ErrorCode.THEME_LIST_NULL_EXCEPTION.getIndex())));
                return ok(result);
            }
        } catch (Exception ex) {
            Logger.error("server exception:" + ex.getMessage());
            result.putPOJO("message", Json.toJson(new Message(Message.ErrorCode.getName(Message.ErrorCode.SERVER_EXCEPTION.getIndex()), Message.ErrorCode.SERVER_EXCEPTION.getIndex())));
            return ok(result);
        }
    }

    /**
     * 获取详细界面
     *
     * @param id    商品ID
     * @param skuId 库存ID
     * @return 返回JSON
     */
    public Result getItemDetail(Long id, Long skuId) {
        Optional<String> header = Optional.ofNullable(request().getHeader("id-token"));
        //组合结果集
        Map<String, Object> map = new HashMap<>();
        try {
            Optional<Map<String, Object>> mapOptional = themeService.getItemDetail(id, skuId);
            if (mapOptional.isPresent()) {
                map = mapOptional.get();
                if (header.isPresent()) {
                    Optional<String> token = Optional.ofNullable(cache.get(header.get()).toString());
                    if (token.isPresent()) {
                        JsonNode userJson = Json.parse(token.get());
                        Long userId = Long.valueOf(userJson.findValue("id").asText());
                        Cart cart = new Cart();
                        cart.setUserId(userId);
                        map.put("cartNum",cartService.getCartByUserSku(cart).size());
                    }
                }
                map.put("message", Json.toJson(new Message(Message.ErrorCode.getName(Message.ErrorCode.SUCCESS.getIndex()), Message.ErrorCode.SUCCESS.getIndex())));
                return ok(Json.toJson(map));
            } else {
                map.put("message", Json.toJson(new Message(Message.ErrorCode.getName(Message.ErrorCode.SKU_DETAIL_NULL_EXCEPTION.getIndex()), Message.ErrorCode.SKU_DETAIL_NULL_EXCEPTION.getIndex())));
                return ok(Json.toJson(map));
            }
        } catch (Exception ex) {
            Logger.error("server exception:" + ex.getMessage());
            map.put("message", Json.toJson(new Message(Message.ErrorCode.getName(Message.ErrorCode.SERVER_EXCEPTION.getIndex()), Message.ErrorCode.SERVER_EXCEPTION.getIndex())));
            return ok(Json.toJson(map));
        }
    }

    /**
     * 获取详细界面,以webView形式返回
     *
     * @param id    商品ID
     * @param skuId 库存ID
     * @return 返回JSON
     */
    public Result getItemDetailWeb(Long id, Long skuId) {
        Optional<String> header = Optional.ofNullable(request().getHeader("id-token"));
        //组合结果集
        Map<String, Object> map = new HashMap<>();
        try {
            Optional<Map<String, Object>> mapOptional = themeService.getItemDetailWeb(id, skuId);
            if (mapOptional.isPresent()) {
                map = mapOptional.get();
                if (header.isPresent()) {
                    Optional<String> token = Optional.ofNullable(cache.get(header.get()).toString());
                    if (token.isPresent()) {
                        JsonNode userJson = Json.parse(token.get());
                        Long userId = Long.valueOf(userJson.findValue("id").asText());
                        Cart cart = new Cart();
                        cart.setUserId(userId);
                        map.put("cartNum",cartService.getCartByUserSku(cart).size());
                    }
                }
                map.put("message", Json.toJson(new Message(Message.ErrorCode.getName(Message.ErrorCode.SUCCESS.getIndex()), Message.ErrorCode.SUCCESS.getIndex())));
                return ok(Json.toJson(map));
            } else {
                map.put("message", Json.toJson(new Message(Message.ErrorCode.getName(Message.ErrorCode.SKU_DETAIL_NULL_EXCEPTION.getIndex()), Message.ErrorCode.SKU_DETAIL_NULL_EXCEPTION.getIndex())));
                return ok(Json.toJson(map));
            }
        } catch (Exception ex) {
            Logger.error("server exception:" + ex.getMessage());
            map.put("message", Json.toJson(new Message(Message.ErrorCode.getName(Message.ErrorCode.SERVER_EXCEPTION.getIndex()), Message.ErrorCode.SERVER_EXCEPTION.getIndex())));
            return ok(Json.toJson(map));
        }
    }

    public void setThemeService(ThemeService themeService) {
        this.themeService = themeService;
    }

    public void setCartService(CartService cartService) {
        this.cartService = cartService;
    }
}
