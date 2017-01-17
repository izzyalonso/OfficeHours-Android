package org.tndata.officehours.activity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.tndata.officehours.R;
import org.tndata.officehours.adapter.PeopleAdapter;
import org.tndata.officehours.databinding.ActivityPeopleBinding;
import org.tndata.officehours.model.Course;
import org.tndata.officehours.model.Person;
import org.tndata.officehours.util.CustomItemDecoration;

import java.util.ArrayList;
import java.util.List;


/**
 * Activity used to display a list of people.
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public class PeopleActivity extends AppCompatActivity implements PeopleAdapter.Listener{
    public static final String COURSE_KEY = "org.tndata.officehours.People.Course";


    private static final String TAG = "PeopleActivity";


    private ActivityPeopleBinding binding;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_people);
        Course course = getIntent().getParcelableExtra(COURSE_KEY);
        List<Person> people = new ArrayList<>();
        people.add(course.getInstructor());
        people.addAll(course.getStudents());

        int[] color = new int[5];
        color[0] = Color.CYAN;
        color[1] = Color.RED;
        color[2] = Color.GREEN;
        color[3] = Color.YELLOW;
        color[4] = Color.DKGRAY;

        for (Person person:people){
            person.setColor(color[(int)(Math.random()*color.length)]);
        }

        binding.peopleList.addItemDecoration(new PeopleItemDecoration(this));
        binding.peopleList.setLayoutManager(new LinearLayoutManager(this));
        binding.peopleList.setAdapter(new PeopleAdapter(this, people, this));
    }

    @Override
    public void onPersonSelected(@NonNull Person person){

    }


    private class PeopleItemDecoration extends CustomItemDecoration{
        private Drawable divider;


        /**
         * Constructor.
         *
         * @param context a reference to the context.
         */
        public PeopleItemDecoration(@NonNull Context context){
            super(context, 12);

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
                divider = getResources().getDrawable(R.drawable.divider);
            }
            else{
                divider = getResources().getDrawable(R.drawable.divider, null);
            }
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state){
            int dividerLeft = parent.getPaddingLeft() + getPixels(70);
            int dividerRight = parent.getWidth() - parent.getPaddingRight();

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount - 1; i++) {
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int dividerTop = child.getBottom() + params.bottomMargin + getPixels(6);
                int dividerBottom = dividerTop + 1;

                divider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom);
                divider.draw(c);
            }
        }
    }
}
