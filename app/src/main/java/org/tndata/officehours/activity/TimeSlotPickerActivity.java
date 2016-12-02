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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TimePicker;

import org.tndata.officehours.R;
import org.tndata.officehours.databinding.ActivityTimeSlotPickerBinding;

import java.util.Calendar;


/**
 * Lets the user choose a time range.
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

    public static final String RESULT_KEY = "org.tndata.officehours.TSP.Result";


    /**
     * Creates an intent to launch a blank picker.
     *
     * @param context a reference to the context.
     * @param multiChoice true if multiple day choice is allowed, false otherwise.
     * @return the intent to launch the activity.
     */
    public static Intent getIntent(@NonNull Context context, boolean multiChoice){
        return new Intent(context, TimeSlotPickerActivity.class)
                .putExtra(MC_KEY, multiChoice);
    }

    /**
     * Creates an intent to launch a pre filled picker.
     *
     * @param context a reference to the context
     * @param slot the data to pre-fill the activity with.
     * @param multiChoice rue if multiple day choice is allowed, false otherwise.
     * @return the intent to launch the activity.
     */
    public static Intent getIntent(@NonNull Context context, @NonNull String slot, boolean multiChoice){
        return new Intent(context, TimeSlotPickerActivity.class)
                .putExtra(SLOT_KEY, slot)
                .putExtra(MC_KEY, multiChoice);
    }


    private ActivityTimeSlotPickerBinding binding;
    private boolean multiChoice;

    private boolean radioSelected;
    private int selectedCheckboxes;
    private int fromHour, fromMinute;
    private int toHour, toMinute;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_time_slot_picker);

        //Display the correct day picker
        multiChoice = getIntent().getBooleanExtra(MC_KEY, false);
        if (multiChoice){
            binding.tspRadio.setVisibility(View.GONE);
        }
        else{
            binding.tspCheck.setVisibility(View.GONE);
        }

        //Set initial state
        radioSelected = false;
        selectedCheckboxes = 0;
        fromHour = -1;
        fromMinute = -1;
        toHour = -1;
        toMinute = -1;
        
        //Set the listeners
        binding.tspRadio.setOnCheckedChangeListener(this);
        binding.tspCheckM.setOnCheckedChangeListener(this);
        binding.tspCheckT.setOnCheckedChangeListener(this);
        binding.tspCheckW.setOnCheckedChangeListener(this);
        binding.tspCheckR.setOnCheckedChangeListener(this);
        binding.tspCheckF.setOnCheckedChangeListener(this);
        binding.tspCheckS.setOnCheckedChangeListener(this);
        binding.tspFrom.setOnClickListener(this);
        binding.tspTo.setOnClickListener(this);
        binding.tspDone.setOnClickListener(this);
        
        //If there is a slot available, fill the form with it
        String slot = getIntent().getStringExtra(SLOT_KEY);
        if (slot != null && !slot.isEmpty()){
            setSlot(slot);
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
                selectTime(fromHour, fromMinute, binding.tspFrom);
                break;

            case R.id.tsp_to:
                selectTime(toHour, toMinute, binding.tspTo);
                break;

            case R.id.tsp_done:
                done();
                break;
        }
    }

    /**
     * Fires a time picker dialog to select a time and sets the result to the selected section.
     * 
     * @param currentHour the hour currently picked in the selected section or -1 if none
     * @param currentMinute the minute currently picked in the selected section or -1 if none
     * @param target the section to which the result should be written.
     */
    private void selectTime(int currentHour, int currentMinute, final EditText target){
        //Set defaults if necessary
        Calendar calendar = Calendar.getInstance();
        if (currentHour == -1){
            currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        }
        if (currentMinute == -1){
            currentMinute = calendar.get(Calendar.MINUTE);
        }

        //Fire the dialog
        new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener(){
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute){
                if (target == binding.tspFrom){
                    fromHour = hour;
                    fromMinute = minute;
                }
                else if (target == binding.tspTo){
                    toHour = hour;
                    toMinute = minute;
                }
                target.setText(hour + ":" + (minute < 10 ? "0" + minute : minute));
                checkFromState();
            }
        }, currentHour, currentMinute, false).show();
    }

    /**
     * Decides whether the done button should be enabled or disabled.
     */
    private void checkFromState(){
        if (radioSelected || selectedCheckboxes != 0){
            if (fromMinute != -1 && toHour != -1){
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


    /*---------------*
     * SLOT HANDLERS *
     *---------------*/

    /**
     * Populates the form with the provided slot.
     *
     * @param slot the slot used to fill the form.
     */
    private void setSlot(@NonNull String slot){
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

        frag = time.split("-");
        String from = frag[0];
        String to = frag[1];

        binding.tspFrom.setText(from);
        binding.tspTo.setText(to);

        frag = from.split(":");
        fromHour = Integer.valueOf(frag[0]);
        fromMinute = Integer.valueOf(frag[1]);

        frag = to.split(":");
        toHour = Integer.valueOf(frag[0]);
        toMinute = Integer.valueOf(frag[1]);

        checkFromState();
    }

    /**
     * Creates a slot string from the data in the form.
     *
     * @return the slot input by the user.
     */
    private String getSlot(){
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

        result += " " + binding.tspFrom.getText().toString().trim();
        return result + "-" + binding.tspTo.getText().toString().trim();
    }
}
