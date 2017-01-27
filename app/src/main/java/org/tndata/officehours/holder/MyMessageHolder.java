package org.tndata.officehours.holder;

import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.tndata.officehours.databinding.ItemMyMessageBinding;
import org.tndata.officehours.model.Message;
import org.tndata.officehours.util.Util;


/**
 * Holder for a message sent by the current user.
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public class MyMessageHolder extends RecyclerView.ViewHolder{
    private final ItemMyMessageBinding binding;

    private Rect textBounds;
    private Rect timestampBounds;


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
        textBounds = null;
        timestampBounds = null;

        ViewTreeObserver vto = binding.myMessageText.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(){
            @Override
            public void onGlobalLayout(){
                TextView view = binding.myMessageText;
                int lineCount = view.getLineCount();
                if (lineCount != 0){
                    textBounds = new Rect();
                    view.getLineBounds(lineCount-1, textBounds);
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    setViews();
                }
            }
        });
        vto = binding.myMessageTime.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(){
            @Override
            public void onGlobalLayout(){
                TextView view = binding.myMessageTime;
                int lineCount = view.getLineCount();
                if (lineCount != 0){
                    timestampBounds = new Rect();
                    view.getLineBounds(lineCount-1, timestampBounds);
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    setViews();
                }
            }
        });

        binding.myMessageText.setText(message.getText());
        binding.myMessageTime.setText("1:24 PM");
    }

    private void setViews(){
        if (textBounds != null && timestampBounds != null){
            Log.d("Holder", "Text" + textBounds);
            Log.d("Holder", "Time" + timestampBounds);

            binding.myMessageContainer.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)binding.myMessageText.getLayoutParams();
            if (textBounds.intersect(timestampBounds)){
                Log.d("Holder", binding.myMessageText.getText().toString() + ", views intersect");

                if (binding.myMessageText.getLineCount() == 1){
                    Log.d("Holder", "Single line message");
                    binding.myMessageContainer.setOrientation(LinearLayout.HORIZONTAL);
                    params.bottomMargin = Util.getPixels(binding.getRoot().getContext(), 8);
                }
                else{
                    Log.d("Holder", "Multi line message");
                    params.bottomMargin = Util.getPixels(binding.getRoot().getContext(), 26);
                }
            }
            else{
                Log.d("Holder", binding.myMessageText.getText().toString() + ", views don't intersect");
                params.bottomMargin = Util.getPixels(binding.getRoot().getContext(), 8);
            }

            binding.getRoot().requestLayout();
        }
    }
}
