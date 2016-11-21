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
    @SerializedName("whatever")
    private String name;
    @SerializedName("whatever")
    private String time;
    @SerializedName("whatever")
    private String instructor;


    /**
     * Constructor.
     *
     * @param name the name of the course.
     * @param time the time of the course.
     * @param instructor the instructor teaching the course.
     */
    public Course(String name, String time, String instructor){
        id = -1;
        this.name = name;
        this.time = time;
        this.instructor = instructor;
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
     * Name getter.
     *
     * @return the name of the course.
     */
    public String getName(){
        return name;
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
     * Instructor getter
     *
     * @return the instructor teaching this course.
     */
    public String getInstructor(){
        return instructor;
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
        parcel.writeString(instructor);
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

    private Course(Parcel src){
        id = src.readLong();
        name = src.readString();
        time = src.readString();
        instructor = src.readString();
    }
}
