package com.kontrakanelite.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class Home extends AppCompatActivity {
    private EditText userEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        userEmail = findViewById(R.id.etUserEmail);

        String getUserEmail = Login.getEmail;
        userEmail.setText(getUserEmail);
    }
}
