package org.tndata.officehours.model;


/**
 * Model for a course from the perspective of the instructor.
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public class InstructorCourse extends Course{
    private String code;


    /**
     * Constructor.
     *
     * @param name the name of the course.
     * @param time the time of the course.
     * @param code the code of the course.
     */
    public InstructorCourse(String name, String time, String code){
        super(name, time);
        this.code = code;
    }

    /**
     * Constructor.
     *
     * @param id the id of the course.
     * @param name the name of the course.
     * @param time the time of the course.
     * @param code the code of the course.
     */
    public InstructorCourse(long id, String name, String time, String code){
        super(id, name, time);
        this.code = code;
    }

    /**
     * Code getter.
     *
     * @return the code to access the course.
     */
    public String getCode(){
        return code;
    }
}
