package org.tndata.officehours;

import android.app.Application;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.util.Log;

import org.tndata.officehours.model.Course;
import org.tndata.officehours.model.Person;
import org.tndata.officehours.model.Question;
import org.tndata.officehours.model.User;
import org.tndata.officehours.receiver.ConnectivityStateReceiver;

import java.util.ArrayList;
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
    private List<Question> questions;


    public void setUser(User user){
        this.user = user;
        HttpRequest.addHeader("Authorization", "Token " + user.getToken());
    }

    public User getUser(){
        return user;
    }

    public void setCourses(@NonNull List<Course> courses){
        if (this.courses == null){
            this.courses = new ArrayList<>();
        }
        else{
            this.courses.clear();
        }
        this.courses.addAll(courses);
    }

    public void addCourse(@NonNull Course course){
        courses.add(course);
    }

    public @NonNull List<Course> getCourses(){
        if (courses == null){
            courses = new ArrayList<>();
        }
        return courses;
    }

    public void setPeople(@NonNull Map<Long, Person> people){
        if (this.people == null){
            this.people = new HashMap<>();
        }
        else{
            this.people.clear();
        }
        this.people.putAll(people);
    }

    public void addPerson(@NonNull Person person){
        if (people == null){
            people = new HashMap<>();
        }
        if (!people.containsKey(person.getId())){
            people.put(person.getId(), person);
        }
    }

    public void updatePerson(@NonNull Person person){
        if (people.containsKey(person.getId())){
            people.get(person.getId()).setLastMessage(person.getLastMessage());
        }
    }

    public Map<Long, Person> getPeople(){
        if (people == null){
            people = new HashMap<>();
        }
        return people;
    }

    public ArrayList<Person> getPeople(boolean instructors){
        ArrayList<Person> people = new ArrayList<>();
        for (Person person:this.people.values()){
            if (person.isInstructor() == instructors){
                people.add(person);
            }
        }
        return people;
    }

    public void setQuestions(@NonNull List<Question> questions){
        if (this.questions == null){
            this.questions = new ArrayList<>();
        }
        else{
            this.questions.clear();
        }
        this.questions.addAll(questions);
    }

    public @NonNull List<Question> getQuestions(){
        if (questions == null){
            questions = new ArrayList<>();
        }
        return questions;
    }


    /*-----------------------------------*
     * Network state distribution system *
     *-----------------------------------*/

    private ArrayList<ConnectionStateChangeListener> connectionStateChangeListeners;


    public void addConnectionStateChangeListener(@NonNull ConnectionStateChangeListener listener){
        connectionStateChangeListeners.add(listener);
    }

    public void removeConnectionStateChangeListener(@NonNull ConnectionStateChangeListener victim){
        connectionStateChangeListeners.remove(victim);
    }

    public void connectionStateChanged(boolean connected){
        Log.i(TAG, "Connection state changed: " + (connected ? "connected" : "not connected"));
        for (ConnectionStateChangeListener listener: connectionStateChangeListeners){
            listener.onConnectionStateChanged(connected);
        }
    }

    public interface ConnectionStateChangeListener{
        void onConnectionStateChanged(boolean connected);
    }


    /*--------------------------*
     * Application's onCreate() *
     *--------------------------*/

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

        connectionStateChangeListeners = new ArrayList<>();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(new ConnectivityStateReceiver(), filter);
    }
}
