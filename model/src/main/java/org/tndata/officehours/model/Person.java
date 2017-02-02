package org.tndata.officehours.model;


import android.os.Parcel;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


/**
 * Model representing a user other than whoever is using the app
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public class Person extends Base{
    @SerializedName("name")
    private String name;
    @SerializedName("avatar")
    private String avatar;

    private boolean isInstructor;
    private String lastMessage;

    private List<Message> messages;

    @ColorInt
    private int color;


    /**
     * Constructor.
     *
     * @param id the id of the person.
     * @param name the name of the person.
     * @param avatar the photo url of the person.
     * @param isInstructor whether the person is an instructor.
     * @param lastMessage the last chat message interchanged with this person.
     */
    public Person(long id, @NonNull String name, @NonNull String avatar, boolean isInstructor, String lastMessage){
        super(id);
        this.name = name;
        this.avatar = avatar;
        this.isInstructor = isInstructor;
        this.lastMessage = lastMessage;
    }

    /**
     * Name getter.
     *
     * @return the name of the person.
     */
    public String getName(){
        return name;
    }

    /**
     * Avatar getter.
     *
     * @return the avatar of the person.
     */
    public String getAvatar(){
        return avatar;
    }

    public boolean isInstructor(){
        return isInstructor;
    }

    public String getLastMessage(){
        return lastMessage == null ? "" : lastMessage;
    }

    public List<Message> getMessages(){
        if (messages == null){
            messages = new ArrayList<>();
        }
        return messages;
    }

    @ColorInt
    public int getColor(){
        return color;
    }

    public void asInstructor(){
        isInstructor = true;
    }

    public void asStudent(){
        isInstructor = false;
    }

    public void setLastMessage(@NonNull String lastMessage){
        this.lastMessage = lastMessage;
    }

    public void setMessages(@NonNull List<Message> messages){
        this.messages = messages;
    }

    public void setColor(@ColorInt int color){
        this.color = color;
    }

    @Override
    public String toString(){
        return "Person #" + getId() + ": " + getName();
    }

    @Override
    public boolean equals(Object o){
        return o instanceof Person && ((Person)o).getId() == getId();
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags){
        super.writeToParcel(parcel, flags);
        parcel.writeString(name);
        parcel.writeString(avatar);
        parcel.writeByte((byte)(isInstructor ? 1 : 0));
        parcel.writeString(getLastMessage());
        parcel.writeInt(color);
        parcel.writeTypedList(getMessages());
    }

    public static final Creator<Person> CREATOR = new Creator<Person>(){
        @Override
        public Person createFromParcel(Parcel in){
            return new Person(in);
        }

        @Override
        public Person[] newArray(int size){
            return new Person[size];
        }
    };

    private Person(Parcel in){
        super(in);
        name = in.readString();
        avatar = in.readString();
        isInstructor = in.readByte() == 0;
        lastMessage = in.readString();
        color = in.readInt();
        messages = new ArrayList<>();
        in.readTypedList(messages, Message.CREATOR);
    }
}
