package com.kontrakanelite.test;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Registration extends AppCompatActivity {

    private Button btnRegist;
    private EditText Email;
    private EditText Password,ConfirmPassword;
    private ImageView Login;
    private RelativeLayout loading;

    DatabaseReference databaseRef;

    List<User> userList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        databaseRef = FirebaseDatabase.getInstance().getReference("user");

        //init views
        Email = findViewById(R.id.etEmailReg);
        Password = findViewById(R.id.etPasswordReg);
        ConfirmPassword = findViewById(R.id.etRePasswordReg);
        Login = findViewById(R.id.backToLogin);
        btnRegist = findViewById(R.id.btnRegis);
        loading = findViewById(R.id.loading_regist);

        //event listener
        btnRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);
                Email.setEnabled(false);
                Password.setEnabled(false);
                ConfirmPassword.setEnabled(false);
                Login.setEnabled(false);
                btnRegist.setEnabled(false);
                SignUp();
            }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Login.class);
                startActivity(intent);
            }
        });
    }

    private void SignUp(){
        String email = Email.getText().toString().trim();
        String pass = Password.getText().toString().trim();
        String confirmPass = ConfirmPassword.getText().toString().trim();

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(pass)&&pass.length()>5&&email.matches(emailPattern)&&pass.equals(confirmPass)){
            cekEmail();
        }else{
            if (TextUtils.isEmpty(email)&&!TextUtils.isEmpty(pass)){
                Toast.makeText(this, "Email must be filled!",Toast.LENGTH_SHORT).show();
            }else if (TextUtils.isEmpty(pass)&&!TextUtils.isEmpty(email)){
                Toast.makeText(this, "Password must be filled!",Toast.LENGTH_SHORT).show();
            }else if(TextUtils.isEmpty(email)&&TextUtils.isEmpty(pass)){
                Toast.makeText(this, "Please fill the email and password fields!",Toast.LENGTH_SHORT).show();
            }else if (pass.length()<6){
                Toast.makeText(this, "Password must have at least 6 characters",Toast.LENGTH_SHORT).show();
            }else if (!confirmPass.equals(pass)){
                Toast.makeText(this, "Please re-type password correctly!",Toast.LENGTH_SHORT).show();
            }else if(!email.matches(emailPattern)){
                Toast.makeText(this, "email format : example@domain.com",Toast.LENGTH_SHORT).show();
            }

            loading.setVisibility(View.GONE);
            Email.setEnabled(true);
            Password.setEnabled(true);
            ConfirmPassword.setEnabled(true);
            Login.setEnabled(true);
            btnRegist.setEnabled(true);
        }
    }

    private void cekEmail(){
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                String email = Email.getText().toString().trim();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()){
                    User user = userSnapshot.getValue(User.class);
                    if (user.getEmail().equals(email)){
                        userList.add(user);
                    }
                }
                if (userList.size()>0){
                    addUser(false);
                }else{
                    addUser(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addUser(boolean status){
        String email = Email.getText().toString().trim();
        String pass = Password.getText().toString().trim();

        if (status==true){
            String id = databaseRef.push().getKey();

            User user = new User(id,email,pass);

            databaseRef.child(id).setValue(user);

            Email.getText().clear();
            Password.getText().clear();
            ConfirmPassword.getText().clear();

            closeKeyboard();
            Toast.makeText(this,"Registration success!",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"Email have been used! Use another email!",Toast.LENGTH_SHORT).show();
        }

        loading.setVisibility(View.GONE);
        Email.setEnabled(true);
        Password.setEnabled(true);
        ConfirmPassword.setEnabled(true);
        Login.setEnabled(true);
        btnRegist.setEnabled(true);
    }

    private void closeKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }
}
