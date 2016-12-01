package org.tndata.officehours.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


/**
 * Model class for a course.
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public class Course implements Parcelable{
    @SerializedName("id")
    private long id;
    @SerializedName("code")
    private String code;
    @SerializedName("whatever")
    private String name;
    @SerializedName("whatever")
    private String time;
    @SerializedName("expiration")
    private String expirationDate;

    private String accessCode;
    private String instructor;


    /**
     * Constructor.
     *
     * @param name the name of the course.
     * @param time the time of the course.
     */
    public Course(String code, String name, String time){
        this(-1, code, name, time);
    }

    /**
     * Constructor.
     *
     * @param id the id of the course.
     * @param name the name of the course.
     * @param time the time of the course.
     */
    public Course(long id, String code, String name, String time){
        this.id = id;
        this.code = code;
        this.name = name;
        this.time = time;
    }

    /**
     * Id getter.
     *
     * @return the id.
     */
    public long getId(){
        return id;
    }

    /**
     * Code getter.
     *
     * @return the course code.
     */
    public String getCode(){
        return code;
    }

    /**
     * Name getter.
     *
     * @return the name of the course.
     */
    public String getName(){
        return name;
    }

    /**
     * Display name getter. Formatted <code>: <name>
     *
     * @return the display name of the course
     */
    public String getDisplayName(){
        return code + ": " + name;
    }

    /**
     * Time getter.
     *
     * @return the time of the course.
     */
    public String getTime(){
        return time;
    }

    /**
     * Expiration date getter.
     *
     * @return the expiration date.
     */
    public String getExpirationDate(){
        return expirationDate;
    }

    /**
     * Access code getter.
     *
     * @return the course access code.
     */
    public String getAccessCode(){
        return accessCode;
    }

    public String getInstructor(){
        //TODO
        return "";
    }


    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags){
        parcel.writeLong(id);
        parcel.writeString(name);
        parcel.writeString(time);
        parcel.writeString(expirationDate);
        parcel.writeString(accessCode);
    }

    public static final Creator<Course> CREATOR = new Creator<Course>(){
        @Override
        public Course createFromParcel(Parcel src){
            return new Course(src);
        }

        @Override
        public Course[] newArray(int count){
            return new Course[count];
        }
    };

    protected Course(Parcel src){
        id = src.readLong();
        name = src.readString();
        time = src.readString();
        expirationDate = src.readString();
        accessCode = src.readString();
    }
}
