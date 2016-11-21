package org.tndata.officehours.holder;

import android.support.v7.widget.RecyclerView;

import org.tndata.officehours.databinding.CardCourseBinding;


/**
 * Created by ialonso on 11/21/16.
 */
public class CourseHolder extends RecyclerView.ViewHolder{
    private CardCourseBinding binding;


    public CourseHolder(CardCourseBinding binding){
        super(binding.getRoot());
        this.binding = binding;
    }

    public void setCourse(){
        
    }
}
