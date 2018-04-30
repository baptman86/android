package com.example.baptiste.smartcity.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.baptiste.smartcity.R;
import com.example.baptiste.smartcity.bdd.UsersBDD;
import com.example.baptiste.smartcity.object.User;

public class LogInActivity extends AppCompatActivity {

    private static UsersBDD usersBDD;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        usersBDD = new UsersBDD(this);
        usersBDD.open();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        String user_login = prefs.getString(getResources().getString(R.string.storedLogin),null);
        String user_password = prefs.getString(getResources().getString(R.string.storedPassword),null);
        if(user_login!=null && user_password!=null) {
            User user = usersBDD.getUserWithLogin(user_login);
            if(user != null && UsersBDD.testPassword(user,user_password)){
                Toast.makeText(this, getResources().getString(R.string.welcome) + " " + user.getName() + " " + user.getSurname(), Toast.LENGTH_LONG).show();
                goToMain(this,user);
            }
            else{
                Toast.makeText(this,getResources().getString(R.string.error_wrong_credentials),Toast.LENGTH_LONG).show();
            }
        }

        setContentView(R.layout.activity_log_in);
        findViewById(R.id.log_in).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_login = ((EditText)findViewById(R.id.User_login)).getText().toString();
                String user_password = ((EditText)findViewById(R.id.User_password)).getText().toString();
                User user = usersBDD.getUserWithLogin(user_login);
                if(user != null && UsersBDD.testPassword(user,user_password)){
                    Boolean store_password = ((CheckBox)findViewById(R.id.storePassword)).isChecked();
                    if(store_password){
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(view.getContext());
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString(getResources().getString(R.string.storedLogin), user_login);
                        editor.putString(getResources().getString(R.string.storedPassword), user_password);
                        editor.apply();
                    }
                    Toast.makeText(view.getContext(), getResources().getString(R.string.welcome) + " " + user.getName() + " " + user.getSurname(), Toast.LENGTH_LONG).show();
                    goToMain(view.getContext(),user);
                }
                else{
                    Toast.makeText(view.getContext(),getResources().getString(R.string.error_wrong_credentials),Toast.LENGTH_LONG).show();
                }
            }
        });

        findViewById(R.id.sign_in).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), SignInActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.remove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), RemoveActivity.class);
                startActivity(intent);
            }
        });
    }

    private void goToMain(Context ctx, User user){
        Intent intent = new Intent(ctx, MainActivity.class);
        intent.putExtra(getResources().getString(R.string.connected_user), user);
        startActivity(intent);
    }

    protected void OnDestroy(){
        super.onDestroy();
        usersBDD.close();
    }
}
