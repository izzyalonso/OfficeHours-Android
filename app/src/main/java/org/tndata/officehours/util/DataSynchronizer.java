package org.tndata.officehours.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import org.tndata.officehours.OfficeHoursApp;
import org.tndata.officehours.activity.TimeSlotPickerActivity;
import org.tndata.officehours.model.Course;
import org.tndata.officehours.model.Person;
import org.tndata.officehours.model.ResultSet;
import org.tndata.officehours.parser.Parser;
import org.tndata.officehours.parser.ParserModels;

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
            List<Course> courses = ((ParserModels.CourseList)result).results;
            for (Course course:courses){
                course.process();
                course.setFormattedMeetingTime(
                        TimeSlotPickerActivity.get12HourFormattedString(
                                course.getMeetingTime(), false
                        )
                );
            }
            application.setCourses(courses);
        }
    }

    @Override
    public void onParseSuccess(int requestCode, ResultSet result){
        for (Course course:((ParserModels.CourseList)result).results){
            Log.d(TAG, course.toString());
            Log.d(TAG, course.getInstructor().toString());
            callback.onDataLoaded();
        }
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
