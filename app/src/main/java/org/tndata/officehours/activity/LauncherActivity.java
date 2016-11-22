package org.tndata.officehours.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.tndata.officehours.R;
import org.tndata.officehours.databinding.ActivityLauncherBinding;


/**
 * Lets the user sign in.
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public class LauncherActivity extends AppCompatActivity implements View.OnClickListener{
    private ActivityLauncherBinding binding;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_launcher);
        binding.launcherGooogleSignIn.setOnClickListener(this);
        binding.launcherProceed.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        if (view.getId() == R.id.launcher_gooogle_sign_in){
            signInWithGoogle();
        }
        else if (view.getId() == R.id.launcher_proceed){
            startActivity(new Intent(this, ScheduleActivity.class));
            finish();
        }
    }

    private void signInWithGoogle(){

    }
}
