package org.tndata.officehours.holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.tndata.officehours.databinding.ItemQuestionBinding;
import org.tndata.officehours.model.Question;


/**
 * View holder for a question
 *
 * @author Ismael Alonso
 */
public class QuestionHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    private ItemQuestionBinding binding;
    private Listener listener;


    public QuestionHolder(@NonNull ItemQuestionBinding binding, @NonNull Listener listener){
        super(binding.getRoot());
        this.binding = binding;
        this.listener = listener;
        itemView.setOnClickListener(this);
    }

    public void bind(@NonNull Question question){
        binding.setQuestion(question);
    }

    @Override
    public void onClick(View v){
        listener.onItemTapped(binding.getQuestion());
    }

    public interface Listener{
        void onItemTapped(@NonNull Question question);
    }
}
