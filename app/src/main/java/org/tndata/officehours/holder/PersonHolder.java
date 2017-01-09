package org.tndata.officehours.holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.tndata.officehours.databinding.ItemPersonBinding;
import org.tndata.officehours.model.Person;
import org.tndata.officehours.util.ImageLoader;


/**
 * Holder to display a Person.
 *
 * @author Ismael Alonso
 * @version 1.0.0
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
        if (!person.getPhotoUrl().isEmpty()){
            ImageLoader.Options options = new ImageLoader.Options().setCropToCircle(true);
            ImageLoader.loadBitmap(binding.personPhoto, person.getPhotoUrl(), options);
        }
        binding.personName.setText(person.getName());
    }

    @Override
    public void onClick(View view){
        listener.onPersonHolderTapped(getAdapterPosition());
    }


    /**
     * Listener interface for the adapter.
     *
     * @author Ismael Alonso
     * @version 1.0.0
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
