package org.tndata.officehours.holder;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import org.tndata.officehours.databinding.ItemPersonBinding;
import org.tndata.officehours.model.Person;
import org.tndata.officehours.util.ImageLoader;


/**
 * Holder to display a Person.
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public class PersonHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    private ItemPersonBinding binding;
    private Listener listener;


    /**
     * Constructor.
     *
     * @param binding the binding object.
     */
    public PersonHolder(@NonNull ItemPersonBinding binding, @NonNull Listener listener){
        super(binding.getRoot());
        this.binding = binding;
        this.listener = listener;

        itemView.setOnClickListener(this);
    }

    /**
     * Populates the holder with a person.
     *
     * @param person the person to use to populate the holder.
     */
    public void setPerson(@NonNull Person person){
        Drawable drawable = binding.personAvatarContainer.getBackground();
        if (drawable == null){
            Log.e("PersonHolder", "drawable is null");
        }
        GradientDrawable gradientDrawable = (GradientDrawable)drawable;
        gradientDrawable.setColor(person.getColor());
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN){
            binding.personAvatarContainer.setBackgroundDrawable(gradientDrawable);
        }
        else{
            binding.personAvatarContainer.setBackground(gradientDrawable);
        }

        if (!person.getAvatar().isEmpty()){
            ImageLoader.Options options = new ImageLoader.Options().setCropToCircle(true);
            ImageLoader.loadBitmap(binding.personAvatar, person.getAvatar(), options);
            binding.personInitials.setVisibility(View.GONE);
            binding.personAvatar.setVisibility(View.VISIBLE);
        }
        else{
            String initials = "";
            String[] names = person.getName().split(" ");
            for (String name:names){
                initials += name.charAt(0);
            }
            binding.personInitials.setText(initials);
            binding.personInitials.setVisibility(View.VISIBLE);
            binding.personAvatar.setVisibility((View.GONE));
        }

        AssetManager assetManager = binding.getRoot().getContext().getAssets();
        String font = "fonts/Roboto-Medium.ttf";
        binding.personName.setTypeface(Typeface.createFromAsset(assetManager, font));
        binding.personName.setText(person.getName());
    }

    @Override
    public void onClick(View view){
        listener.onPersonHolderTapped(getAdapterPosition());
    }


    /**
     * Listener interface for the adapter.
     *
     * @author Ismael Alonso
     * @version 1.0.0
     */
    public interface Listener{
        /**
         * Called when the holder is tapped.
         *
         * @param position the current position of the holder within the adapter.
         */
        void onPersonHolderTapped(int position);
    }
}
