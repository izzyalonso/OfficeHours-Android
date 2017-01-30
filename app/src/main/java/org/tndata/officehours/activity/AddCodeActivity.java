package org.tndata.officehours.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import org.tndata.officehours.R;
import org.tndata.officehours.database.CourseTableHandler;
import org.tndata.officehours.database.PersonTableHandler;
import org.tndata.officehours.databinding.ActivityAddCodeBinding;
import org.tndata.officehours.model.Course;
import org.tndata.officehours.model.ResultSet;
import org.tndata.officehours.parser.Parser;
import org.tndata.officehours.util.API;

import es.sandwatch.httprequests.HttpRequest;
import es.sandwatch.httprequests.HttpRequestError;


/**
 * Dialog through which the user can input a code to access a course.
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public class AddCodeActivity
        extends AppCompatActivity
        implements
                TextWatcher,
                HttpRequest.RequestCallback,
                Parser.ParserCallback{

    public static final String COURSE_KEY = "org.tndata.officehours.AddCode.Course";


    private ActivityAddCodeBinding binding;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_code);

        addListeners();
    }

    private void addListeners(){
        binding.addCode1.addTextChangedListener(this);
        binding.addCode2.addTextChangedListener(this);
        binding.addCode3.addTextChangedListener(this);
        binding.addCode4.addTextChangedListener(this);
    }

    private void removeListeners(){
        binding.addCode1.removeTextChangedListener(this);
        binding.addCode2.removeTextChangedListener(this);
        binding.addCode3.removeTextChangedListener(this);
        binding.addCode4.removeTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){
        //Unused
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2){
        //Unused
    }

    @Override
    public void afterTextChanged(Editable editable){
        if (editable == binding.addCode1.getText()){
            binding.addCode2.requestFocus();
        }
        else if (editable == binding.addCode2.getText()){
            binding.addCode3.requestFocus();
        }
        else if (editable == binding.addCode3.getText()){
            binding.addCode4.requestFocus();
        }
        else if (editable == binding.addCode4.getText()){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(binding.getRoot().getWindowToken(), 0);
            binding.addCode4.clearFocus();
            removeListeners();
            onCodeEntered();
        }
    }

    private void onCodeEntered(){
        binding.addCode1.setEnabled(false);
        binding.addCode2.setEnabled(false);
        binding.addCode3.setEnabled(false);
        binding.addCode4.setEnabled(false);
        binding.addCodeError.setVisibility(View.GONE);
        binding.addCodeProgress.setVisibility(View.VISIBLE);

        String code = binding.addCode1.getText().toString().trim();
        code += binding.addCode2.getText().toString().trim();
        code += binding.addCode3.getText().toString().trim();
        code += binding.addCode4.getText().toString().trim();

        HttpRequest.post(this, API.URL.courseEnroll(), API.BODY.courseEnroll(code));
    }

    @Override
    public void onRequestComplete(int requestCode, String result){
        Parser.parse(result, Course.class, this);
    }

    @Override
    public void onRequestFailed(int requestCode, HttpRequestError error){
        binding.addCodeProgress.setVisibility(View.GONE);
        binding.addCodeError.setVisibility(View.VISIBLE);

        binding.addCode1.setEnabled(true);
        binding.addCode2.setEnabled(true);
        binding.addCode3.setEnabled(true);
        binding.addCode4.setEnabled(true);

        binding.addCode1.setText("");
        binding.addCode2.setText("");
        binding.addCode3.setText("");
        binding.addCode4.setText("");

        addListeners();
    }

    @Override
    public void onProcessResult(int requestCode, ResultSet result){
        if (result instanceof Course){
            CourseTableHandler courseHandler = new CourseTableHandler(this);
            PersonTableHandler personHandler = new PersonTableHandler(this);

            Course course = (Course)result;
            courseHandler.saveCourse(course);
            personHandler.savePerson(course.getInstructor(), course);
            personHandler.savePeople(course.getStudents(), course);

            personHandler.close();
            courseHandler.close();
        }
    }

    @Override
    public void onParseSuccess(int requestCode, ResultSet result){
        if (result instanceof Course){
            Intent intent = new Intent().putExtra(COURSE_KEY, (Course)result);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void onParseFailed(int requestCode){

    }
}
