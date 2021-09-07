package com.example.sreevarsh.koach_ai_assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    EditText mUname, mEmail, mPassword, mRepass;
    Button mRegisterBtn;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    DatabaseReference databaseUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mUname = (EditText) findViewById(R.id.name);
        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);
        mRepass = (EditText) findViewById(R.id.reenterpassword);
        mRegisterBtn = (Button) findViewById(R.id.registerbtn);
        mLoginBtn = (TextView) findViewById(R.id.createText);

        fAuth = FirebaseAuth.getInstance();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        databaseUsers= FirebaseDatabase.getInstance().getReference();

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = mUname.getText().toString().trim();
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String repass = mRepass.getText().toString().trim();

                if(TextUtils.isEmpty(username)){
                    mUname.setError("Username is required.");
                    return;
                }
                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is required.");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password is required.");
                    return;
                }
                if(password.length() < 6){
                    mPassword.setError("Password can't be less than 6 characters.");
                    return;
                }
                if(TextUtils.isEmpty(repass)){
                    mRepass.setError("Please re enter the password.");
                    return;
                }
                if(!password.equals(repass)){
                    mRepass.setError("Passwords do not match. Please re enter the password.");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            Toast.makeText(Register.this, "User registration successful", Toast.LENGTH_SHORT).show();
                            addUserIDToDatabase();
                            Intent i = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(i);
                        }else {
                            Toast.makeText(Register.this, "ERROR ! "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),Login.class);
                startActivity(i);
            }
        });
    }

    protected void addUserIDToDatabase(){
        String uid=fAuth.getUid();
        String uname = mUname.getText().toString().trim();
        String email = mEmail.getText().toString().trim();
        RegisterClassDB rcdb =new RegisterClassDB(uid,uname,email);
        databaseUsers.child(uid).setValue(rcdb);
    }
}