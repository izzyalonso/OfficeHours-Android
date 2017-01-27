package org.tndata.officehours.util;


import org.tndata.officehours.model.Message;

import java.util.Queue;


/**
 * Created by isma on 1/27/17.
 */
public class MessageDispatcher implements WebSocketClient.Listener{
    private Queue<Message> message;
    private boolean connected;


    @Override
    public void onConnect(){
        connected = true;
    }

    @Override
    public void onMessage(String message){

    }

    @Override
    public void onMessage(byte[] data){

    }

    @Override
    public void onDisconnect(int code, String reason){

    }

    @Override
    public void onError(Exception error){

    }
}
