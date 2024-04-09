package com.sidabw.socket.io.step4;

import com.corundumstudio.socketio.*;
import com.sidabw.socket.io.commons.ChatObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Step4ServerOfRoom {

    private static final Logger log = LoggerFactory.getLogger(Step4ServerOfRoom.class);


    static final String MY_NS_ROOM_MSG_EVENT = "my_ns_room_msg_event";
    static final String NAMESPACE1 = "/chat1";

    static final String ROOM1 = "/room1";
    static final String ROOM2 = "/room2";

    public static void main(String[] args) throws InterruptedException {

        Configuration config = new Configuration();
        config.setHostname("127.0.0.1");
        config.setPort(9092);

        final SocketIOServer server = new SocketIOServer(config);
        final SocketIONamespace chat1namespace = server.addNamespace(NAMESPACE1);
        AtomicInteger i = new AtomicInteger();
        chat1namespace.addConnectListener((SocketIOClient client) -> {
            //前两个client加入到room1里，后面1个加入到room2里
            if (i.getAndIncrement() < 2) {
                client.joinRoom(ROOM1);
            } else {
                client.joinRoom(ROOM1);
                client.joinRoom(ROOM2);
            }
        });

        server.start();


        TimeUnit.SECONDS.sleep(20);
        chat1namespace.getRoomOperations(ROOM1).sendEvent(MY_NS_ROOM_MSG_EVENT, new ChatObject("username1_1", "message1_1"));
        chat1namespace.getRoomOperations(ROOM2).sendEvent(MY_NS_ROOM_MSG_EVENT, new ChatObject("username2_1", "message2_1"));

        Thread.sleep(Integer.MAX_VALUE);
        server.stop();
    }

}
