package org.tndata.officehours.model;


import android.os.Parcel;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


/**
 * Model class for a course.
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public class Course extends Base{
    //@SerializedName("code")
    //private String code;
    @SerializedName("name")
    private String name;
    @SerializedName("location")
    private String location;
    @SerializedName("meetingtime")
    private String meetingTime;
    @SerializedName("last_meeting_date")
    private String lastMeetingDate;
    @SerializedName("access_code")
    private String accessCode;

    @SerializedName("teacher")
    private Person instructor;
    @SerializedName("students")
    private List<Person> students;


    private String formattedMeetingTime;


    /**
     * Constructor. This one is meant to be used when creating courses locally, before posting
     * them to the backend.
     *
     * //@param code the code of the course.
     * @param name the name of the course.
     * @param location the location of the course.
     * @param meetingTime a representation of the days and times the course meets.
     * @param lastMeetingDate the last day the class meets.
     */
    public Course(/*@NonNull String code, */@NonNull String name, @NonNull String location,
                  @NonNull String meetingTime, @NonNull String lastMeetingDate){

        this(-1, /*code,*/ name, location, meetingTime, lastMeetingDate, "");
    }

    /**
     * Constructor. This one is meant to be used when creating courses from database records.
     *
     * @param id the id of the course.
     * //@param code the code of the course.
     * @param name the name of the course.
     * @param location the location of the course.
     * @param meetingTime a representation of the days and times the course meets.
     * @param lastMeetingDate the last day the class meets.
     * @param accessCode the code that grants access to the course.
     */
    public Course(long id, /*@NonNull String code,*/ @NonNull String name, @NonNull String location,
                  @NonNull String meetingTime, @NonNull String lastMeetingDate,
                  @NonNull String accessCode){

        super(id);
        this.name = name;
        this.location = location;
        this.meetingTime = meetingTime;
        formattedMeetingTime = "";
        this.lastMeetingDate = lastMeetingDate;
        this.accessCode = accessCode;
    }

    /**
     * Code getter.
     *
     * @return the course code.
     */
    /*public String getCode(){
        return code;
    }*/

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
    /*public String getDisplayName(){
        return code + ": " + name;
    }*/

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

    public String getFormattedMeetingTime(){
        return formattedMeetingTime;
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
     * Instructor getter.
     *
     * @return the instructor of this course.
     */
    public Person getInstructor(){
        return instructor;
    }

    /**
     * Student getter.
     *
     * @return the list of students enrolled in this course.
     */
    public List<Person> getStudents(){
        return students;
    }

    /**
     * Instructor name getter.
     *
     * @return the name of this course's instructor.
     */
    /*public String getInstructorName(){
        return instructorName;
    }*/

    /**
     * Code setter.
     *
     * @param code the course code.
     */
    /*public void setCode(String code){
        this.code = code;
    }*/

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

    public void setFormattedMeetingTime(String formattedMeetingTime){
        this.formattedMeetingTime = formattedMeetingTime;
    }

    /**
     * Last meeting date setter.
     *
     * @param lastMeetingDate the last meeting date of the course.
     */
    public void setLastMeetingDate(String lastMeetingDate){
        this.lastMeetingDate = lastMeetingDate;
    }

    /**
     * Instructor setter.
     *
     * @param instructor the instructor of this course.
     */
    public void setInstructor(Person instructor){
        this.instructor = instructor;
    }

    /**
     * Student setter.
     *
     * @param students the students enrolled in this course.
     */
    public void setStudents(List<Person> students){
        this.students = students;
    }

    /**
     * Post processes the course after fetching it from the API.
     */
    public void process(){
        getInstructor().asInstructor();
        for (Person student:getStudents()){
            student.asStudent();
        }
    }

    @Override
    public String toString(){
        return "Course #" + getId() + ": " + getName() + " -> " + getLocation();
    }

    /*------------------*
     * PARCELABLE STUFF *
     *------------------*/

    @Override
    public void writeToParcel(Parcel parcel, int flags){
        super.writeToParcel(parcel, flags);
        //parcel.writeString(code);
        parcel.writeString(name);
        parcel.writeString(location);
        parcel.writeString(meetingTime);
        parcel.writeString(formattedMeetingTime);
        parcel.writeString(lastMeetingDate);
        parcel.writeString(accessCode);
        parcel.writeParcelable(instructor, flags);
        parcel.writeTypedList(students);
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
        super(src);
        //code = src.readString();
        name = src.readString();
        location = src.readString();
        meetingTime = src.readString();
        formattedMeetingTime = src.readString();
        lastMeetingDate = src.readString();
        accessCode = src.readString();
        instructor = src.readParcelable(Person.class.getClassLoader());
        students = new ArrayList<>();
        src.readTypedList(students, Person.CREATOR);
    }
}
