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
import org.tndata.officehours.model.StudentCourse;

import java.util.List;


/**
 * Adapter used in ScheduleActivity to display the student's add
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public class ScheduleAdapter extends RecyclerView.Adapter<CourseHolder>{
    private Context context;
    private List<Course> courses;


    public ScheduleAdapter(@NonNull Context context, @NonNull List<Course> courses){
        this.context = context;
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
        return new CourseHolder(binding);
    }

    @Override
    public void onBindViewHolder(CourseHolder holder, int position){
        holder.setCourse(courses.get(position));
    }

    public void addCourse(@NonNull Course course){
        courses.add(course);
        notifyItemChanged(courses.size()-2);
        notifyItemInserted(courses.size()-1);
    }
}
