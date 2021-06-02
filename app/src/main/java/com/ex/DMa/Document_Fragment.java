package com.ex.Pocket_Data;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Document_Fragment extends Fragment {

    View v;
    Button PDF_Page;


    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        v = inflater.inflate(R.layout.fragment_document_, container, false);

        PDF_Page = (Button) v.findViewById(R.id.PDF_LOG);

        PDF_Page.setOnClickListener(v -> openPDF());

        return v;
    }

    public void openPDF(){
        Intent i = new Intent(requireActivity(),PDF.class);
        requireActivity().startActivity(i);
    }
}