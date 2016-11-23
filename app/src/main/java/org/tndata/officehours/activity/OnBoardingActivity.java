package org.tndata.officehours.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;

import org.tndata.officehours.OfficeHoursApp;
import org.tndata.officehours.R;
import org.tndata.officehours.databinding.ActivityOnBoardingBinding;
import org.tndata.officehours.model.User;


/**
 * Class that handles on boarding.
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public class OnBoardingActivity extends AppCompatActivity implements View.OnClickListener{
    private ActivityOnBoardingBinding binding;

    private User user;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_on_boarding);

        setSupportActionBar(binding.onBoardingToolbar.toolbar);

        user = ((OfficeHoursApp)getApplication()).getUser();

        if (user.isStudent()){
            binding.onBoardingStudent.setChecked(true);
            binding.onBoardingInfoContainer.setVisibility(View.VISIBLE);
        }
        else if (user.isTeacher()){
            binding.onBoardingTeacher.setChecked(true);
            binding.onBoardingInfoContainer.setVisibility(View.VISIBLE);
        }

        binding.onBoardingFirstName.setText(user.getFirstName());
        binding.onBoardingLastName.setText(user.getLastName());
        binding.onBoardingEmail.setText(user.getEmail());

        binding.onBoardingFinish.setOnClickListener(this);
    }

    public void onTypeButtonClick(View view){
        //If the user has not selected a type yet, show more options
        if (!user.hasDefinedType()){
            binding.onBoardingInfoContainer.setVisibility(View.VISIBLE);
        }

        switch (view.getId()){
            case R.id.on_boarding_student:
                if (!user.isStudent()){
                    user.setAsStudent();
                    user.writeToPreferences(this);
                }
                break;

            case R.id.on_boarding_teacher:
                if (!user.isTeacher()){
                    user.setAsTeacher();
                    user.writeToPreferences(this);
                }
                break;
        }
    }

    @Override
    public void onClick(View view){
        if (view.getId() == R.id.on_boarding_finish){
            finishOnBoarding();
        }
    }

    private void finishOnBoarding(){
        String firstName = binding.onBoardingFirstName.getText().toString().trim();
        String lastName = binding.onBoardingLastName.getText().toString().trim();
        String email = binding.onBoardingEmail.getText().toString().trim();
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
            user.setName(firstName, lastName);
            user.setSchoolEmail(email);
            user.setPhoneNumber(phone);
            user.onBoardingCompleted();
            user.writeToPreferences(this);
            startActivity(new Intent(this, ScheduleActivity.class));
            finish();
        }
    }
}
