package com.yamen.restapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class MainActivity extends AppCompatActivity {

    private EditText etUsername,etPassword;
    private Utilities utils;
    private FirebaseServices fbs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectComponents();
    }

    private void connectComponents() {
        etUsername = findViewById(R.id.etUsernameMain);
        etPassword = findViewById(R.id.etPasswordMain);
        utils = Utilities.getInstance();
        fbs = FirebaseServices.getInstance();
    }

    public void login(View view) {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        if (username.trim().isEmpty() || password.trim().isEmpty())
        {
            Toast.makeText(this, "Some fields are empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!utils.validateEmail(username) || !utils.validatePassword(password))
        {
            Toast.makeText(this, "Incorrect email or password!", Toast.LENGTH_SHORT).show();
            return;
        }

        fbs.getAuth().signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent i = new Intent(MainActivity.this, AllRestActivity.class);
                            startActivity(i);

                        } else {
                            // TODO: what to do if fails
                        }
                    }
                });
    }

    public void gotoSignup(View view) {
        Intent i = new Intent(this, SignupActivity.class);
        startActivity(i);
    }

    public void gotoAddRest(View view) {
        Intent i = new Intent(this, AddRestActivity.class);
        startActivity(i);
    }

    public void gotoAllRests(View view) {
        Intent i = new Intent(this, AllRestActivity.class);
        startActivity(i);
    }
}