package org.tndata.officehours.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.tndata.officehours.OfficeHoursApp;
import org.tndata.officehours.R;
import org.tndata.officehours.adapter.PeopleAdapter;
import org.tndata.officehours.databinding.FragmentListBinding;
import org.tndata.officehours.model.Person;
import org.tndata.officehours.util.PeopleItemDecoration;

import java.util.ArrayList;


/**
 * Fragment used to display a list of people.
 *
 * @author Ismael Alonso
 */
public class PeopleFragment extends Fragment implements PeopleAdapter.Listener{
    public static final String INSTRUCTORS_KEY = "org.tndata.officehours.PeopleFragment.Instructors";


    private boolean instructors;
    private FragmentListBinding binding;

    private RecyclerView.OnScrollListener currentScrollListener;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            instructors = getArguments().getBoolean(INSTRUCTORS_KEY, false);
        }
        else{
            instructors = false;
        }
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
        ArrayList<Person> people = app.getPeople(instructors);

        binding.list.addItemDecoration(new PeopleItemDecoration(getContext()));
        binding.list.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.list.setAdapter(new PeopleAdapter(getContext(), people, true, this));

        if (currentScrollListener != null){
            binding.list.addOnScrollListener(currentScrollListener);
        }
    }

    @Override
    public void onPersonSelected(@NonNull Person person){

    }

    public void setScrollListener(@NonNull RecyclerView.OnScrollListener listener){
        if (binding == null){
            currentScrollListener = listener;
        }
        else{
            if (currentScrollListener != null){
                binding.list.removeOnScrollListener(currentScrollListener);
            }
            binding.list.addOnScrollListener(listener);
            currentScrollListener = listener;
        }
    }
}
