package org.tndata.officehours.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import org.tndata.officehours.OfficeHoursApp;
import org.tndata.officehours.activity.TimeSlotPickerActivity;
import org.tndata.officehours.database.CourseTableHandler;
import org.tndata.officehours.database.PersonTableHandler;
import org.tndata.officehours.model.Course;
import org.tndata.officehours.model.Person;
import org.tndata.officehours.model.ResultSet;
import org.tndata.officehours.parser.Parser;
import org.tndata.officehours.parser.ParserModels;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.sandwatch.httprequests.HttpRequest;
import es.sandwatch.httprequests.HttpRequestError;


/**
 * A less sophisticated version of the Data synchronizer. Much simpler too.
 *
 * @author Ismael Alonso
 */
public class DumbDataSynchronizer implements HttpRequest.RequestCallback, Parser.ParserCallback{
    private static final String TAG = "DumbDataSynchronizer";


    /**
     * Starts the synchronization process.
     *
     * @param context a reference to the context.
     * @param callback the callback object.
     */
    public static void sync(@NonNull Context context, @NonNull Callback callback){
        new DumbDataSynchronizer(context, callback);
    }


    //Reference to the app and the callback object
    private OfficeHoursApp app;
    private Callback callback;

    //Things we need to update
    private int getCoursesRC;


    /**
     * Constructor.
     *
     * @param context a reference to the context.
     * @param callback the callback object.
     */
    private DumbDataSynchronizer(@NonNull Context context, @NonNull Callback callback){
        app = (OfficeHoursApp)context.getApplicationContext();
        this.callback = callback;

        getCoursesRC = HttpRequest.get(this, API.URL.courses());
    }


    @Override
    public void onRequestComplete(int requestCode, String result){
        if (requestCode == getCoursesRC){
            Log.d(TAG, result);
            Parser.parse(result, ParserModels.CourseList.class, this);
        }
    }

    @Override
    public void onRequestFailed(int requestCode, HttpRequestError error){

    }

    @Override
    public void onProcessResult(int requestCode, ResultSet result){
        if (result instanceof ParserModels.CourseList){
            //Open a connection to the database
            CourseTableHandler courseHandler = new CourseTableHandler(app);
            PersonTableHandler personHandler = new PersonTableHandler(app);

            courseHandler.erase();
            personHandler.erase();

            Map<Long, Person> people = new HashMap<>();

            //Get the lists of courses in the backend and the database
            List<Course> courses = ((ParserModels.CourseList)result).results;
            for (Course course:courses){
                course.process();
                String display = TimeSlotPickerActivity.get12HourFormattedString(course.getMeetingTime(), false);
                course.setFormattedMeetingTime(display);

                course.getInstructor().asInstructor();
                if (!people.containsKey(course.getInstructor().getId())){
                    people.put(course.getInstructor().getId(), course.getInstructor());
                }
                personHandler.savePerson(course.getInstructor(), course);

                for (Person student:course.getStudents()){
                    student.asStudent();
                    if (!people.containsKey(student.getId())){
                        people.put(student.getId(), student);
                    }
                }
                personHandler.savePeople(course.getStudents(), course);
            }

            courseHandler.saveCourses(courses);

            personHandler.close();
            courseHandler.close();

            app.setCourses(courses);
            app.setPeople(people);
        }
    }

    @Override
    public void onParseSuccess(int requestCode, ResultSet result){
        callback.onDataLoaded();
    }

    @Override
    public void onParseFailed(int requestCode){
        callback.onDataLoadFailed();
    }


    /**
     * Callback interface for DataSynchronizer.
     *
     * @author Ismael Alonso
     * @version 1.0.0
     */
    public interface Callback{
        /**
         * Called when the data has been loaded.
         */
        void onDataLoaded();

        /**
         * Called when the data load has failed.
         */
        void onDataLoadFailed();
    }
}
