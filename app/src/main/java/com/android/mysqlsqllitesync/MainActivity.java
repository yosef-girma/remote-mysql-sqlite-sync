package com.android.mysqlsqllitesync;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.AsyncHttpResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    // DB Class to perform DB related operations

    DBController controller = new DBController(this);

        // Progress Dialog Object

    ProgressDialog prgDialog;

    HashMap<String, String> queryValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        // Get User records from SQLite DB

        ArrayList<HashMap<String, String>> userList = controller.getAllUsers();

        // If users exists in SQLite DB

        if (userList.size() != 0) {

            // Set the User Array list in ListView
                       ListAdapter adapter = new SimpleAdapter(MainActivity.this, userList, R.layout.view_user_entry, new String[] {
                    "userId", "userName" }, new int[] { R.id.userId, R.id.userName });

            ListView myList = (ListView) findViewById(android.R.id.list);

            myList.setAdapter(adapter);

        }

        // Initialize Progress Dialog properties

        prgDialog = new ProgressDialog(this);

        prgDialog.setMessage("Transferring Data from Remote MySQL DB and Syncing SQLite. Please wait...");

        prgDialog.setCancelable(false);
        // BroadCase Receiver Intent Object

        Intent alarmIntent = new Intent(getApplicationContext(), SampleBC.class);

        // Pending Intent Object

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Alarm Manager Object

        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);


        // Remote MySQL DB

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis() + 5000, 10 * 1000, pendingIntent);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Method to Sync MySQL to SQLite DB

    public void syncSQLiteMySQLDB() {

        // Create AsycHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();

        // Http Request Params Object

        RequestParams params = new RequestParams();

        // Show ProgressBar

        prgDialog.show();

        // Make Http call to getusers.php


        //client.post("<a class="vglnk" href="http:192.168.2.4:9000/mysqlsqlitesync/getusers.php" rel="nofollow"><span>http</span><span>://</span><span>192</span><span>.</span><span>168</span><span>.</span><span>2</span><span>.</span><span>4</span><span>:</span><span>9000</span><span>/</span><span>mysqlsqlitesync</span><span>/</span><span>getusers</span><span>.</span><span>php</span></a>"
        // Make Http call to getusers.php

        client.post("<a class='vglnk' href=' 192.168.127.1/mysqlsqlitesync/getusers.php' rel='nofollow'><span>http</span><span>://</span><span>192</span><span>.</span><span>168</span><span>.</span><span>127</span><span>.</span><span>1</span><span>/</span><span>mysqlsqlitesync</span><span>/</span><span>updatesyncsts</span><span>.</span><span>php</span></a>" , params, new AsyncHttpResponseHandler() {


            @Override
          public void onSuccess(String response) {

            // Hide ProgressBar

            prgDialog.hide();

            // Update SQLite DB with response sent by getusers.php

            updateSQLite(response);

        }

        // When error occured

        @Override
        public void onFailure(int statusCode, Throwable error, String content) {
            // TODO Auto-generated method stub

            // Hide ProgressBar

            prgDialog.hide();

            if (statusCode == 404) {

                Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();

            } else if (statusCode == 500) {

                Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();

            } else {

                Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]",

                        Toast.LENGTH_LONG).show();

            }
            }

    });

}



public void updateSQLite(String response){
        ArrayList<HashMap<String, String>> usersynclist;

        usersynclist = new ArrayList<HashMap<String, String>>();

        // Create GSON object

        Gson gson = new GsonBuilder().create();

        try {

        // Extract JSON array from the response
        JSONArray arr = new JSONArray(response);

        System.out.println(arr.length());

        // If no of array elements is not zero

        if(arr.length() != 0){

        // Loop through each array element, get JSON object which has userid and username

        for (int i = 0; i < arr.length(); i++) {

        // Get JSON object

        JSONObject obj = (JSONObject) arr.get(i);

        System.out.println(obj.get("userId"));

        System.out.println(obj.get("userName"));

        // DB QueryValues Object to insert into SQLite

        queryValues = new HashMap<String, String>();

        // Add userID extracted from Object

        queryValues.put("userId", obj.get("userId").toString());

        // Add userName extracted from Object

        queryValues.put("userName", obj.get("userName").toString());

        // Insert User into SQLite DB

        controller.insertUser(queryValues);

        HashMap<String, String> map = new HashMap<String, String>();

        // Add status for each User in Hashmap

        map.put("Id", obj.get("userId").toString());

        map.put("status", "1");

        usersynclist.add(map);

        }

        // Inform Remote MySQL DB about the completion of Sync activity by passing Sync status of Users

        updateMySQLSyncSts(gson.toJson(usersynclist));

        // Reload the Main Activity

        reloadActivity();

        }

        } catch (JSONException e) {

        // TODO Auto-generated catch block

        e.printStackTrace();

        }
        }

        // Method to inform remote MySQL DB about completion of Sync activity

public void updateMySQLSyncSts(String json) {

        System.out.println(json);

        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();

        params.put("syncsts", json);

        // Make Http call to updatesyncsts.php with JSON parameter which has Sync statuses of Users

        client.post("<a class='vglnk' href='192.168.127.1/mysqlsqlitesync/updatesyncsts.php' rel='nofollow'><span>http</span><span>://</span><span>192</span><span>.</span><span>168</span><span>.</span><span>127</span><span>.</span><span>1</span> <span>/</span><span>mysqlsqlitesync</span><span>/</span><span>updatesyncsts</span><span>.</span><span>php</span></a>",

      params, new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(String response) {

                        Toast.makeText(getApplicationContext(), "MySQL DB has been informed about Sync activity", Toast.LENGTH_LONG).show();

                    }


                    @Override
                    public void onFailure(int statusCode, Throwable error, String content) {

                        Toast.makeText(getApplicationContext(), "Error Occured", Toast.LENGTH_LONG).show();

                    }

        });
};


public void reloadActivity()
        {

        Intent objIntent = new Intent(getApplicationContext(), MainActivity.class);

        startActivity(objIntent);

        }

        }

        // Reload SecondActivity







