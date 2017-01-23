package org.tndata.officehours.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;

import org.tndata.officehours.OfficeHoursApp;
import org.tndata.officehours.R;
import org.tndata.officehours.databinding.ActivityOnBoardingBinding;
import org.tndata.officehours.model.User;
import org.tndata.officehours.util.ImageLoader;

import java.util.ArrayList;


/**
 * Class that handles on boarding.
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public class OnBoardingActivity
        extends AppCompatActivity
        implements
                View.OnClickListener,
                CompoundButton.OnCheckedChangeListener{

    private static final int TIME_SLOT_PICKER_RC = 2753;


    private ActivityOnBoardingBinding binding;

    private User user;
    private ArrayList<String> officeHours;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_on_boarding);

        setSupportActionBar(binding.onBoardingToolbar.toolbar);

        user = ((OfficeHoursApp)getApplication()).getUser();
        officeHours = new ArrayList<>();

        if (user.isStudent()){
            binding.onBoardingStudent.setChecked(true);
            displayForm(false);
        }
        else if (user.isTeacher()){
            binding.onBoardingTeacher.setChecked(true);
            displayForm(true);
        }

        if (!user.getPhotoUrl().isEmpty()){
            ImageLoader.Options options = new ImageLoader.Options().setCropToCircle(true);
            ImageLoader.loadBitmap(binding.onBoardingAvatar, user.getPhotoUrl(), options);
            binding.onBoardingAvatar.setVisibility(View.VISIBLE);
        }
        binding.onBoardingFirstName.setText(user.getFirstName());
        binding.onBoardingLastName.setText(user.getLastName());
        binding.onBoardingEmail.setText(user.getEmail());

        binding.onBoardingOfficeHoursAdd.setOnClickListener(this);
        binding.onBoardingSchoolEmailDisable.setOnCheckedChangeListener(this);
        binding.onBoardingFinish.setOnClickListener(this);
    }

    public void onTypeButtonClick(View view){
        switch (view.getId()){
            case R.id.on_boarding_student:
                if (!user.isStudent()){
                    user.setAsStudent();
                    user.writeToPreferences(this);
                    displayForm(false);
                }
                break;

            case R.id.on_boarding_teacher:
                if (!user.isTeacher()){
                    user.setAsTeacher();
                    user.writeToPreferences(this);
                    displayForm(true);
                }
                break;
        }
    }

    private void displayForm(boolean includeTeacherFields){
        if (includeTeacherFields){
            binding.onBoardingOfficeHoursSection.setVisibility(View.VISIBLE);
        }
        else{
            binding.onBoardingOfficeHoursSection.setVisibility(View.GONE);
        }
        binding.onBoardingPersonalInfoSection.setVisibility(View.VISIBLE);
        binding.onBoardingButtonContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view){
        if (view.getId() == R.id.on_boarding_office_hours_add){
            startActivityForResult(TimeSlotPickerActivity.getIntent(this, true, false, false), TIME_SLOT_PICKER_RC);
        }
        else if (view.getId() == R.id.on_boarding_finish){
            finishOnBoarding();
        }
        else{
            int index = binding.onBoardingOfficeHours.indexOfChild(view);
            binding.onBoardingOfficeHours.removeViewAt(index);
            officeHours.remove(index);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == TIME_SLOT_PICKER_RC && resultCode == RESULT_OK){
            String slot = data.getStringExtra(TimeSlotPickerActivity.RESULT_KEY);
            String display = TimeSlotPickerActivity.get12HourFormattedString(slot, true);
            Button button = new Button(this);
            button.setText(display);
            button.setOnClickListener(this);
            binding.onBoardingOfficeHours.addView(button);
            officeHours.add(slot);
        }
        else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean checked){
        if (checked){
            binding.onBoardingSchoolEmail.setText(user.getEmail());
        }
        else{
            binding.onBoardingSchoolEmail.setText("");
        }
        binding.onBoardingSchoolEmail.setEnabled(!checked);
    }

    private void finishOnBoarding(){
        String firstName = binding.onBoardingFirstName.getText().toString().trim();
        String lastName = binding.onBoardingLastName.getText().toString().trim();
        String email = binding.onBoardingSchoolEmail.getText().toString().trim();
        String phone = binding.onBoardingPhone.getText().toString().trim();

        binding.onBoardingError.setVisibility(View.GONE);

        if (firstName.isEmpty() || lastName.isEmpty()){
            binding.onBoardingError.setText(R.string.on_boarding_error_name);
            binding.onBoardingError.setVisibility(View.VISIBLE);
        }
        else if (email.isEmpty()){
            binding.onBoardingError.setText(R.string.on_boarding_error_email);
            binding.onBoardingError.setVisibility(View.VISIBLE);
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.onBoardingError.setText(R.string.on_boarding_error_email_invalid);
            binding.onBoardingError.setVisibility(View.VISIBLE);
        }
        else{
            user.setOfficeHours(officeHours);
            user.setName(firstName, lastName);
            user.setSchoolEmail(email);
            user.setPhoneNumber(phone);
            user.onBoardingCompleted();
            user.writeToPreferences(this);
            Intent launcher = new Intent(this, LauncherActivity.class)
                    .putExtra(LauncherActivity.FROM_ON_BOARDING_KEY, true);
            startActivity(launcher);
            finish();
        }
    }
}
