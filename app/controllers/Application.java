package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import domain.*;
import middle.ThemeListMid;
import redis.clients.jedis.Jedis;
import util.RedisPool;
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
import java.util.*;
import java.util.stream.Collectors;

public class Application extends Controller {


    @Inject
    private ThemeService themeService;

    @Inject
    private CartService cartService;

    @Inject
    private MemcachedClient cache;

    @Inject
    private ThemeListMid themeListMid;


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

            Theme tempTheme=new Theme();
            tempTheme.setOffset(offset);
            tempTheme.setPageSize(SysParCom.PAGE_SIZE);
            tempTheme.setThemeState(3);
            tempTheme.setThemeCateCode(1);
            Optional<List<Theme>> listOptional = themeService.getThemes(tempTheme);

            if (listOptional.isPresent()) {
                List<Theme> themeList = listOptional.get().stream().map(l -> {

                    return handleThemeShow(l);

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

                    if(userId>0) { //用户登录了
                        //如果是2状态的，使用redis中SISMEMBER这个主题ID为Key，并将请求中的id-token转为userId作为value，判断用户是否在专用用户之列，如果在责在状态3查询结果基础上增加2状态查询结果
                        tempTheme = new Theme();
                        tempTheme.setThemeState(2);
                        Optional<List<Theme>> themeList2 = themeService.getThemes(tempTheme);
                        if (themeList2.isPresent() && themeList2.get().size() > 0) {
                            List<Theme> themes2=new ArrayList<>();
                            for (Theme theme : themeList2.get()) {
                                try (Jedis jedis = RedisPool.createPool().getResource()) {
                                    if(jedis.sismember(theme.getId()+"", userId + "")){
                                        themes2.add(handleThemeShow(theme));
                                    }
                                }
                            }
                            if(null!=themes2&&themes2.size()>0){
                                themeList.addAll(0,themes2);
                            }
                        }
                    }




                    //导航菜单
                    Slider tempSlider=new Slider();
                    tempSlider.setOrNav(true);
                    Optional<List<Slider>> listOptionalSliderNav = themeService.getSlider(tempSlider);
                    if (listOptionalSliderNav.isPresent()) {
                        //slider取出链接
                        List<SliderNav> sliderNavImgList = listOptionalSliderNav.get().stream().map(s -> {
                            String url="";
                            if (s.getImg().contains("url")) {
                                JsonNode jsonNode2 = Json.parse(s.getImg());
                                if (jsonNode2.has("url")) {
                               //     ((ObjectNode) jsonNode2).put("url", SysParCom.IMAGE_URL + jsonNode2.get("url").asText());
                                //    s.setUrl(Json.stringify(jsonNode2));
                                    url=SysParCom.IMAGE_URL + jsonNode2.get("url").asText();
                                }
                            }

                            if("M".equals(s.getTargetType())){
                                s.setItemTarget(SysParCom.DEPLOY_URL+"/comm/nav/"+s.getId()+"/");
                            }else if(!"U".equals(s.getTargetType())){ //T:主题，D:详细页面，P:拼购商品页，U:一个促销活动的链接（h5主题），M：一对多关系，需要去查询nav_item_cate表
                                s.setItemTarget(SysParCom.DEPLOY_URL + s.getItemTarget());
                            }

                            return new SliderNav(s.getItemTarget(),s.getTargetType(),url,s.getNavText());

                        }).collect(Collectors.toList());
                        result.putPOJO("sliderNav", Json.toJson(sliderNavImgList));
                    }

                    //滚动图
                    Slider temp=new Slider();
                    temp.setSliderType(1);
                    temp.setOrNav(false);
                    Optional<List<Slider>> listOptionalSlider = themeService.getSlider(temp);
                    if (listOptionalSlider.isPresent()) {
                        //slider取出链接
                        List<Slider> sliderImgList = listOptionalSlider.get().stream().map(s -> {
                           // String url="";
                            if (s.getImg().contains("url")) {
                                JsonNode jsonNode2 = Json.parse(s.getImg());
                                if (jsonNode2.has("url")) {
                                   // ((ObjectNode) jsonNode2).put("url", SysParCom.IMAGE_URL + jsonNode2.get("url").asText());
                                    s.setUrl(SysParCom.IMAGE_URL + jsonNode2.get("url").asText());
                                }
                            }
                            //T:主题，D:详细页面，P:拼购商品页，U:一个促销活动的链接（h5主题），M：一对多关系，需要去查询nav_item_cate表
                            if("M".equals(s.getTargetType())){
                                s.setItemTarget(SysParCom.DEPLOY_URL+"/comm/nav/"+s.getId()+"/");
                            }else if(!"U".equals(s.getTargetType())){
                                s.setItemTarget(SysParCom.DEPLOY_URL + s.getItemTarget());
                            }
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
     * 处理主题展示数据
     * @param l
     * @return
     */
    private Theme handleThemeShow(Theme l){
        if (l.getThemeImg().contains("url")) {
            JsonNode jsonNode1 = Json.parse(l.getThemeImg());
            if (jsonNode1.has("url")) {
                ((ObjectNode) jsonNode1).put("url", SysParCom.IMAGE_URL + jsonNode1.get("url").asText());
                l.setThemeImg(Json.stringify(jsonNode1));
            }
        } else l.setThemeImg(SysParCom.IMAGE_URL + l.getThemeImg());


        if (l.getType().equals("ordinary")) {
            l.setThemeUrl(SysParCom.DEPLOY_URL + "/topic/list/" + l.getId());
        } else if (l.getType().equals("detail")||l.getType().equals("pin")) {
            l.setThemeUrl(SysParCom.DEPLOY_URL  + l.getH5Link());
        }else{
            l.setThemeUrl(l.getH5Link());
        }

        return l;
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

    /**
     * 根据展示位置显示主题
     * @param themeCateCode 展示位置（2.展示在拼购频道3.展示在礼品频道）
     * @param pageNum
     * @return
     */
    public Result getThemeByThemeCateCode(int themeCateCode,int pageNum){
        ObjectNode result = Json.newObject();

        if (pageNum > 0) {

            //计算从第几条开始取数据
            int offset = (pageNum - 1) * SysParCom.PAGE_SIZE;

            Theme tempTheme=new Theme();
            tempTheme.setOffset(offset);
            tempTheme.setPageSize(SysParCom.PAGE_SIZE);
            tempTheme.setThemeState(3);
            tempTheme.setThemeCateCode(themeCateCode);
            Optional<List<Theme>> listOptional = themeService.getThemes(tempTheme);

            if (listOptional.isPresent()) {
                List<Theme> themeList = listOptional.get().stream().map(l -> {
                    return handleThemeShow(l);
                }).collect(Collectors.toList());

                int page_count = 0;
                if (themeList.size() > 0) {
                    page_count = themeList.get(0).getThemeNum() % SysParCom.PAGE_SIZE == 0 ? themeList.get(0).getThemeNum() / SysParCom.PAGE_SIZE : themeList.get(0).getThemeNum() / SysParCom.PAGE_SIZE + 1;
                }

                result.putPOJO("message", Json.toJson(new Message(Message.ErrorCode.getName(Message.ErrorCode.SUCCESS.getIndex()), Message.ErrorCode.SUCCESS.getIndex())));
                result.putPOJO("theme", Json.toJson(themeList));
                result.putPOJO("page_count", page_count);
                return ok(result);

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
     * 获取分类数据
     * @param navId
     * @param pageNum
     * @return
     */
    public Result getSkusByNavItemCate(Long navId,int pageNum) {
        ObjectNode result = Json.newObject();

        if (pageNum > 0&&navId>0) {
            List<ThemeItem> themeItemList=new ArrayList<>();
            NavItemCate navItemCate=new NavItemCate();
            navItemCate.setNavId(navId);
            List<NavItemCate> navItemCateList=themeService.getNavItemCate(navItemCate);
            if(null!=navItemCateList&&navItemCateList.size()>0){
                //计算从第几条开始取数据
                int offset = (pageNum - 1) * SysParCom.PAGE_SIZE;
                NavItemCateQuery navItemCateQuery=makeNavItemCateQuery(navItemCateList);
                navItemCateQuery.setPageSize(SysParCom.PAGE_SIZE);
                navItemCateQuery.setOffset(offset);
                List<SkuVo> listOptional = themeService.getSkusByNavItemCate(navItemCateQuery);
                if(null!=listOptional&&listOptional.size()>0){
                    int page_count = listOptional.get(0).getSkuNum() % SysParCom.PAGE_SIZE == 0 ? listOptional.get(0).getSkuNum() / SysParCom.PAGE_SIZE : listOptional.get(0).getSkuNum() / SysParCom.PAGE_SIZE + 1;
                    result.putPOJO("page_count", page_count);
                    if (pageNum == 1) {
                        Slider temp=new Slider();
                        temp.setId(navId);
                        Optional<List<Slider>> listOptionalSlider = themeService.getSlider(temp);
                        String title="商品分类"; //分类界面标题
                        if (listOptionalSlider.isPresent()&&listOptionalSlider.get().size()>0) {
                            title=listOptionalSlider.get().get(0).getNavText();
                        }
                        result.put("title", title);
                    }
                    for(SkuVo skuVo:listOptional){
                        try{
                            themeItemList.add(themeListMid.makeThemeItem(skuVo));
                        }catch (Exception e){

                        }
                    }
                }
            }

            result.putPOJO("themeItemList", Json.toJson(themeItemList));
            result.putPOJO("message", Json.toJson(new Message(Message.ErrorCode.getName(Message.ErrorCode.SUCCESS.getIndex()), Message.ErrorCode.SUCCESS.getIndex())));

            return ok(result);

        } else {
            result.putPOJO("message", Json.toJson(new Message(Message.ErrorCode.getName(Message.ErrorCode.BAD_PARAMETER.getIndex()), Message.ErrorCode.BAD_PARAMETER.getIndex())));
            return badRequest(result);
        }
    }

    /**
     * 构造查询分类条件
     * @param navItemCateList
     * @return
     */
    private NavItemCateQuery makeNavItemCateQuery( List<NavItemCate> navItemCateList){
        NavItemCateQuery navItemCateQuery=new NavItemCateQuery();
        for(NavItemCate navItemCate:navItemCateList){
            //1.商品，2.sku，3.拼购商品，4.商品分类，5.主题
            if(navItemCate.getCateType()==1){
                List<Long> itemIdList=navItemCateQuery.getItemIdList();
                if(null==itemIdList){
                    itemIdList=new ArrayList<>();
                    navItemCateQuery.setItemIdList(itemIdList);
                }
                navItemCateQuery.getItemIdList().add(navItemCate.getCateTypeId());
            }else if(navItemCate.getCateType()==2){
                List<Long> invIdList=navItemCateQuery.getInvIdList();
                if(null==invIdList){
                    invIdList=new ArrayList<>();
                    navItemCateQuery.setInvIdList(invIdList);
                }
                navItemCateQuery.getInvIdList().add(navItemCate.getCateTypeId());
            }else if(navItemCate.getCateType()==3){
                List<Long> list=navItemCateQuery.getPinIdList();
                if(null==list){
                    list=new ArrayList<>();
                    navItemCateQuery.setPinIdList(list);
                }
                navItemCateQuery.getPinIdList().add(navItemCate.getCateTypeId());
            }else if(navItemCate.getCateType()==4){
                List<Long> list=navItemCateQuery.getCateIdList();
                if(null==list){
                    list=new ArrayList<>();
                    navItemCateQuery.setCateIdList(list);
                }
                navItemCateQuery.getCateIdList().add(navItemCate.getCateTypeId());
            }
        }

        return  navItemCateQuery;
    }
}
