package com.quickhire.quickhire;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/** CreateJobPosting2 ************************************************
 * Created by nick on 2018-03-27.
 * Description: Activity to show the UI for creating the job posting.
 *********************************************************************/
public class CreateJobPosting2 extends AppCompatActivity {
    Button SaveButton;
    EditText JobTitle;
    EditText Company;
    EditText Description;
    jobPosting post;
    public static List<Question> questionsList = new ArrayList<>(); //list of questions created in the job posting
    public static Activity activity = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_job_posting2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        activity=this; //important

        addKeyLister();
        configureCreatePostButton();
        configureCancelButton();
    }

    private void configureCreatePostButton() {
        Button postButton = (Button) findViewById(R.id.questions);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreateJobPosting2.this, questionList.class));
            }
        });
    }

    private void configureCancelButton() {
        Button cancel = (Button) findViewById(R.id.cancelButton2);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
            }
        });
    }

    private void addKeyLister() {
        JobTitle = (EditText) findViewById(R.id.JobTitleText);
        Company = (EditText) findViewById(R.id.CompanyText);
        Description = (EditText) findViewById(R.id.DescriptionText);
        SaveButton = (Button) findViewById(R.id.saveButton2);

        SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(JobTitle.getText().toString().trim().length() != 0 && Company.getText().toString().trim().length() != 0 && Description.getText().toString().trim().length() != 0){
                    final Snackbar mySnackbar = Snackbar.make(view, "Post Sent", Snackbar.LENGTH_SHORT);

                    post = new jobPosting(Company.getText().toString(), JobTitle.getText().toString(), Description.getText().toString());

                    Iterator<Question> i = questionsList.iterator();

                    while(i.hasNext()){
                        post.addQuestion(i.next()); //Add questions to the posting
                    }

                    connection.getConnection().saveJobPosting(post, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) { //save the job to the database using the connection class.
                            //Do something
                            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                            builder.setMessage(response.toString())
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            activity.finish();
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                    });

                } else {
                    final Snackbar mySnackbar = Snackbar.make(view, "Please fill all requirements", Snackbar.LENGTH_SHORT);
                    mySnackbar.show();
                }
            }
        });
    }

    public static void display(String message){
        new AlertDialog.Builder(CreateJobPosting2.activity)
                .setTitle("Server Respose")
                .setMessage(message)
                .setCancelable(true)
                .show();
    }

    @Override protected void onDestroy(){
        CreateJobPosting2.activity=null;
        super.onDestroy();
    }
}