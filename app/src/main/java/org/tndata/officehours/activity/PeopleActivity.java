package org.tndata.officehours.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import org.tndata.officehours.R;
import org.tndata.officehours.adapter.PeopleAdapter;
import org.tndata.officehours.databinding.ActivityPeopleBinding;
import org.tndata.officehours.model.Person;

import java.util.List;


/**
 * Activity used to display a list of people.
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public class PeopleActivity extends AppCompatActivity implements PeopleAdapter.Listener{
    public static final String PEOPLE_KEY = "org.tndata.officehours.People.People";


    private static final String TAG = "PeopleActivity";


    private ActivityPeopleBinding binding;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_people);
        List<Person> people = getIntent().getParcelableArrayListExtra(PEOPLE_KEY);

        binding.peopleList.setLayoutManager(new LinearLayoutManager(this));
        binding.peopleList.setAdapter(new PeopleAdapter(this, people, this));
    }

    @Override
    public void onPersonSelected(@NonNull Person person){

    }
}
