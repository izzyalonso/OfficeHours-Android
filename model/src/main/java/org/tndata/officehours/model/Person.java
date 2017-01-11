package org.tndata.officehours.model;


import android.os.Parcel;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;


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


    /**
     * Constructor.
     *
     * @param id the id of the person.
     * @param name the name of the person.
     * @param avatar the photo url of the person.
     * @param isInstructor whether the person is an instructor.
     */
    public Person(long id, @NonNull String name, @NonNull String avatar, boolean isInstructor){
        super(id);
        this.isInstructor = isInstructor;
        this.avatar = avatar;
        this.name = name;
    }

    public boolean isInstructor(){
        return isInstructor;
    }

    public String getAvatar(){
        return avatar;
    }

    public String getName(){
        return name;
    }

    @Override
    public String toString(){
        String result = "Person #" + getId() + ": " + getName();
        if (isInstructor()){
            result += " (instructor)";
        }
        return result;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags){
        super.writeToParcel(parcel, flags);
        parcel.writeString(name);
        parcel.writeString(avatar);
        parcel.writeByte((byte)(isInstructor ? 1 : 0));
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
        isInstructor = in.readByte() == 1;
    }
}
