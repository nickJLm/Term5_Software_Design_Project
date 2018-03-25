package com.quickhire.quickhire;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configureRegisterButton();

        //Don't touch this line
        connection.createinitialConnection(this.getApplicationContext()); //I'm an important line.
        //Please don't touch that line

        startActivity(new Intent(MainActivity.this, RegisterLoginActivity.class));



    }


    private void configureRegisterButton() {
        Button postButton = (Button) findViewById(R.id.loginRegister);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegisterLoginActivity.class));
            }
        });
    }
}

