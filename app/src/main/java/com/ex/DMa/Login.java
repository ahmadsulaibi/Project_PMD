package com.ex.Pocket_Data;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

public class Login extends AppCompatActivity {

    Button log, reg;
    EditText email, pass;
    TextView forgot_pass;
    CheckBox remember;

    SharedPreferences preferences;

    FirebaseAuth mAuth;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Login
        email = (EditText) findViewById(R.id.Email);
        pass = (EditText) findViewById(R.id.Password);
        log = (Button) findViewById(R.id.Sign);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        log.setOnClickListener(v -> loginUser());

        //Register
        reg = (Button) findViewById(R.id.Register);
        reg.setOnClickListener(v1 -> openReg());

        //forgot password
        forgot_pass = (TextView) findViewById(R.id.Forgot_Password);
        forgot_pass.setOnClickListener(v1 -> openPass());

        // CheckBox
        remember = (CheckBox) findViewById(R.id.Remember);
        remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(compoundButton.isChecked()){

                    SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember", "true");
                    editor.apply();

                    Toast.makeText(Login.this, "Checked", Toast.LENGTH_SHORT).show();

                }else if(!compoundButton.isChecked()) {
                    SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember", "false");
                    editor.apply();
                    Toast.makeText(Login.this, "Unchecked", Toast.LENGTH_SHORT).show();
                }
            }
        });
        SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
        String checkbox = preferences.getString("remember","");
        if(checkbox.equals("true")){
            Intent intent = new Intent(Login.this, MainPage.class);
            startActivity(intent);
        }else {
            //
        }
    }

    // Login
    public void loginUser(){
        String ThisEmail = email.getText().toString();
        String ThisPassword = pass.getText().toString();

        if (ThisEmail.isEmpty()) {
            email.setError("Email is requested");
            email.requestFocus();
        } else if (ThisPassword.isEmpty()) {
            pass.setError("Password is requested");
            pass.requestFocus();
        }
        else{
            mAuth.signInWithEmailAndPassword(ThisEmail,ThisPassword).addOnCompleteListener(task -> {
                if(task.isSuccessful() && !user.isEmailVerified()){

                    remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if(remember.isChecked()){
                                SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("remember", "true");
                                editor.apply();
                                Toast.makeText(Login.this,"Checked", Toast.LENGTH_SHORT).show();
                            }else if(!remember.isChecked()){
                                SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("remember", "false");
                                editor.apply();
                                Toast.makeText(Login.this,"UnChecked", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(Login.this,"Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getBaseContext(),"Verification has been sent to your mail, please check.", Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            Log.d("tag","Email not sent"+ e.getMessage());
                        }
                    });
                }
                else if(task.isSuccessful() && user.isEmailVerified()){
                    NextPage();
                }
                else if(!task.isSuccessful()){
                    Toast.makeText(this,"Unsuccessful Login. Check your email or password", Toast.LENGTH_SHORT).show();
                }
                else{
                    //
                }
            });
        }
    }

    //Register
    public void openReg() {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

    //Forgot Password
    public void openPass(){
         Intent intent = new Intent(this, Forgot_password.class);
         startActivity(intent);
    }

    //After Login
    public void NextPage(){
        Intent intent = new Intent(this, MainPage.class);
        startActivity(intent);
    }

}
