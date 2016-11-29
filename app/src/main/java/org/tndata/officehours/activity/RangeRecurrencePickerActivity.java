package org.tndata.officehours.activity;


import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import org.tndata.officehours.R;
import org.tndata.officehours.databinding.ActivityRangeRecurrencePickerBinding;

import java.util.Calendar;


/**
 * Lets the user choose a time range.
 *
 * @author Ismael Alonso
 */
public class RangeRecurrencePickerActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String MC_KEY = "org.tndata.officehours.RRP.MultipleChoice";


    public static Intent getIntent(@NonNull Context context, boolean multipleChoice){
        return new Intent(context, RangeRecurrencePickerActivity.class)
                .putExtra(MC_KEY, multipleChoice);
    }


    private ActivityRangeRecurrencePickerBinding binding;
    private boolean multipleChoice;

    private int fromHour;
    private int fromMinute;

    private int toHour;
    private int toMinute;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_range_recurrence_picker);

        multipleChoice = getIntent().getBooleanExtra(MC_KEY, false);

        if (multipleChoice){
            binding.rrpRadio.setVisibility(View.GONE);
        }
        else{
            binding.rrpCheck.setVisibility(View.GONE);
        }

        binding.rrpFrom.setOnClickListener(this);
        binding.rrpTo.setOnClickListener(this);

        fromHour = -1;
        fromMinute = -1;

        toHour = -1;
        toMinute = -1;
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.rrp_from:
                selectTime(fromHour, fromMinute, binding.rrpFrom);
                break;

            case R.id.rrp_to:
                selectTime(toHour, toMinute, binding.rrpTo);
                break;
        }
    }

    //TODO Haphazard!! I can and will do better than this when time allows
    private void selectTime(int currentHour, int currentMinute, final EditText target){
        Calendar calendar = Calendar.getInstance();
        if (currentHour == -1){
            currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        }
        if (currentMinute == -1){
            currentMinute = calendar.get(Calendar.MINUTE);
        }

        new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener(){
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute){
                if (target == binding.rrpFrom){
                    fromHour = hour;
                    fromMinute = minute;
                }
                else if (target == binding.rrpTo){
                    toHour = hour;
                    toMinute = minute;
                }
                target.setText(hour + ":" + minute);
            }
        }, currentHour, currentMinute, true).show();
    }
}
