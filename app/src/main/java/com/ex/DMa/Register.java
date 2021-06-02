package com.ex.Pocket_Data;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    private static final String TAG = "Register";
    EditText TheEmail, ThePassword,TheResetPass, TheFullName;
    Button TheSave;

    private String userID;

    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;

    SharedPreferences sharedPreferences;
    public static final String SHARED_PREF_NAME = "users";
    public static final String KEY_NAME = "fullName";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASS = "password";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        TheEmail = findViewById(R.id.Email);
        ThePassword = findViewById(R.id.Password);
        TheResetPass = findViewById(R.id.resetPassword);
        TheFullName = findViewById(R.id.Fullname);

        TheSave = findViewById(R.id.Save);

        TextView LoginPage = (TextView) findViewById(R.id.Loginpage);
        LoginPage.setOnClickListener(v -> BackToLogin());

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        TheSave.setOnClickListener(v -> {

            String ThisEmail = TheEmail.getText().toString();
            String ThisPassword = ThePassword.getText().toString();
            String ThisRepeatPass = TheResetPass.getText().toString();
            String ThisFullName = TheFullName.getText().toString();

            if (ThisFullName.isEmpty()) {
                TheFullName.setError("Full name is requested");
                TheFullName.requestFocus();
            } else if (ThisEmail.isEmpty()) {
                TheEmail.setError("Email is requested");
                TheEmail.requestFocus();
            } else if (ThisPassword.isEmpty()) {
                ThePassword.setError("Password is requested");
                ThePassword.requestFocus();
            } else if (ThisRepeatPass.isEmpty()) {
                TheResetPass.setError("Password is requested");
                TheResetPass.requestFocus();
            } else if (ThisPassword.length() < 6) {
                ThePassword.setError("6 or more");
            } else if (!ThisRepeatPass.equals(ThisPassword)) {
                TheResetPass.setError("Repeat the password");
            } else {
                mAuth.createUserWithEmailAndPassword(ThisEmail, ThisPassword).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        // Saving information in FirebaseFireStore
                        FirebaseUser user = mAuth.getCurrentUser();
                        userID = mAuth.getCurrentUser().getUid();

                        DocumentReference documentReference = fStore.collection(SHARED_PREF_NAME).document(userID);
                        Map<String,Object> user2 = new HashMap<>();
                        user2.put(KEY_NAME, ThisFullName);
                        user2.put(KEY_EMAIL, ThisEmail);
                        user2.put(KEY_PASS, ThisPassword);

                        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(KEY_NAME,ThisFullName);
                        editor.putString(KEY_EMAIL,ThisEmail);
                        editor.apply();
                        finish();

                        documentReference.set(user2).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d(TAG, "User is created for "+ userID);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                Toast.makeText(Register.this, "User wasn't created", Toast.LENGTH_LONG).show();
                            }
                        });


                        user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(Register.this, "Verification is required", Toast.LENGTH_SHORT).show();
                                TheEmail.setText("");
                                ThePassword.setText("");
                                TheFullName.setText("");
                                TheResetPass.setText("");

                                BackToLogin();
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                Toast.makeText(Register.this, "The email could not be sent", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else{
                        Toast.makeText(Register.this,"Failed to register", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });


    }

    public void BackToLogin() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}