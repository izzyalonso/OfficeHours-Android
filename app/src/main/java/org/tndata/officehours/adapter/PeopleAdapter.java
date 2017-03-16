package org.tndata.officehours.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
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
        extends RecyclerView.Adapter
        implements PersonHolder.Listener{

    private static final int TYPE_PERSON = 1;
    private static final int TYPE_FOOTER_SPACE = 2;


    private Context context;
    private List<Person> people;
    private boolean includeFooterSpace;
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

        this(context, people, false, listener);
    }

    /**
     * Constructor.
     *
     * @param context a reference to the context.
     * @param people the list of people to display.
     * @param listener the listener object.
     */
    public PeopleAdapter(@NonNull Context context, @NonNull List<Person> people,
                         boolean includeFooterSpace, @NonNull Listener listener){

        this.context = context;
        this.people = people;
        this.includeFooterSpace = includeFooterSpace;
        this.listener = listener;
    }

    @Override
    public int getItemCount(){
        return people.size() + (includeFooterSpace ? 1 : 0);
    }

    @Override
    public int getItemViewType(int position){
        if (includeFooterSpace && position == people.size()){
            return TYPE_FOOTER_SPACE;
        }
        return TYPE_PERSON;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == TYPE_PERSON){
            int res = R.layout.item_person;
            ItemPersonBinding binding = DataBindingUtil.inflate(inflater, res, parent, false);
            return new PersonHolder(binding, this);
        }
        else if (viewType == TYPE_FOOTER_SPACE){
            View view = inflater.inflate(R.layout.item_people_footer_space, parent, false);
            return new RecyclerView.ViewHolder(view){};
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder rawHolder, int position){
        if (getItemViewType(position) == TYPE_PERSON){
            PersonHolder holder = (PersonHolder)rawHolder;
            holder.setPerson(people.get(position));
        }
    }

    @Override
    public void onPersonHolderTapped(int position){
        listener.onPersonSelected(people.get(position));
    }

    public boolean includesFooterSpace(){
        return includeFooterSpace;
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
