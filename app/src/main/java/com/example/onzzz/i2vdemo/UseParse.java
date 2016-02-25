package com.example.onzzz.i2vdemo;

import com.parse.Parse;

/**
 * Created by onzzz on 26/2/2016.
 */
public class UseParse extends android.app.Application {
    @Override
    public void onCreate(){
        super.onCreate();

        Parse.initialize(this,
                getResources().getString(R.string.parse_application_id),
                getResources().getString(R.string.parse_client_key));
    }
}
