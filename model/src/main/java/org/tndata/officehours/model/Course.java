package org.tndata.officehours.model;


import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

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
    @SerializedName("name")
    private String name;
    @SerializedName("meeting_time")
    private String meetingTime;
    @SerializedName("expiration_date")
    private String expirationDate;
    @SerializedName("access_code")
    private String accessCode;
    @SerializedName("instructor_name")
    private String instructorName;


    /**
     * Constructor. This one is meant to be used when creating courses locally, before posting
     * them to the backend.
     *
     * @param code the code of the course.
     * @param name the name of the course.
     * @param meetingTime a representation of the days and times the course meets.
     * @param expirationDate the day the course expires.
     * @param instructorName the name of the instructor who teaches the course.
     */
    public Course(@NonNull String code, @NonNull String name, @NonNull String meetingTime,
                  @NonNull String expirationDate, @NonNull String instructorName){

        this(-1, code, name, meetingTime, expirationDate, "", instructorName);
    }

    /**
     * Constructor. This one is meant to be used when creating courses from database records.
     *
     * @param id the id of the course.
     * @param code the code of the course.
     * @param name the name of the course.
     * @param meetingTime a representation of the days and times the course meets.
     * @param expirationDate the day the course expires.
     * @param accessCode the code that grants access to the course.
     * @param instructorName the name of the instructor who teaches the course.
     */
    public Course(long id, @NonNull String code, @NonNull String name, @NonNull String meetingTime,
                  @NonNull String expirationDate, @NonNull String accessCode,
                  @NonNull String instructorName){

        this.id = id;
        this.code = code;
        this.name = name;
        this.meetingTime = meetingTime;
        this.expirationDate = expirationDate;
        this.accessCode = accessCode;
        this.instructorName = instructorName;
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
     * Meeting time getter.
     *
     * @return the meeting time of the course.
     */
    public String getMeetingTime(){
        return meetingTime;
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

    /**
     * Instructor name getter.
     *
     * @return the name of this course's instructor.
     */
    public String getInstructorName(){
        return instructorName;
    }


    /*------------------*
     * PARCELABLE STUFF *
     *------------------*/

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags){
        parcel.writeLong(id);
        parcel.writeString(code);
        parcel.writeString(name);
        parcel.writeString(meetingTime);
        parcel.writeString(expirationDate);
        parcel.writeString(accessCode);
        parcel.writeString(instructorName);
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

    /**
     * Constructor. Used by the creator.
     *
     * @param src the source parcel.
     */
    protected Course(Parcel src){
        id = src.readLong();
        code = src.readString();
        name = src.readString();
        meetingTime = src.readString();
        expirationDate = src.readString();
        accessCode = src.readString();
        instructorName = src.readString();
    }
}
