package mapper.controllers;

import play.mvc.Controller;
import play.mvc.Result;

/**
 * Created by sibyl.sun on 16/2/25.
 */
public class TestCtrl extends Controller {

    public Result test(){

        return ok("success");
    }


}
