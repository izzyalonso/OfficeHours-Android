package org.tndata.officehours.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import org.apache.http.message.BasicNameValuePair;
import org.tndata.officehours.OfficeHoursApp;
import org.tndata.officehours.R;
import org.tndata.officehours.adapter.ChatAdapter;
import org.tndata.officehours.databinding.ActivityChatBinding;
import org.tndata.officehours.model.Course;
import org.tndata.officehours.model.Message;
import org.tndata.officehours.model.Person;
import org.tndata.officehours.model.ResultSet;
import org.tndata.officehours.parser.Parser;
import org.tndata.officehours.util.CustomItemDecoration;
import org.tndata.officehours.util.ImageLoader;
import org.tndata.officehours.util.WebSocketClient;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;


/**
 * Allows a user to chat with one or more peers.
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public class ChatActivity
        extends AppCompatActivity
        implements
                WebSocketClient.Listener,
                View.OnClickListener,
                Parser.ParserCallback{

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


    private ActivityChatBinding binding;
    private WebSocketClient socketClient;

    private Person person;
    private Course course;

    private ChatAdapter adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
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
            adapter = new ChatAdapter(this);

            binding.chatMessages.setLayoutManager(layoutManager);
            binding.chatMessages.addItemDecoration(new CustomItemDecoration(this, 8));
            binding.chatMessages.setAdapter(adapter);

            binding.chatSend.setOnClickListener(this);

            try{
                socketClient = new WebSocketClient(new URI("wss://staging.tndata.org/chat/" + person.getId() + "/"), this, new ArrayList<BasicNameValuePair>());
                socketClient.connect();
            }
            catch (URISyntaxException usx){
                usx.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.chat_send:
                sendMessage();
        }
    }

    private void sendMessage(){
        String newMessage = binding.chatNewMessage.getText().toString().trim();
        if (!newMessage.isEmpty()){
            OfficeHoursApp app = (OfficeHoursApp)getApplication();
            Message message = new Message(app.getUser().getId(), newMessage);
            adapter.addMessage(message);
            binding.chatNewMessage.setText("");
        }
    }

    @Override
    public void onConnect(){
        Log.d(TAG, "onConnect()");
    }

    @Override
    public void onMessage(String message){
        Log.d(TAG, "onMessage()");
        Log.d(TAG, message);
        Parser.parse(message, Message.class, this);
    }

    @Override
    public void onMessage(byte[] data){
        Log.d(TAG, "onMessage()");
    }

    @Override
    public void onDisconnect(int code, String reason){
        Log.d(TAG, "onDisconnect()");
        Log.d(TAG, reason);
    }

    @Override
    public void onError(Exception error){
        Log.d(TAG, "onError()");
        error.printStackTrace();
    }

    @Override
    public void onProcessResult(int requestCode, ResultSet result){

    }

    @Override
    public void onParseSuccess(int requestCode, ResultSet result){
        if (result instanceof Message){
            Message message = (Message)result;
            if (!message.getSender().equals("system")){
                adapter.addMessage(message);
            }
        }
    }

    @Override
    public void onParseFailed(int requestCode){

    }
}
