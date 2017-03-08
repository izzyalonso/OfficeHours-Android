package org.tndata.officehours.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.tndata.officehours.OfficeHoursApp;
import org.tndata.officehours.R;
import org.tndata.officehours.adapter.ScheduleAdapter;
import org.tndata.officehours.databinding.FragmentListBinding;
import org.tndata.officehours.model.Course;
import org.tndata.officehours.util.CustomItemDecoration;


/**
 * Fragment to display a list of courses the user is enrolled in.
 *
 * @author Ismael Alonso
 */
public class ScheduleFragment extends Fragment implements ScheduleAdapter.Listener{
    private ScheduleAdapter.Listener listener;
    private FragmentListBinding binding;
    private ScheduleAdapter adapter;


    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        try{
            listener = (ScheduleAdapter.Listener)context;
        }
        catch (ClassCastException ccx){
            String description = "ScheduleFragment's host activity must implement" +
                    " the ScheduleAdapter.Listener interface.";
            throw new ClassCastException(description);
        }
    }

    @Override
    public void onDetach(){
        listener = null;
        super.onDetach();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        OfficeHoursApp app = (OfficeHoursApp)getContext().getApplicationContext();
        adapter = new ScheduleAdapter(getContext(), app.getCourses(), true, this);
        binding.list.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.list.setAdapter(adapter);
        binding.list.addItemDecoration(new CustomItemDecoration(getContext(), 12));
    }

    @Override
    public void onCourseSelected(@NonNull Course course){
        if (listener != null){
            listener.onCourseSelected(course);
        }
    }
}
