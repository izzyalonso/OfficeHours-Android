package org.tndata.officehours.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import org.tndata.officehours.OfficeHoursApp;
import org.tndata.officehours.R;
import org.tndata.officehours.adapter.ChatAdapter;
import org.tndata.officehours.database.MessageTableHandler;
import org.tndata.officehours.database.PersonTableHandler;
import org.tndata.officehours.databinding.ActivityChatBinding;
import org.tndata.officehours.model.Course;
import org.tndata.officehours.model.Message;
import org.tndata.officehours.model.Person;
import org.tndata.officehours.model.ResultSet;
import org.tndata.officehours.parser.Parser;
import org.tndata.officehours.parser.ParserModels;
import org.tndata.officehours.util.API;
import org.tndata.officehours.util.CustomItemDecoration;
import org.tndata.officehours.util.ImageLoader;
import org.tndata.officehours.util.MessageDispatcher;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import es.sandwatch.httprequests.HttpRequest;
import es.sandwatch.httprequests.HttpRequestError;


/**
 * Allows a user to chat with one or more peers.
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public class ChatActivity
        extends AppCompatActivity
        implements
                HttpRequest.RequestCallback,
                Parser.ParserCallback,
                ChatAdapter.Listener,
                View.OnClickListener,
                MessageDispatcher.Listener{

    private static final String TAG = "ChatActivity";

    private static final String PERSON_KEY = "org.tndata.officehours.ChatActivity.Person";
    private static final String COURSE_KEY = "org.tndata.officehours.ChatActivity.Course";


    /**
     * Gets the intent that launches a chat with one person.
     *
     * @param context a reference to the context.
     * @param person the person to start the chat session with.
     * @return the intent that launches such activity.
     */
    public static Intent getIntent(@NonNull Context context, @NonNull Person person){
        return new Intent(context, ChatActivity.class).putExtra(PERSON_KEY, person);
    }

    /**
     * Gets the intent that launches a chat with all the people in a course.
     *
     * @param context a reference to the context.
     * @param course the course to start the chat session with.
     * @return the intent that launches such activity.
     */
    public static Intent getIntent(@NonNull Context context, @NonNull Course course){
        return new Intent(context, ChatActivity.class).putExtra(COURSE_KEY, course);
    }


    private OfficeHoursApp app;

    private Person person;
    //private Course course;

    private ChatAdapter adapter;
    private List<Message> messages;

    private ActivityChatBinding binding;
    private MessageDispatcher dispatcher;

    private int getMessageHistoryRC;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        app = (OfficeHoursApp)getApplication();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat);

        setSupportActionBar(binding.chatToolbar);

        person = getIntent().getParcelableExtra(PERSON_KEY);
        if (person != null){
            Drawable drawable = binding.chatAvatarContainer.getBackground();
            GradientDrawable gradientDrawable = (GradientDrawable)drawable;
            gradientDrawable.setColor(person.getColor());
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN){
                binding.chatAvatarContainer.setBackgroundDrawable(gradientDrawable);
            }
            else{
                binding.chatAvatarContainer.setBackground(gradientDrawable);
            }

            if (!person.getAvatar().isEmpty()){
                ImageLoader.Options options = new ImageLoader.Options().setCropToCircle(true);
                ImageLoader.loadBitmap(binding.chatAvatar, person.getAvatar(), options);
                binding.chatInitials.setVisibility(View.GONE);
                binding.chatAvatar.setVisibility(View.VISIBLE);
            }
            else{
                String initials = "";
                String[] names = person.getName().split(" ");
                for (String name:names){
                    initials += name.charAt(0);
                }
                binding.chatInitials.setText(initials);
                binding.chatInitials.setVisibility(View.VISIBLE);
                binding.chatAvatar.setVisibility((View.GONE));
            }

            AssetManager assetManager = binding.getRoot().getContext().getAssets();
            String font = "fonts/Roboto-Medium.ttf";
            binding.chatName.setTypeface(Typeface.createFromAsset(assetManager, font));
            binding.chatName.setText(person.getName());

            //Create the layout manager and the adapter
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setReverseLayout(true);
            messages = new ArrayList<>();
            adapter = new ChatAdapter(this, this, messages);

            binding.chatMessages.setLayoutManager(layoutManager);
            binding.chatMessages.addItemDecoration(new CustomItemDecoration(this, 8));
            binding.chatMessages.setAdapter(adapter);

            binding.chatSend.setOnClickListener(this);

            dispatcher = new MessageDispatcher(this, person, this);

            fetchMessagesBefore(System.currentTimeMillis());
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        dispatcher.connect();
    }

    @Override
    protected void onStop(){
        dispatcher.disconnect();
        super.onStop();
    }

    private void fetchMessagesBefore(long timestamp){
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:sZ", Locale.getDefault());
        String date = formatter.format(new Date(timestamp));
        String url = API.URL.chatHistoryBefore(app.getUser().getId(), person.getId(), date);
        getMessageHistoryRC = HttpRequest.get(this, url);
    }

    @Override
    public void onRequestComplete(int requestCode, String result){
        if (requestCode == getMessageHistoryRC){
            Parser.parse(result, ParserModels.MessageList.class, this);
        }
    }

    @Override
    public void onRequestFailed(int requestCode, HttpRequestError error){

    }

    @Override
    public void onProcessResult(int requestCode, ResultSet result){
        for (Message message:((ParserModels.MessageList)result).results){
            message.process();
            message.sent(false);
            this.messages.add(0, message);
        }
    }

    @Override
    public void onParseSuccess(int requestCode, ResultSet result){
        List<Message> messages = ((ParserModels.MessageList)result).results;
        adapter.notifyDataSetChanged();
        adapter.onLoadComplete(!messages.isEmpty());
        binding.chatMessages.setVisibility(View.VISIBLE);
        binding.chatNewMessage.setEnabled(true);
        binding.chatLoading.setVisibility(View.GONE);
    }

    @Override
    public void onParseFailed(int requestCode){

    }

    @Override
    public void onTopReached(){
        fetchMessagesBefore(messages.get(0).getTimestamp());
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.chat_send:
                String text = binding.chatNewMessage.getText().toString().trim();
                if (!text.isEmpty()){
                    Message message = new Message(app.getUser().getId(), text);
                    messages.add(message);
                    adapter.notifyDataSetChanged();
                    binding.chatMessages.scrollToPosition(0);
                    dispatcher.queue(message);
                    binding.chatNewMessage.setText("");
                    person.setLastMessage(text);
                    new PersonUpdater().execute();
                }
                break;
        }
    }

    @Override
    public void onMessageSent(@NonNull Message message){
        Log.d(TAG, message.toString());
        adapter.notifyItemChanged(messages.indexOf(message));
    }

    @Override
    public void onMessageReceived(@NonNull Message message){
        Log.d(TAG, message.toString());
        if (message.getSenderId() != -1){
            messages.add(message);
            adapter.notifyDataSetChanged();
            person.setLastMessage(message.getText());
            new PersonUpdater().execute();
        }
    }

    private class PersonUpdater extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... params){
            PersonTableHandler handler = new PersonTableHandler(ChatActivity.this);
            handler.updatePerson(person);
            handler.close();
            return null;
        }
    }

    private class MessageFetcher extends AsyncTask<Void, Void, Boolean>{
        private long timestamp;


        private MessageFetcher(long timestamp){
            this.timestamp = timestamp;
        }

        @Override
        protected Boolean doInBackground(Void... params){
            /*MessageTableHandler handler = new MessageTableHandler(ChatActivity.this);
            List<Message> messages = handler.getMessages(person, timestamp);
            handler.close();
            Log.d(TAG, messages.size() + " read from the database.");
            for (Message message:messages){
                ChatActivity.this.messages.add(0, message);
            }
            return !messages.isEmpty();*/
            return false;
        }

        @Override
        protected void onPostExecute(Boolean results){
            if (results){
                adapter.onLoadComplete(true);
                adapter.notifyDataSetChanged();
            }
            else{
                //If there are no more messages in the local db start fetching from the backend
                //fetchingLocally = false;
                fetchMessagesBefore(timestamp);
            }
        }
    }
}
