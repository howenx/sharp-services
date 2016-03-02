package mapper.controllers;


import akka.actor.ActorSystem;
import domain.Msg;
import domain.MsgRec;
import domain.PushMsg;
import modules.RemoteActorModule;
import play.mvc.Controller;
import play.mvc.Result;


import javax.inject.Inject;
import java.sql.Timestamp;

/**
 * 调用shopping项目下的actor
 * Created by sibyl.sun on 16/2/26.
 */
public class ShoppingActorCtrl extends Controller {

    @Inject
    private ActorSystem system;
    @Inject
    private RemoteActorModule remoteActorModule;

    public Result test(){
        addSysMsg("system","title","content","/uploads/minify/f4e65749a1b0407f977d25d1f9ec5c841445411170985.jpg","/comm/detail/888301/111324","D",new Timestamp(System.currentTimeMillis()+24*60*60*1000));
        addMsgRec(1000073L,"system","title","content","/uploads/minify/f4e65749a1b0407f977d25d1f9ec5c841445411170985.jpg","/comm/detail/888301/111324","D");
        sendPushMsg("content","title","/uploads/minify/f4e65749a1b0407f977d25d1f9ec5c841445411170985.jpg","/comm/detail/888301/111324","all");
        return ok("success");
    }

    /**
     * 发送推送消息
     * @param alert  内容
     * @param title  标题
     * @param url    链接
     * @param targetType 目标
     * @param audience  all alias tag
     * @param aliasOrTag alias或者tag时的参数
     */
    public void sendPushMsg(String alert,String title,String url,String targetType,String audience,String ...aliasOrTag){
        PushMsg pushMsg=new PushMsg();
        pushMsg.setAlert(alert);
        pushMsg.setTitle(title);
        pushMsg.setUrl(url);
        pushMsg.setTargetType(targetType);
        pushMsg.setAudience(audience);
        pushMsg.setAliasOrTag(aliasOrTag);
        remoteActorModule.pushActor.tell(pushMsg, null);
    }
    /**
     * 添加给全体发送的系统消息
     * @param msgType 消息类型
     * @param title   标题
     * @param msgContent 内容
     * @param msgImg  图片地址
     * @param msgUrl  url
     * @param targetType T:主题，D:详细页面，P:拼购商品页，A:活动页面，U:一个促销活动的链接
     * @param endAt   失效时间
     * @return
     */
    public void addSysMsg(String msgType,String title,String msgContent,String msgImg,String msgUrl,String targetType,Timestamp endAt){
        Msg msg=new Msg();
        msg.setMsgType(msgType);
        msg.setMsgTitle(title);
        msg.setMsgContent(msgContent);
        msg.setMsgImg(msgImg);
        msg.setMsgUrl(msgUrl);
        msg.setTargetType(targetType);
        msg.setEndAt(endAt);
        remoteActorModule.msgActor.tell(msg,null);

    }

    /**
     * 给
     * 指定用户发送消息
     * @param userId  用户ID
     * @param msgType  消息类型
     * @param title   标题
     * @param msgContent 内容
     * @param msgImg  图片地址
     * @param msgUrl  url
     * @param targetType T:主题，D:详细页面，P:拼购商品页，A:活动页面，U:一个促销活动的链接
     */
    public void addMsgRec(Long userId, String msgType,String title,String msgContent,String msgImg,String msgUrl,String targetType){
        MsgRec msgRec=new MsgRec();
        msgRec.setUserId(userId);
        msgRec.setMsgType(msgType);
        msgRec.setMsgTitle(title);
        msgRec.setMsgContent(msgContent);
        msgRec.setMsgImg(msgImg);
        msgRec.setMsgUrl(msgUrl);
        msgRec.setReadStatus(1);
        msgRec.setDelStatus(1);
        msgRec.setCreateAt(new Timestamp(System.currentTimeMillis()));
        msgRec.setTargetType(targetType);
        msgRec.setMsgRecType(1);
        msgRec.setMsgId(0);
        remoteActorModule.msgActor.tell(msgRec,null);

    }


}
