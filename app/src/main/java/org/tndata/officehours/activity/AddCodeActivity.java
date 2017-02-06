package org.tndata.officehours.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.tndata.officehours.OfficeHoursApp;
import org.tndata.officehours.R;
import org.tndata.officehours.database.CourseTableHandler;
import org.tndata.officehours.database.PersonTableHandler;
import org.tndata.officehours.databinding.ActivityAddCodeBinding;
import org.tndata.officehours.model.Course;
import org.tndata.officehours.model.Person;
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
                View.OnClickListener,
                HttpRequest.RequestCallback,
                Parser.ParserCallback{

    public static final String COURSE_KEY = "org.tndata.officehours.AddCode.Course";


    private ActivityAddCodeBinding binding;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_code);
        binding.addCodeAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        if (view.getId() == R.id.add_code_add){
            String code = binding.addCodeCode.getText().toString().trim();
            if (code.length() < 4){
                binding.addCodeError.setText(R.string.add_code_length_error);
                binding.addCodeError.setVisibility(View.VISIBLE);
            }
            else{
                binding.addCodeError.setVisibility(View.GONE);
                binding.addCodeProgress.setVisibility(View.VISIBLE);

                HttpRequest.post(this, API.URL.courseEnroll(), API.BODY.courseEnroll(code));
            }
        }
    }

    @Override
    public void onRequestComplete(int requestCode, String result){
        Parser.parse(result, Course.class, this);
    }

    @Override
    public void onRequestFailed(int requestCode, HttpRequestError error){
        binding.addCodeError.setText(R.string.add_code_invalid_error);
        binding.addCodeProgress.setVisibility(View.GONE);
        binding.addCodeError.setVisibility(View.VISIBLE);
    }

    @Override
    public void onProcessResult(int requestCode, ResultSet result){
        if (result instanceof Course){
            CourseTableHandler courseHandler = new CourseTableHandler(this);
            PersonTableHandler personHandler = new PersonTableHandler(this);

            Course course = (Course)result;
            course.process();
            courseHandler.saveCourse(course);
            personHandler.savePerson(course.getInstructor(), course);
            personHandler.savePeople(course.getStudents(), course);

            personHandler.close();
            courseHandler.close();

            //Add the object to the global sets
            OfficeHoursApp app = (OfficeHoursApp)getApplication();
            app.addCourse(course);
            app.addPerson(course.getInstructor());
            course.setInstructor(app.getPeople().get(course.getInstructor().getId()));
            for (int i = 0; i < course.getStudents().size(); i++){
                Person student = course.getStudents().get(i);
                app.addPerson(student);
                course.getStudents().set(i, app.getPeople().get(student.getId()));
            }
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
        binding.addCodeError.setText(R.string.add_code_invalid_error);
        binding.addCodeProgress.setVisibility(View.GONE);
        binding.addCodeError.setVisibility(View.VISIBLE);
    }
}
