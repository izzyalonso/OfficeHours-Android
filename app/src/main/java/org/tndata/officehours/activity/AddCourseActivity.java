package org.tndata.officehours.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.tndata.officehours.R;
import org.tndata.officehours.databinding.ActivityAddCourseBinding;
import org.tndata.officehours.model.Course;


/**
 * Lets the user decide whether this is the course they want to add to their schedule.
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public class AddCourseActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String COURSE_KEY = "org.tndata.officehours.AddCourse.Course";


    static Intent getIntent(@NonNull Context context, @NonNull Course course){
        return new Intent(context, AddCourseActivity.class).putExtra(COURSE_KEY, course);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        ActivityAddCourseBinding binding;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_course);

        setSupportActionBar(binding.addCourseToolbar.toolbar);

        binding.setCourse((Course)getIntent().getParcelableExtra(COURSE_KEY));
        binding.addCourseAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        if (view.getId() == R.id.add_course_add){
            setResult(RESULT_OK);
            finish();
        }
    }
}
