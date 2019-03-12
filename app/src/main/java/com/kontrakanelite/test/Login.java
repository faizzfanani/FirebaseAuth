package com.kontrakanelite.test;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {

    private Button Login;
    private EditText Email;
    private EditText Password;
    private TextView Regist;
    private RelativeLayout loading;

    public static final String USER_EMAIL = "user_email";
    public static String getEmail;

    DatabaseReference databaseRef;

    List<User> userList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        databaseRef = FirebaseDatabase.getInstance().getReference("user");

        Email = findViewById(R.id.etEmail);
        Password = findViewById(R.id.etPassword);
        Login = findViewById(R.id.btnLogin);
        Regist = findViewById(R.id.tvToRegist);
        loading = findViewById(R.id.layout_loading);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);
                Email.setEnabled(false);
                Password.setEnabled(false);
                Login.setEnabled(false);
                Regist.setEnabled(false);
                CekInput();
            }
        });

        Regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Registration.class);
                startActivity(intent);
            }
        });
    }

    private void CekInput(){
        String email = Email.getText().toString().trim();
        String pass = Password.getText().toString().trim();

        if (!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(pass)&&pass.length()>5){

            CekLogin();

        }else{
            if (TextUtils.isEmpty(email)&&!TextUtils.isEmpty(pass)){
                Toast.makeText(this, "Email must be filled!",Toast.LENGTH_SHORT).show();
            }else if (TextUtils.isEmpty(pass)&&!TextUtils.isEmpty(email)){
                Toast.makeText(this, "Password must be filled!",Toast.LENGTH_SHORT).show();
            }else if(TextUtils.isEmpty(email)&&TextUtils.isEmpty(pass)){
                Toast.makeText(this, "Please fill the email and password field!",Toast.LENGTH_SHORT).show();
            }else{
                CekLogin();
            }
            loading.setVisibility(View.GONE);
            Email.setEnabled(true);
            Password.setEnabled(true);
            Login.setEnabled(true);
            Regist.setEnabled(true);
        }
    }

    private void CekLogin(){
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                String email = Email.getText().toString().trim();
                String password = Password.getText().toString().trim();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()){
                    User user = userSnapshot.getValue(User.class);
                    if (user.getEmail().equals(email) && user.getPassword().equals(password)){
                        userList.add(user);
                    }
                }

                if (userList.size()>0){
                      SignIn(email);
                }else{
                    SignIn("wrong");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void SignIn(String status){

        if (status.equalsIgnoreCase("wrong")){
            Toast.makeText(this,"Email or Password is wrong!",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this,"Login success!",Toast.LENGTH_LONG).show();
            getEmail = Email.getText().toString();
            Intent intent = new Intent(getApplicationContext(),Home.class);
            intent.putExtra(USER_EMAIL,status);
            startActivity(intent);
            finish();
        }

        loading.setVisibility(View.GONE);
        Email.setEnabled(true);
        Password.setEnabled(true);
        Login.setEnabled(true);
        Regist.setEnabled(true);
    }


}

