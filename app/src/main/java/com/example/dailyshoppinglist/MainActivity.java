package com.example.dailyshoppinglist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText email_login;
    private EditText password_login;
    private TextView signUp;
    private Button btnLogin;

    private FirebaseAuth mAuth;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mDialog = new ProgressDialog(this);

        email_login = findViewById(R.id.email_login);
        password_login = findViewById(R.id.password_login);

        btnLogin = findViewById(R.id.btn_login);
        signUp = findViewById(R.id.signup_txt);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mEmail = email_login.getText().toString().trim();
                String mPass = password_login.getText().toString().trim();

                if(TextUtils.isEmpty(mEmail)){
                    email_login.setError("Email Required");
                    return;
                }
                if(TextUtils.isEmpty(mPass)){
                    password_login.setError("Password Required");
                    return;
                }

                mDialog.setMessage("Processing..");
                mDialog.show();

                mAuth.signInWithEmailAndPassword(mEmail,mPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            startActivity(new Intent(getApplicationContext(),Home.class));
                            Toast.makeText(getApplicationContext(), "Successful", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                        }
                        mDialog.dismiss();
                    }
                });
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
            }
        });
    }
}