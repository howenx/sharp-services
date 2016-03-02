package modules;

import actor.MsgActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import play.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by sibyl.sun on 16/3/1.
 */
@Singleton
public class RemoteActorModule {
//    @Inject
//    public ActorSystem system;
//    @Inject
//    private MsgService msgService;
//    @Inject
//    private PushCtrl pushCtrl;

    public final  static String pushPath="akka.tcp://application@127.0.0.1:2003/user/push";
    public final  static String msgPath="akka.tcp://application@127.0.0.1:2003/user/msg";
    /**接收消息*/
    public  static ActorRef msgActor;
    /**接收推送*/
    public static ActorRef pushActor;
    @Inject
    public RemoteActorModule(ActorSystem system) {
        pushActor = system.actorOf(
                Props.create(MsgActor.class, pushPath), "msgActor");
        System.out.println("Started MsgRecActor,path="+pushActor.path());

        msgActor= system.actorOf(
                Props.create(MsgActor.class, msgPath), "msgActor1");
        System.out.println("Started PushActor,path="+msgActor.path());
    }
}
