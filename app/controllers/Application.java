package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import domain.Address;
import domain.Message;
import domain.Theme;
import net.spy.memcached.MemcachedClient;
import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import service.ThemeService;

import javax.inject.Inject;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Application extends Controller {

    //每页固定的取数
    public static final int PAGE_SIZE = 20;

    //图片服务器url
    public static final String IMAGE_URL = play.Play.application().configuration().getString("image.server.url");

    //发布服务器url
    public static final String DEPLOY_URL = play.Play.application().configuration().getString("deploy.server.url");

    @Inject
    private ThemeService themeService;

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

        List<Theme> themeList = themeService.getThemes(PAGE_SIZE, offset).stream().map(l -> {
            l.setThemeImg(IMAGE_URL + l.getThemeImg());
            l.setThemeUrl(DEPLOY_URL + "/topic/list/" + l.getId());
            return l;
        }).collect(Collectors.toList());

        //slider取出链接
        List<Map> sliderImgList = new ArrayList<>(themeService.getSlider().stream().map(l -> {
            Map<String,Object> map  = new HashMap<>();
            map.put("url",IMAGE_URL + l.getImg());
            map.put("itemTarget","");
            return map;
        }).collect(Collectors.toList()));

        //组合结果集
        ObjectNode result = Json.newObject();
        try{
            result.putPOJO("message", Json.toJson(new Message(Message.ErrorCode.getName(Message.ErrorCode.SUCCESS.getIndex()),Message.ErrorCode.SUCCESS.getIndex())));
            result.putPOJO("slider", Json.toJson(sliderImgList));
            result.putPOJO("theme", Json.toJson(themeList));
            return ok(result);
        }catch (Exception ex){
            Logger.error("server exception:"+ex.toString());
            result.putPOJO("message", Json.toJson(new Message(Message.ErrorCode.getName(Message.ErrorCode.SERVER_EXCEPTION.getIndex()),Message.ErrorCode.SERVER_EXCEPTION.getIndex())));
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
        //组合结果集
        ObjectNode result = Json.newObject();
        try{
            result.putPOJO("message", Json.toJson(new Message(Message.ErrorCode.getName(Message.ErrorCode.SUCCESS.getIndex()),Message.ErrorCode.SUCCESS.getIndex())));
            result.putPOJO("themeList", Json.toJson(themeService.getThemeList(themeId)));
            return ok(result);
        }catch (Exception ex){
            Logger.error("server exception:"+ex.toString());
            result.putPOJO("message", Json.toJson(new Message(Message.ErrorCode.getName(Message.ErrorCode.SERVER_EXCEPTION.getIndex()),Message.ErrorCode.SERVER_EXCEPTION.getIndex())));
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
        //组合结果集
        Map<String,Object> map = new HashMap<>();
        try{
            map = themeService.getItemDetail(id, skuId);
            map.put("message", Json.toJson(new Message(Message.ErrorCode.getName(Message.ErrorCode.SUCCESS.getIndex()),Message.ErrorCode.SUCCESS.getIndex())));
            return ok(Json.toJson(map));
        }catch (Exception ex){
            Logger.error("server exception:"+ex.toString());
            map.put("message", Json.toJson(new Message(Message.ErrorCode.getName(Message.ErrorCode.SERVER_EXCEPTION.getIndex()),Message.ErrorCode.SERVER_EXCEPTION.getIndex())));
            return ok(Json.toJson(map));
        }
    }

    /**
     * 获取用户收货地址列表
     *
     * @return 返回Json
     */
    public Result allAddress() {
        ObjectNode result = Json.newObject();
        try{
            Logger.error("测试用户信息:" + cache.get("1d3f3fcf313b7b0332d64db15986bd66").toString());
            List<Address> addressList = themeService.getAddress(Long.valueOf(Json.parse(cache.get("1d3f3fcf313b7b0332d64db15986bd66").toString()).findValue("id").asText()));
            result.putPOJO("message", Json.toJson(new Message(Message.ErrorCode.getName(Message.ErrorCode.SUCCESS.getIndex()),Message.ErrorCode.SUCCESS.getIndex())));
            result.putPOJO("address", Json.toJson(addressList));
            return ok(result);
        }catch (NullPointerException ex_null){
            Logger.error("404 cache not found token:"+ex_null.toString());
            result.putPOJO("message", Json.toJson(new Message(Message.ErrorCode.getName(Message.ErrorCode.BAD_USER_TOKEN.getIndex()),Message.ErrorCode.BAD_USER_TOKEN.getIndex())));
            return ok(result);
        }catch (Exception ex){
            Logger.error("server exception:"+ex.toString());
            result.putPOJO("message", Json.toJson(new Message(Message.ErrorCode.getName(Message.ErrorCode.SERVER_EXCEPTION.getIndex()),Message.ErrorCode.SERVER_EXCEPTION.getIndex())));
            return ok(result);
        }
    }

    /**
     * 更新用户收货地址列表
     *
     * @return 返回Json
     */
    public Result handleAddress(Integer handle) {
//        long start = System.currentTimeMillis();
        JsonNode json = request().body().asJson();
        ObjectNode result = Json.newObject();
        Logger.error("客户端返回:" + json);
        try{
            Logger.error("测试用户信息:" + cache.get(request().getHeader("id-token")).toString());
            Address address = Json.fromJson(json,Address.class);
            address.setUserId(Long.valueOf(Json.parse(cache.get(request().getHeader("id-token")).toString()).findValue("id").asText()));
            long start1 = System.currentTimeMillis();
            if (themeService.handleAddress(address,handle)){
                result.putPOJO("message", Json.toJson(new Message(Message.ErrorCode.getName(Message.ErrorCode.SUCCESS.getIndex()),Message.ErrorCode.SUCCESS.getIndex())));
//                Logger.error("运行时间1：" +(System.currentTimeMillis()-start)+ "毫秒");
//                Logger.error("运行时间2：" +(System.currentTimeMillis()-start1)+ "毫秒");
                result.putPOJO("address",Json.toJson(address));
                Logger.error("返回ID:"+result.toString());
                return ok(result);
            }
            else {
                result.putPOJO("message", Json.toJson(new Message(Message.ErrorCode.getName(Message.ErrorCode.DATABASE_EXCEPTION.getIndex()),Message.ErrorCode.DATABASE_EXCEPTION.getIndex())));
//                Logger.error("运行时间：" +(System.currentTimeMillis()-start)+ "毫秒");
                return ok(result);
            }
        }catch (Exception ex){
            Logger.error("server exception:"+ex.toString());
            result.putPOJO("message", Json.toJson(new Message(Message.ErrorCode.getName(Message.ErrorCode.SERVER_EXCEPTION.getIndex()),Message.ErrorCode.SERVER_EXCEPTION.getIndex())));
            return ok(result);
        }
    }

    //测试File
    @BodyParser.Of(value = BodyParser.Json.class, maxLength = 10*1024 * 1024)
    public Result testFile(){
//        Logger.error("测试request:" + request().body());
        JsonNode json = request().body().asJson();
        ObjectNode result = Json.newObject();
        Logger.error("客户端返回:" + json);
        try{
            Logger.error("真实姓名"+json.findValue("realName").toString());
            Logger.error("身份证编号"+json.findValue("cardNum").toString());
            byte[] bufferA= org.apache.commons.codec.binary.Base64.decodeBase64(json.findValue("cardImgA").toString());
            FileOutputStream outA = new FileOutputStream(new File("/Users/howen/git/style-services/1."+json.findValue("aType").asText()));
            outA.write(bufferA);
            outA.flush();
            outA.close();
            byte[] bufferB= org.apache.commons.codec.binary.Base64.decodeBase64(json.findValue("cardImgB").toString());
            FileOutputStream outB = new FileOutputStream(new File("/Users/howen/git/style-services/2."+json.findValue("bType").asText()));
            outB.write(bufferB);
            outB.flush();
            outB.close();
            Logger.error("测试用户信息:" + cache.get(request().getHeader("id-token")).toString());
            result.putPOJO("message", Json.toJson(new Message(Message.ErrorCode.getName(Message.ErrorCode.SUCCESS.getIndex()),Message.ErrorCode.SUCCESS.getIndex())));
            return ok(result);
        }catch (Exception ex){
            Logger.error("server exception:"+ex.toString());
            result.putPOJO("message", Json.toJson(new Message(Message.ErrorCode.getName(Message.ErrorCode.SERVER_EXCEPTION.getIndex()),Message.ErrorCode.SERVER_EXCEPTION.getIndex())));
            return ok(result);
        }
    }
}
