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
    @SerializedName("location")
    private String location;
    @SerializedName("meeting_time")
    private String meetingTime;
    @SerializedName("last_meeting_date")
    private String lastMeetingDate;
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
     * @param location the location of the course.
     * @param meetingTime a representation of the days and times the course meets.
     * @param lastMeetingDate the last day the class meets.
     * @param instructorName the name of the instructor who teaches the course.
     */
    public Course(@NonNull String code, @NonNull String name, @NonNull String location,
                  @NonNull String meetingTime, @NonNull String lastMeetingDate,
                  @NonNull String instructorName){

        this(-1, code, name, location, meetingTime, lastMeetingDate, "", instructorName);
    }

    /**
     * Constructor. This one is meant to be used when creating courses from database records.
     *
     * @param id the id of the course.
     * @param code the code of the course.
     * @param name the name of the course.
     * @param location the location of the course.
     * @param meetingTime a representation of the days and times the course meets.
     * @param lastMeetingDate the last day the class meets.
     * @param accessCode the code that grants access to the course.
     * @param instructorName the name of the instructor who teaches the course.
     */
    public Course(long id, @NonNull String code, @NonNull String name, @NonNull String location,
                  @NonNull String meetingTime, @NonNull String lastMeetingDate,
                  @NonNull String accessCode, @NonNull String instructorName){

        this.id = id;
        this.code = code;
        this.name = name;
        this.location = location;
        this.meetingTime = meetingTime;
        this.lastMeetingDate = lastMeetingDate;
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
     * Location getter.
     *
     * @return the location of the course.
     */
    public String getLocation(){
        return location;
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
     * Last meeting date getter.
     *
     * @return the expiration date.
     */
    public String getLastMeetingDate(){
        return lastMeetingDate;
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

    /**
     * Code setter.
     *
     * @param code the course code.
     */
    public void setCode(String code){
        this.code = code;
    }

    /**
     * Name setter.
     *
     * @param name the name of the course.
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * Meeting time setter.
     *
     * @param meetingTime the meeting time of the course.
     */
    public void setMeetingTime(String meetingTime){
        this.meetingTime = meetingTime;
    }

    /**
     * Last meeting date setter.
     *
     * @param lastMeetingDate the last meeting date of the course.
     */
    public void setLastMeetingDate(String lastMeetingDate){
        this.lastMeetingDate = lastMeetingDate;
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
        parcel.writeString(location);
        parcel.writeString(meetingTime);
        parcel.writeString(lastMeetingDate);
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
        location = src.readString();
        meetingTime = src.readString();
        lastMeetingDate = src.readString();
        accessCode = src.readString();
        instructorName = src.readString();
    }
}
