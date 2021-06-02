package com.ex.Pocket_Data;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

public class Forgot_password extends AppCompatActivity {

    private EditText EmailTxt;
    private Button resetPassword;
    private ProgressBar progressBar;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        EmailTxt = (EditText) findViewById(R.id.Email);
        resetPassword = (Button) findViewById(R.id.resetPassword);

        mAuth = FirebaseAuth.getInstance();

        resetPassword.setOnClickListener(v -> openReset());
    }

    public void openReset(){
        String email = EmailTxt.getText().toString().trim();

        if(email.isEmpty()){
            EmailTxt.setError("Email is required!");
            EmailTxt.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            EmailTxt.setError("Please provide a valid email!");
            EmailTxt.requestFocus();
            return;
        }

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Forgot_password.this, "Verification email sent. Check your Email!", Toast.LENGTH_LONG).show();

                }else{
                    Toast.makeText(Forgot_password.this, "Something went wrong, try again!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}