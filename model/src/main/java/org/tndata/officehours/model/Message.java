package org.tndata.officehours.model;

import android.os.Parcel;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;


/**
 * Model for a chat message.
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public class Message extends Base{
    @SerializedName("from_id")
    private long senderId;
    @SerializedName("from")
    private String sender;
    @SerializedName("text")
    private String text;
    @SerializedName("read")
    private boolean isRead;
    @SerializedName("created_at")
    private String createdAt;

    private long timestamp;


    public Message(long senderId, @NonNull String text, long timestamp){
        this(-1, senderId, text, timestamp);
    }

    public Message(long id, long senderId, @NonNull String text, long timestamp){
        super(id);
        this.senderId = senderId;
        this.text = text;
        this.isRead = false;
        this.timestamp = timestamp;
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

    public boolean isRead(){
        return isRead;
    }

    public long getTimestamp(){
        return timestamp;
    }

    public void read(){
        isRead = true;
    }

    public void process(){
        //TODO read createdAt into a date and save the timestamp in milliseconds since epoch
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags){
        super.writeToParcel(parcel, flags);
        parcel.writeLong(senderId);
        parcel.writeString(sender);
        parcel.writeString(text);
        parcel.writeByte((byte)(isRead ? 1 : 0));
    }

    public static final Creator<Message> CREATOR = new Creator<Message>(){
        @Override
        public Message createFromParcel(Parcel in){
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size){
            return new Message[size];
        }
    };

    private Message(Parcel in){
        super(in);
        senderId = in.readLong();
        sender = in.readString();
        text = in.readString();
        isRead = in.readByte() == 1;
    }
}
