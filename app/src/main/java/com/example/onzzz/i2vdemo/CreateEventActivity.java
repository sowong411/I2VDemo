package com.example.onzzz.i2vdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Arrays;
import java.util.List;

/**
 * Created by WAICHONG on 31/12/2015.
 */
public class CreateEventActivity extends ActionBarActivity {

    String userObjectId;

    String[] memberId = new String[20];
    int numOfMember = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        Intent intent = getIntent();
        userObjectId = intent.getStringExtra("UserObjectId");

        memberId[0] = userObjectId;

        assert (intent != null);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // after typing all info (date time place , app will create event)
        findViewById(R.id.create_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText eventNameText = (EditText) findViewById(R.id.name);
                final String eventName = eventNameText.getText().toString();
                ParseObject event = new ParseObject("Event");
                event.put("EventName", eventName);
                event.put("Date", "");
                event.put("Time", "");
                event.put("PhotoNumber", 0);
                event.put("MemberNumber", numOfMember);
                event.addAllUnique("Member", Arrays.asList(memberId));
                event.put("EventHolder", memberId[0]);
                event.put("VideoNumber", 0);
                event.put("Video", "");
                event.saveInBackground();

                ParseQuery<ParseObject> query = ParseQuery.getQuery("Account");
                query.whereExists("Name");
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        for (int i = 0; i < objects.size(); i++) {
                            for (int j = 0; j < numOfMember; j++) {
                                if (objects.get(i).getObjectId().equals(memberId[j])) {
                                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Account");
                                    query.getInBackground(objects.get(i).getObjectId(), new GetCallback<ParseObject>() {
                                        @Override
                                        public void done(ParseObject object, ParseException e) {
                                            if (e == null) {
                                                object.add("Event", eventName);
                                                object.saveInBackground();
                                            }
                                        }
                                    });
                                } else {

                                }
                            }
                            ParseQuery<ParseObject> eventQuery = ParseQuery.getQuery("Event");
                            eventQuery.whereEqualTo("EventName", eventName);
                            eventQuery.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(List<ParseObject> objects, ParseException e) {
                                    if (e == null) {
                                        if (objects.size() == 1) {
                                            Intent intent = new Intent();
                                            intent.setClass(CreateEventActivity.this, EventActivity.class);
                                            intent.putExtra("UserObjectId", userObjectId);
                                            intent.putExtra("EventObjectId", objects.get(0).getObjectId());
                                            startActivity(intent);
                                        }
                                    }
                                }
                            });
                        }

                    }
                });

                eventNameText.setText("");
            }
        });

        findViewById(R.id.addmem_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CreateEventActivity.this, AddMemberActivity.class);
                startActivityForResult(i, 100);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.userButton) {
            return true;
        }
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
        //  return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (100) : {
                if (resultCode == Activity.RESULT_OK) {
                    String[] newText = data.getStringArrayExtra("Member");
                    numOfMember = data.getIntExtra("NumOfMember", 1);
                    // TODO Update your TextView.
                    for (int i=1; i<numOfMember; i++){
                        memberId[i] = newText[i];
                    }
                }
                break;
            }
        }
    }

    public void Close(View view) {
        finish();
    }
}
