package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import domain.Slider;
import domain.ThemeDto;
import domain.ThemeListDto;
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

    public Result getIndex(int pageNum){

        //计算从第几条开始取数据
        int offset = (pageNum-1)*PAGE_SIZE;

        List<ThemeDto> themeList = themeService.getThemes(PAGE_SIZE,offset).stream().map(l -> {
            l.setThemeImg(IMAGE_URL+l.getThemeImg());
            l.setThemeUrl(DEPLOY_URL+"/topic/list/"+l.getId());
            return l;
        }).collect(Collectors.toList());

        //slider取出链接
        List<Slider> sliderList = themeService.getSlider().stream().map(l -> {
            l.setImg(IMAGE_URL + l.getImg());
            return l;
        }).collect(Collectors.toList());


        //组合结果集
        ObjectNode result = Json.newObject();
        result.putPOJO("slider", Json.toJson(sliderList));
        result.putPOJO("theme",Json.toJson(themeList));

        return ok(result);
    }

    public Result getThemeList(Long themeId){

        //对图片url和请求链接进行相应更改
        List<ThemeListDto> themeListDtoList = themeService.getThemeList(((Integer)100033).longValue()).stream().map(l -> {
            l.setItemImg(IMAGE_URL + l.getItemImg());
            l.setItemUrl(DEPLOY_URL + "/comm/detail/" + l.getItemId());

            if(null!=l.getMasterItemImg() && !("").equals(l.getMasterItemImg()) && !"null".equals(l.getMasterItemImg())){
                l.setMasterItemImg(IMAGE_URL + l.getMasterItemImg());
            }
            return l;
        }).collect(Collectors.toList());

        return ok(Json.toJson(themeListDtoList));
    }

    public Result getItemDetail(Long id,Long skuId){

        return ok(Json.toJson(themeService.getItemDetail(id,skuId)));
    }
}
