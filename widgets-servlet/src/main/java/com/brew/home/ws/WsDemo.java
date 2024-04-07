package com.brew.home.ws;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.server.ServerEndpoint;

/**
 * @author shaogz
 * @since 2024/4/6 16:44
 */
@ServerEndpoint("/websocketendpoint")
public class WsDemo {

    @OnOpen
    public void onOpen(){
        System.out.println("Open Connection ...............");
    }

    @OnClose
    public void onClose(){
        System.out.println("Close Connection ...............");
    }

    @OnMessage
    public String onMessage(String message){
        System.out.println("Message from the client: " + message);
        String echoMsg = "Echo from the server : " + message;
        return echoMsg;
    }

    @OnError
    public void onError(Throwable e){
        e.printStackTrace();
    }
}
