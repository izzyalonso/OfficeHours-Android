package org.tndata.officehours.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.tndata.officehours.R;
import org.tndata.officehours.databinding.ItemLoadingBinding;
import org.tndata.officehours.databinding.ItemMessageBinding;
import org.tndata.officehours.holder.LoadingHolder;
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
    private static final int TYPE_DATE = 2;
    private static final int TYPE_LOADING = 3;


    private Context context;
    private Listener listener;
    private List<Message> messages;

    private boolean canLoadMore;
    private boolean topReachedSignalSent;


    /**
     * Constructor.
     *
     * @param context a reference to the context.
     * @param listener the listener object.
     * @param messages the list of messages to display.
     */
    public ChatAdapter(@NonNull Context context, @NonNull Listener listener, @NonNull List<Message> messages){
        this.context = context;
        this.listener = listener;
        this.messages = messages;
        canLoadMore = true;
        topReachedSignalSent = false;
    }

    @Override
    public int getItemCount(){
        int count = messages.size();
        if (canLoadMore){
            count++;
        }
        return count;
    }

    @Override
    public int getItemViewType(int position){
        if (position == messages.size()){
            return TYPE_LOADING;
        }
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
        else if (viewType == TYPE_LOADING){
            @LayoutRes int layoutRes = R.layout.item_loading;
            ItemLoadingBinding binding = DataBindingUtil.inflate(inflater, layoutRes, parent, false);
            return new LoadingHolder(binding);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder rawHolder, int position){
        switch (getItemViewType(position)){
            case TYPE_MESSAGE:
                MessageHolder holder = (MessageHolder)rawHolder;
                holder.setMessage(messages.get(messages.size() - position - 1));
                break;

            case TYPE_LOADING:
                if (!topReachedSignalSent && canLoadMore){
                    listener.onTopReached();
                    topReachedSignalSent = true;
                }
                break;
        }
    }

    public void onLoadComplete(boolean canLoadMore){
        topReachedSignalSent = false;
        this.canLoadMore = canLoadMore;
    }


    public interface Listener{
        void onTopReached();
    }
}
