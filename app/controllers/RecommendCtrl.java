package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import domain.*;
import middle.ThemeListMid;
import net.spy.memcached.MemcachedClient;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import service.CartService;
import service.ThemeService;
import util.SysParCom;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sibyl.sun on 16/8/29.
 */
public class RecommendCtrl extends Controller {


    @Inject
    private ThemeService themeService;

    @Inject
    private CartService cartService;

    @Inject
    private MemcachedClient cache;

    @Inject
    private ThemeListMid themeListMid;

    /**
     * 获取推荐商品
     * @param position
     * @return
     */
    public Result getRecommendSku(int position){
        ObjectNode result = Json.newObject();
        if (position>0) {
            List<ThemeItem> themeItemList=new ArrayList<>();
            SkuVo temp=new SkuVo();
            if(position==4){
                temp.setSkuType("pin");
            }
            temp.setLimitNum(SysParCom.PAGE_SIZE);
            List<SkuVo> listOptional = themeService.getRecommendSku(temp);
            if(null!=listOptional&&listOptional.size()>0){
                for(SkuVo skuVo:listOptional){
                    try{
                        themeItemList.add(themeListMid.makeThemeItem(skuVo));
                    }catch (Exception e){
                        Logger.error("构造ThemeItem异常"+skuVo);
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

}
