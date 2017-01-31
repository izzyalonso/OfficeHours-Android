package org.tndata.officehours;

import android.app.Application;
import android.support.annotation.NonNull;
import android.util.Log;

import org.tndata.officehours.model.Course;
import org.tndata.officehours.model.Person;
import org.tndata.officehours.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.sandwatch.httprequests.HttpRequest;


/**
 * Application specific class.
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public class OfficeHoursApp extends Application{
    private static final String TAG = "OfficeHoursApp";


    private User user;
    private List<Course> courses;
    private Map<Long, Person> people;


    public void setUser(User user){
        this.user = user;
        HttpRequest.addHeader("Authorization", "Token " + user.getToken());
    }

    public User getUser(){
        return user;
    }

    public void setCourses(@NonNull List<Course> courses){
        this.courses = courses;
    }

    public void addCourse(@NonNull Course course){
        courses.add(course);
    }

    public List<Course> getCourses(){
        return courses;
    }

    public void setPeople(@NonNull Map<Long, Person> people){
        this.people = people;
    }

    public void updatePerson(@NonNull Person person){
        if (people.containsKey(person.getId())){
            people.get(person.getId()).setLastMessage(person.getLastMessage());
        }
    }


    @Override
    public void onCreate(){
        super.onCreate();
        Log.d(TAG, "onCreate()");
        HttpRequest.init(this);

        user = User.readFromPreferences(this);
        if (user != null){
            Log.i(TAG, "Current session -> " + user);
            HttpRequest.addHeader("Authorization", "Token " + user.getToken());
        }
        else{
            Log.i(TAG, "No user is signed in");
        }
    }
}
