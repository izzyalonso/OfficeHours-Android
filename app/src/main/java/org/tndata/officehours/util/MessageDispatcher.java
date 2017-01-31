package org.tndata.officehours.util;


import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import org.apache.http.message.BasicNameValuePair;
import org.tndata.officehours.OfficeHoursApp;
import org.tndata.officehours.model.Message;
import org.tndata.officehours.model.Person;
import org.tndata.officehours.model.ResultSet;
import org.tndata.officehours.model.SocketMessage;
import org.tndata.officehours.parser.Parser;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


/**
 * Handles sending messages through a websocket.
 *
 * @author Ismael Alonso
 */
public class MessageDispatcher implements WebSocketClient.Listener, Parser.ParserCallback{
    private static final String TAG = "MessageDispatcher";

    private OfficeHoursApp app;
    private WebSocketClient socket;
    private Listener listener;

    private Queue<Message> messageQueue;
    private boolean connected;


    public MessageDispatcher(@NonNull Context context, @NonNull Person recipient,
                             @NonNull Listener listener){
        try{
            this.app = (OfficeHoursApp)context.getApplicationContext();
            this.listener = listener;
            connected = false;
            messageQueue = new LinkedList<>();
            URI uri = new URI(API.URL.chatSocket(recipient));
            socket = new WebSocketClient(uri, this, getHeaders());
        }
        catch (URISyntaxException usx){
            usx.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
    private List<BasicNameValuePair> getHeaders(){
        List<BasicNameValuePair> headers = new ArrayList<>();
        headers.add(new BasicNameValuePair("Authorization", "Token " + app.getUser().getToken()));
        return headers;
    }

    public void connect(){
        socket.connect();
    }

    public void queue(@NonNull Message message){
        messageQueue.add(message);
        if (connected && messageQueue.size() == 1){
            dispatch();
        }
    }

    public void disconnect(){
        socket.disconnect();
    }

    private void dispatch(){
        socket.send(API.BODY.chatMessage(messageQueue.peek()));
    }

    @Override
    public void onConnect(){
        Log.i(TAG, "Connected to the websocket");
        connected = true;
        if (!messageQueue.isEmpty()){
            dispatch();
        }
    }

    @Override
    public void onMessage(String message){
        Log.d(TAG, "Message received as a String");
        Log.d(TAG, message);
        Parser.parse(message, SocketMessage.class, this);
    }

    @Override
    public void onMessage(byte[] data){
        Log.d(TAG, "Message received as a byte stream");
        String message = new String(data);
        Parser.parse(message, SocketMessage.class, this);
    }

    @Override
    public void onDisconnect(int code, String reason){
        Log.i(TAG, "Disconnected from the socket, reason: " + reason);
        connected = false;
    }

    @Override
    public void onError(Exception error){
        Log.e(TAG, "onError(): " + error.getMessage());
        error.printStackTrace();
    }

    @Override
    public void onProcessResult(int requestCode, ResultSet result){
        //Unused, no heavy ops to perform
    }

    @Override
    public void onParseSuccess(int requestCode, ResultSet result){
        if (result instanceof SocketMessage){
            SocketMessage receivedMessage = (SocketMessage)result;
            if (receivedMessage.getSenderId() == app.getUser().getId()){
                Message message = messageQueue.remove();
                message.sent();
                listener.onMessageSent(message);
            }
            else{
                Message message = new Message(
                        receivedMessage.getSenderId(),
                        app.getUser().getId(),
                        receivedMessage.getText(),
                        System.currentTimeMillis(),
                        true
                );
                listener.onMessageReceived(message);
            }
        }
    }

    @Override
    public void onParseFailed(int requestCode){

    }


    public interface Listener{
        void onMessageSent(@NonNull Message message);
        void onMessageReceived(@NonNull Message message);
    }
}
