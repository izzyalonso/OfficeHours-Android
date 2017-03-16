package org.tndata.officehours.database;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import org.tndata.officehours.activity.TimeSlotPickerActivity;
import org.tndata.officehours.model.Course;
import org.tndata.officehours.model.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Reads the database in the background thread.
 *
 * @author Ismael Alonso
 */
public class DatabaseReader extends AsyncTask<Void, Void, Void>{
    private static final String TAG = "DatabaseReader";

    public static void start(@NonNull Context context, @NonNull Callback callback){
        new DatabaseReader(context, callback).execute();
    }


    private Context context;
    private Callback callback;

    private List<Course> courses;
    private Map<Long, Person> peopleMap;


    /**
     * Constructor.
     *
     * @param context a reference to the Context.
     * @param callback the callback.
     */
    private DatabaseReader(@NonNull Context context, @NonNull Callback callback){
        this.context = context;
        this.callback = callback;
    }

    @Override
    protected Void doInBackground(Void... unused){
        Log.i(TAG, "Reading database...");
        CourseTableHandler courseHandler = new CourseTableHandler(context);
        PersonTableHandler personHandler = new PersonTableHandler(context);
        //MessageTableHandler messageHandler = new MessageTableHandler(context);

        //Read courses and close the handler
        courses = courseHandler.getCourses();
        courseHandler.close();

        peopleMap = new HashMap<>();
        for (Course course:courses){
            Log.i(TAG, course.toString());
            String display = TimeSlotPickerActivity.get12HourFormattedString(course.getMeetingTime(), false);
            course.setFormattedMeetingTime(display);

            Log.i(TAG, "Reading people...");
            ArrayList<Person> people = personHandler.getPeople(course);
            Log.i(TAG, people.size() + " people found.");
            for (int i = 0; i < people.size(); i++){
                Person person = people.get(i);
                Log.i(TAG, person.toString());
                if (!peopleMap.containsKey(person.getId())){
                    peopleMap.put(person.getId(), person);
                    long time = System.currentTimeMillis();
                    //person.setMessages(messageHandler.getMessages(person, time));
                    Log.i(TAG, "New person! ");// + person.getMessages().size() + " messages read.");
                }
                else{
                    //If a Person instance already exists, use it instead
                    people.set(i, peopleMap.get(person.getId()));
                }

                if (person.isInstructor()){
                    course.setInstructor(person);
                    people.remove(i--);
                }
            }
            course.setStudents(people);
        }
        //messageHandler.close();
        personHandler.close();
        return null;
    }

    @Override
    protected void onPostExecute(Void unused){
        callback.onComplete(courses, peopleMap);
    }


    /**
     * Callback interface for the reader.
     *
     * @author Ismael Alonso.
     */
    public interface Callback{
        /**
         * Called in the UI thread when the database has been loaded.
         *
         * @param courses the list of courses in the DB.
         * @param peopleMap a map pf Person.id -> Person containing all the people in the DB.
         */
        void onComplete(@NonNull List<Course> courses, @NonNull Map<Long, Person> peopleMap);
    }
}
