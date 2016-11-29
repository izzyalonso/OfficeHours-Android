package org.tndata.officehours.activity;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import org.tndata.officehours.R;


/**
 * Lets the user choose a time range.
 *
 * @author Ismael Alonso
 */
public class RangeRecurrencePickerActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_range_recurrence_picker);
    }
}
