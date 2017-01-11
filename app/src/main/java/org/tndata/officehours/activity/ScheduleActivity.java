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
import org.tndata.officehours.model.User;
import org.tndata.officehours.util.CustomItemDecoration;
import org.tndata.officehours.util.DataSynchronizer;

import java.util.ArrayList;
import java.util.List;


/**
 * Activity that displays a student's add
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public class ScheduleActivity extends AppCompatActivity implements ScheduleAdapter.Listener{
    private static final int ADD_CODE_RC = 7529;
    private static final int NEW_COURSE_RC = 6392;


    private ActivityScheduleBinding binding;
    private ScheduleAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_schedule);

        setSupportActionBar(binding.scheduleToolbar.toolbar);

        List<Course> courses = new ArrayList<>();
        courses.add(new Course("Intro to cs", "Dunn 255", "MW 11:00-12:25", "12/25/2016"));
        courses.add(new Course("Discrete math", "Dunn 230",  "TR 11:00-12:25", "12/25/2016"));
        courses.add(new Course("Some higher level course", "Dunn 235", "MWF 11:00-12:00", "12/25/2016"));

        adapter = new ScheduleAdapter(this, this, courses);
        binding.scheduleList.setLayoutManager(new LinearLayoutManager(this));
        binding.scheduleList.setAdapter(adapter);
        binding.scheduleList.addItemDecoration(new CustomItemDecoration(this, 12));

        DataSynchronizer.sync(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == R.id.add_add){
            if (((OfficeHoursApp)getApplication()).getUser().isTeacher()){
                startActivityForResult(new Intent(this, CourseEditorActivity.class), NEW_COURSE_RC);
            }
            else{
                startActivityForResult(new Intent(this, AddCodeActivity.class), ADD_CODE_RC);
            }
            return true;
        }
        else if (item.getItemId() == R.id.add_logout){
            User.deleteFromPreferences(this);
            startActivity(new Intent(this, LauncherActivity.class));
            finish();
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == ADD_CODE_RC){
            if (resultCode == RESULT_OK){
                Course course = data.getParcelableExtra(AddCodeActivity.COURSE_KEY);
                adapter.addCourse(course);
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
}
