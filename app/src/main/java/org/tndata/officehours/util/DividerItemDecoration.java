package org.tndata.officehours.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.tndata.officehours.R;
import org.tndata.officehours.adapter.PeopleAdapter;


/**
 * Adds dividers to the functionality already provided by the parent.
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public class DividerItemDecoration extends CustomItemDecoration{
    private Drawable divider;


    /**
     * Constructor.
     *
     * @param context a reference to the context.
     */
    @SuppressWarnings("deprecation")
    public DividerItemDecoration(@NonNull Context context){
        super(context, 12);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            divider = context.getResources().getDrawable(R.drawable.divider);
        }
        else{
            divider = context.getResources().getDrawable(R.drawable.divider, null);
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state){
        int dividerLeft = parent.getPaddingLeft() + getPixels(70);
        int dividerRight = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        if (((PeopleAdapter)parent.getAdapter()).includesFooterSpace()){
            childCount--;
        }
        for (int i = 0; i < childCount - 1; i++){
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int dividerTop = child.getBottom() + params.bottomMargin + getPixels(6);
            int dividerBottom = dividerTop + 1;

            divider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom);
            divider.draw(c);
        }
    }
}
