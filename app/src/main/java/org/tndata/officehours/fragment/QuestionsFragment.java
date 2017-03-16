package org.tndata.officehours.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.tndata.officehours.R;
import org.tndata.officehours.databinding.FragmentListBinding;


/**
 * Created by isma on 3/16/17.
 */
public class QuestionsFragment extends Fragment{
    private FragmentListBinding binding;


    @Override
    public void onAttach(Context context){
        super.onAttach(context);

    }

    @Override
    public void onDetach(){
        super.onDetach();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
    }
}
