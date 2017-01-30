package org.tndata.officehours.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.tndata.officehours.R;
import org.tndata.officehours.databinding.ItemMessageBinding;
import org.tndata.officehours.holder.MessageHolder;
import org.tndata.officehours.model.Message;

import java.util.List;


/**
 * Adapter for the list of chat messages in ChatActivity.
 *
 * @author Ismael Alonso
 */
public class ChatAdapter extends RecyclerView.Adapter{
    private static final int TYPE_MESSAGE = 1;


    private Context context;
    private RecyclerView recyclerView;
    private List<Message> messages;


    /**
     * Constructor.
     *
     * @param context a reference to the context.
     * @param messages the list of messages to display.
     */
    public ChatAdapter(@NonNull Context context, @NonNull List<Message> messages){
        this.context = context;
        this.messages = messages;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        this.recyclerView = recyclerView;
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView){
        if (this.recyclerView == recyclerView){
            this.recyclerView = null;
        }
    }

    @Override
    public int getItemViewType(int position){
        return TYPE_MESSAGE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == TYPE_MESSAGE){
            @LayoutRes int layoutRes = R.layout.item_message;
            ItemMessageBinding binding = DataBindingUtil.inflate(inflater, layoutRes, parent, false);
            return new MessageHolder(binding);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder rawHolder, int position){
        if (getItemViewType(position) == TYPE_MESSAGE){
            MessageHolder holder = (MessageHolder)rawHolder;
            holder.setMessage(messages.get(getItemCount() - position - 1));
        }
    }

    @Override
    public int getItemCount(){
        return messages.size();
    }
}
