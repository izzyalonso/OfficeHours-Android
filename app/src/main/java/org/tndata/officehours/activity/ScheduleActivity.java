package org.tndata.officehours.activity;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;

import org.tndata.officehours.OfficeHoursApp;
import org.tndata.officehours.databinding.ActivityScheduleBinding;
import org.tndata.officehours.model.Course;
import org.tndata.officehours.R;
import org.tndata.officehours.adapter.ScheduleAdapter;
import org.tndata.officehours.model.Person;
import org.tndata.officehours.model.User;
import org.tndata.officehours.util.CustomItemDecoration;
import org.tndata.officehours.util.DataSynchronizer;

import java.util.ArrayList;


/**
 * Activity that displays a student's schedule_instructor
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public class ScheduleActivity extends AppCompatActivity implements ScheduleAdapter.Listener, DataSynchronizer.Callback{
    private static final int ADD_CODE_RC = 7529;
    private static final int NEW_COURSE_RC = 6392;


    private ActivityScheduleBinding binding;
    private OfficeHoursApp app;
    private ScheduleAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_schedule);
        setSupportActionBar(binding.scheduleToolbar.toolbar);

        app = (OfficeHoursApp)getApplication();
        adapter = new ScheduleAdapter(this, this, app.getCourses());
        binding.scheduleList.setLayoutManager(new LinearLayoutManager(this));
        binding.scheduleList.setAdapter(adapter);
        binding.scheduleList.addItemDecoration(new CustomItemDecoration(this, 12));

        DataSynchronizer.sync(this, this);
    }

    @Override
    protected void onDestroy(){
        System.out.println("onDestroy() got called");
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        //The student menu contains an option to see all instructors
        if (app.getUser().isStudent()){
            getMenuInflater().inflate(R.menu.schedule_student, menu);
        }
        else{
            getMenuInflater().inflate(R.menu.schedule_instructor, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == R.id.schedule_add){
            if (app.getUser().isTeacher()){
                startActivityForResult(new Intent(this, CourseEditorActivity.class), NEW_COURSE_RC);
            }
            else{
                startActivityForResult(new Intent(this, AddCodeActivity.class), ADD_CODE_RC);
            }
            return true;
        }
        else if (item.getItemId() == R.id.schedule_faculty){
            //Gather a list of unique instructors
            ArrayList<Person> instructors = new ArrayList<>();
            for (Course course:app.getCourses()){
                if (!instructors.contains(course.getInstructor())){
                    instructors.add(course.getInstructor());
                }
            }

            //Start the activity to display them with the appropriate title
            String title = getString(R.string.schedule_my_instructors_label);
            startActivity(PeopleActivity.getIntent(this, title, instructors));
        }
        else if (item.getItemId() == R.id.schedule_logout){
            User.deleteFromPreferences(this);
            startActivity(new Intent(this, LauncherActivity.class));
            finish();
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == ADD_CODE_RC){
            if (resultCode == RESULT_OK){
                Course course = data.getParcelableExtra(AddCodeActivity.COURSE_KEY);
                adapter.addCourse(course);
                onCourseSelected(course);
            }
        }
        else if (requestCode == NEW_COURSE_RC){
            if (resultCode == RESULT_OK){
                Course course = data.getParcelableExtra(CourseEditorActivity.COURSE_KEY);
                adapter.addCourse(course);
            }
        }
        else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onCourseSelected(@NonNull Course course){
        startActivity(CourseActivity.getIntent(this, course));
    }

    @Override
    public void onDataLoaded(){
        adapter.setCourses(((OfficeHoursApp)getApplication()).getCourses());
    }

    @Override
    public void onDataLoadFailed(){

    }
}
