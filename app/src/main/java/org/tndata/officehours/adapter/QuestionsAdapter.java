package org.tndata.officehours.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.tndata.officehours.R;
import org.tndata.officehours.databinding.ItemQuestionBinding;
import org.tndata.officehours.holder.QuestionHolder;
import org.tndata.officehours.model.Question;

import java.util.List;


/**
 * Adapter for a list of questions.
 *
 * @author Ismael Alonso
 */
public class QuestionsAdapter extends RecyclerView.Adapter implements QuestionHolder.Listener{
    private static final int TYPE_QUESTION = 1;
    private static final int TYPE_FOOTER_SPACE = 2;


    private Context context;
    private List<Question> questions;
    private Listener listener;


    public QuestionsAdapter(@NonNull Context context, @NonNull List<Question> questions,
                            @NonNull Listener listener){

        this.context = context;
        this.questions = questions;
        this.listener = listener;
    }

    @Override
    public int getItemCount(){
        return questions.size()+1;
    }

    @Override
    public int getItemViewType(int position){
        if (position == questions.size()){
            return TYPE_FOOTER_SPACE;
        }
        return TYPE_QUESTION;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == TYPE_QUESTION){
            @LayoutRes int res = R.layout.item_question;
            ItemQuestionBinding binding = DataBindingUtil.inflate(inflater, res, parent, false);
            return new QuestionHolder(binding, this);
        }
        if (viewType == TYPE_FOOTER_SPACE){
            View view = inflater.inflate(R.layout.item_people_footer_space, parent, false);
            return new RecyclerView.ViewHolder(view){};
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder rawHolder, int position){
        if (getItemViewType(position) == TYPE_QUESTION){
            ((QuestionHolder)rawHolder).bind(questions.get(position));
        }
    }

    @Override
    public void onItemTapped(@NonNull Question question){
        listener.onQuestionSelected(question);
    }


    /**
     * Listener for {@code QuestionAdapter}.
     *
     * @author Ismael Alonso.
     */
    public interface Listener{
        void onQuestionSelected(@NonNull Question question);
    }
}
