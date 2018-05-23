package com.example.baptiste.smartcity.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.baptiste.smartcity.R;
import com.example.baptiste.smartcity.adapters.ListAnnoncesAdapter;
import com.example.baptiste.smartcity.adapters.ListNewsAdapter;
import com.example.baptiste.smartcity.objects.Annonce;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class AnnoncesFragment extends Fragment {

    private static DatabaseReference mDataBase = FirebaseDatabase.getInstance().getReference();


    ListView listAnn;
    LinearLayout formAnn;

    Button consultAnnButton;
    Button createAnnButton;
    Button setToLocButton;
    Button publishAnnButton;

    EditText nameAnn;
    EditText descAnn;
    EditText lieuAnn;
    EditText priceAnn;
    EditText dateDebAnn;
    EditText dateFinAnn;

    public static final String KEY_TITLE = "nom";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_DATE_DEBUT = "date_debut";
    public static final String KEY_DATE_FIN = "date_fin";
    public static final String KEY_PRIX = "prix";
    public static final String KEY_LIEU = "lieu";

    View v;

    ArrayList<HashMap<String, String>> dataList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_annonces, container, false);

        listAnn = v.findViewById(R.id.listAnn);
        formAnn = v.findViewById(R.id.formAnn);

        consultAnnButton = v.findViewById(R.id.consultAnn);
        createAnnButton = v.findViewById(R.id.createAnn);
        setToLocButton = v.findViewById(R.id.setCurrLocAnn);
        publishAnnButton = v.findViewById(R.id.publishAnn);

        nameAnn = v.findViewById(R.id.nameAnn);
        descAnn = v.findViewById(R.id.descAnn);
        lieuAnn = v.findViewById(R.id.lieuAnn);
        priceAnn = v.findViewById(R.id.priceAnn);
        dateDebAnn = v.findViewById(R.id.dateDebAnn);
        dateFinAnn = v.findViewById(R.id.dateFinAnn);

        generateListAnn();

        consultAnnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateListAnn();
                listAnn.setVisibility(View.VISIBLE);
                formAnn.setVisibility(View.INVISIBLE);
            }
        });

        createAnnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listAnn.setVisibility(View.INVISIBLE);
                formAnn.setVisibility(View.VISIBLE);
            }
        });

        setToLocButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lieuAnn.setText("here");
            }
        });

        publishAnnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDataBase.child("annonces").push().setValue(new Annonce(nameAnn.getText().toString(),descAnn.getText().toString(),dateDebAnn.getText().toString(),dateFinAnn.getText().toString(),lieuAnn.getText().toString(),priceAnn.getText().toString()));
                nameAnn.setText("");
                descAnn.setText("");
                lieuAnn.setText("");
                priceAnn.setText("");
                dateDebAnn.setText("");
                dateFinAnn.setText("");
                priceAnn.setText("");
                Toast.makeText(getContext(),getResources().getString(R.string.add_annonce_success),Toast.LENGTH_LONG).show();
            }
        });

        return v;
    }

    public void generateListAnn(){
        dataList = new ArrayList<>();



        //exemple
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(KEY_TITLE, "titre1");
        map.put(KEY_DESCRIPTION, "desc1");
        map.put(KEY_DATE_DEBUT, "14/12/1994");
        map.put(KEY_DATE_FIN, "17/11/1996");
        map.put(KEY_PRIX,"0");
        map.put(KEY_LIEU,"here");
        dataList.add(map);

        ListAnnoncesAdapter adapter = new ListAnnoncesAdapter(AnnoncesFragment.this, dataList);
        listAnn.setAdapter(adapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

}
