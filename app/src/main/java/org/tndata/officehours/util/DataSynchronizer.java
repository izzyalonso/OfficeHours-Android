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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private OfficeHoursApp app;
    private Callback callback;

    //Things we need to update
    private int getProfileRC;
    private int getCoursesRC;
    private Set<Integer> getMessagesRequestCodeSet;


    /**
     * Constructor.
     *
     * @param context a reference to the context.
     * @param callback the callback object.
     */
    private DataSynchronizer(@NonNull Context context, @NonNull Callback callback){
        app = (OfficeHoursApp)context.getApplicationContext();
        this.callback = callback;

        getCoursesRC = HttpRequest.get(this, API.URL.courses());
        getMessagesRequestCodeSet = new HashSet<>();
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

            //Get the lists of courses in the backend and the database
            List<Course> backendCourses = ((ParserModels.CourseList)result).results;
            //We'll be removing courses from the list, so let's make another copy
            List<Course> loadedCourses = new ArrayList<>(app.getCourses());

            //List of courses in the backend but not in the database, to be bulk written
            List<Course> newCourses = new ArrayList<>();

            //Compare the two lists
            for (Course course:backendCourses){
                course.process();
                if (loadedCourses.contains(course)){
                    //Remove so we have a record of which courses need to be removed
                    Course loadedCourse = loadedCourses.remove(loadedCourses.indexOf(course));
                    if (!loadedCourse.parametersMatch(course)){
                        //If the course exists and needs updating, update it
                        loadedCourse.update(course);
                        loadedCourse.setFormattedMeetingTime(
                                TimeSlotPickerActivity.get12HourFormattedString(
                                        loadedCourse.getMeetingTime(), false
                                )
                        );
                        courseHandler.updateCourse(loadedCourse);
                    }

                    List<Person> newPeople = new ArrayList<>();

                    Person instructor = course.getInstructor();
                    //If the instructors mismatch, add the new one to the saving list
                    if (loadedCourse.getInstructor().equals(instructor)){
                        newPeople.add(instructor);
                        loadedCourse.setInstructor(instructor);
                    }

                    //Compare the list of people as well
                    List<Person> backendPeople = course.getStudents();
                    List<Person> loadedPeople = new ArrayList<>(loadedCourse.getStudents());

                    //This is the same process followed by course matching, the only difference is
                    //  that people are not editable for the time being
                    for (Person person:backendPeople){
                        if (loadedPeople.contains(person)){
                            loadedPeople.remove(person);
                        }
                        else{
                            newPeople.add(person);
                        }
                    }

                    //People that need to be deleted
                    for (Person person:loadedPeople){
                        personHandler.deletePerson(person, loadedCourse);
                        loadedCourse.getStudents().remove(person);
                    }

                    //People who need to be added
                    personHandler.savePeople(newPeople, loadedCourse);
                    for (Person person:newPeople){
                        app.addPerson(person);
                        loadedCourse.getStudents().add(app.getPeople().get(person.getId()));
                    }
                }
                else{
                    //If the course doesn't exist add it to the list to bulk save later
                    newCourses.add(course);

                    //NOTE: If the course is new, the people in it can be saved at a later time
                }
            }

            //If a course needs to be deleted, delete both the course and the people in it
            for (Course course:loadedCourses){
                courseHandler.deleteCourse(course);
                personHandler.deletePeople(course);
                app.getCourses().remove(course);
            }

            //New courses (to save)
            courseHandler.saveCourses(newCourses);
            for (Course course:newCourses){
                course.setFormattedMeetingTime(
                        TimeSlotPickerActivity.get12HourFormattedString(
                                course.getMeetingTime(), false
                        )
                );
                app.addCourse(course);
                personHandler.savePerson(course.getInstructor(), course);
                personHandler.savePeople(course.getStudents(), course);
            }

            personHandler.close();
            courseHandler.close();
        }
    }

    @Override
    public void onParseSuccess(int requestCode, ResultSet result){
        for (Course course:app.getCourses()){
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
