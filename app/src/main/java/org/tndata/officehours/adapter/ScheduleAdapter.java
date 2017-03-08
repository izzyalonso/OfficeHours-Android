package org.tndata.officehours.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.tndata.officehours.model.Course;
import org.tndata.officehours.R;
import org.tndata.officehours.databinding.CardCourseBinding;
import org.tndata.officehours.holder.CourseHolder;

import java.util.List;


/**
 * Adapter used in ScheduleActivity to display the student's schedule_instructor
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public class ScheduleAdapter
        extends RecyclerView.Adapter
        implements CourseHolder.Listener{

    private static final int TYPE_COURSE = 1;
    private static final int TYPE_FOOTER_SPACE = 2;


    private Context context;
    private List<Course> courses;
    private boolean includeFooterSpace;
    private Listener listener;


    /**
     * Constructor.
     *
     * @param context a reference to the context.
     * @param listener the listener object.
     * @param courses the list of courses to be initially displayed.
     */
    public ScheduleAdapter( @NonNull Context context, @NonNull List<Course> courses,
                            @NonNull Listener listener){
        this(context, courses, false, listener);
    }

    /**
     * Constructor.
     *
     * @param context a reference to the context.
     * @param listener the listener object.
     * @param courses the list of courses to be initially displayed.
     */
    public ScheduleAdapter(@NonNull Context context, @NonNull List<Course> courses,
                           boolean includeFooterSpace, @NonNull Listener listener){

        this.context = context;
        this.listener = listener;
        this.includeFooterSpace = includeFooterSpace;
        this.courses = courses;
    }

    @Override
    public int getItemCount(){
        return courses.size() + (includeFooterSpace ? 1 : 0);
    }

    @Override
    public int getItemViewType(int position){
        if (includeFooterSpace && position == courses.size()){
            return TYPE_FOOTER_SPACE;
        }
        return TYPE_COURSE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == TYPE_COURSE){
            CardCourseBinding binding;
            binding = DataBindingUtil.inflate(inflater, R.layout.card_course, parent, false);
            return new CourseHolder(binding, this);
        }
        else if (viewType == TYPE_FOOTER_SPACE){
            View view = inflater.inflate(R.layout.item_people_footer_space, parent, false);
            return new RecyclerView.ViewHolder(view){};
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder rawHolder, int position){
        if (getItemViewType(position) == TYPE_COURSE){
            CourseHolder holder = (CourseHolder)rawHolder;
            holder.setCourse(courses.get(position));
        }
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
