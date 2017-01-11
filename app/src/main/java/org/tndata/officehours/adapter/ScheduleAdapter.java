package org.tndata.officehours.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.tndata.officehours.model.Course;
import org.tndata.officehours.R;
import org.tndata.officehours.databinding.CardCourseBinding;
import org.tndata.officehours.holder.CourseHolder;

import java.util.List;


/**
 * Adapter used in ScheduleActivity to display the student's add
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public class ScheduleAdapter
        extends RecyclerView.Adapter<CourseHolder>
        implements CourseHolder.Listener{

    private Context context;
    private Listener listener;
    private List<Course> courses;


    /**
     * Constructor.
     *
     * @param context a reference to the context.
     * @param listener the listener object.
     * @param courses the list of courses to be initially displayed.
     */
    public ScheduleAdapter(
            @NonNull Context context, @NonNull Listener listener, @NonNull List<Course> courses
    ){
        this.context = context;
        this.listener = listener;
        this.courses = courses;
    }

    @Override
    public int getItemCount(){
        return courses.size();
    }

    @Override
    public CourseHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(context);
        CardCourseBinding binding;
        binding = DataBindingUtil.inflate(inflater, R.layout.card_course, parent, false);
        return new CourseHolder(binding, this);
    }

    @Override
    public void onBindViewHolder(CourseHolder holder, int position){
        holder.setCourse(courses.get(position));
    }

    public void setCourses(List<Course> courses){
        this.courses = courses;
        notifyDataSetChanged();
    }

    public void addCourse(@NonNull Course course){
        courses.add(course);
        notifyItemChanged(courses.size()-2);
        notifyItemInserted(courses.size()-1);
    }

    @Override
    public void onItemTapped(int position){
        listener.onCourseSelected(courses.get(position));
    }


    /**
     * Listener interface for the schedule adapter.
     *
     * @author Ismael Alonso
     * @version 1.0.0
     */
    public interface Listener{
        /**
         * Called when a course is tapped.
         *
         * @param course the tapped course.
         */
        void onCourseSelected(@NonNull Course course);
    }
}
