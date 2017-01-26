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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TimePicker;

import org.tndata.officehours.R;
import org.tndata.officehours.databinding.ActivityTimeSlotPickerBinding;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * Lets the user choose a time range. Takes and delivers time ranges in the following format:
 *
 * M{0,1}T{0,1}W{0,1}R{0,1}F{0,1}S{0,1} [1-2]{0,1}[0-9]:[1-6]{0,1}[0-9]\-[1-2]{0,1}[0-9]:[1-6]{0,1}[0-9]
 *
 * Example: MW 13:00-14:15
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public class TimeSlotPickerActivity
        extends AppCompatActivity
        implements
                RadioGroup.OnCheckedChangeListener,
                CompoundButton.OnCheckedChangeListener,
                View.OnClickListener{

    //Keys and such
    private static final String SLOT_KEY = "org.tndata.officehours.TSP.Slot";
    private static final String MC_KEY = "org.tndata.officehours.TSP.MultiChoice";
    private static final String SINGLE_TIME_KEY = "org.tndata.officehours.TSP.SingleTime";
    private static final String IS_24_HOUR_FORMAT_KEY = "org.tndata.officehours.TSP.24HourFormat";

    public static final String RESULT_KEY = "org.tndata.officehours.TSP.Result";


    private static final String TAG = "TimeSlotPickerActivity";

    private static final DateFormat parser = new SimpleDateFormat("H:mm", Locale.getDefault());
    private static final DateFormat formatter12 = new SimpleDateFormat("K:mm a", Locale.getDefault());
    private static final DateFormat formatter24 = new SimpleDateFormat("H:mm", Locale.getDefault());


    /**
     * Creates an intent to launch a blank picker.
     *
     * @param context a reference to the context.
     * @param multiChoice true if multiple day choice is allowed, false otherwise.
     * @param singleTime true if the picker should allow to pick only a start time, false otherwise.
     * @param is24HourFormat true if the picker should do 24 hour time formatting, false otherwise.
     * @return the intent to launch the activity.
     */
    public static Intent getIntent(@NonNull Context context, boolean multiChoice,
                                   boolean singleTime, boolean is24HourFormat){
        return new Intent(context, TimeSlotPickerActivity.class)
                .putExtra(MC_KEY, multiChoice)
                .putExtra(SINGLE_TIME_KEY, singleTime)
                .putExtra(IS_24_HOUR_FORMAT_KEY, is24HourFormat);
    }

    /**
     * Creates an intent to launch a pre filled picker.
     *
     * @param context a reference to the context
     * @param slot the data to pre-fill the activity with.
     * @param multiChoice rue if multiple day choice is allowed, false otherwise.
     * @param singleTime true if the picker should allow to pick only a start time, false otherwise.
     * @param is24HourFormat true if the picker should do 24 hour time formatting, false otherwise.
     * @return the intent to launch the activity.
     */
    public static Intent getIntent(@NonNull Context context, @NonNull String slot,
                                   boolean multiChoice, boolean singleTime, boolean is24HourFormat){

        return new Intent(context, TimeSlotPickerActivity.class)
                .putExtra(SLOT_KEY, slot)
                .putExtra(MC_KEY, multiChoice)
                .putExtra(SINGLE_TIME_KEY, singleTime)
                .putExtra(IS_24_HOUR_FORMAT_KEY, is24HourFormat);
    }


    private ActivityTimeSlotPickerBinding binding;
    private boolean multiChoice;
    private boolean singleTime;
    private boolean is24HourFormat;

    private boolean radioSelected;
    private int selectedCheckboxes;

    private Date from, to;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_time_slot_picker);

        //Grab the setting flags
        multiChoice = getIntent().getBooleanExtra(MC_KEY, false);
        singleTime = getIntent().getBooleanExtra(SINGLE_TIME_KEY, false);
        is24HourFormat = getIntent().getBooleanExtra(IS_24_HOUR_FORMAT_KEY, false);

        //Display the selected day picker
        if (multiChoice){
            binding.tspRadio.setVisibility(View.GONE);
        }
        else{
            binding.tspCheck.setVisibility(View.GONE);
        }

        //Set initial state
        radioSelected = false;
        selectedCheckboxes = 0;
        
        //Set the listeners
        binding.tspRadio.setOnCheckedChangeListener(this);
        binding.tspCheckM.setOnCheckedChangeListener(this);
        binding.tspCheckT.setOnCheckedChangeListener(this);
        binding.tspCheckW.setOnCheckedChangeListener(this);
        binding.tspCheckR.setOnCheckedChangeListener(this);
        binding.tspCheckF.setOnCheckedChangeListener(this);
        binding.tspCheckS.setOnCheckedChangeListener(this);
        binding.tspFrom.setOnClickListener(this);
        if (singleTime){
            binding.tspDash.setVisibility(View.GONE);
            binding.tspTo.setVisibility(View.GONE);
        }
        else{
            binding.tspTo.setOnClickListener(this);
        }
        binding.tspDone.setOnClickListener(this);
        
        //If there is a slot available, fill the form with it
        String slot = getIntent().getStringExtra(SLOT_KEY);
        if (slot != null && !slot.isEmpty()){
            setSlot(slot);
        }
        else{
            from = null;
            to = null;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int which){
        //Once a button is selected in a radio group, there will always be one button selected
        radioSelected = true;
        checkFromState();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean checked){
        //In the case of checkboxes, it is easier to keep track of the number of currently
        //  selected boxes
        if (checked){
            selectedCheckboxes++;
        }
        else{
            selectedCheckboxes--;
        }
        checkFromState();
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tsp_from:
                selectTime(from, binding.tspFrom);
                break;

            case R.id.tsp_to:
                selectTime(to, binding.tspTo);
                break;

            case R.id.tsp_done:
                done();
                break;
        }
    }

    /**
     * Fires a time picker dialog to select a time and sets the result to the selected section.
     * 
     * @param selectedTime the current choice in the selected section.
     * @param target the section to which the result should be written.
     */
    private void selectTime(@Nullable Date selectedTime, final EditText target){
        //Extract hour and minute
        Calendar time = Calendar.getInstance();
        if (selectedTime != null){
            time.setTime(selectedTime);
        }
        int currentHour = time.get(Calendar.HOUR_OF_DAY);
        int currentMinute = time.get(Calendar.MINUTE);

        //Fire the dialog
        new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener(){
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute){
                Calendar time = Calendar.getInstance();
                time.set(Calendar.HOUR_OF_DAY, hour);
                time.set(Calendar.MINUTE, minute);
                if (target == binding.tspFrom){
                    from = time.getTime();
                }
                else if (target == binding.tspTo){
                    to = time.getTime();
                }
                target.setText(getSelectedFormatter().format(time.getTime()));
                checkFromState();
            }
        }, currentHour, currentMinute, is24HourFormat).show();
    }

    /**
     * Decides whether the done button should be enabled or disabled.
     */
    private void checkFromState(){
        if (radioSelected || selectedCheckboxes != 0){
            if (from != null && (singleTime || to != null)){
                binding.tspDone.setEnabled(true);
            }
            else{
                binding.tspDone.setEnabled(false);
            }
        }
        else{
            binding.tspDone.setEnabled(false);
        }
    }

    /**
     * Bundles the result in an intent and finishes the activity.
     */
    private void done(){
        setResult(RESULT_OK, new Intent().putExtra(RESULT_KEY, getSlot()));
        finish();
    }

    /**
     * Gets the correct formatter.
     *
     * @return either the 24 or the 12 hour time formatter.
     */
    private DateFormat getSelectedFormatter(){
        return is24HourFormat ? formatter24 : formatter12;
    }


    /*---------------*
     * SLOT HANDLERS *
     *---------------*/

    /**
     * Populates the form with the provided slot.
     *
     * @param slot the slot used to fill the form.
     */
    private void setSlot(@NonNull String slot){
        Log.d(TAG, "Setting slot: " + slot);

        //Get the parts of the recurrence
        String frag[] = slot.split(" ");
        String days = frag[0];
        String time = frag[1];

        while (!days.isEmpty()){
            String day = days.substring(0, 1);
            if (multiChoice){
                if (day.equals("M")){
                    binding.tspCheckM.setChecked(true);
                    selectedCheckboxes++;
                }
                if (day.equals("T")){
                    binding.tspCheckT.setChecked(true);
                    selectedCheckboxes++;
                }
                if (day.equals("W")){
                    binding.tspCheckW.setChecked(true);
                    selectedCheckboxes++;
                }
                if (day.equals("R")){
                    binding.tspCheckR.setChecked(true);
                    selectedCheckboxes++;
                }
                if (day.equals("F")){
                    binding.tspCheckF.setChecked(true);
                    selectedCheckboxes++;
                }
                if (day.equals("S")){
                    binding.tspCheckS.setChecked(true);
                    selectedCheckboxes++;
                }
            }
            else{
                if (day.equals("M")){
                    binding.tspRadioM.setChecked(true);
                    radioSelected = true;
                }
                else if (day.equals("T")){
                    binding.tspRadioT.setChecked(true);
                    radioSelected = true;
                }
                else if (day.equals("W")){
                    binding.tspRadioW.setChecked(true);
                    radioSelected = true;
                }
                else if (day.equals("R")){
                    binding.tspRadioR.setChecked(true);
                    radioSelected = true;
                }
                else if (day.equals("F")){
                    binding.tspRadioF.setChecked(true);
                    radioSelected = true;
                }
                else if (day.equals("S")){
                    binding.tspRadioS.setChecked(true);
                    radioSelected = true;
                }
            }
            days = days.substring(1);
        }

        if (singleTime){
            frag[0] = time;
        }
        else{
            frag = time.split("-");
        }
        try{
            DateFormat formatter = getSelectedFormatter();

            from = parser.parse(frag[0]);
            binding.tspFrom.setText(formatter.format(from));
            if (!singleTime){
                to = parser.parse(frag[1]);
                binding.tspTo.setText(formatter.format(to));
            }
        }
        catch (ParseException px){
            px.printStackTrace();
        }

        checkFromState();
    }

    /**
     * Creates a slot string from the data in the form.
     *
     * @return the slot input by the user.
     */
    private String getSlot(){
        Log.d(TAG, "Generating slot...");

        String result = "";
        if (multiChoice){
            if (binding.tspCheckM.isChecked()){
                result += "M";
            }
            if (binding.tspCheckT.isChecked()){
                result += "T";
            }
            if (binding.tspCheckW.isChecked()){
                result += "W";
            }
            if (binding.tspCheckR.isChecked()){
                result += "R";
            }
            if (binding.tspCheckF.isChecked()){
                result += "F";
            }
            if (binding.tspCheckS.isChecked()){
                result += "S";
            }
        }
        else{
            if (binding.tspRadioM.isChecked()){
                result += "M";
            }
            else if (binding.tspRadioT.isChecked()){
                result += "T";
            }
            else if (binding.tspRadioW.isChecked()){
                result += "W";
            }
            else if (binding.tspRadioR.isChecked()){
                result += "R";
            }
            else if (binding.tspRadioF.isChecked()){
                result += "F";
            }
            else if (binding.tspRadioS.isChecked()){
                result += "S";
            }
        }

        result += " " + formatter24.format(from);
        if (!singleTime){
            result += "-" + formatter24.format(to);
        }
        Log.d(TAG, "Result: " + result);
        return result;
    }

    public static String get12HourFormattedString(@NonNull String src, boolean expandWeekDays){
        Log.d(TAG, "Formatting " + src + " to 12 hour format...");

        String result = "";

        //Split times the days and times sections
        String frags[] = src.split(" ");
        String days = frags[0];
        String times = frags[1];

        //Format the week days
        if (!expandWeekDays){
            result = days;
        }
        else{
            while (!days.isEmpty()){
                if (!result.isEmpty()){
                    if (!result.contains(" ") && days.length() == 1){
                        result += " and ";
                    }
                    else{
                        result += ", ";
                        if (days.length() == 1){
                            result += "and ";
                        }
                    }
                }

                String day = days.substring(0, 1);
                days = days.substring(1);
                switch (day){
                    case "M":
                        result += "Monday";
                        break;

                    case "T":
                        result += "Tuesday";
                        break;

                    case "W":
                        result += "Wednesday";
                        break;

                    case "R":
                        result += "Thursday";
                        break;

                    case "F":
                        result += "Friday";
                        break;

                    case "S":
                        result += "Saturday";
                        break;

                }
            }
        }
        result += " ";

        //Split from and to
        frags = times.split("-");
        //Add the formatted dates
        try{
            result += formatter12.format(parser.parse(frags[0]));
            if (frags.length == 2){
                result += " - ";
                result += formatter12.format(parser.parse(frags[1]));
            }
        }
        catch (ParseException px){
            px.printStackTrace();
        }

        Log.d(TAG, "Result: " + result);

        return result;
    }
}
