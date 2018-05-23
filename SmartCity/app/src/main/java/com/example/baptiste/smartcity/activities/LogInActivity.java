package com.example.baptiste.smartcity.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.baptiste.smartcity.R;
import com.example.baptiste.smartcity.objects.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LogInActivity extends AppCompatActivity {

    private final int MY_PERMISSION_REQUEST_LOCATION = 1234;

    private static DatabaseReference mDataBase = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSION_REQUEST_LOCATION);
        }
        else {
            launch();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_LOCATION: {
                launch();
                break;
            }
        }
    }

    private void launch (){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        String user_login = prefs.getString(getResources().getString(R.string.storedLogin),null);
        String user_password = prefs.getString(getResources().getString(R.string.storedPassword),null);


        if(user_login!=null && user_password!=null) {
            directLogIn(this,user_login,user_password);
        }

        setContentView(R.layout.activity_log_in);
        findViewById(R.id.log_in).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_login = ((EditText)findViewById(R.id.User_login)).getText().toString();
                String user_password = ((EditText)findViewById(R.id.User_password)).getText().toString();
                Boolean store_password = ((CheckBox)findViewById(R.id.storePassword)).isChecked();

                logIn(view.getContext(),user_login,user_password,store_password);
            }
        });

        findViewById(R.id.sign_in).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), SignInActivity.class);
                startActivity(intent);
            }
        });
    }

    private void directLogIn(final Context ctx_param,final String user_login_param,final String user_password_param){
        ValueEventListener vel = new ValueEventListener() {

            private Context ctx = ctx_param;
            private String user_login= user_login_param;
            private String user_password = user_password_param;

            private User user;
            private String key;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = null;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user_found = snapshot.getValue(User.class);
                    if(user_found != null && user_found.getLogin().equals(user_login)){
                        user = user_found;
                        key = snapshot.getKey();
                    }
                }
                mDataBase.child("users").removeEventListener(this);

                if(user != null && User.testPassword(user,user_password)){
                    Toast.makeText(ctx, getResources().getString(R.string.welcome) + " " + user.getName() + " " + user.getSurname(), Toast.LENGTH_LONG).show();
                    goToMain(ctx,key);
                }
                else{
                    Toast.makeText(ctx,getResources().getString(R.string.error_wrong_credentials),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                mDataBase.child("users").removeEventListener(this);
            }
        };
        mDataBase.child("users").addValueEventListener(vel);
    }

    private void logIn(final Context ctx_param,final String user_login_param,final String user_password_param, final Boolean store_password_param){
        ValueEventListener vel = new ValueEventListener() {

            private Context ctx = ctx_param;
            private String user_login= user_login_param;
            private String user_password = user_password_param;
            private Boolean store_password = store_password_param;

            private User user;
            private String key;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = null;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user_found = snapshot.getValue(User.class);
                    if(user_found != null && user_found.getLogin().equals(user_login)){
                        user = user_found;
                        key = snapshot.getKey();
                    }
                }
                mDataBase.child("users").removeEventListener(this);

                if(user != null && User.testPassword(user,user_password)){
                    if(store_password){
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString(getResources().getString(R.string.storedLogin), user_login);
                        editor.putString(getResources().getString(R.string.storedPassword), user_password);
                        editor.apply();
                    }
                    Toast.makeText(ctx, getResources().getString(R.string.welcome) + " " + user.getName() + " " + user.getSurname(), Toast.LENGTH_LONG).show();
                    goToMain(ctx,key);
                }
                else{
                    Toast.makeText(ctx,getResources().getString(R.string.error_wrong_credentials),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(ctx,getResources().getString(R.string.error_wrong_credentials),Toast.LENGTH_LONG).show();
                mDataBase.child("users").removeEventListener(this);
            }
        };
        mDataBase.child("users").addValueEventListener(vel);
    }

    private void goToMain(Context ctx, String user_id){
        Intent intent = new Intent(ctx, MainActivity.class);
        intent.putExtra(getResources().getString(R.string.connected_user_id), user_id);
        startActivity(intent);
    }
}
