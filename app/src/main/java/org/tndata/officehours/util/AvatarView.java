package org.tndata.officehours.util;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import org.tndata.officehours.R;
import org.tndata.officehours.databinding.AvatarBinding;
import org.tndata.officehours.model.Person;


/**
 * View for a user's avatar.
 *
 * @author Ismael Alonso
 */
public class AvatarView extends FrameLayout{
    private AvatarBinding binding;

    private int colors[];


    public AvatarView(Context context, AttributeSet attrs){
        super(context, attrs);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        binding = DataBindingUtil.inflate(inflater, R.layout.avatar, this, true);

        setColors();
    }

    @SuppressWarnings("deprecation")
    private void setColors(){
        colors = new int[5];
        colors[0] = getContext().getResources().getColor(R.color.avatar_cyan);
        colors[1] = getContext().getResources().getColor(R.color.avatar_indigo);
        colors[2] = getContext().getResources().getColor(R.color.avatar_purple);
        colors[3] = getContext().getResources().getColor(R.color.avatar_green);
        colors[4] = getContext().getResources().getColor(R.color.avatar_orange);
    }

    @SuppressWarnings("deprecation")
    public void setPerson(@NonNull Person person){
        Drawable drawable = binding.avatarContainer.getBackground();
        GradientDrawable gradientDrawable = (GradientDrawable)drawable;
        gradientDrawable.setColor(colors[intFromString(person.getName())%colors.length]);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN){
            binding.avatarContainer.setBackgroundDrawable(gradientDrawable);
        }
        else{
            binding.avatarContainer.setBackground(gradientDrawable);
        }

        if (!person.getAvatar().isEmpty()){
            ImageLoader.Options options = new ImageLoader.Options().setCropToCircle(true);
            ImageLoader.loadBitmap(binding.avatarImage, person.getAvatar(), options);
            binding.avatarInitials.setVisibility(View.GONE);
            binding.avatarImage.setVisibility(View.VISIBLE);
        }
        else{
            String initials = "";
            String[] names = person.getName().split(" ");
            for (String name:names){
                initials += name.charAt(0);
            }
            binding.avatarInitials.setText(initials);
            binding.avatarInitials.setVisibility(View.VISIBLE);
            binding.avatarImage.setVisibility((View.GONE));
        }
    }

    private int intFromString(@NonNull String src){
        int result = 0;
        for (int i = 0; i < src.length(); i++){
            result += src.charAt(i);
        }
        return result;
    }
}
