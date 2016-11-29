package org.tndata.officehours.activity;


import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.tndata.officehours.R;
import org.tndata.officehours.databinding.ActivityRangeRecurrencePickerBinding;


/**
 * Lets the user choose a time range.
 *
 * @author Ismael Alonso
 */
public class RangeRecurrencePickerActivity extends AppCompatActivity{
    private static final String MC_KEY = "org.tndata.officehours.RRP.MultipleChoice";

    public static Intent getIntent(@NonNull Context context, boolean multipleChoice){
        return new Intent(context, RangeRecurrencePickerActivity.class)
                .putExtra(MC_KEY, multipleChoice);
    }


    private ActivityRangeRecurrencePickerBinding binding;
    private boolean multipleChoice;

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
    }
}
