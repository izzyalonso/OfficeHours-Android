package org.tndata.officehours.holder;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import org.tndata.officehours.databinding.ItemPersonBinding;
import org.tndata.officehours.model.Person;


/**
 * Holder to display a Person.
 *
 * @author Ismael Alonso
 */
public class PersonHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    private ItemPersonBinding binding;
    private Listener listener;


    /**
     * Constructor.
     *
     * @param binding the binding object.
     */
    public PersonHolder(@NonNull ItemPersonBinding binding, @NonNull Listener listener){
        super(binding.getRoot());
        this.binding = binding;
        this.listener = listener;

        itemView.setOnClickListener(this);
    }

    /**
     * Populates the holder with a person.
     *
     * @param person the person to use to populate the holder.
     */
    public void setPerson(@NonNull Person person){
        binding.personAvatar.setPerson(person);

        AssetManager assetManager = binding.getRoot().getContext().getAssets();
        String font = "fonts/Roboto-Medium.ttf";
        binding.personName.setTypeface(Typeface.createFromAsset(assetManager, font));
        binding.personName.setText(person.getName());
        Log.d("PersonHolder", "Last message: " + person.getLastMessage());
        binding.personLastMessage.setText(person.getLastMessage());
    }

    @Override
    public void onClick(View view){
        listener.onPersonHolderTapped(getAdapterPosition());
    }


    /**
     * Listener interface for the adapter.
     *
     * @author Ismael Alonso
     */
    public interface Listener{
        /**
         * Called when the holder is tapped.
         *
         * @param position the current position of the holder within the adapter.
         */
        void onPersonHolderTapped(int position);
    }
}
