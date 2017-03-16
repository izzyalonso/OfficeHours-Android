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
import android.widget.Toast;

import org.tndata.officehours.OfficeHoursApp;
import org.tndata.officehours.R;
import org.tndata.officehours.databinding.ActivityCourseBinding;
import org.tndata.officehours.model.Course;
import org.tndata.officehours.model.OfficeHours;
import org.tndata.officehours.model.Person;
import org.tndata.officehours.util.Util;

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

        if (((OfficeHoursApp)getApplication()).getUser().is(course.getInstructor())){
            binding.courseInstructorContainer.setVisibility(View.GONE);
            binding.courseOfficeHoursContainer.setVisibility(View.GONE);
            binding.courseEnrollmentList.setOnClickListener(this);
            binding.courseEnrollmentBroadcast.setOnClickListener(this);
        }
        else{
            binding.courseAccessCode.setVisibility(View.GONE);
            binding.courseEnrollmentContainer.setVisibility(View.GONE);
            binding.courseInstructorChat.setOnClickListener(this);
            if (course.getInstructor().getOfficeHours() == null){
                binding.courseOfficeHoursContainer.setVisibility(View.GONE);
            }
            else{
                setOfficeHours();
            }
        }
        String content = getString(R.string.course_enrollment_content, course.getStudents().size());
        binding.courseEnrollment.setText(content);
    }

    private void setOfficeHours(){
        OfficeHours hours = course.getInstructor().getOfficeHours();

        if (hours.hasMondayHours()){
            String text = getString(R.string.office_hours_monday) + hours.getMondayHours();
            binding.courseOfficeHoursMonday.setText(text);
        }
        else{
            binding.courseOfficeHoursMonday.setVisibility(View.GONE);
        }

        if (hours.hasTuesdayHours()){
            String text = getString(R.string.office_hours_tuesday) + hours.getTuesdayHours();
            binding.courseOfficeHoursTuesday.setText(text);
        }
        else{
            binding.courseOfficeHoursTuesday.setVisibility(View.GONE);
        }

        if (hours.hasWednesdayHours()){
            String text = getString(R.string.office_hours_wednesday) + hours.getWednesdayHours();
            binding.courseOfficeHoursWednesday.setText(text);
        }
        else{
            binding.courseOfficeHoursWednesday.setVisibility(View.GONE);
        }

        if (hours.hasThursdayHours()){
            String text = getString(R.string.office_hours_thursday) + hours.getThursdayHours();
            binding.courseOfficeHoursThursday.setText(text);
        }
        else{
            binding.courseOfficeHoursThursday.setVisibility(View.GONE);
        }

        if (hours.hasFridayHours()){
            String text = getString(R.string.office_hours_friday) + hours.getFridayHours();
            binding.courseOfficeHoursFriday.setText(text);
        }
        else{
            binding.courseOfficeHoursFriday.setVisibility(View.GONE);
        }

        if (hours.hasSaturdayHours()){
            String text = getString(R.string.office_hours_saturday) + hours.getSaturdayHours();
            binding.courseOfficeHoursSaturday.setText(text);
        }
        else{
            binding.courseOfficeHoursSaturday.setVisibility(View.GONE);
        }

        if (hours.hasSundayHours()){
            String text = getString(R.string.office_hours_sunday) + hours.getSundayHours();
            binding.courseOfficeHoursSunday.setText(text);
        }
        else{
            binding.courseOfficeHoursSunday.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        if (((OfficeHoursApp)getApplication()).getUser().is(course.getInstructor())){
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
                if (Util.isNetworkAvailable(this)){
                    startActivity(ChatActivity.getIntent(this, course.getInstructor()));
                }
                else{
                    Toast.makeText(this, R.string.chat_error_offline, Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.course_enrollment_list:
                String name = course.getName() + " students";
                ArrayList<Person> people = new ArrayList<>(course.getStudents());
                startActivity(PeopleActivity.getIntent(this, name, people));
                break;

            case R.id.course_enrollment_broadcast:
                startActivity(new Intent(this, ChatActivity.class));
                break;
        }
    }
}
