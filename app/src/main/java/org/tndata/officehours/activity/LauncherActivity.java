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
import org.tndata.officehours.model.User;
import org.tndata.officehours.util.API;

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
                HttpRequest.RequestCallback{

    private static final String TAG = "LauncherActivity";

    private static final int GOOGLE_SIGN_IN_RC = 5327;


    private ActivityLauncherBinding binding;

    private GoogleApiClient googleApiClient;

    private User user;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_launcher);

        User user = User.readFromPreferences(this);
        if (user != null){
            ((OfficeHoursApp)getApplication()).setUser(user);
            if (user.isOnBoardingComplete()){
                loadData();
            }
            else{
                startActivity(new Intent(this, OnBoardingActivity.class));
            }
            finish();
        }
        else{
            //Set listeners
            binding.launcherGooogleSignIn.setOnClickListener(this);

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
        if (view.getId() == R.id.launcher_gooogle_sign_in){
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
            user = new User(result.getSignInAccount());
            HttpRequest.post(this, API.URL.signIn(), API.BODY.signIn(user));
            //onRequestComplete(0, null);
        }
        else{
            //Why would this happen?
            Log.e(TAG, "Sign in with Google failed");
            Toast.makeText(this, "Couldn't sign in", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult){
        Log.e(TAG, "Couldn't connect to GoogleApiClient");
        binding.launcherGooogleSignIn.setEnabled(false);
    }

    private void loadData(){
        //TODO Load from DB
        startActivity(new Intent(this, ScheduleActivity.class));
    }

    @Override
    public void onRequestComplete(int requestCode, String result){
        user.writeToPreferences(this);
        ((OfficeHoursApp)getApplication()).setUser(user);
        startActivity(new Intent(this, OnBoardingActivity.class));
        finish();
    }

    @Override
    public void onRequestFailed(int requestCode, HttpRequestError error){
        Log.d(TAG, error.toString());
    }
}
