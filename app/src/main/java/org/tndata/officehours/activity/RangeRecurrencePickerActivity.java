package org.tndata.officehours.activity;


import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import org.tndata.officehours.R;
import org.tndata.officehours.databinding.ActivityRangeRecurrencePickerBinding;

import java.util.Calendar;


/**
 * Lets the user choose a time range.
 *
 * TODO I need a better name for this.
 *
 * TODO disable the button if the form ain't complete
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public class RangeRecurrencePickerActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String RECURRENCE_KEY = "org.tndata.officehours.RRP.Recurrence";
    private static final String MC_KEY = "org.tndata.officehours.RRP.MultipleChoice";

    public static final String RESULT_KEY = "org.tndata.officehours.RRP.Result";


    public static Intent getIntent(@NonNull Context context, boolean multipleChoice){
        return new Intent(context, RangeRecurrencePickerActivity.class)
                .putExtra(MC_KEY, multipleChoice);
    }

    public static Intent getIntent(@NonNull Context context, String recurrence, boolean multipleChoice){
        return new Intent(context, RangeRecurrencePickerActivity.class)
                .putExtra(RECURRENCE_KEY, recurrence)
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
        String recurrence = getIntent().getStringExtra(RECURRENCE_KEY);
        Log.d("Blah@,", "Recurrence " + recurrence);
        if (recurrence != null && !recurrence.isEmpty()){
            setRecurrence(recurrence);
        }

        if (multipleChoice){
            binding.rrpRadio.setVisibility(View.GONE);
        }
        else{
            binding.rrpCheck.setVisibility(View.GONE);
        }

        binding.rrpFrom.setOnClickListener(this);
        binding.rrpTo.setOnClickListener(this);
        binding.rrpDone.setOnClickListener(this);

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

            case R.id.rrp_done:
                done();
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

    private void done(){
        setResult(RESULT_OK, new Intent().putExtra(RESULT_KEY, getRecurrence()));
        finish();
    }


    /*---------------------*
     * RECURRENCE HANDLERS *
     *---------------------*/

    /**
     * Sets a recurrence into the form.
     *
     * @param recurrence the recurrence to be parsed and set.
     */
    private void setRecurrence(@NonNull String recurrence){
        //Get the parts of the recurrence
        String frag[] = recurrence.split(" ");
        String days = frag[0];
        String time = frag[1];

        while (!days.isEmpty()){
            String day = days.substring(0, 1);
            if (multipleChoice){
                if (day.equals("M")){
                    binding.rrpCheckM.setChecked(true);
                }
                if (day.equals("T")){
                    binding.rrpCheckT.setChecked(true);
                }
                if (day.equals("W")){
                    binding.rrpCheckW.setChecked(true);
                }
                if (day.equals("R")){
                    binding.rrpCheckR.setChecked(true);
                }
                if (day.equals("F")){
                    binding.rrpCheckF.setChecked(true);
                }
                if (day.equals("S")){
                    binding.rrpCheckS.setChecked(true);
                }
            }
            else{
                if (day.equals("M")){
                    binding.rrpRadioM.setChecked(true);
                }
                else if (day.equals("T")){
                    binding.rrpRadioT.setChecked(true);
                }
                else if (day.equals("W")){
                    binding.rrpRadioW.setChecked(true);
                }
                else if (day.equals("R")){
                    binding.rrpRadioR.setChecked(true);
                }
                else if (day.equals("F")){
                    binding.rrpRadioF.setChecked(true);
                }
                else if (day.equals("S")){
                    binding.rrpRadioS.setChecked(true);
                }
            }
            days = days.substring(1);
        }

        frag = time.split("-");
        String from = frag[0];
        String to = frag[1];

        binding.rrpFrom.setText(from);
        binding.rrpTo.setText(to);

        frag = from.split(":");
        fromHour = Integer.valueOf(frag[0]);
        fromMinute = Integer.valueOf(frag[1]);

        frag = to.split(":");
        toHour = Integer.valueOf(frag[0]);
        toMinute = Integer.valueOf(frag[1]);
    }

    /**
     * Creates a recurrence string from the data in the form.
     *
     * @return the recurrence input by the user.
     */
    private String getRecurrence(){
        String result = "";

        if (multipleChoice){
            if (binding.rrpCheckM.isChecked()){
                result += "M";
            }
            if (binding.rrpCheckT.isChecked()){
                result += "T";
            }
            if (binding.rrpCheckW.isChecked()){
                result += "W";
            }
            if (binding.rrpCheckR.isChecked()){
                result += "R";
            }
            if (binding.rrpCheckF.isChecked()){
                result += "F";
            }
            if (binding.rrpCheckS.isChecked()){
                result += "S";
            }
        }
        else{
            if (binding.rrpRadioM.isChecked()){
                result += "M";
            }
            else if (binding.rrpRadioT.isChecked()){
                result += "T";
            }
            else if (binding.rrpRadioW.isChecked()){
                result += "W";
            }
            else if (binding.rrpRadioR.isChecked()){
                result += "R";
            }
            else if (binding.rrpRadioF.isChecked()){
                result += "F";
            }
            else if (binding.rrpRadioS.isChecked()){
                result += "S";
            }
        }

        result += " " + binding.rrpFrom.getText().toString().trim();
        return result + "-" + binding.rrpTo.getText().toString().trim();
    }
}
