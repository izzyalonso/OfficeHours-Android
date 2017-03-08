package org.tndata.officehours.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import org.tndata.officehours.OfficeHoursApp;
import org.tndata.officehours.R;
import org.tndata.officehours.adapter.MainPagerAdapter;
import org.tndata.officehours.adapter.ScheduleAdapter;
import org.tndata.officehours.databinding.ActivityMainBinding;
import org.tndata.officehours.model.Course;


/**
 * Activity used to display the main screen of the app.
 *
 * @author Ismael Alonso
 */
public class MainActivity
        extends AppCompatActivity
        implements
                ViewPager.OnPageChangeListener,
                MainPagerAdapter.Listener,
                TextWatcher,
                View.OnClickListener,
                ScheduleAdapter.Listener{

    private static final int ADD_CODE_RC = 7529;
    private static final int NEW_COURSE_RC = 6392;


    private ActivityMainBinding binding;
    private OfficeHoursApp app;

    private float boxMinimumWidth;
    private float boxVariableWidth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        app = (OfficeHoursApp)getApplication();

        setSupportActionBar(binding.mainToolbar.toolbar);
        binding.mainPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(), this, this));
        binding.mainPager.addOnPageChangeListener(this);
        binding.mainPager.setOffscreenPageLimit(5);

        //Initial width calculations
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        float margin = getResources().getDimension(R.dimen.people_footer_margin)*2;
        float componentMaxWidth = screenWidth - margin;
        boxMinimumWidth = getResources().getDimension(R.dimen.people_footer_height);
        boxVariableWidth = componentMaxWidth-boxMinimumWidth;

        binding.mainTextBox.addTextChangedListener(this);
        binding.mainFab.setOnClickListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels){
        if (!app.getUser().isStudent()){
            position++;
        }

        if (position == 1){
            setBottomBoxWidth(1f-positionOffset);
            if (positionOffsetPixels == 0 && !binding.mainTextBox.getText().toString().trim().isEmpty()){
                binding.mainFab.setImageResource(R.drawable.ic_send_white_24dp);
            }
        }
        else if (position == 2){
            setBottomBoxWidth(positionOffset);
            if (positionOffsetPixels == 0){
                binding.mainFab.setImageResource(R.drawable.ic_add_white_24dp);
            }
        }
        else if (position == 3){
            setBottomBoxWidth(1);
            if (!binding.mainTextBox.getText().toString().trim().isEmpty()){
                binding.mainFab.setImageResource(R.drawable.ic_send_white_24dp);
            }
        }
    }

    @Override
    public void onPageSelected(int position){
        //Unused
    }

    @Override
    public void onPageScrollStateChanged(int state){
        //Unused
    }

    /**
     * Updates the size of the bottom box.
     *
     * @param size a fraction of the total width. 1 = match parent, 0 = min size.
     */
    public void setBottomBoxWidth(float size){
        if (size >= 1.0){
            binding.mainFooterBox.getLayoutParams().width = ViewPager.LayoutParams.MATCH_PARENT;
            binding.mainTextBox.setAlpha(1);
        }
        else{
            if (size < 0){
                size = 0;
            }
            float componentWidth = boxMinimumWidth + boxVariableWidth*size;
            binding.mainFooterBox.getLayoutParams().width = (int)componentWidth;

            float alphaState = (size-0.5f)*2;
            if (alphaState < 0){
                alphaState = 0;
            }
            binding.mainTextBox.setAlpha(alphaState);
        }
        binding.mainFooterBox.requestLayout();
    }

    @Override
    public void onContentScrolled(int dx, int dy){
        /*Log.d("MainActivity", "dy: " + dy);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)binding.mainFooter.getLayoutParams();
        if (dy > 0 && !hidingFooter){
            params.topMargin += dy;
            binding.mainFooter.setLayoutParams(params);
            binding.mainFooter.requestLayout();
        }
        if (dy < 0 && !showingFooter){

        }*/
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after){

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count){
        if (count == 0){
            binding.mainFab.setImageResource(R.drawable.ic_add_white_24dp);
        }
        else{
            binding.mainFab.setImageResource(R.drawable.ic_send_white_24dp);
        }
    }

    @Override
    public void afterTextChanged(Editable s){

    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.main_fab:
                String text = binding.mainTextBox.getText().toString().trim();
                if (text.isEmpty()){
                    if (app.getUser().isStudent()){
                        startActivityForResult(new Intent(this, AddCodeActivity.class), ADD_CODE_RC);
                    }
                    else{
                        startActivityForResult(new Intent(this, CourseEditorActivity.class), NEW_COURSE_RC);
                    }
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == ADD_CODE_RC){
            if (resultCode == RESULT_OK){
                Course course = data.getParcelableExtra(AddCodeActivity.COURSE_KEY);
                /*adapter.notifyItemInserted(app.getCourses().size()-1);
                if (app.getCourses().size() != 1){
                    adapter.notifyItemChanged(app.getCourses().size()-2);
                }
                onCourseSelected(course);*/
            }
        }
        else if (requestCode == NEW_COURSE_RC){
            if (resultCode == RESULT_OK){
                /*Course course = data.getParcelableExtra(CourseEditorActivity.COURSE_KEY);
                adapter.addCourse(course);*/
            }
        }
        else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onCourseSelected(@NonNull Course course){
        startActivity(CourseActivity.getIntent(this, course));
    }
}
