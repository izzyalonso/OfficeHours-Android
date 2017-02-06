package org.tndata.officehours.activity;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import org.tndata.officehours.R;
import org.tndata.officehours.adapter.PeopleAdapter;
import org.tndata.officehours.databinding.ActivityPeopleBinding;
import org.tndata.officehours.model.Person;
import org.tndata.officehours.util.CustomItemDecoration;
import org.tndata.officehours.util.Util;

import java.util.ArrayList;
import java.util.List;


/**
 * Activity used to display a list of people.
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public class PeopleActivity extends AppCompatActivity implements PeopleAdapter.Listener{
    private static final String NAME_KEY = "org.tndata.officehours.People.Name";
    private static final String PEOPLE_KEY = "org.tndata.officehours.People.People";


    private static final String TAG = "PeopleActivity";


    /**
     * Gets the intent to properly launch the activity.
     *
     * @param context a reference to the context.
     * @param name the name of the activity.
     * @param people the list of people to display.
     * @return the intent to lunch the activity.
     */
    public static Intent getIntent(@NonNull Context context, @NonNull String name,
                                   @NonNull ArrayList<Person> people){

        return new Intent(context, PeopleActivity.class)
                .putExtra(NAME_KEY, name)
                .putParcelableArrayListExtra(PEOPLE_KEY, people);
    }


    private ActivityPeopleBinding binding;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_people);

        setTitle(getIntent().getExtras().getString(NAME_KEY, ""));
        setSupportActionBar(binding.peopleToolbar.toolbar);

        List<Person> people = getIntent().getParcelableArrayListExtra(PEOPLE_KEY);

        binding.peopleList.addItemDecoration(new PeopleItemDecoration(this));
        binding.peopleList.setLayoutManager(new LinearLayoutManager(this));
        binding.peopleList.setAdapter(new PeopleAdapter(this, people, this));
    }

    @Override
    public void onPersonSelected(@NonNull Person person){
        if (Util.isNetworkAvailable(this)){
            startActivity(ChatActivity.getIntent(this, person));
        }
        else{
            Toast.makeText(this, R.string.chat_error_offline, Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * ItemDecoration class that adds dividers to the functionality already provided by the parent.
     *
     * @author Ismael Alonso
     * @version 1.0.0
     */
    private class PeopleItemDecoration extends CustomItemDecoration{
        private Drawable divider;


        /**
         * Constructor.
         *
         * @param context a reference to the context.
         */
        @SuppressWarnings("deprecation")
        private PeopleItemDecoration(@NonNull Context context){
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
