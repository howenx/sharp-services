package actor;

import akka.actor.*;
import akka.japi.Procedure;
import domain.AbstractRemoteMsg;
import domain.Msg;
import domain.MsgRec;
import domain.PushMsg;
import play.Logger;
import scala.concurrent.duration.Duration;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by sibyl.sun on 16/2/26.
 */
public class MsgActor extends UntypedActor {
    private final String path;
    private ActorRef msgActor = null;

    public MsgActor(String path) {
        this.path = path;
        sendIdentifyRequest();
    }

    private void sendIdentifyRequest() {
        getContext().actorSelection(path).tell(new Identify(path), getSelf());
        getContext()
                .system()
                .scheduler()
                .scheduleOnce(Duration.create(3, SECONDS), getSelf(),
                        ReceiveTimeout.getInstance(), getContext().dispatcher(), getSelf());
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof ActorIdentity) {
            msgActor = ((ActorIdentity) message).getRef();
            if (msgActor == null) {
                System.out.println("Remote actor not available: " + path);
            } else {
                getContext().watch(msgActor);
                getContext().become(active, true);
            }

        } else if (message instanceof ReceiveTimeout) {
            sendIdentifyRequest();

        } else {
            System.out.println("Not ready yet");

        }
    }

    Procedure<Object> active = new Procedure<Object>() {
        @Override
        public void apply(Object message) {
//            if (message instanceof Msg ) {
//                // send message to server actor
//                msgActor.tell(message, getSelf());
//
//            } if (message instanceof MsgRec) {
//                // send message to server actor
//                msgActor.tell(message, getSelf());
//
//            }if (message instanceof PushMsg) {
//                // send message to server actor
//                msgActor.tell(message, getSelf());
//
//            }
            if (message instanceof AbstractRemoteMsg){
                msgActor.tell(message, getSelf());
            }else {
                unhandled(message);
            }

        }
    };
}
