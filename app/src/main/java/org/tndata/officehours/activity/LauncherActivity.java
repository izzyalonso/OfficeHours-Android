package org.tndata.officehours.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.tndata.officehours.OfficeHoursApp;
import org.tndata.officehours.R;
import org.tndata.officehours.databinding.ActivityLauncherBinding;
import org.tndata.officehours.model.Course;
import org.tndata.officehours.model.ResultSet;
import org.tndata.officehours.model.User;
import org.tndata.officehours.database.DatabaseReader;
import org.tndata.officehours.parser.Parser;
import org.tndata.officehours.util.API;
import org.tndata.officehours.util.DataSynchronizer;

import java.util.List;

import es.sandwatch.httprequests.HttpRequest;
import es.sandwatch.httprequests.HttpRequestError;


/**
 * Lets the user sign in.
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public class LauncherActivity
        extends AppCompatActivity
        implements
                View.OnClickListener,
                GoogleApiClient.OnConnectionFailedListener,
                HttpRequest.RequestCallback,
                Parser.ParserCallback,
                DataSynchronizer.Callback,
                DatabaseReader.Listener{

    public static final String FROM_ON_BOARDING_KEY = "org.tndata.officehours.Launcher.FromOnBoarding";


    private static final String TAG = "LauncherActivity";
    private static final int GOOGLE_SIGN_IN_RC = 5327;


    private ActivityLauncherBinding binding;
    private GoogleApiClient googleApiClient;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_launcher);

        User user = User.readFromPreferences(this);
        if (user != null){
            binding.launcherGoogleSignIn.setVisibility(View.GONE);
            binding.launcherProgress.setVisibility(View.VISIBLE);
            ((OfficeHoursApp)getApplication()).setUser(user);
            if (user.isOnBoardingComplete()){
                if (getIntent().getBooleanExtra(FROM_ON_BOARDING_KEY, false)){
                    DataSynchronizer.sync(this, this);
                }
                else{
                    DatabaseReader.start(this, this);
                }
            }
            else{
                startActivity(new Intent(this, OnBoardingActivity.class));
                finish();
            }
        }
        else{
            //Set listeners
            binding.launcherGoogleSignIn.setOnClickListener(this);

            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    //.requestServerAuthCode(getString(R.string.server_client_id))
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();

            googleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
        }
    }

    @Override
    public void onClick(View view){
        if (view.getId() == R.id.launcher_google_sign_in){
            signInWithGoogle();
        }
    }

    private void signInWithGoogle(){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN_RC);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == GOOGLE_SIGN_IN_RC){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleGoogleSignInResult(result);
        }
        else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleGoogleSignInResult(GoogleSignInResult result){
        if (result.isSuccess()){
            Log.i(TAG, "Sign in with google successful");
            HttpRequest.post(this, API.URL.signIn(), API.BODY.signIn(result.getSignInAccount()));
        }
        else{
            //Why would this happen?
            Log.e(TAG, "Sign in with Google failed");
            Log.e(TAG, result.getStatus().toString());
            Toast.makeText(this, "Couldn't sign in", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult){
        Log.e(TAG, "Couldn't connect to GoogleApiClient");
        binding.launcherGoogleSignIn.setEnabled(false);
    }

    @Override
    public void onRequestComplete(int requestCode, String result){
        Log.i(TAG, "Authentication with the backend succeeded");
        Parser.parse(result, User.class, this);
    }

    @Override
    public void onRequestFailed(int requestCode, HttpRequestError error){
        Log.d(TAG, error.toString());
    }

    @Override
    public void onProcessResult(int requestCode, ResultSet result){
        if (result instanceof User){
            ((User)result).process();
        }
    }

    @Override
    public void onParseSuccess(int requestCode, ResultSet result){
        if (result instanceof User){
            User user = (User)result;
            user.writeToPreferences(this);
            ((OfficeHoursApp)getApplication()).setUser(user);
            if (user.isOnBoardingComplete()){
                DataSynchronizer.sync(this, this);
            }
            else{
                startActivity(new Intent(this, OnBoardingActivity.class));
                finish();
            }
        }
    }

    @Override
    public void onParseFailed(int requestCode){

    }

    @Override
    public void onDataLoaded(){
        startActivity(new Intent(this, ScheduleActivity.class));
        finish();
    }

    @Override
    public void onDataLoadFailed(){

    }

    @Override
    public void onComplete(List<Course> courses){
        ((OfficeHoursApp)getApplication()).setCourses(courses);
        startActivity(new Intent(this, ScheduleActivity.class));
        finish();
    }
}
