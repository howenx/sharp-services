package mapper.controllers;

import play.mvc.Controller;
import play.mvc.Result;

/**
 * 关于我们,隐私政策这些页面
 * Created by howen on 16/1/21.
 */
public class CommonViewCtrl extends Controller {

    public Result aboutUs() {
        return ok(views.html.aboutus.render());
    }

    public Result privacy(){
        return ok(views.html.privacy.render());
    }

    public Result agreement(){
        return ok(views.html.agreement.render());
    }

}
