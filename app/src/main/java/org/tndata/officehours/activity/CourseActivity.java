package org.tndata.officehours.activity;


import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.tndata.officehours.OfficeHoursApp;
import org.tndata.officehours.R;
import org.tndata.officehours.databinding.ActivityCourseBinding;
import org.tndata.officehours.model.Course;
import org.tndata.officehours.model.Person;

import java.util.ArrayList;


/**
 * Displays a course with different fields depending on who's viewing it.
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public class CourseActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String COURSE_KEY = "org.tndata.officehours.Course.Course";

    private static final int EDIT_RC = 6293;


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

        binding.courseToolbar.toolbar.setTitle(course.getName());
        setSupportActionBar(binding.courseToolbar.toolbar);

        if (((OfficeHoursApp)getApplication()).getUser().isStudent()){
            binding.courseAccessCodeHint.setVisibility(View.GONE);
            binding.courseAccessCode.setVisibility(View.GONE);
            binding.courseInstructorChat.setOnClickListener(this);
        }
        else{
            binding.courseInstructorContainer.setVisibility(View.GONE);
        }

        binding.courseChatRoom.setOnClickListener(this);
        binding.coursePeople.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        if (((OfficeHoursApp)getApplication()).getUser().isTeacher()){
            getMenuInflater().inflate(R.menu.edit, menu);
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == R.id.edit_edit){
            Intent edit = new Intent(this, CourseEditorActivity.class)
                    .putExtra(CourseEditorActivity.COURSE_KEY, course);
            startActivityForResult(edit, EDIT_RC);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == EDIT_RC){
            if (resultCode == RESULT_OK){
                course = data.getParcelableExtra(CourseEditorActivity.COURSE_KEY);
                binding.courseToolbar.toolbar.setTitle(course.getName());
                binding.setCourse(course);
            }
        }
        else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.course_instructor_chat:
                //TODO start a chat with the dude
                break;

            case R.id.course_people:
                String name = course.getName() + " students";
                ArrayList<Person> people = new ArrayList<>(course.getStudents());
                startActivity(PeopleActivity.getIntent(this, name, people));
                break;

            case R.id.course_chat_room:
                startActivity(new Intent(this, ChatActivity.class));
                break;
        }
    }
}
