package controllers;

import domain.ThemeDto;
import domain.ThemeListDto;
import play.libs.Json;
import play.mvc.*;

import service.ThemeService;
import views.html.*;

import javax.inject.Inject;
import javax.inject.Named;

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


    public Result getThemes(int pageNum){

        //计算从第几条开始取数据
        int offset = (pageNum-1)*PAGE_SIZE;

        List<ThemeDto> themeList = themeService.getThemes(PAGE_SIZE,offset);

        for (int i=0;i<themeList.size();i++) {
            ThemeDto themeDto = themeList.get(i);
            themeDto.setThemeImg(IMAGE_URL+themeDto.getThemeImg());
            themeDto.setThemeUrl(DEPLOY_URL+"/getthemelist/"+themeDto.getId());
            themeList.set(i,themeDto);
        }

        return ok(Json.toJson(themeList));
    }

    public Result getThemeList(Long themeId){

        List<ThemeListDto> themeListDtoList = themeService.getThemeList(Long.valueOf(100033));

        for (int i=0;i<themeListDtoList.size();i++) {
            ThemeListDto themeListDto = themeListDtoList.get(i);
            themeListDto.setItemImg(IMAGE_URL + themeListDto.getItemImg());
            themeListDto.setItemUrl(DEPLOY_URL + "/getitem/" + themeListDto.getItemId());

            if(null!=themeListDto.getMasterItemImg() && !("").equals(themeListDto.getMasterItemImg()) && !"null".equals(themeListDto.getMasterItemImg())){
                themeListDto.setMasterItemImg(IMAGE_URL + themeListDto.getMasterItemImg());
            }
            themeListDtoList.set(i,themeListDto);
        }
        return ok(Json.toJson(themeListDtoList));
    }
}
