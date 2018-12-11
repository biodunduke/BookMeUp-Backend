/**
 * Team LastMinSub:
 * Abiodun Ojo
 * David Uche
 * Elias Sabbagh
 *
 *
 * The purpose of this Activity is to handle facebook login. It does this
 * By using the facebook sdk to handle authentication and checks whether the token
 * is still active when the user opens the app. If so, they are directed to the main
 * activity, else, they should login again
 *
 * */


package com.humber.bookmeup.views;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.humber.bookmeup.R;

public class LoginActivity extends AppCompatActivity {
    private CallbackManager mCallbackManager;
    private FirebaseAuth mAuth;
    private FirebaseAuth mTestAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions(getString(R.string.email), getString(R.string.publicProfile));
        mAuth = FirebaseAuth.getInstance();
        Button testLogin= (Button)findViewById(R.id.testBtn);
        mTestAuth = FirebaseAuth.getInstance();

        testLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleAnonymousRegistration();
            }
        });
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }
            @Override
            public void onCancel() { }

            @Override
            public void onError(FacebookException error) { }
        });
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        //updateUI(currentUser);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    private void handleAnonymousRegistration(){
        mTestAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mTestAuth.getCurrentUser();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, getString(R.string.authFailed),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            /**If the user is already logged in, go to the main activity*/
                            if (user != null) {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("", getString(R.string.signWithCredFailure), task.getException());
                            Toast.makeText(LoginActivity.this, getString(R.string.authFailed),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onBackPressed(){
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle(R.string.login_title)
                .setIcon(R.drawable.ic_happy)
                .setCancelable(true)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        builder.show();

    }


}
