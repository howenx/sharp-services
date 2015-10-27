package controllers;

import domain.Theme;
import domain.ThemeDto;
import domain.ThemeListDto;
import domain.Vuser;
import play.Logger;
import play.api.Play;
import play.libs.Json;
import play.mvc.*;

import service.H2Service;
import service.PostgresqlService;
import service.ThemeService;
import views.html.*;

import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.Valid;
import play.data.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Application extends Controller {

    @Named("ServiceH2")
    @Inject
    private H2Service h2Service;

    @Inject
    private PostgresqlService postgresqlService;

    //每页固定的取数
    public static final int PAGE_SIZE = 20;

    //图片服务器url
    public static final String IMAGE_URL = play.Play.application().configuration().getString("image.server.url");

    @Inject
    private ThemeService themeService;

    public Result listUsers(String lang) {
        return ok(users.render(h2Service.getAllUser(),lang));
    }

    public Result listBrands(String lang) {
        return ok(brands.render(postgresqlService.getAllBrand(),lang));
    }

    public Result validate(String lang) {
        //        if (user.validate()!=null){
        //
        //        }
        //        Form<Vuser> userForm = Form.form(Vuser.class);
        //        Vuser user = userForm.bindFromRequest().get();
        return ok(validate.render(lang));
    }

    public Result validation(){
        return ok("success");
    }


    public Result getThemes(int pageNum){

        //计算从第几条开始取数据
        int offset = (pageNum-1)*PAGE_SIZE;

        List<ThemeDto> themeList = themeService.getThemes(PAGE_SIZE,offset);

        for (int i=0;i<themeList.size();i++) {
            ThemeDto themeDto = themeList.get(i);
            themeDto.setThemeImg(IMAGE_URL+themeDto.getThemeImg());
            themeDto.setThemeUrl(IMAGE_URL+"/getthemelist/"+themeDto.getId());
            themeList.set(i,themeDto);
        }

        return ok(Json.toJson(themeList));
    }

    public Result getThemeList(Long themeId){

        List<ThemeListDto> themeListDtoList = themeService.getThemeList(themeId);

        return ok(Json.toJson(themeListDtoList));
    }
}
