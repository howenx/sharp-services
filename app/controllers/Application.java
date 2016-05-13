package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import domain.*;
import util.SysParCom;
import net.spy.memcached.MemcachedClient;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import service.CartService;
import service.ThemeService;
import util.cache.MemcachedClientWrapper;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Application extends Controller {


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
            int offset = (pageNum - 1) * SysParCom.PAGE_SIZE;

            Optional<List<Theme>> listOptional = themeService.getThemes(SysParCom.PAGE_SIZE, offset);


            if (listOptional.isPresent()) {
                List<Theme> themeList = listOptional.get().stream().map(l -> {
                    if (l.getThemeImg().contains("url")) {
                        JsonNode jsonNode1 = Json.parse(l.getThemeImg());
                        if (jsonNode1.has("url")) {
                            ((ObjectNode) jsonNode1).put("url", SysParCom.IMAGE_URL + jsonNode1.get("url").asText());
                            l.setThemeImg(Json.stringify(jsonNode1));
                        }
                    } else l.setThemeImg(SysParCom.IMAGE_URL + l.getThemeImg());


                    if (l.getType().equals("ordinary")) {
                        l.setThemeUrl(SysParCom.DEPLOY_URL + "/topic/list/" + l.getId());
                    } else {
                        l.setThemeUrl(l.getH5Link());
                    }

                    return l;
                }).collect(Collectors.toList());

                int page_count = 0;
                if (themeList.size() > 0) {
                    page_count = themeList.get(0).getThemeNum() % SysParCom.PAGE_SIZE == 0 ? themeList.get(0).getThemeNum() / SysParCom.PAGE_SIZE : themeList.get(0).getThemeNum() / SysParCom.PAGE_SIZE + 1;
                }

                Long userId = -1L;
                //消息提醒
                result.putPOJO("msgRemind", 0); //消息不提醒
                Optional<String> header = Optional.ofNullable(request().getHeader("id-token"));
                if (header.isPresent()) {
                    Optional<Object> token = Optional.ofNullable(cache.get(header.get()));
                    if (token.isPresent()) {
                        JsonNode userJson = Json.parse(token.get().toString());
                        userId = Long.valueOf(userJson.findValue("id").asText());    //登录了
                        result.putPOJO("msgRemind", msgRemind(userId)); //消息提醒
                    }
                }

                if (pageNum == 1) {
                    Optional<List<Slider>> listOptionalSlider = themeService.getSlider();
                    if (listOptionalSlider.isPresent()) {
                        //slider取出链接
                        List<Slider> sliderImgList = listOptionalSlider.get().stream().map(s -> {
                            if (s.getImg().contains("url")) {
                                JsonNode jsonNode2 = Json.parse(s.getImg());
                                if (jsonNode2.has("url")) {
                                    ((ObjectNode) jsonNode2).put("url", SysParCom.IMAGE_URL + jsonNode2.get("url").asText());
                                    s.setUrl(Json.stringify(jsonNode2));
                                }
                            }
                            s.setItemTarget(SysParCom.DEPLOY_URL + s.getItemTarget());
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

    /***
     * 消息是否提醒   0-不提醒  1-提醒
     *
     * @param userId
     * @return
     */
    private int msgRemind(Long userId) {
        try {
            MsgRec msgRec = new MsgRec();
            msgRec.setUserId(userId);
            msgRec.setReadStatus(1);
            if (cartService.getNotReadMsgNum(msgRec) > 0) {   //未读消息
                return 1;
            } else {
                Optional<List<Msg>> msgList = Optional.ofNullable(cartService.getNotRecMsg(userId));
                if (msgList.isPresent() && msgList.get().size() > 0) {  //还未接收的系统消息
                    return 1;
                }
            }
        } catch (Exception ex) {
            Logger.info("get msg remind exception" + ex.getMessage());
        }
        return 0;
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
                Optional<Object> token = Optional.ofNullable(cache.get(header.get()));
                if (token.isPresent()) {
                    JsonNode userJson = Json.parse(token.get().toString());
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


    public Result test(){
        new MemcachedClientWrapper().removeGroup("mapper.ThemeMapper");
        return ok("贼你妈的");
    }
}
