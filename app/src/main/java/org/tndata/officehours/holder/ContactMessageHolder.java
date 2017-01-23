package org.tndata.officehours.holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import org.tndata.officehours.databinding.ItemContactMessageBinding;
import org.tndata.officehours.model.Message;


/**
 * Holder for a message sent by a contact of the current user.
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public class ContactMessageHolder extends RecyclerView.ViewHolder{
    private final ItemContactMessageBinding binding;

    /**
     * Constructor.
     *
     * @param binding the binding object for R.layout.item_contact_message
     */
    public ContactMessageHolder(@NonNull ItemContactMessageBinding binding){
        super(binding.getRoot());
        this.binding = binding;
    }


    /**
     * Displays a message in the view contained by this holder.
     *
     * @param message the message to be displayed.
     */
    public void setMessage(@NonNull Message message){
        binding.contactMessageText.setText(message.getText());
        binding.contactMessageTime.setText("1:24 PM");
    }
}
