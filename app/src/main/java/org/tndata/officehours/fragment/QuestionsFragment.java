package org.tndata.officehours.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.tndata.officehours.OfficeHoursApp;
import org.tndata.officehours.R;
import org.tndata.officehours.adapter.QuestionsAdapter;
import org.tndata.officehours.databinding.FragmentListBinding;
import org.tndata.officehours.model.Question;

import java.util.List;


/**
 * Adapter to display a compact list of questions.
 *
 * @author Ismael Alonso
 */
public class QuestionsFragment extends Fragment{
    private FragmentListBinding binding;
    private QuestionsAdapter adapter;
    private QuestionsAdapter.Listener listener;


    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        try{
            listener = (QuestionsAdapter.Listener)context;
        }
        catch (ClassCastException ccx){
            String description = "QuestionsFragment's host Activity must implement" +
                    " the QuestionsAdapter.Listener interface.";
            throw new ClassCastException(description);
        }
    }

    @Override
    public void onDetach(){
        super.onDetach();
        listener = null;
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
        List<Question> questions = ((OfficeHoursApp)getActivity().getApplication()).getQuestions();
        adapter = new QuestionsAdapter(getContext(), questions, listener);
        binding.list.setAdapter(adapter);
        binding.list.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
