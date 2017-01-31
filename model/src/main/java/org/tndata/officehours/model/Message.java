package org.tndata.officehours.model;

import android.os.Parcel;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * Model for a chat message.
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public class Message extends Base{
    @SerializedName("user")
    private long senderId;
    private long recipientId;
    @SerializedName("user_full_name")
    private String sender;
    @SerializedName("text")
    private String text;
    @SerializedName("read")
    private boolean isRead;
    @SerializedName("created_at")
    private String createdAt;

    private long timestamp;
    private boolean isSent;


    public Message(long senderId, @NonNull String text){
        this(senderId, text, -1);
    }

    public Message(long senderId, @NonNull String text, long timestamp){
        this(senderId, -1, text, timestamp, false);
    }

    public Message(long senderId, long recipientId, @NonNull String text, long timestamp, boolean isSent){
        super(-1);
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.text = text;
        this.isRead = false;
        this.timestamp = timestamp;
        this.isSent = isSent;
    }

    public long getSenderId(){
        return senderId;
    }

    public long getRecipientId(){
        return recipientId;
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

    public String getTime(){
        DateFormat formatter = new SimpleDateFormat("h:mm a", Locale.getDefault());
        Date date = new Date();
        date.setTime(timestamp);
        return formatter.format(date);
    }

    public boolean isSent(){
        return isSent;
    }

    public void read(){
        isRead = true;
    }

    public void setTimestamp(long timestamp){
        this.timestamp = timestamp;
    }

    public void sent(){
        isSent = true;
        timestamp = System.currentTimeMillis();
    }

    public void process(){
        DateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:m:s.SZ", Locale.getDefault());
        try{
            timestamp = parser.parse(createdAt).getTime();
        }
        catch (ParseException px){
            px.printStackTrace();
        }
    }

    public void become(@NonNull Message message){
        setId(message.getId());
        this.isRead = message.isRead();
        this.timestamp = message.getTimestamp();
        this.isSent = message.isSent();
    }

    @Override
    public String toString(){
        return "Message (id: " + getId() +  ", sender: " + senderId + "): " + text;
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
