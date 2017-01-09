package org.tndata.officehours.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.tndata.officehours.R;
import org.tndata.officehours.databinding.ItemPersonBinding;
import org.tndata.officehours.holder.PersonHolder;
import org.tndata.officehours.model.Person;

import java.util.List;


/**
 * Adapter to display a list of people.
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public class PeopleAdapter
        extends RecyclerView.Adapter<PersonHolder>
        implements PersonHolder.Listener{

    private Context context;
    private List<Person> people;
    private Listener listener;


    /**
     * Constructor.
     *
     * @param context a reference to the context.
     * @param people the list of people to display.
     * @param listener the listener object.
     */
    public PeopleAdapter(@NonNull Context context, @NonNull List<Person> people,
                         @NonNull Listener listener){

        this.context = context;
        this.people = people;
        this.listener = listener;
    }

    @Override
    public PersonHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(context);
        int resource = R.layout.item_person;
        ItemPersonBinding binding = DataBindingUtil.inflate(inflater, resource, parent, false);
        return new PersonHolder(binding, this);
    }

    @Override
    public void onBindViewHolder(PersonHolder holder, int position){
        holder.setPerson(people.get(position));
    }

    @Override
    public int getItemCount(){
        return people.size();
    }

    @Override
    public void onPersonHolderTapped(int position){
        listener.onPersonSelected(people.get(position));
    }


    /**
     * Listener for PeopleAdapter.
     *
     * @author Ismael Alonso
     * @version 1.0.0
     */
    public interface Listener{
        /**
         * Called when a person is selected.
         *
         * @param person the selected person.
         */
        void onPersonSelected(@NonNull Person person);
    }
}
