package org.tndata.officehours.model;

import com.google.gson.annotations.SerializedName;


/**
 * Model for a chat message.
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public class Message implements ResultSet{
    @SerializedName("from_id")
    private long senderId;
    @SerializedName("from")
    private String sender;
    @SerializedName("text")
    private String text;


    public Message(long senderId, String text){
        this.senderId = senderId;
        this.text = text;
    }

    public long getSenderId(){
        return senderId;
    }

    public String getSender(){
        return sender;
    }

    public String getText(){
        return text;
    }
}
