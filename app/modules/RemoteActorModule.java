package modules;

import actor.MsgActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import play.Configuration;
import play.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by sibyl.sun on 16/3/1.
 */
@Singleton
public class RemoteActorModule {

//    public final  static String pushPath="akka.tcp://application@127.0.0.1:2003/user/push";
//    public final  static String msgPath="akka.tcp://application@127.0.0.1:2003/user/msg";
    /**接收消息*/
    public  static ActorRef msgActor;
    /**接收推送*/
    public static ActorRef pushActor;
    @Inject
    public RemoteActorModule(ActorSystem system, Configuration configuration) {
        String pushPath=configuration.getString("shop.push");
        pushActor = system.actorOf(
                Props.create(MsgActor.class, pushPath), "msgActor");
        System.out.println("Started MsgRecActor,pushPath="+pushPath+",path="+pushActor.path());

        String msgPath=configuration.getString("shop.msg");
        msgActor= system.actorOf(
                Props.create(MsgActor.class, msgPath), "msgActor1");
        System.out.println("Started PushActor,msgPath="+msgPath+",path="+msgActor.path());
    }
}
