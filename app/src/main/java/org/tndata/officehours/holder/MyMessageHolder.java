package org.tndata.officehours.holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import org.tndata.officehours.databinding.ItemMyMessageBinding;
import org.tndata.officehours.model.Message;


/**
 * Holder for a message sent by the current user.
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public class MyMessageHolder extends RecyclerView.ViewHolder{
    private final ItemMyMessageBinding binding;


    /**
     * Constructor.
     *
     * @param binding the binding object for R.layout.item_my_message
     */
    public MyMessageHolder(@NonNull ItemMyMessageBinding binding){
        super(binding.getRoot());
        this.binding = binding;
    }

    /**
     * Displays a message in the view contained by this holder.
     *
     * @param message the message to be displayed.
     */
    public void setMessage(@NonNull Message message){
        binding.myMessageText.setText(message.getText());
        binding.myMessageTime.setText("1:24 PM");
    }
}
