package com.sidabw.socket.io.step1;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import com.sidabw.socket.io.commons.ChatObject;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author shaogz
 * @since 2024/4/8 18:17
 */
public class Step1SocketIoServer {

    static String MY_MESSAGE_EVENT = "chatevent";

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Step1SocketIoClient.class);


    public static void main(String[] args) throws InterruptedException {
        Configuration config = new Configuration();
        config.setHostname("localhost");
        config.setPort(9092);

        final SocketIOServer server = new SocketIOServer(config);
        server.addEventListener(MY_MESSAGE_EVENT, ChatObject.class, new DataListener<ChatObject>() {
            @Override
            public void onData(SocketIOClient client, ChatObject data, AckRequest ackRequest) {
                LOGGER.info("server::got message: {}", data);

                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (Exception ignore) {}
                ChatObject chatObject = new ChatObject("kkk", "hhh");
                server.getBroadcastOperations().sendEvent(MY_MESSAGE_EVENT, chatObject);
                LOGGER.info("server::send message: {}", chatObject);
            }
        });

        server.addConnectListener((SocketIOClient client) -> {
            LOGGER.info("server:::EVENT_CONNECT............{}", client.getSessionId());
        });

        server.addDisconnectListener((SocketIOClient client) -> {
            LOGGER.info("server:::EVENT_CONNECT............{}", client.getSessionId());
        });


        server.start();

        Thread.sleep(Integer.MAX_VALUE);

        server.stop();
    }
}
