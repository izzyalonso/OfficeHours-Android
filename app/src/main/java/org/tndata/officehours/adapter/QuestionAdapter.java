package org.tndata.officehours.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.tndata.officehours.R;
import org.tndata.officehours.databinding.ItemQuestionBinding;
import org.tndata.officehours.holder.QuestionHolder;
import org.tndata.officehours.model.Question;


/**
 * Adapter for QuestionActivity. Displays a question and some of its answers.
 *
 * @author Ismael Alonso
 */
public class QuestionAdapter extends RecyclerView.Adapter{
    private static final int TYPE_QUESTION = 0;
    private static final int TYPE_ANSWER = 1;
    private static final int TYPE_LOADING = 2;


    private final Context context;
    private final Question question;


    public QuestionAdapter(@NonNull Context context, @NonNull Question question){
        this.context = context;
        this.question = question;
    }

    @Override
    public int getItemCount(){
        //Question + Answers + Loading
        return 1;
    }

    @Override
    public int getItemViewType(int position){
        return TYPE_QUESTION;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == TYPE_QUESTION){
            @LayoutRes int res = R.layout.item_question;
            ItemQuestionBinding binding = DataBindingUtil.inflate(inflater, res, parent, false);
            return new QuestionHolder(binding);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder rawHolder, int position){
        if (position == 0){
            ((QuestionHolder)rawHolder).bind(question);
        }
    }
}
