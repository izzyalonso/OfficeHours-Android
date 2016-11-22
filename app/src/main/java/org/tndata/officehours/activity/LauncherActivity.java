package org.tndata.officehours.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.tndata.officehours.R;
import org.tndata.officehours.databinding.ActivityLauncherBinding;


/**
 * Lets the user sign in.
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public class LauncherActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener{
    private static final int GOOGLE_SIGN_IN_RC = 5327;


    private ActivityLauncherBinding binding;

    private GoogleApiClient googleApiClient;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_launcher);

        //Set listeners
        binding.launcherGooogleSignIn.setOnClickListener(this);
        binding.launcherProceed.setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    public void onClick(View view){
        if (view.getId() == R.id.launcher_gooogle_sign_in){
            signInWithGoogle();
        }
        else if (view.getId() == R.id.launcher_proceed){
            startActivity(new Intent(this, ScheduleActivity.class));
            finish();
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
            Log.d("LauncherFragment", "Sign in success");
            //Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            //updateUI(true);
        }
        else{
            //Signed out, show unauthenticated UI.
            //updateUI(false);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult){
        //TODO?
    }
}
