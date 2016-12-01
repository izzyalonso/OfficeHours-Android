package org.tndata.officehours;

import android.app.Application;

import org.tndata.officehours.model.Course;
import org.tndata.officehours.model.User;

import java.util.List;


/**
 * Application specific class.
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public class OfficeHoursApp extends Application{
    private User user;
    private List<Course> courses;


    public void setUser(User user){
        this.user = user;
    }

    public User getUser(){
        return user;
    }

    public void setCourses(List<Course> courses){
        this.courses = courses;
    }

    public void addCourse(Course course){
        courses.add(course);
    }

    public List<Course> getCourses(){
        return courses;
    }
}
