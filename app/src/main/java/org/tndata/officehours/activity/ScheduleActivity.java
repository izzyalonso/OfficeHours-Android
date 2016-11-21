package org.tndata.officehours.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.tndata.officehours.databinding.ActivityScheduleBinding;
import org.tndata.officehours.model.Course;
import org.tndata.officehours.R;
import org.tndata.officehours.adapter.ScheduleAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * Activity that displays a student's schedule
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public class ScheduleActivity extends AppCompatActivity{
    private ActivityScheduleBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_schedule);

        setSupportActionBar(binding.scheduleToolbar.toolbar);

        List<Course> courses = new ArrayList<>();
        courses.add(new Course("COMP1900", "MW 11:00-12:25", "Mr. Someone 3rd"));
        courses.add(new Course("COMP2700", "TR 11:00-12:25", "Mr. Someone Jr"));
        courses.add(new Course("COMP4421", "MWF 11:00-12:00", "Mr. Someone Sr"));

        binding.scheduleList.setLayoutManager(new LinearLayoutManager(this));
        binding.scheduleList.setAdapter(new ScheduleAdapter(this, courses));
        binding.scheduleList.addItemDecoration(new MockItemDecoration(this, 12));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.schedule, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == R.id.schedule_add){
            startActivity(new Intent(this, AddCodeActivity.class));
            return true;
        }
        return false;
    }

    class MockItemDecoration extends RecyclerView.ItemDecoration{
        private int mMargin;


        /**
         * Constructor.
         *
         * @param context a reference to the context.
         * @param marginInDp the maximum spacing between items, items, and edges.
         */
        public MockItemDecoration(Context context, int marginInDp){
            mMargin = getPixels(context, marginInDp);
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state){
            outRect.top = mMargin/2;
            outRect.left = mMargin;
            outRect.bottom = mMargin/2;
            outRect.right = mMargin;
            if (parent.getChildAdapterPosition(view) == 0){
                outRect.top = mMargin;
            }
            else if (parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount()-1){
                outRect.bottom = mMargin;
            }
        }

        public int getPixels(Context context, int densityPixels){
            return (int)Math.ceil(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, densityPixels,
                    context.getResources().getDisplayMetrics()));
        }
    }
}
