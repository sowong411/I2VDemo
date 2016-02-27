package com.example.onzzz.i2vdemo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

/**
 * Created by onzzz on 26/2/2016.
 */
public class UserInfoActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info);

        Intent intent = getIntent();
        String name = intent.getStringExtra("Name");
        String id = intent.getStringExtra("Id");
        String profilePicUri = intent.getStringExtra("ProfilePicUri");
        String loginMethod = intent.getStringExtra("LoginMethod");

        TextView loginMethodText = (TextView) findViewById(R.id.login_method);
        if (loginMethod.equals("Facebook")){
            loginMethodText.setText("You are logged in using Facebook");
            //ProfilePictureView displayProfilePic = (ProfilePictureView) findViewById(R.id.profilePicFacebook);
            ImageView displayProfilePic = (ImageView) findViewById(R.id.profilePicFacebook);
            displayProfilePic.setVisibility(View.VISIBLE);
            new LoadProfileImage(displayProfilePic).execute(profilePicUri);
        }
        else if (loginMethod.equals("Google")){
            loginMethodText.setText("You are logged in using Google");
            ImageView displayProfilePic = (ImageView) findViewById(R.id.profilePicGoogle);
            displayProfilePic.setVisibility(View.VISIBLE);
            new LoadProfileImage(displayProfilePic).execute(profilePicUri);
        }

        TextView displayName = (TextView) findViewById(R.id.userName);
        displayName.setText(name);

    }

    /**
     * Background Async task to load user profile picture from url
     * */
    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public LoadProfileImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    public void Close(View view) {
        finish();
    }
}
