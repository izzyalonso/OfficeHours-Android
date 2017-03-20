package org.tndata.officehours.holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import org.tndata.officehours.databinding.ItemQuestionBinding;
import org.tndata.officehours.model.Question;


/**
 * Holder to display a full question.
 *
 * @author Ismael Alonso
 */
public class QuestionHolder extends RecyclerView.ViewHolder{
    private ItemQuestionBinding binding;


    /**
     * Constructor.
     *
     * @param binding the binding object for item_question.xml
     */
    public QuestionHolder(ItemQuestionBinding binding){
        super(binding.getRoot());
        this.binding = binding;
    }

    /**
     * Binds a question to this holder.
     *
     * @param question the question to bind.
     */
    public void bind(@NonNull Question question){
        binding.setQuestion(question);
    }
}
