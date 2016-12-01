package org.tndata.officehours.util;


import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;


/**
 * Spaces evenly items in a RecyclerView.
 *
 * @author Isamel Alosno
 * @version 1.0.0
 */
public class CustomItemDecoration extends RecyclerView.ItemDecoration{
    private int mMargin;


    /**
     * Constructor.
     *
     * @param context a reference to the context.
     * @param marginInDp the maximum spacing between items, items, and edges.
     */
    public CustomItemDecoration(Context context, int marginInDp){
        mMargin = getPixels(context, marginInDp);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state){
        outRect.top = mMargin/2;
        outRect.left = mMargin;
        outRect.bottom = mMargin/2;
        outRect.right = mMargin;
        if (parent.getChildAdapterPosition(view) == 0){
            outRect.top = mMargin;
        }
        else if (parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount()-1){
            outRect.bottom = mMargin;
        }
    }

    private int getPixels(Context context, int densityPixels){
        return (int)Math.ceil(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, densityPixels,
                context.getResources().getDisplayMetrics()));
    }
}
