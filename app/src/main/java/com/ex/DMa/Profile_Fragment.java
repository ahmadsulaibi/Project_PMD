package com.ex.Pocket_Data;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;


public class Profile_Fragment extends Fragment {

    View v;

    private TextView nameText,emailText;

    private FirebaseFirestore fStore;
    private FirebaseAuth mAuth;
    private String userID;

    SharedPreferences preferences;


    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        v = inflater.inflate(R.layout.fragment_profile_, container, false);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        // Logout Button
        Button logout_But = v.findViewById(R.id.Logout);
        logout_But.setOnClickListener(v1 -> {
            // Unchecking and logout
            preferences = getActivity().getSharedPreferences("checkbox", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("remember", "false");
            editor.apply();
            getActivity().finish();

            SharedPreferences pref = getActivity().getSharedPreferences(Register.SHARED_PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor_del = pref.edit();
            editor_del.clear();
            editor_del.apply();
            getActivity().finish();


            FirebaseAuth.getInstance().signOut();
            Intent i = new Intent(getActivity(), MainActivity.class);
            getActivity().startActivity(i);
        });

        emailText = v.findViewById(R.id.Email_Text);
        nameText = v.findViewById(R.id.fullName_Text);

        DocumentReference documentReference = fStore.collection(Register.SHARED_PREF_NAME).document(userID);

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    if(documentSnapshot.exists()){

                        String name = documentSnapshot.getString(Register.KEY_NAME);
                        String email = documentSnapshot.getString(Register.KEY_EMAIL);


                        nameText.setText("Name: "+name);
                        emailText.setText("Email: "+email);


                    }else{
                        Toast.makeText(getActivity(), "Document doesn't exist", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getActivity(),"Document doesn't exist", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(getActivity(),"Error!", Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }

}