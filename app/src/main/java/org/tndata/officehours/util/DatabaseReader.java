package org.tndata.officehours.util;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import org.tndata.officehours.activity.TimeSlotPickerActivity;
import org.tndata.officehours.database.CourseTableHandler;
import org.tndata.officehours.model.Course;

import java.util.List;


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


    private DatabaseReader(@NonNull Context context, @NonNull Listener listener){
        this.context = context;
        this.listener = listener;
    }


    @Override
    protected Void doInBackground(Void... unused){
        CourseTableHandler handler = new CourseTableHandler(context);
        courses = handler.getCourses();
        handler.close();
        for (Course course:courses){
            String display = TimeSlotPickerActivity.get12HourFormattedString(course.getMeetingTime(), false);
            course.setFormattedMeetingTime(display);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void unused){
        listener.onComplete(courses);
    }


    public interface Listener{
        void onComplete(List<Course> courses);
    }
}
