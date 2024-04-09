package com.sidabw.socket.io.step4;

import com.sidabw.socket.io.commons.JsonUtil;
import groovy.util.logging.Slf4j;
import io.socket.client.IO;
import io.socket.client.Socket;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

import static com.sidabw.socket.io.step4.Step4ServerOfRoom.*;


/**
 * @author shaogz
 * @since 2024/4/8 18:17
 */
@Slf4j
public class Step4Client2 {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Step4Client2.class);

    public static void main(String[] args) throws URISyntaxException, InterruptedException {
        IO.Options options = new IO.Options();
        Socket socketClient = IO.socket("http://127.0.0.1:9092"+NAMESPACE1, options);
        //连接成功监听事件
        socketClient.on(Socket.EVENT_CONNECT_ERROR, objects -> {
                    LOGGER.info( "con err. :{}", JsonUtil.serialize(objects));
                })
                //监听事件：连接成功
                .on(Socket.EVENT_CONNECT, objects -> {
                    LOGGER.info("client:::EVENT_CONNECT............dataLen:0 ? {}", objects.length==0);
                })
                //监听事件：断开连接
                .on(Socket.EVENT_DISCONNECT, objects -> {
                    LOGGER.info("client:::EVENT_DISCONNECT............dataLen:0 ? {}", objects.length==0);
                })
                //监听事件：自定义消息
                .on(MY_NS_ROOM_MSG_EVENT, objects -> {
                    LOGGER.info("client::: got msg from event: chatevent............datalen: {}. data class: {}, data:{}",
                            objects.length, objects[0].getClass(), objects[0]);
                });


        socketClient.connect();

        TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
    }


}
