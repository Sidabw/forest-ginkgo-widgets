package com.sidabw.socket.io.step3;

import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.listener.DataListener;
import com.sidabw.socket.io.commons.ChatObject;
import com.sidabw.socket.io.step2.AckChatLauncher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class Step3ServerOfNamespace {

    private static final Logger log = LoggerFactory.getLogger(Step3ServerOfNamespace.class);


    static final String MY_NS_MSG_EVENT = "my_ns_msg_event";
    static final String NAMESPACE1 = "/chat1";
    static final String NAMESPACE2 = "/chat2";

    public static void main(String[] args) throws InterruptedException {

        Configuration config = new Configuration();
        config.setHostname("127.0.0.1");
        config.setPort(9092);

        final SocketIOServer server = new SocketIOServer(config);
        final SocketIONamespace chat1namespace = server.addNamespace(NAMESPACE1);
        chat1namespace.addEventListener(MY_NS_MSG_EVENT, ChatObject.class, new DataListener<ChatObject>() {
            @Override
            public void onData(SocketIOClient client, ChatObject data, AckRequest ackRequest) {
                log.info("got msg on namespace:{} from client:{}. data:{}", NAMESPACE1, client.getSessionId(), data);
            }
        });

        final SocketIONamespace chat2namespace = server.addNamespace(NAMESPACE2);
        chat2namespace.addEventListener(MY_NS_MSG_EVENT, ChatObject.class, new DataListener<ChatObject>() {
            @Override
            public void onData(SocketIOClient client, ChatObject data, AckRequest ackRequest) {
                log.info("got msg on namespace:{} from client:{}. data:{}", NAMESPACE2, client.getSessionId(), data);
            }
        });

        server.start();


        TimeUnit.SECONDS.sleep(20);
        chat1namespace.getBroadcastOperations().sendEvent(MY_NS_MSG_EVENT, new ChatObject("username1_1", "message1_1"));

        TimeUnit.SECONDS.sleep(20);
        chat2namespace.getBroadcastOperations().sendEvent(MY_NS_MSG_EVENT, new ChatObject("username2_1", "message2_1"));

        Thread.sleep(Integer.MAX_VALUE);
        server.stop();
    }

}
