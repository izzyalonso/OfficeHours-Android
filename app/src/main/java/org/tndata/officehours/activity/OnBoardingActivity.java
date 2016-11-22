package org.tndata.officehours.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import org.tndata.officehours.R;
import org.tndata.officehours.databinding.ActivityOnBoardingBinding;


/**
 * Class that handles on boarding.
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public class OnBoardingActivity extends AppCompatActivity{
    private ActivityOnBoardingBinding binding;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_on_boarding);

        setSupportActionBar(binding.onBoardingToolbar.toolbar);
    }
}
