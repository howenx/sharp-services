package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import domain.Message;
import play.Logger;
import play.Play;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.io.File;

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

    public Result androidDownload(String fileName){
        ObjectNode result = Json.newObject();

        File file = Play.application().getFile("/conf/android/"+fileName);
        //hanmimei-v0.1.2patch.apk
        if (file.exists()){
            Logger.info("有人在下载Android APP: "+request().remoteAddress());
            return ok(Play.application().getFile("/conf/android/"+fileName));
        }else{
            result.putPOJO("message", Json.toJson(new Message(Message.ErrorCode.getName(Message.ErrorCode.FAILURE_REQUEST_ERROR.getIndex()), Message.ErrorCode.FAILURE_REQUEST_ERROR.getIndex())));
            return badRequest(result);
        }
    }
}
