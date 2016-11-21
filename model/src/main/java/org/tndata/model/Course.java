package org.tndata.model;


import com.google.gson.annotations.SerializedName;


/**
 * Model class for a course.
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public class Course{
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
}
