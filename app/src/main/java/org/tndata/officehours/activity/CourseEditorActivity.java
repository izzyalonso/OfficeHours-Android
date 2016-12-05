package org.tndata.officehours.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;

import org.tndata.officehours.OfficeHoursApp;
import org.tndata.officehours.R;
import org.tndata.officehours.databinding.ActivityCourseEditorBinding;
import org.tndata.officehours.model.Course;

import java.util.Calendar;


/**
 * Activity used to create a course.
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public class CourseEditorActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener{
    private static final int TIME_SLOT_PICKER_RC = 6286;

    public static final String COURSE_KEY = "org.tndata.officehours.CourseEditor.Course";


    private ActivityCourseEditorBinding binding;

    private Course course;

    private String selectedTimeSlot;
    private int expirationYear;
    private int expirationMonth;
    private int expirationDay;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_course_editor);

        setSupportActionBar(binding.courseEditorToolbar.toolbar);

        binding.courseEditorTime.setOnClickListener(this);
        binding.courseEditorExpiration.setOnClickListener(this);
        binding.courseEditorDone.setOnClickListener(this);

        course = getIntent().getParcelableExtra(COURSE_KEY);
        if (course == null){
            selectedTimeSlot = "";
            expirationYear = -1;
            expirationMonth = -1;
            expirationDay = -1;

            binding.courseEditorToolbar.toolbar.setTitle(R.string.course_editor_label_new);
        }
        else{
            selectedTimeSlot = course.getMeetingTime();
            String date[] = course.getExpirationDate().split("/");
            expirationYear = Integer.valueOf(date[2]);
            expirationMonth = Integer.valueOf(date[0])-1;
            expirationDay = Integer.valueOf(date[1]);

            binding.courseEditorCode.setText(course.getCode());
            binding.courseEditorName.setText(course.getName());
            binding.courseEditorTime.setText(selectedTimeSlot);
            binding.courseEditorExpiration.setText(course.getExpirationDate());

            binding.courseEditorToolbar.toolbar.setTitle(R.string.course_editor_label_edit);
        }
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.course_editor_time:
                if (selectedTimeSlot.isEmpty()){
                    startActivityForResult(TimeSlotPickerActivity.getIntent(this, true, false), TIME_SLOT_PICKER_RC);
                }
                else{
                    startActivityForResult(TimeSlotPickerActivity.getIntent(this, selectedTimeSlot, true, false), TIME_SLOT_PICKER_RC);
                }
                break;

            case R.id.course_editor_expiration:
                pickExpiration();
                break;

            case R.id.course_editor_done:
                if (areFieldsSet()){
                    binding.courseEditorError.setVisibility(View.GONE);
                    saveCourse();
                }
                else{
                    binding.courseEditorError.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == TIME_SLOT_PICKER_RC && resultCode == RESULT_OK){
            selectedTimeSlot = data.getStringExtra(TimeSlotPickerActivity.RESULT_KEY);
            String display = TimeSlotPickerActivity.get12HourFormattedString(selectedTimeSlot, false);
            binding.courseEditorTime.setText(display);
        }
    }

    private void pickExpiration(){
        Calendar calendar = Calendar.getInstance();
        int year = expirationYear != -1 ? expirationYear : calendar.get(Calendar.YEAR);
        int month = expirationMonth != -1 ? expirationMonth : calendar.get(Calendar.MONTH);
        int day = expirationDay != -1 ? expirationDay : calendar.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(this, this, year, month, day).show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth){
        expirationYear = year;
        expirationMonth = monthOfYear;
        expirationDay = dayOfMonth;

        String expiration = (expirationMonth+1) + "/" + expirationDay + "/" + expirationYear;
        binding.courseEditorExpiration.setText(expiration);
    }

    private boolean areFieldsSet(){
        if (binding.courseEditorCode.getText().toString().trim().isEmpty()){
            binding.courseEditorError.setText(R.string.course_editor_error_code);
            return false;
        }
        else if (binding.courseEditorName.getText().toString().trim().isEmpty()){
            binding.courseEditorError.setText(R.string.course_editor_error_name);
            return false;
        }
        else if (selectedTimeSlot.isEmpty()){
            binding.courseEditorError.setText(R.string.course_editor_error_meeting_time);
            return false;
        }
        else if (binding.courseEditorExpiration.getText().toString().trim().isEmpty()){
            binding.courseEditorError.setText(R.string.course_editor_error_expiration_date);
            return false;
        }
        return true;
    }

    private void saveCourse(){
        setFormState(true);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                if (course == null){
                    Course course = new Course(
                            binding.courseEditorCode.getText().toString().trim(),
                            binding.courseEditorName.getText().toString().trim(),
                            selectedTimeSlot,
                            binding.courseEditorExpiration.getText().toString().trim(),
                            ((OfficeHoursApp)getApplication()).getUser().getName()
                    );
                    setResult(RESULT_OK, new Intent().putExtra(COURSE_KEY, course));
                }
                else{
                    course.setCode(binding.courseEditorCode.getText().toString().trim());
                    course.setName(binding.courseEditorName.getText().toString().trim());
                    course.setMeetingTime(selectedTimeSlot);
                    course.setExpirationDate(binding.courseEditorExpiration.getText().toString().trim());
                    setResult(RESULT_OK, new Intent().putExtra(COURSE_KEY, course));
                }
                finish();
            }
        }, 2500);
    }

    private void setFormState(boolean loading){
        binding.courseEditorCode.setEnabled(!loading);
        binding.courseEditorName.setEnabled(!loading);
        binding.courseEditorTime.setEnabled(!loading);
        binding.courseEditorExpiration.setEnabled(!loading);
        if (loading){
            binding.courseEditorProgress.setVisibility(View.VISIBLE);
        }
        else{
            binding.courseEditorProgress.setVisibility(View.GONE);
        }
        binding.courseEditorDone.setEnabled(!loading);
    }
}
