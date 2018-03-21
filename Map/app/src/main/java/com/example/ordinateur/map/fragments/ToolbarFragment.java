package com.example.ordinateur.map.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ordinateur.map.R;
import com.example.ordinateur.map.activities.LogInActivity;
import com.example.ordinateur.map.activities.MapsActivity;
import com.example.ordinateur.map.object.User;

public class ToolbarFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.toolbar, container, false);

        v.findViewById(R.id.imageButton_profil).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = getActivity().getIntent().getParcelableExtra(getResources().getString(R.string.connected_user));
                if(user == null){
                    resetPref(view.getContext());
                    Intent intent = new Intent(view.getContext(), LogInActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(view.getContext(), "accés au profil de "+ user.getName()+ " "+ user.getSurname(), Toast.LENGTH_LONG).show();
                    /*Intent intent = new Intent(view.getContext(), SignInActivity.class);
                    propagateUser(intent);
                    startActivity(intent);*/
                }
            }
        });

        v.findViewById(R.id.button_accueil).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MapsActivity.class);
                propagateUser(intent);
                startActivity(intent);
            }
        });

        v.findViewById(R.id.button_pro).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),"accés à la page de gestion des commerces/news",Toast.LENGTH_LONG).show();
                /*Intent intent = new Intent(view.getContext(), SignInActivity.class);
                propagateUser(intent);
                startActivity(intent);*/
            }
        });

        v.findViewById(R.id.button_disconnect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPref(view.getContext());
                Intent intent = new Intent(view.getContext(), LogInActivity.class);
                startActivity(intent);
            }
        });
        return v;
    }

    private void resetPref(Context ctx){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(getResources().getString(R.string.storedLogin), null);
        editor.putString(getResources().getString(R.string.storedPassword), null);
        editor.apply();
    }

    //meilleure méthode ?
    private void propagateUser(Intent intent){
        User user = getActivity().getIntent().getParcelableExtra(getResources().getString(R.string.connected_user));
        intent.putExtra(getResources().getString(R.string.connected_user),user);
    }
}
