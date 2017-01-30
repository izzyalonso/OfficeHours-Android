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

import java.util.ArrayList;
import java.util.List;

import es.sandwatch.httprequests.HttpRequest;
import es.sandwatch.httprequests.HttpRequestError;


/**
 * Class to sync up all the necessary data in the background. For now works one way.
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public class DataSynchronizer implements HttpRequest.RequestCallback, Parser.ParserCallback{
    private static final String TAG = "DataSynchronizer";


    /**
     * Starts the synchronization process.
     *
     * @param context a reference to the context.
     * @param callback the callback object.
     */
    public static void sync(@NonNull Context context, @NonNull Callback callback){
        new DataSynchronizer(context, callback);
    }


    //Reference to the app and the callback object
    private OfficeHoursApp application;
    private Callback callback;

    //Things we need to update
    private int getProfileRC;
    private int getCoursesRC;


    /**
     * Constructor.
     *
     * @param context a reference to the context.
     * @param callback the callback object.
     */
    private DataSynchronizer(@NonNull Context context, @NonNull Callback callback){
        application = (OfficeHoursApp)context.getApplicationContext();
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
            CourseTableHandler courseHandler = new CourseTableHandler(application);
            PersonTableHandler personHandler = new PersonTableHandler(application);

            //Get the lists of courses in the backend and the database
            List<Course> backendCourses = ((ParserModels.CourseList)result).results;
            List<Course> dbCourses = courseHandler.getCourses();

            //List of courses in the backend not available in the database, to be bulk written
            List<Course> newCourses = new ArrayList<>();

            //Compare the two lists
            for (Course course:backendCourses){
                if (dbCourses.contains(course)){
                    //Remove so we have a record of which courses need to be removed
                    Course dbCourse = dbCourses.remove(dbCourses.indexOf(course));
                    if (!course.parametersMatch(dbCourse)){
                        //If the course exists and needs updating, update it
                        courseHandler.updateCourse(course);
                    }
                }
                else{
                    //If the course doesn't exist schedule_instructor it to the list to bulk schedule_instructor later
                    newCourses.add(course);
                }

                //Process the course
                course.setFormattedMeetingTime(
                        TimeSlotPickerActivity.get12HourFormattedString(
                                course.getMeetingTime(), false
                        )
                );


                List<Person> backendPeople = course.getStudents();
                List<Person> dbPeople = personHandler.getPeople(course);

                List<Person> newPeople = new ArrayList<>();

                Person instructor = course.getInstructor();
                //If the instructors mismatch, schedule_instructor the new one to the saving list
                if (!dbPeople.contains(instructor)){
                    newPeople.add(instructor);
                }

                //This is the same process followed by course matching, the only difference is
                //  that people are not editable for the time being
                for (Person person:backendPeople){
                    if (dbPeople.contains(person)){
                        dbPeople.remove(dbPeople.indexOf(person));
                    }
                    else{
                        newPeople.add(person);
                    }
                }

                for (Person person:dbPeople){
                    personHandler.deletePerson(person, course);
                }
                personHandler.savePeople(newPeople, course);
            }

            //If a course needs to be deleted, delete both the course and the people in it
            for (Course course:dbCourses){
                courseHandler.deleteCourse(course);
                personHandler.deletePeople(course);
            }
            courseHandler.saveCourses(newCourses);

            personHandler.close();
            courseHandler.close();
            application.setCourses(backendCourses);
        }
    }

    @Override
    public void onParseSuccess(int requestCode, ResultSet result){
        for (Course course:((ParserModels.CourseList)result).results){
            Log.d(TAG, course.toString());
            Log.d(TAG, course.getInstructor().toString());
        }
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
