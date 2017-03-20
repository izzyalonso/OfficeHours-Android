package org.tndata.officehours.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import org.tndata.officehours.R;
import org.tndata.officehours.adapter.QuestionAdapter;
import org.tndata.officehours.databinding.ActivityListBinding;
import org.tndata.officehours.model.Question;


/**
 * Class to display a Question and its Answers.
 *
 * @author Ismael Alonso
 */
public class QuestionActivity extends AppCompatActivity{
    private static final String QUESTION_KEY = "org.tndata.QuestionActivity.Question";


    public Intent getIntent(@NonNull Context context, @NonNull Question question){
        return new Intent(context, QuestionActivity.class)
                .putExtra(QUESTION_KEY, question);
    }


    private ActivityListBinding binding;
    private Question question;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_list);

        setSupportActionBar(binding.listToolbar.toolbar);

        question = getIntent().getExtras().getParcelable(QUESTION_KEY);
        binding.listList.setLayoutManager(new LinearLayoutManager(this));
        binding.listList.setAdapter(new QuestionAdapter(this, question));
    }
}
