package com.quickhire.quickhire;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

/**
 * Created by matth_000 on 2018-03-27.
 * This class is an asycronous thread which handles the serialization, encoding, and uploading of the video files used as for the video reponses.
 */
public class videoStreamer extends AsyncTask<videoAnswer, Void, Void> {

    int index = 0;

    @Override
    protected Void doInBackground(videoAnswer... f) {

        connection c = connection.getConnection();
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;  //Bytearray
        int maxBufferSize = 1*1024*1024;    //Max packet size of 1Mb
        String urlString = "UploadVideo";
        this.index=0;
        try{

            Uri video = f[0].getUri(); //assume only one video at a time (async task supports arbitrary number of inputs)

            InputStream fileInputStream = connection.getConnection().appcontext.getContentResolver().openInputStream(video); //open the file as an inputstream.

            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            while (bytesRead > 0){
                //create a listener for each packet.
                Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                    int i = index;
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Transmitting",("Packet number "+String.valueOf(i)));
                    }
                };
                Response.ErrorListener errorListener =new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                };
                //Format json
                String JSON = "{\"applicationID\":"+f[0].getApplicationID()+","
                            +"\"questionID\":"+f[0].getQuestionID()+","
                            +"\"packetNum\":"+this.index+","
                            +"\"data\":\""+ Base64.encodeToString(buffer,Base64.URL_SAFE)+"\"}";    //
                c.publicgeneric(JSON,urlString,listener,errorListener);
                //Logic for next request generation
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);    //Read the next set of data.
                this.index++;   //index the packets. They must be numbered so they may be put together in order on the serverside.
            }
            Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                int i = index;
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("Transmitting",("Final Packet: "+String.valueOf(i)));
                }
            };
            Response.ErrorListener errorListener =new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            };
            String JSON = "{\"applicationID\":"+f[0].getApplicationID()+","
                    +"\"questionID\":"+f[0].getQuestionID()+","
                    +"\"packetNum\":"+this.index+","
                    +"\"data\":\"done\"}";
            c.publicgeneric(JSON,urlString,listener,errorListener); //A final message to tell the server that the video upload is complete.

            fileInputStream.close();
        }
        catch (MalformedURLException ex){
            Log.e("Debug", "error: " + ex.getMessage(), ex);
        }
        catch (IOException ioe){
            Log.e("Debug", "error: " + ioe.getMessage(), ioe);
        }
        catch (Exception fnoe){
            fnoe.printStackTrace();
            Log.e("Broke", fnoe.getMessage());
        }

        c.disableExternal();    //Security purposes
        return null;
    }

}