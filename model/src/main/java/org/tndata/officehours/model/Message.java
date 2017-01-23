package org.tndata.officehours.model;

import com.google.gson.annotations.SerializedName;


/**
 * Created by ialonso on 1/23/17.
 */
public class Message{
    @SerializedName("from_id")
    private long senderId;
    @SerializedName("text")
    private String text;


    public Message(long senderId, String text){
        this.senderId = senderId;
        this.text = text;
    }

    public long getSenderId(){
        return senderId;
    }

    public String getText(){
        return text;
    }
}
