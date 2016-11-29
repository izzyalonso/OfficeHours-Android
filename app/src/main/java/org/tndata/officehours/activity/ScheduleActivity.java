package org.tndata.officehours.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.tndata.officehours.OfficeHoursApp;
import org.tndata.officehours.databinding.ActivityScheduleBinding;
import org.tndata.officehours.model.Course;
import org.tndata.officehours.R;
import org.tndata.officehours.adapter.ScheduleAdapter;
import org.tndata.officehours.model.StudentCourse;
import org.tndata.officehours.util.CustomItemDecoration;

import java.util.ArrayList;
import java.util.List;


/**
 * Activity that displays a student's add
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public class ScheduleActivity extends AppCompatActivity{
    private static final int ADD_CODE_RC = 7529;


    private ActivityScheduleBinding binding;
    private ScheduleAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_schedule);

        setSupportActionBar(binding.scheduleToolbar.toolbar);

        List<Course> courses = new ArrayList<>();
        courses.add(new StudentCourse("COMP1900", "MW 11:00-12:25", "Mr. Someone 3rd"));
        courses.add(new StudentCourse("COMP2700", "TR 11:00-12:25", "Mr. Someone Jr"));
        courses.add(new StudentCourse("COMP4421", "MWF 11:00-12:00", "Dr. Someone Sr"));

        adapter = new ScheduleAdapter(this, courses);
        binding.scheduleList.setLayoutManager(new LinearLayoutManager(this));
        binding.scheduleList.setAdapter(adapter);
        binding.scheduleList.addItemDecoration(new CustomItemDecoration(this, 12));
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
                startActivity(new Intent(this, NewCourseActivity.class));
            }
            else{
                startActivityForResult(new Intent(this, AddCodeActivity.class), ADD_CODE_RC);
            }
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == ADD_CODE_RC){
            if (resultCode == RESULT_OK){
                StudentCourse course = data.getParcelableExtra(AddCodeActivity.COURSE_KEY);
                adapter.addCourse(course);
            }
        }
        else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
