package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import domain.Address;
import domain.Message;
import domain.ThemeDto;
import domain.ThemeListDto;
import net.spy.memcached.MemcachedClient;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import service.ThemeService;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
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

        List<ThemeDto> themeList = themeService.getThemes(PAGE_SIZE, offset).stream().map(l -> {
            l.setThemeImg(IMAGE_URL + l.getThemeImg());
            l.setThemeUrl(DEPLOY_URL + "/topic/list/" + l.getId());
            return l;
        }).collect(Collectors.toList());

        //slider取出链接
        List<String> sliderImgList = new ArrayList<>(themeService.getSlider().stream().map(l -> (IMAGE_URL + l.getImg())).collect(Collectors.toList()));

        //组合结果集
        ObjectNode result = Json.newObject();
        result.putPOJO("slider", Json.toJson(sliderImgList));
        result.putPOJO("theme", Json.toJson(themeList));

        return ok(result);
    }

    /**
     * 获取主题列表
     *
     * @param themeId 主题ID
     * @return 返回主题列表JSON
     */
    public Result getThemeList(Long themeId) {

        //对图片url和请求链接进行相应更改
        List<ThemeListDto> themeListDtoList = themeService.getThemeList(((Integer) 100033).longValue()).stream().map(l -> {
            l.setItemImg(IMAGE_URL + l.getItemImg());
            l.setItemUrl(DEPLOY_URL + "/comm/detail/" + l.getItemId());

            if (null != l.getMasterItemImg() && !("").equals(l.getMasterItemImg()) && !"null".equals(l.getMasterItemImg())) {
                l.setMasterItemImg(IMAGE_URL + l.getMasterItemImg());
            }
            return l;
        }).collect(Collectors.toList());

        return ok(Json.toJson(themeListDtoList));
    }

    /**
     * 获取详细界面
     *
     * @param id    商品ID
     * @param skuId 库存ID
     * @return 返回JSON
     */
    public Result getItemDetail(Long id, Long skuId) {

        return ok(Json.toJson(themeService.getItemDetail(id, skuId)));
    }

    public Result putAddress() {
        JsonNode json = request().body().asJson();
        Logger.error("客户端返回:" + json.toString());
        Message message = new Message(Message.ErrorCode.getName(Message.ErrorCode.SUCCESS.getIndex()),Message.ErrorCode.SUCCESS.getIndex());

        //组合结果集
        ObjectNode result = Json.newObject();
        result.putPOJO("message", Json.toJson(message));
        return ok(result);
    }

    /**
     * 获取用户收货地址列表
     *
     * @return 返回Json
     */
    public Result allAddress() {
        ObjectNode result = Json.newObject();
        try{
            Logger.debug("测试用户信息:" + cache.get("1d3f3fcf313b7b0332d64db15986bd66").toString());
            Logger.error(Json.parse(cache.get("1d3f3fcf313b7b0332d64db15986bd66").toString()).findValue("id").asText());


            List<Address> addressList = themeService.getAddress(Long.valueOf(Json.parse(cache.get("1d3f3fcf313b7b0332d64db15986bd66").toString()).findValue("id").asText()));
            

            result.putPOJO("message", Json.toJson(new Message(Message.ErrorCode.getName(Message.ErrorCode.SUCCESS.getIndex()),Message.ErrorCode.SUCCESS.getIndex())));
            result.putPOJO("address", Json.toJson(themeService.getAddress(Long.valueOf(Json.parse(cache.get("1d3f3fcf313b7b0332d64db15986bd66").toString()).findValue("id").asText()))));
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
}
