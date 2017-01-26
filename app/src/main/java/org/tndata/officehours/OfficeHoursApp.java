package org.tndata.officehours;

import android.app.Application;

import org.tndata.officehours.model.Course;
import org.tndata.officehours.model.User;

import java.util.List;

import es.sandwatch.httprequests.HttpRequest;


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
        HttpRequest.addHeader("Authorization", "Token " + user.getToken());
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


    @Override
    public void onCreate(){
        super.onCreate();
        HttpRequest.init(this);

        User user = User.readFromPreferences(this);
        if (user != null){
            HttpRequest.addHeader("Authorization", "Token " + user.getToken());
        }
    }
}
