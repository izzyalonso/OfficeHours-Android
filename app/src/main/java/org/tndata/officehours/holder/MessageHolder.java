package org.tndata.officehours.holder;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.tndata.officehours.OfficeHoursApp;
import org.tndata.officehours.R;
import org.tndata.officehours.databinding.ItemMessageBinding;
import org.tndata.officehours.model.Message;
import org.tndata.officehours.util.Util;


/**
 * Holder for a message.
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public class MessageHolder extends RecyclerView.ViewHolder{
    private final ItemMessageBinding binding;

    private Rect textBounds;
    private Rect timestampBounds;


    /**
     * Constructor.
     *
     * @param binding the binding object for R.layout.item_message
     */
    public MessageHolder(@NonNull ItemMessageBinding binding){
        super(binding.getRoot());
        this.binding = binding;
    }

    /**
     * Displays a message in the view contained by this holder.
     *
     * @param message the message to be displayed.
     */
    public void setMessage(@NonNull Message message){
        textBounds = null;
        timestampBounds = null;

        ViewTreeObserver vto = binding.messageText.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(){
            @Override
            public void onGlobalLayout(){
                TextView view = binding.messageText;
                int lineCount = view.getLineCount();
                if (lineCount != 0){
                    textBounds = new Rect();
                    view.getLineBounds(lineCount-1, textBounds);
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    setViews();
                }
            }
        });
        vto = binding.messageTime.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(){
            @Override
            public void onGlobalLayout(){
                TextView view = binding.messageTime;
                int lineCount = view.getLineCount();
                if (lineCount != 0){
                    timestampBounds = new Rect();
                    view.getLineBounds(lineCount-1, timestampBounds);
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    setViews();
                }
            }
        });

        OfficeHoursApp app = (OfficeHoursApp)binding.getRoot().getContext().getApplicationContext();
        int padding = Util.getPixels(binding.getRoot().getContext(), 16);
        FrameLayout.LayoutParams params;
        params = (FrameLayout.LayoutParams)binding.messageBubble.getLayoutParams();
        if (message.getSenderId() == app.getUser().getId()){
            binding.messageFrame.setPadding(padding, 0, 0, 0);
            params.gravity = Gravity.END;
            binding.messageState.setVisibility(View.VISIBLE);
            if (message.isSent()){
                if (message.isRead()){
                    binding.messageState.setImageResource(R.drawable.ic_double_check_white_18dp);
                }
                else{
                    binding.messageState.setImageResource(R.drawable.ic_check_white_18dp);
                }
            }
        }
        else{
            binding.messageFrame.setPadding(0, 0, padding, 0);
            params.gravity = Gravity.START;
            binding.messageContainer.setGravity(GravityCompat.START);
            binding.messageState.setVisibility(View.GONE);
        }

        binding.messageText.setText(message.getText());
        binding.messageTime.setText("1:24 PM");
    }

    private void setViews(){
        if (textBounds != null && timestampBounds != null){
            Log.d("Holder", "Text" + textBounds);
            Log.d("Holder", "Time" + timestampBounds);

            binding.messageContainer.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)binding.messageText.getLayoutParams();
            if (textBounds.intersect(timestampBounds)){
                Log.d("Holder", binding.messageText.getText().toString() + ", views intersect");

                if (binding.messageText.getLineCount() == 1){
                    Log.d("Holder", "Single line message");
                    binding.messageContainer.setOrientation(LinearLayout.HORIZONTAL);
                    params.bottomMargin = Util.getPixels(binding.getRoot().getContext(), 8);
                }
                else{
                    Log.d("Holder", "Multi line message");
                    params.bottomMargin = Util.getPixels(binding.getRoot().getContext(), 26);
                }
            }
            else{
                Log.d("Holder", binding.messageText.getText().toString() + ", views don't intersect");
                params.bottomMargin = Util.getPixels(binding.getRoot().getContext(), 8);
            }

            binding.getRoot().requestLayout();
        }
    }
}
