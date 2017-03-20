package org.tndata.officehours.holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.tndata.officehours.databinding.ItemCompactQuestionBinding;
import org.tndata.officehours.model.Question;


/**
 * View holder for a question
 *
 * @author Ismael Alonso
 */
public class CompactQuestionHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    private ItemCompactQuestionBinding binding;
    private Listener listener;


    public CompactQuestionHolder(@NonNull ItemCompactQuestionBinding binding, @NonNull Listener listener){
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
