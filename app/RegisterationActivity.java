package com.example.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterationActivity extends AppCompatActivity {
    EditText userNameEditText, emailEditText, passwordEditText;
    Button registerButton;
    TextView loginTextView;
    FirebaseAuth firebaseAuth;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);
        userNameEditText = findViewById(R.id.username_edit_texit);
        emailEditText =findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.pasword_edit_text);
        loginTextView = findViewById(R.id.login_text_view);
        registerButton = findViewById(R.id.Register_button);
        firebaseAuth = FirebaseAuth.getInstance();
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String userName = userNameEditText.getText().toString();
                final String email = emailEditText.getText().toString();
                final String password = passwordEditText.getText().toString();
                if(userName.equals("") || email.equals("") || password.equals("")){
                    Toast.makeText(RegisterationActivity.this, "Invalid!", Toast.LENGTH_SHORT).show();
                }
                else if(password.length() <= 6){
                    Toast.makeText(RegisterationActivity.this, "Password length should be greater than 6"+email, Toast.LENGTH_SHORT).show();
                }
                else {

                    firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(RegisterationActivity.this, "Already have an account please login", Toast.LENGTH_SHORT).show();
                                userNameEditText.setText("");
                                emailEditText.setText("");
                                passwordEditText.setText("");
                                firebaseAuth.getInstance().signOut();

                            }
                            else{

                                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()) {
                                            Toast.makeText(RegisterationActivity.this, "User created" + email, Toast.LENGTH_SHORT).show();
                                           UserModel userModel = new UserModel();
                                            userModel.setUserName(userName);
                                            userModel.setEmail(email);
                                            userModel.setPassword(password);
                                            ref.push().setValue(userModel);

                                            userNameEditText.setText("");
                                            emailEditText.setText("");
                                            passwordEditText.setText("");


                                            Intent intent = new Intent(RegisterationActivity.this, HomeActivity.class);
                                            startActivity(intent); }
                                        else{

                                            Toast.makeText(RegisterationActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    });

                }
            }
        });
        loginTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Intent intent = new Intent(RegisterationActivity.this, LoginActivity.class);
                startActivity(intent);
                return false;
            }
        });


    }
        }







