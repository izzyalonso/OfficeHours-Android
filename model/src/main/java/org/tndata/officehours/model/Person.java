package org.tndata.officehours.model;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Model representing a user other than whoever is using the app
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public class Person implements Parcelable{
    private long id;
    private String photoUrl;
    private String name;


    public String getPhotoUrl(){
        return photoUrl;
    }

    public String getName(){
        return name;
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags){
        parcel.writeLong(id);
        parcel.writeString(photoUrl);
        parcel.writeString(name);
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
        id = in.readLong();
        photoUrl = in.readString();
        name = in.readString();
    }
}
