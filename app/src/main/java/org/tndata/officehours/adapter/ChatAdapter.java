package org.tndata.officehours.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.tndata.officehours.OfficeHoursApp;
import org.tndata.officehours.R;
import org.tndata.officehours.databinding.ItemContactMessageBinding;
import org.tndata.officehours.databinding.ItemMyMessageBinding;
import org.tndata.officehours.holder.ContactMessageHolder;
import org.tndata.officehours.holder.MyMessageHolder;
import org.tndata.officehours.model.Message;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ialonso on 1/23/17.
 */
public class ChatAdapter extends RecyclerView.Adapter{

    private static final int TYPE_MY_MESSAGE = 1;
    private static final int TYPE_CONTACT_MESSAGE = 2;


    private Context context;
    private RecyclerView recyclerView;
    private List<Message> messages;

    private long currentUserId;


    public ChatAdapter(@NonNull Context context){
        this.context = context;
        messages = new ArrayList<>();
        currentUserId = ((OfficeHoursApp)context.getApplicationContext()).getUser().getId();
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
        if (messages.get(position).getSenderId() == currentUserId){
            return TYPE_MY_MESSAGE;
        }
        else{
            return TYPE_CONTACT_MESSAGE;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == TYPE_MY_MESSAGE){
            @LayoutRes int layoutRes = R.layout.item_my_message;
            ItemMyMessageBinding binding = DataBindingUtil.inflate(inflater, layoutRes, parent, false);
            return new MyMessageHolder(binding);
        }
        else{
            @LayoutRes int layoutRes = R.layout.item_contact_message;
            ItemContactMessageBinding binding = DataBindingUtil.inflate(inflater, layoutRes, parent, false);
            return new ContactMessageHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder rawHolder, int position){
        if (getItemViewType(position) == TYPE_MY_MESSAGE){
            MyMessageHolder holder = (MyMessageHolder)rawHolder;
            holder.setMessage(messages.get(getItemCount() - position - 1));
        }
        else{
            ContactMessageHolder holder = (ContactMessageHolder)rawHolder;
            holder.setMessage(messages.get(getItemCount() - position - 1));
        }
    }

    @Override
    public int getItemCount(){
        return messages.size();
    }

    public void addMessage(Message message){
        messages.add(message);
        notifyDataSetChanged();
        if (recyclerView != null){
            recyclerView.scrollToPosition(0);
        }
    }
}
