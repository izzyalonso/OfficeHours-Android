package org.tndata.officehours.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.RecyclerView;

import org.tndata.officehours.OfficeHoursApp;
import org.tndata.officehours.fragment.PeopleFragment;
import org.tndata.officehours.fragment.ScheduleFragment;
import org.tndata.officehours.model.Course;


/**
 * Provides fragments for the ViewPager in MainActivity.
 *
 * @author Ismael Alonso
 */
public class MainPagerAdapter extends FragmentPagerAdapter{
    private static final int PAGE_COUNT = 4;


    private final OfficeHoursApp app;
    private final Listener listener;
    private ScrollListener scrollListener;

    private PeopleFragment helpersFragment;
    private PeopleFragment peersFragment;
    private ScheduleFragment scheduleFragment;
    private PeopleFragment forumFragment;

    /**
     * Constructor.
     *
     * @param fm the host activity's FragmentManager.
     */
    public MainPagerAdapter(@NonNull FragmentManager fm, @NonNull Context context, @NonNull Listener listener){
        super(fm);
        this.app = (OfficeHoursApp)context.getApplicationContext();
        this.listener = listener;
        scrollListener = new ScrollListener();
    }

    @Override
    public int getCount(){
        return PAGE_COUNT - (app.getUser().isStudent() ? 0 : 1);
    }

    @Override
    public Fragment getItem(int position){
        if (!app.getUser().isStudent()){
            position++;
        }
        if (position == 0){
            if (helpersFragment == null){
                Bundle args = new Bundle();
                args.putBoolean(PeopleFragment.INSTRUCTORS_KEY, true);
                helpersFragment = new PeopleFragment();
                helpersFragment.setArguments(args);

            }
            helpersFragment.setScrollListener(scrollListener);
            return helpersFragment;
        }
        if (position == 1){
            if (peersFragment == null){
                Bundle args = new Bundle();
                args.putBoolean(PeopleFragment.INSTRUCTORS_KEY, false);
                peersFragment = new PeopleFragment();
                peersFragment.setArguments(args);
            }
            peersFragment.setScrollListener(scrollListener);
            return peersFragment;
        }
        if (position == 2){
            if (scheduleFragment == null){
                scheduleFragment = new ScheduleFragment();
            }
            return scheduleFragment;
        }
        if (position == 3){
            if (forumFragment == null){
                forumFragment = new PeopleFragment();
            }
            forumFragment.setScrollListener(scrollListener);
            return forumFragment;
        }
        return new PeopleFragment();
    }

    @Override
    public CharSequence getPageTitle(int position){
        if (!app.getUser().isStudent()){
            position++;
        }

        if (position == 0){
            return "Helpers";
        }
        if (position == 1){
            return app.getUser().isStudent() ? "Peers" : "Students";
        }
        if (position == 2){
            return "Groups";
        }
        if (position == 3){
            return "Forum";
        }
        return "";
    }

    public boolean shouldDisplayFooter(){
        return false;
    }

    public void onCourseAdded(@NonNull Course course){

    }


    private class ScrollListener extends RecyclerView.OnScrollListener{
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy){
            listener.onContentScrolled(dx, dy);
        }
    }


    public interface Listener{
        void onContentScrolled(int dx, int dy);
    }
}
