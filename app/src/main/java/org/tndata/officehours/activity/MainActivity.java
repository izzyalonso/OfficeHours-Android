package org.tndata.officehours.activity;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.tndata.officehours.R;
import org.tndata.officehours.databinding.ActivityMainBinding;


/**
 * Activity that displays a student's schedule
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public class MainActivity extends AppCompatActivity{
    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setSupportActionBar(binding.mainToolbar);
    }
}
