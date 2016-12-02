package org.tndata.officehours.activity;


import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.tndata.officehours.OfficeHoursApp;
import org.tndata.officehours.R;
import org.tndata.officehours.databinding.ActivityCourseBinding;
import org.tndata.officehours.model.Course;


/**
 * Displays a course with different fields depending on who's viewing it.
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public class CourseActivity extends AppCompatActivity{
    private static final String COURSE_KEY = "org.tndata.officehours.Course.Course";


    public static Intent getIntent(@NonNull Context context, @NonNull Course course){
        return new Intent(context, CourseActivity.class).putExtra(COURSE_KEY, course);
    }


    private ActivityCourseBinding binding;
    private Course course;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_course);

        course = getIntent().getParcelableExtra(COURSE_KEY);
        binding.setCourse(course);

        binding.courseToolbar.toolbar.setTitle(course.getDisplayName());
        setSupportActionBar(binding.courseToolbar.toolbar);

        if (((OfficeHoursApp)getApplication()).getUser().isStudent()){
            binding.courseAccessCodeHint.setVisibility(View.GONE);
            binding.courseAccessCode.setVisibility(View.GONE);
        }
    }
}
