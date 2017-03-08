package org.tndata.officehours.holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.tndata.officehours.model.Course;
import org.tndata.officehours.databinding.CardCourseBinding;


/**
 * View holder for a listed course item.
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public class CourseHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    private CardCourseBinding binding;
    private Listener listener;


    /**
     * Constructor.
     *
     * @param binding the binding object.
     */
    public CourseHolder(@NonNull CardCourseBinding binding, @NonNull Listener listener){
        super(binding.getRoot());
        this.binding = binding;
        this.listener = listener;
        itemView.setOnClickListener(this);
    }

    /**
     * Binds a course to this view holder.
     *
     * @param course the course to be bound.
     */
    public void setCourse(@NonNull Course course){
        binding.setCourse(course);
    }

    @Override
    public void onClick(View view){
        listener.onItemTapped(getAdapterPosition());
    }


    /**
     * Listener interface for the holder.
     *
     * @author Ismael Alonso
     * @version 1.0.0
     */
    public interface Listener{
        /**
         * Called when the holder is tapped.
         *
         * @param position the position of this holder within the adapter.
         */
        void onItemTapped(int position);
    }
}
