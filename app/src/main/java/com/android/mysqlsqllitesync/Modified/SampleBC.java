package com.android.mysqlsqllitesync.Modified;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ethiopia on 11/5/2018.
 */

public class SampleBC extends BroadcastReceiver {
    static int noOfTimes = 0;



        // Method gets called when Broad Case is issued from SecondActivity for every 10 seconds

    @Override

    public void onReceive(final Context context, Intent intent) {



        noOfTimes++;

        Toast.makeText(context, "BC Service Running for " + noOfTimes + " times", Toast.LENGTH_SHORT).show();

        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();

        // Checks if new records are inserted in Remote MySQL DB to proceed with Sync operation

       // client.post("<a class="vglnk" href="http://192.168.2.4:9000/mysqlsqlitesync/getdbrowcount.php",params" rel="nofollow"><span>http</span><span>://</span><span>192</span><span>.</span><span>168</span><span>.</span><span>2</span><span>.</span><span>4</span><span>:</span><span>9000</span><span>/</span><span>mysqlsqlitesync</span><span>/</span><span>getdbrowcount</span><span>.</span><span>php</span><span>",</span><span>params</span></a> ,new AsyncHttpResponseHandler() {
       //client.post("<a class="vglnk" href="http://192.168.2.4:9000/mysqlsqlitesync/getdbrowcount.php",params" rel="nofollow"><span>http</span><span>://</span><span>192</span><span>.</span><span>168</span><span>.</span><span>2</span><span>.</span><span>4</span><span>:</span><span>9000</span><span>/</span><span>mysqlsqlitesync</span><span>/</span><span>getdbrowcount</span><span>.</span><span>php</span><span>",</span><span>params</span></a> ,new AsyncHttpResponseHandler() {

        client.post("<a class='vglnk' href=' 192.168.127.1/mysqlsqlitesync/getdbrowcount.php' rel='nofollow'><span>http</span><span>://</span><span>192</span><span>.</span><span>168</span><span>.</span><span>127</span><span>.</span><span>1</span><span>/</span><span>mysqlsqlitesync</span><span>/</span><span>getdbrowcount</span><span>.</span><span>php</span></a>" , params, new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(String response) {

            System.out.println(response);

            try {

                // Create JSON object out of the response sent by getdbrowcount.php

                JSONObject obj = new JSONObject(response);

                System.out.println(obj.get("count"));

                // If the count value is not zero, call MyService to display notification

                if(obj.getInt("count") != 0){

                    final Intent intnt = new Intent(context, MyService.class);

                    // Set unsynced count in intent data

                    intnt.putExtra("intntdata", "Unsynced Rows Count "+obj.getInt("count"));

                    // Call MyService

                    context.startService(intnt);


                }
                else
                    {

                    Toast.makeText(context, "Sync not needed", Toast.LENGTH_SHORT).show();

                }

            } catch (JSONException e) {

                // TODO Auto-generated catch block

                e.printStackTrace();

            }

        }



        @Override
        public void onFailure(int statusCode, Throwable error, String content) {



            if(statusCode == 404){

                Toast.makeText(context, "404", Toast.LENGTH_SHORT).show();

            }else if(statusCode == 500){

                Toast.makeText(context, "500", Toast.LENGTH_SHORT).show();
            }
            else
                {

                Toast.makeText(context, "Error occured!", Toast.LENGTH_SHORT).show();

               }

        }

    });

}
}


