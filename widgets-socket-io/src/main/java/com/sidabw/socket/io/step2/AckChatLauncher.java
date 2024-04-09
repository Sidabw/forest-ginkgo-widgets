package com.sidabw.socket.io.step2;

import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.listener.DataListener;
import com.sidabw.socket.io.commons.ChatObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class AckChatLauncher {

    private static final Logger log = LoggerFactory.getLogger(AckChatLauncher.class);

    public static void main(String[] args) throws InterruptedException {

        Configuration config = new Configuration();
        config.setHostname("localhost");
        config.setPort(9092);

        LinkedList<SocketIOClient> clients = new LinkedList<>();
        final SocketIOServer server = new SocketIOServer(config);
        server.addEventListener("ackevent1", ChatObject.class, new DataListener<ChatObject>() {
            @Override
            public void onData(final SocketIOClient client, ChatObject data, final AckRequest ackRequest) {

                log.info("data: {}", data);
                if (ackRequest.isAckRequested()) {
                    ackRequest.sendAckData("client message was delivered to server!", "yeah!");
                }
                clients.add(client);
            }
        });

        server.start();


        try {
            while (clients.isEmpty()) {
                TimeUnit.SECONDS.sleep(1);
            }
        }catch (Exception ignore) {}

        SocketIOClient client = clients.get(0);
        // send message back to client with ack callback WITH data
        ChatObject ackChatObjectData = new ChatObject("sidabw", "message with ack data");
        client.sendEvent("ackevent2", new AckCallback<String>(String.class) {
            @Override
            public void onSuccess(String result) {
                log.info("ack from client: " + client.getSessionId() + " data: " + result);
            }
        }, ackChatObjectData);

        ChatObject ackChatObjectData1 = new ChatObject("sidabw", "message with void ack");
        client.sendEvent("ackevent3", new VoidAckCallback() {

            protected void onSuccess() {
                log.info("void ack from: " + client.getSessionId());
            }

        }, ackChatObjectData1);

        Thread.sleep(Integer.MAX_VALUE);
        server.stop();
    }

}
