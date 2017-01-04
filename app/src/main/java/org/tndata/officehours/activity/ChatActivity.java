package org.tndata.officehours.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.apache.http.message.BasicNameValuePair;
import org.tndata.officehours.util.WebSocketClient;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Created by ialonso on 1/4/17.
 */
public class ChatActivity extends AppCompatActivity implements WebSocketClient.Listener{
    private static final String TAG = "SocketTest";


    private WebSocketClient socketClient;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        try{
            socketClient = new WebSocketClient(new URI("wss://staging.tndata.org/chat/ialonso/"), this, new ArrayList<BasicNameValuePair>());
            socketClient.connect();
        }
        catch (URISyntaxException usx){
            usx.printStackTrace();
        }
    }

    @Override
    public void onConnect(){
        Log.d(TAG, "onConnect()");
    }

    @Override
    public void onMessage(String message){
        Log.d(TAG, "onMessage()");
        Log.d(TAG, message);
    }

    @Override
    public void onMessage(byte[] data){
        Log.d(TAG, "onMessage()");
    }

    @Override
    public void onDisconnect(int code, String reason){
        Log.d(TAG, "onDisconnect()");
        Log.d(TAG, reason);
    }

    @Override
    public void onError(Exception error){
        Log.d(TAG, "onError()");
        error.printStackTrace();
    }
}
