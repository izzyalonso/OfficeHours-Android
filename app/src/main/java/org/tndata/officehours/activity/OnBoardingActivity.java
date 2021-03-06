package org.tndata.officehours.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import org.tndata.officehours.OfficeHoursApp;
import org.tndata.officehours.R;
import org.tndata.officehours.databinding.ActivityOnBoardingBinding;
import org.tndata.officehours.model.User;
import org.tndata.officehours.util.API;
import org.tndata.officehours.util.ImageLoader;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import es.sandwatch.httprequests.HttpRequest;
import es.sandwatch.httprequests.HttpRequestError;


/**
 * Class that handles on boarding.
 *
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public class OnBoardingActivity
        extends AppCompatActivity
        implements
                View.OnClickListener,
                CompoundButton.OnCheckedChangeListener,
                HttpRequest.RequestCallback{

    private static final int TIME_SLOT_PICKER_RC = 2753;


    private ActivityOnBoardingBinding binding;

    private User user;
    private ArrayList<String> officeHours;
    private Set<Integer> officeHoursRequestCodes;
    private int profileRequestCode;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_on_boarding);

        setSupportActionBar(binding.onBoardingToolbar.toolbar);

        user = ((OfficeHoursApp)getApplication()).getUser();
        officeHours = new ArrayList<>();

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
                user.asStudent();
                displayForm(false);
                break;

            case R.id.on_boarding_teacher:
                user.asTeacher();
                displayForm(true);
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
        /*else if (email.isEmpty()){
            binding.onBoardingError.setText(R.string.on_boarding_error_email);
            binding.onBoardingError.setVisibility(View.VISIBLE);
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.onBoardingError.setText(R.string.on_boarding_error_email_invalid);
            binding.onBoardingError.setVisibility(View.VISIBLE);
        }*/
        else{
            user.setOfficeHours(officeHours);
            user.setName(firstName, lastName);
            user.setSchoolEmail(email);
            user.setPhoneNumber(phone);
            user.onBoardingCompleted();
            user.writeToPreferences(this);

            officeHoursRequestCodes = new HashSet<>();
            if (binding.onBoardingTeacher.isChecked() && !officeHours.isEmpty()){
                for (String slot:officeHours){
                    officeHoursRequestCodes.add(HttpRequest.post(this, API.URL.officeHours(), API.BODY.officeHours(slot)));
                }
            }
            else{
                profileRequestCode = HttpRequest.put(this, API.URL.profile(user), API.BODY.profile(user));
            }
            binding.onBoardingProgress.setVisibility(View.VISIBLE);
            binding.onBoardingFinish.setEnabled(false);
        }
    }

    @Override
    public void onRequestComplete(int requestCode, String result){
        if (officeHoursRequestCodes.contains(requestCode)){
            officeHoursRequestCodes.remove(requestCode);
            if (officeHoursRequestCodes.isEmpty()){
                profileRequestCode = HttpRequest.put(this, API.URL.profile(user), API.BODY.profile(user));
            }
        }
        else if (requestCode == profileRequestCode){
            Intent launcher = new Intent(this, LauncherActivity.class)
                    .putExtra(LauncherActivity.FROM_ON_BOARDING_KEY, true);
            startActivity(launcher);
            finish();
        }
    }

    @Override
    public void onRequestFailed(int requestCode, HttpRequestError error){
        binding.onBoardingProgress.setVisibility(View.GONE);
        binding.onBoardingFinish.setEnabled(true);

        Toast.makeText(this, R.string.on_boarding_error_network, Toast.LENGTH_LONG).show();
    }
}
