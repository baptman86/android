package com.example.baptiste.smartcity.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.baptiste.smartcity.utils.Function;
import com.example.baptiste.smartcity.R;
import com.example.baptiste.smartcity.activities.LogInActivity;
import com.example.baptiste.smartcity.objects.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class ProfilFragment extends Fragment {

    private static DatabaseReference mDataBase = FirebaseDatabase.getInstance().getReference();
    View profil_view;
    String actual_user_id;
    private User actual_user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        profil_view = inflater.inflate(R.layout.profil,container,false);

        if(actual_user_id != null) { // bug fix en attendant résolution
            updateFields(this.getContext(), actual_user_id);
        }

        profil_view.findViewById(R.id.modify).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            String user_login = ((EditText) profil_view.findViewById(R.id.user_login_profil)).getText().toString();
            String user_password = ((EditText) profil_view.findViewById(R.id.user_password)).getText().toString();
            String user_conf_password = ((EditText) profil_view.findViewById(R.id.user_password_conf)).getText().toString();
            String user_name = ((EditText) profil_view.findViewById(R.id.user_name)).getText().toString();
            String user_surname = ((EditText) profil_view.findViewById(R.id.user_surname)).getText().toString();
            String user_email = ((EditText) profil_view.findViewById(R.id.user_email)).getText().toString();

            if(Function.isValidLogin(getContext(),user_login)) {
                if(Function.isValidPassword(getContext(),user_password)) {
                    if (user_password.equals(user_conf_password)) {
                        String salt = User.generateSalt();
                        User newUser = new User(user_login, User.encrypt(user_password, salt), salt, user_name, user_surname, user_email);
                        mDataBase.child("users").child(actual_user_id).setValue(newUser);
                        actual_user = newUser;
                        Toast.makeText(getContext(), R.string.account_modified, Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(getContext(), R.string.error_conf_password, Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(getContext(), getResources().getString(R.string.invalid_password)+" : ne doit contenir que des caractères alphanumerique et posséder plus de "+Function.LOGIN_MINIMUM_LENGTH+" caractère(s)", Toast.LENGTH_LONG).show();
                }
            }
            else{
                Toast.makeText(getContext(), getResources().getString(R.string.invalid_login)+" : ne doit contenir que des caractères alphanumerique et posséder plus de "+Function.PASSWORD_MINIMUM_LENGTH+" caractère(s)", Toast.LENGTH_LONG).show();
            }
            }
        });

        profil_view.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_login = ((EditText) profil_view.findViewById(R.id.user_login_profil)).getText().toString();
                removeUser(profil_view.getContext(),user_login);

                deleteRefUser();

                Intent intent = new Intent(profil_view.getContext(), LogInActivity.class);
                startActivity(intent);
            }
        });
        return profil_view;
    }

    @Override
    public void onAttach(Context context) {
        actual_user_id = getActivity().getIntent().getExtras().getString(getResources().getString(R.string.connected_user_id));
        super.onAttach(context);
    }

    private void modifyUser(final Context ctx_param, final String user_login_param, final String user_password_param, final String user_conf_password_param, final String user_name_param, final String user_surname_param, final String user_email_param){
        ValueEventListener vel = new ValueEventListener() {

            private Context ctx = ctx_param;
            private String user_login= user_login_param;
            private String user_password = user_password_param;
            private String user_conf_password = user_conf_password_param;
            private String user_name = user_name_param;
            private String user_surname = user_surname_param;
            private String user_email = user_email_param;

            private User user;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = null;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user_found = snapshot.getValue(User.class);
                    if(user_found != null && snapshot.getKey().equals(actual_user_id)){
                        user = user_found;
                    }
                }
                /*mDataBase.child("users").removeEventListener(this);

                if(user != null){
                    Toast.makeText(ctx,R.string.error_user_already_exist,Toast.LENGTH_LONG).show();
                }
                else {
                    if(Function.isValidLogin(ctx,user_login)) {
                        if(Function.isValidPassword(ctx,user_conf_password)) {
                            if (user_password.equals(user_conf_password)) {
                                String salt = User.generateSalt();
                                mDataBase.child("users").push().setValue(new User(user_login, User.encrypt(user_password, salt), salt, user_name, user_surname, user_email));
                                removeUser(ctx,user_login);
                            } else {
                                Toast.makeText(ctx, R.string.error_conf_password, Toast.LENGTH_LONG).show();
                            }
                            Toast.makeText(ctx, R.string.account_created, Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(ctx, R.string.invalid_password+" : ne doit contenir que des caractères alphanumerique et posséder plus de "+ctx.getResources().getInteger(R.integer.LOGIN_MINIMUM_LENGTH)+" caractère(s)", Toast.LENGTH_LONG).show();
                        }
                    }
                    else{
                        Toast.makeText(ctx, R.string.invalid_login+" : ne doit contenir que des caractères alphanumerique et posséder plus de "+ctx.getResources().getInteger(R.integer.PASSWORD_MINIMUM_LENGTH)+" caractère(s)", Toast.LENGTH_LONG).show();
                    }

                }*/

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.d("test", "error");
            }
        };
        mDataBase.child("users").addValueEventListener(vel);
    }

    private void updateFields(final Context ctx_param, final String user_id_param) {

        ValueEventListener vel = new ValueEventListener() {

            private Context ctx = ctx_param;
            private String user_id = user_id_param;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                actual_user = null;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user_found = snapshot.getValue(User.class);
                    if(user_found != null && snapshot.getKey().equals(user_id)){
                        actual_user = user_found;
                    }
                }
                ((EditText) profil_view.findViewById(R.id.user_login_profil)).setText(actual_user.getIdentifiant());
                ((EditText) profil_view.findViewById(R.id.user_name)).setText(actual_user.getName());
                ((EditText) profil_view.findViewById(R.id.user_surname)).setText(actual_user.getSurname());
                ((EditText) profil_view.findViewById(R.id.user_email)).setText(actual_user.getEmail());
                mDataBase.child("users").removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ctx, R.string.error_get_user, Toast.LENGTH_LONG).show();
                mDataBase.child("users").removeEventListener(this);
            }
        };
        mDataBase.child("users").addValueEventListener(vel);
    }

    private void removeUser(final Context ctx_param, final String user_login_param) {
        ValueEventListener vel = new ValueEventListener() {

            private Context ctx = ctx_param;
            private String user_login = user_login_param;

            private DatabaseReference user_ref;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user_ref = null;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user_found = snapshot.getValue(User.class);
                    if(user_found != null && user_found.getIdentifiant().equals(user_login)){
                        user_ref = snapshot.getRef();
                    }
                }
                mDataBase.child("users").removeEventListener(this);
                user_ref.removeValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ctx, R.string.error_delete_user, Toast.LENGTH_LONG).show();
                mDataBase.child("users").removeEventListener(this);
            }
        };
        mDataBase.child("users").addValueEventListener(vel);
    }

    private void deleteRefUser(){
        getActivity().getIntent().removeExtra("user_id");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(profil_view.getContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(profil_view.getContext().getResources().getString(R.string.storedLogin));
        editor.remove(profil_view.getContext().getResources().getString(R.string.storedPassword));
        editor.apply();
    }
}
