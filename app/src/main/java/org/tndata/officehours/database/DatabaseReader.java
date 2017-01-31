package org.tndata.officehours.database;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import org.tndata.officehours.activity.TimeSlotPickerActivity;
import org.tndata.officehours.model.Course;
import org.tndata.officehours.model.Person;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by ialonso on 1/12/17.
 */
public class DatabaseReader extends AsyncTask<Void, Void, Void>{

    public static void start(@NonNull Context context, @NonNull Listener listener){
        new DatabaseReader(context, listener).execute();
    }


    private Context context;
    private Listener listener;

    private List<Course> courses;
    private Map<Long, Person> peopleMap;


    private DatabaseReader(@NonNull Context context, @NonNull Listener listener){
        this.context = context;
        this.listener = listener;
    }


    @Override
    protected Void doInBackground(Void... unused){
        CourseTableHandler courseHandler = new CourseTableHandler(context);
        PersonTableHandler personHandler = new PersonTableHandler(context);
        MessageTableHandler messageHandler = new MessageTableHandler(context);
        courses = courseHandler.getCourses();
        peopleMap = new HashMap<>();
        courseHandler.close();
        for (Course course:courses){
            String display = TimeSlotPickerActivity.get12HourFormattedString(course.getMeetingTime(), false);
            course.setFormattedMeetingTime(display);
            List<Person> people = personHandler.getPeople(course);
            for (int i = 0; i < people.size(); i++){
                Person person = people.get(i);
                if (!peopleMap.containsKey(person.getId())){
                    peopleMap.put(person.getId(), person);
                    long time = System.currentTimeMillis();
                    person.setMessages(messageHandler.getMessages(person, time));
                }
                if (person.isInstructor()){
                    course.setInstructor(person);
                    people.remove(i--);
                }
            }
            course.setStudents(people);
        }
        messageHandler.close();
        personHandler.close();
        return null;
    }

    @Override
    protected void onPostExecute(Void unused){
        listener.onComplete(courses, peopleMap);
    }


    public interface Listener{
        void onComplete(@NonNull List<Course> courses, @NonNull Map<Long, Person> peopleMap);
    }
}
