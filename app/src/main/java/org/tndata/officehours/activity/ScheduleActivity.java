package org.tndata.officehours.activity;


import android.app.AlertDialog;
import android.content.DialogInterface;
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
import org.tndata.officehours.util.DumbDataSynchronizer;

import java.util.ArrayList;


/**
 * Activity that displays a student's schedule_instructor
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public class ScheduleActivity
        extends AppCompatActivity
        implements
                ScheduleAdapter.Listener,
                DialogInterface.OnClickListener,
                DumbDataSynchronizer.Callback{

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

        DumbDataSynchronizer.sync(this, this);
    }

    @Override
    protected void onDestroy(){
        System.out.println("onDestroy() got called");
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.schedule, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == R.id.schedule_add){
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.schedule_dialog_add_title)
                    .setItems(R.array.schedule_dialog_add_options, this)
                    .create();
            dialog.show();
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
    public void onClick(DialogInterface dialog, int which){
        if (which == 0){
            startActivityForResult(new Intent(this, AddCodeActivity.class), ADD_CODE_RC);
        }
        else{
            startActivityForResult(new Intent(this, CourseEditorActivity.class), NEW_COURSE_RC);
        }
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
        adapter.notifyDataSetChanged();
        adapter.setCourses(((OfficeHoursApp)getApplication()).getCourses());
    }

    @Override
    public void onDataLoadFailed(){

    }
}
