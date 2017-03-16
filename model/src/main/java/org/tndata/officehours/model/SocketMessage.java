package org.tndata.officehours.model;

import com.google.gson.annotations.SerializedName;


/**
 * Model for a message received through the socket. The rationale of this class' existence
 * is that the formats of the messages exposed by the API and received through the socket
 * are different.
 *
 * @author Ismael Alonso
 */
public class SocketMessage implements ResultSet{
    @SerializedName("from_id")
    private int senderId;
    @SerializedName("text")
    private String text;


    /**
     * Sender ID getter.
     *
     * @return whoever sent the message.
     */
    public int getSenderId(){
        return senderId;
    }

    /**
     * Text getter.
     *
     * @return the content of the message.
     */
    public String getText(){
        return text;
    }
}
