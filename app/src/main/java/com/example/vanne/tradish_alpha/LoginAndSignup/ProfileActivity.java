package com.example.vanne.tradish_alpha.LoginAndSignup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vanne.tradish_alpha.MainActivity;
import com.example.vanne.tradish_alpha.R;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.wallet.fragment.SupportWalletFragment;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.NumberFormat;

import static com.example.vanne.tradish_alpha.R.id.changeSignupModeTextView;
import static com.example.vanne.tradish_alpha.R.id.linearLayout;
import static com.example.vanne.tradish_alpha.R.id.savechanges;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

    /*Define up the views*/
    EditText nicknameEditText;
    EditText addressEditText;
    EditText emailEditText;
    EditText phoneEditText;
    TextView usernameTextView;
    ParseUser currentUser;
    ParseFile file;
    RelativeLayout backgroundRelativeLayout;
    ImageView profileImageView;
    ImageView identityImageView;
    /*Define user data*/
    String nickname;
    String address;
    String phone;
    String email;

    /*Maximum Byte Size*/
    int MAX_IMG_BYTE_SIZE = 10485760;

    /*Debug and log string */
    String ProfileTag = "InProfileTag";

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent){
        if(i == keyEvent.KEYCODE_ENTER && keyEvent.getAction() == keyEvent.ACTION_DOWN){//make sure the key is pressed down once
            //signUp(view);
            //save data here
            Log.e(ProfileTag, "Keydown");
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getPhoto();
            }
        }
    }
    @Override
    public void onClick(View view){
        if(view.getId() == R.id.backgroundRelativeLayout || view.getId() == R.id.identityImageView){
            InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }else if(view.getId() == R.id.savechanges){
            nickname = nicknameEditText.getText().toString();
            address = addressEditText.getText().toString();
            phone = phoneEditText.getText().toString();
            Number num = null;
            try {
                if(phone != null) {
                    num = NumberFormat.getInstance().parse(phone);
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            email = emailEditText.getText().toString();

            if(phone != null && nickname != null && address != null && email != null) {
                currentUser.put("Phone", num);
                currentUser.put("email", email);
                currentUser.put("Address", address);
                currentUser.put("Nickname", nickname);
                currentUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast.makeText(ProfileActivity.this, "Information Successfully Updated", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        } else {
                            Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            }else if(view.getId() == R.id.profileImageView){
                Log.e(ProfileTag, "you just click your profile");
                if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                } else {
                    Log.e(ProfileTag, "permission granted, yo");
                    getPhoto();
                }
            }else{
                Toast.makeText(ProfileActivity.this, "Your information is not completed", Toast.LENGTH_SHORT).show();
            }

            //Log.e(ProfileTag, "Getting current user " + currentUser.getUsername() + " " + currentUser.getString("Nickname") + currentUser.get("Identity"));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data != null){
            Uri selectedImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                ImageView imageView = (ImageView)findViewById(R.id.profileImageView);
                imageView.setImageBitmap(bitmap);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
                byte[] byteArray = stream.toByteArray();
                //If the image
                if(byteArray.length >= MAX_IMG_BYTE_SIZE){
                    Toast.makeText(ProfileActivity.this,"Your Image is too large, Please upload again!", Toast.LENGTH_SHORT).show();
                    return;
                }
                file = new ParseFile(currentUser.getUsername() +"Img.png", byteArray);
                currentUser.put("Image",file);
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle("Profile Activity");
        /*Get the current user and load their information*/
        currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            //showUserList();
            Log.e(ProfileTag, "Getting current user " + currentUser.getUsername() + " " + currentUser.getSessionToken() + " " + currentUser.get("Identity"));
            //switchIntent();
        }
        /*Catches all the views*/
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        nicknameEditText = (EditText) findViewById(R.id.nicknameEditText);
        addressEditText = (EditText) findViewById(R.id.addressEditText);
        phoneEditText = (EditText) findViewById(R.id.phoneEditText);
        usernameTextView = (TextView) findViewById(R.id.usernameEditText);
        backgroundRelativeLayout = (RelativeLayout) findViewById(R.id.backgroundRelativeLayout);
        profileImageView = (ImageView) findViewById(R.id.profileImageView);
        identityImageView = (ImageView) findViewById(R.id.identityImageView);
        /*Set up listeners for views*/
        backgroundRelativeLayout.setOnClickListener(this);
        profileImageView.setOnClickListener(this);

        /*Set up the view by optaining views*/
        usernameTextView.setText(currentUser.getUsername());
        nickname = currentUser.getString("Nickname");
        address = currentUser.getString("Address");
        email = currentUser.getEmail();
        file = (ParseFile)currentUser.get("Image");
        if(file != null) {
            file.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    if (e == null && data != null) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                        profileImageView.setImageBitmap(bitmap);
                    } else {
                        profileImageView.setImageResource(R.drawable.add_contacts_icon);
                    }
                }
            });
        }


        if (currentUser.getNumber("Phone") != null) {
            //phone = Integer.toString((Integer) currentUser.getNumber("Phone"));
            phone = Long.toString( currentUser.getNumber("Phone").longValue());
            Log.e(ProfileTag, phone);
        }

        if (nickname != null) {
            nicknameEditText.setText(nickname);
        }
        if (address != null) {
            addressEditText.setText(address);
        }
        if (phone != null) {
            phoneEditText.setText(phone);
        }
        if (email != null) {
            emailEditText.setText(email);
        }
        String identity = currentUser.getString("Identity");
        switch (identity) {
            case "Driver":
                identityImageView.setImageResource(R.drawable.driver_icon);
                break;
            case "Customer":
                identityImageView.setImageResource(R.drawable.consumer_icon);
                break;
            case "Owner":
                identityImageView.setImageResource(R.drawable.owner_icon);
                break;
            default:
                identityImageView.setImageResource(R.drawable.question_sign_icon);
                break;
        }

    }
    public void getPhoto(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        /*Log out current user*/
        finish();
    }
}
