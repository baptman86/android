package com.example.baptiste.smartcity.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.example.baptiste.smartcity.R;
import com.example.baptiste.smartcity.bdd.UsersBDD;
import com.example.baptiste.smartcity.object.User;

@SuppressLint("Registered")
public class SignInActivity extends AppCompatActivity {

    static UsersBDD usersBDD;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        usersBDD = new UsersBDD(this);
        usersBDD.open();

        setContentView(R.layout.activity_sign_in);
        findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_login = ((EditText)findViewById(R.id.User_login)).getText().toString();
                User actualUser = usersBDD.getUserWithLogin(user_login);
                if(actualUser != null){
                    Toast.makeText(view.getContext(),R.string.error_user_already_exist,Toast.LENGTH_LONG).show();
                }
                else {
                    String user_password = ((EditText)findViewById(R.id.User_password)).getText().toString();
                    String user_conf_password = ((EditText)findViewById(R.id.User_conf_password)).getText().toString();
                    if (user_password.equals(user_conf_password)) {
                        String user_name = ((EditText) findViewById(R.id.User_name)).getText().toString();
                        String user_surname = ((EditText) findViewById(R.id.User_surname)).getText().toString();
                        String user_email = ((EditText) findViewById(R.id.User_email)).getText().toString();
                        usersBDD.addUser(user_login, user_password, user_name, user_surname, user_email);
                    }
                    else {
                        Toast.makeText(view.getContext(), R.string.error_conf_password, Toast.LENGTH_LONG).show();
                    }
                }
                Toast.makeText(view.getContext(),R.string.account_created,Toast.LENGTH_LONG).show();
                goToMain(view.getContext(),usersBDD.getUserWithLogin(user_login));
            }
        });
    }

    private void goToMain(Context ctx, User user){
        Intent intent = new Intent(ctx, MainActivity.class);
        intent.putExtra("user_info", user);
        startActivity(intent);
    }

    protected void OnDestroy(){
        super.onDestroy();
        //usersBDD.close();
    }
}
