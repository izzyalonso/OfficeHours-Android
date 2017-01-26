package org.tndata.officehours.util;


import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;


/**
 * Spaces evenly items in a RecyclerView.
 *
 * @author Isamel Alosno
 * @version 1.0.0
 */
public class CustomItemDecoration extends RecyclerView.ItemDecoration{
    private Context context;
    private int margin;


    /**
     * Constructor.
     *
     * @param context a reference to the context.
     * @param marginInDp the maximum spacing between items, items, and edges.
     */
    public CustomItemDecoration(@NonNull Context context, int marginInDp){
        this.context = context;
        margin = getPixels(marginInDp);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state){
        outRect.top = margin/2;
        outRect.left = margin;
        outRect.bottom = margin/2;
        outRect.right = margin;

        boolean reversed = false;
        RecyclerView.LayoutManager manager = parent.getLayoutManager();
        if (manager instanceof LinearLayoutManager){
            reversed = ((LinearLayoutManager)manager).getReverseLayout();
        }

        int position = parent.getChildAdapterPosition(view);
        if (position == 0){
            if (reversed){
                outRect.bottom = margin;
            }
            else{
                outRect.top = margin;
            }
        }
        else if (position == parent.getAdapter().getItemCount()-1){
            if (reversed){
                outRect.top = margin;
            }
            else{
                outRect.bottom = margin;
            }
        }
    }

    public Context getContext(){
        return context;
    }

    protected int getPixels(int densityPixels){
        return (int)Math.ceil(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, densityPixels,
                context.getResources().getDisplayMetrics()));
    }
}
