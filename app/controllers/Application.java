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

        List<ThemeDto> themeList = themeService.getThemes(PAGE_SIZE,offset);

        for (int i=0;i<themeList.size();i++) {

            ThemeDto themeDto = themeList.get(i);
            themeDto.setThemeImg(IMAGE_URL+themeDto.getThemeImg());
            themeDto.setThemeUrl(DEPLOY_URL+"/topic/list/"+themeDto.getId());
            themeList.set(i,themeDto);
        }

        //slider取出链接
        List<Slider> sliderList = themeService.getSlider();
        List<String> sliderImgList = new ArrayList<>();

        for (int i=0;i<sliderList.size();i++) {
            Slider slider = sliderList.get(i);
            sliderImgList.add(IMAGE_URL + slider.getImg());
        }

        //组合结果集
        ObjectNode result = Json.newObject();
        result.putPOJO("slider", Json.toJson(sliderImgList));
        result.putPOJO("theme",Json.toJson(themeList));

        return ok(result);
    }

    public Result getThemeList(Long themeId){

        List<ThemeListDto> themeListDtoList = themeService.getThemeList(Long.valueOf(100033));

        //对图片url和请求链接进行相应更改
        for (int i=0;i<themeListDtoList.size();i++) {

            ThemeListDto themeListDto = themeListDtoList.get(i);
            themeListDto.setItemImg(IMAGE_URL + themeListDto.getItemImg());
            themeListDto.setItemUrl(DEPLOY_URL + "/comm/detail/" + themeListDto.getItemId());

            if(null!=themeListDto.getMasterItemImg() && !("").equals(themeListDto.getMasterItemImg()) && !"null".equals(themeListDto.getMasterItemImg())){
                themeListDto.setMasterItemImg(IMAGE_URL + themeListDto.getMasterItemImg());
            }

            themeListDtoList.set(i,themeListDto);
        }
        return ok(Json.toJson(themeListDtoList));
    }

    public Result getItemDetail(Long id,Long skuId){

        return ok(Json.toJson(themeService.getItemDetail(id,skuId)));
    }
}
