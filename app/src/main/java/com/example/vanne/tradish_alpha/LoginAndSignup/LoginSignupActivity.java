package com.example.vanne.tradish_alpha.LoginAndSignup;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;

import com.example.vanne.tradish_alpha.MainActivity;
import com.example.vanne.tradish_alpha.Menus.CustomerMenu;
import com.example.vanne.tradish_alpha.Menus.DriverMenu;
import com.example.vanne.tradish_alpha.Menus.RestaurantMenu;
import com.example.vanne.tradish_alpha.R;
import com.parse.ParseException;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.List;


/*Login in and Sign up*/
public class LoginSignupActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

    Boolean signUpModeActive = true;

    String LoginTag = "InLogIn";
    String identity = "";

    /*Set up the view */
    TextView changeSignupModeTextView;
    EditText passwordEditText;
    EditText usernameEditText;
    EditText emailEditText;
    TextView identityBar;
    Button signupButton;
    RelativeLayout backgroundRelativeLayout;
    ImageView logoImageView;
    int maxUserID = 0;

    /*Shared Preference for user name and password*/
    SharedPreferences sharedPreferences;

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent){
        if(i == keyEvent.KEYCODE_ENTER && keyEvent.getAction() == keyEvent.ACTION_DOWN){//make sure the key is pressed down once
            signUp(view);
        }
        return false;
    }

    @Override
    public void onClick(View view){
        if (view.getId() == R.id.changeSignupModeTextView) {
            Log.i("AppInfo", "Change Signup Mode");
            if (signUpModeActive) {
                emailEditText.setVisibility(View.INVISIBLE);
                signUpModeActive = false;
                signupButton.setText("Login");
                changeSignupModeTextView.setText("Or, Signup");
            } else {
                signUpModeActive = true;
                signupButton.setText("Signup");
                changeSignupModeTextView.setText("Or, Login");
                emailEditText.setVisibility(View.VISIBLE);
            }
        }else if(view.getId() == R.id.backgroundRelativeLayout || view.getId() == R.id.logoImageView){
            InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
    }

    public void signUp(View view){
        signupButton = (Button) findViewById(R.id.signupButton);
        if(usernameEditText.getText().toString().matches("")|| passwordEditText.getText().toString().matches("")){
            Toast.makeText(this, "A username and password are required", Toast.LENGTH_SHORT).show();
        }else{
            if(signUpModeActive) {
                ParseUser user= new ParseUser();
                /*Populate user data*/
                user.setUsername(usernameEditText.getText().toString());
                user.setPassword(passwordEditText.getText().toString());
                user.setEmail(emailEditText.getText().toString());
                user.put("Identity", identity);
                user.put("UserID", maxUserID + 1);
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast.makeText(LoginSignupActivity.this, "Sign up successful", Toast.LENGTH_SHORT).show();
                            /*Stored in local storage（sharedpreference)*/
                            sharedPreferences.edit().putString("username",usernameEditText.getText().toString() ).apply();
                            sharedPreferences.edit().putString("password", passwordEditText.getText().toString()).apply();
                            /*Send user data to server */
                            /*Switch to the right activity base on identity*/
                            switchIntent();
                        } else {
                            Toast.makeText(LoginSignupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }else{
                /*Log in, if user selected log in options*/
                ParseUser.logInInBackground(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if(user != null){
                            Log.e(LoginTag, "Login good");
                            Log.e(LoginTag, user.getEmail() + " " + user.getUsername() + " " + user.get("Nickname") + " " + user.get("Identity"));
                            if(user.getString("Identity").matches(identity)) {
                                /*Stored in local storage（sharedpreference)*/
                                sharedPreferences.edit().putString("username",usernameEditText.getText().toString() ).apply();
                                sharedPreferences.edit().putString("password", passwordEditText.getText().toString()).apply();
                                switchIntent();
                            }else{
                                String message = "The user we detected is identified as a " + user.getString("Identity") + ", please double check";
                                Toast.makeText(LoginSignupActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(LoginSignupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }

    public void switchIntent(){
        if(identity.matches("Driver")){
            Intent driverIntent = new Intent(getApplicationContext(), DriverMenu.class);
            driverIntent.putExtra("Identity", identity);
            startActivity(driverIntent);
        }else if(identity.matches("Customer")){
            Intent customerIntent = new Intent(getApplicationContext(), CustomerMenu.class);
            customerIntent.putExtra("Identity", identity);
            startActivity(customerIntent);
        }else if(identity.matches("Restaurant")){
            Intent restIntent = new Intent(getApplicationContext(), RestaurantMenu.class);
            restIntent.putExtra("Identity", identity);
            startActivity(restIntent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup);
        setTitle("Login/Signup");

        /*Log out current user*/
        ParseUser.getCurrentUser().logOut();


        Intent intent = getIntent();
        identity = intent.getStringExtra("Identity");
        Toast.makeText(LoginSignupActivity.this, "You identify yourself as " + identity, Toast.LENGTH_SHORT).show();

        /*Obtain textviews */
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        identityBar = (TextView) findViewById(R.id.IdentityBar);
        signupButton = (Button) findViewById(R.id.signupButton);
        changeSignupModeTextView = (TextView)findViewById(R.id.changeSignupModeTextView);
        changeSignupModeTextView = (TextView)findViewById(R.id.changeSignupModeTextView);
        backgroundRelativeLayout = (RelativeLayout)findViewById(R.id.backgroundRelativeLayout);
        logoImageView = (ImageView) findViewById(R.id.logoImageView);

        /*Initiate shared preference, if data is stored in the */
        sharedPreferences = this.getSharedPreferences("com.example.vanne.tradish_alpha", Context.MODE_PRIVATE);
        String myUsername = sharedPreferences.getString("username", "");
        String myPassword = sharedPreferences.getString("password","");

        if(!myUsername.matches("") && !myPassword.matches("")){
            signUpModeActive = false;
            usernameEditText.setText(myUsername);
            passwordEditText.setText(myPassword);
            Log.e(LoginTag, "got pass user" + myUsername + " " + myPassword);
            signupButton.setText("Login");
            changeSignupModeTextView.setText("Or, Signup");
        }

        /*Check visiblity*/
        if(signUpModeActive){
            emailEditText.setVisibility(View.VISIBLE);
        }

        /*Set up identity bar*/
        identityBar.setText("Identified as: " + identity);

        /*Set onclick listeners for elements*/
        changeSignupModeTextView.setOnClickListener(this);
        backgroundRelativeLayout.setOnClickListener(this);
        logoImageView.setOnClickListener(this);
        passwordEditText = (EditText)findViewById(R.id.passwordEditText);
        passwordEditText.setOnKeyListener(this);

        /*Assign user id by making query of identities and putting the largest integer for latest user*/
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        //query.whereEqualTo("Identity", identity);
        query.orderByDescending("UserID");
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e == null && objects != null){
                    Log.e(LoginTag, "Retrived " + objects.size() + " objects");
                    maxUserID = objects.size();
                }
            }
        });
        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        /*Log out current user*/
        ParseUser.getCurrentUser().logOut();
        /*Navigate back to main activity*/
        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(mainIntent);
        finish();
    }

}