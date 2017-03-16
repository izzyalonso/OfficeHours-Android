package org.tndata.officehours.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;


/**
 * Created by isma on 3/16/17.
 */
public class QuestionsAdapter extends RecyclerView.Adapter{

    private static final int TYPE_COURSE = 1;
    private static final int TYPE_FOOTER_SPACE = 2;


    private Context context;
    

    @Override
    public int getItemCount(){
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position){

    }
}
