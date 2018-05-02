package com.example.baptiste.smartcity.activities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.example.baptiste.smartcity.R;
import com.example.baptiste.smartcity.object.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;


public class SignInActivity extends AppCompatActivity {

    private static final int LOGIN_MINIMUM_LENGTH = 4;
    private static final int PASSWORD_MINIMUM_LENGTH = 4;

    private static DatabaseReference mDataBase = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_in);
        findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_login = ((EditText) findViewById(R.id.User_login)).getText().toString();
                String user_password = ((EditText) findViewById(R.id.User_password)).getText().toString();
                String user_conf_password = ((EditText) findViewById(R.id.User_conf_password)).getText().toString();
                String user_name = ((EditText) findViewById(R.id.User_name)).getText().toString();
                String user_surname = ((EditText) findViewById(R.id.User_surname)).getText().toString();
                String user_email = ((EditText) findViewById(R.id.User_email)).getText().toString();

                signInUser(view.getContext(),user_login,user_password,user_conf_password,user_name,user_surname,user_email);
            }
        });
    }

    private void signInUser(final Context ctx_param,final String user_login_param,final String user_password_param,final String user_conf_password_param,final String user_name_param,final String user_surname_param,final String user_email_param){
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
                    if(user_found != null && user_found.getIdentifiant().equals(user_login)){
                        user = user_found;
                        Log.d("test",user_found.getIdentifiant());
                    }
                }
                mDataBase.child("users").removeEventListener(this);

                if(user != null){
                    Toast.makeText(ctx,R.string.error_user_already_exist,Toast.LENGTH_LONG).show();
                }
                else {
                    if(isValidLogin(user_login)) {
                        if(isValidPassword(user_conf_password)) {
                            if (user_password.equals(user_conf_password)) {
                                String salt = User.generateSalt();
                                mDataBase.child("users").push().setValue(new User(user_login, User.encrypt(user_password, salt), salt, user_name, user_surname, user_email));
                            } else {
                                Toast.makeText(ctx, R.string.error_conf_password, Toast.LENGTH_LONG).show();
                            }
                            Toast.makeText(ctx, R.string.account_created, Toast.LENGTH_LONG).show();
                            goToMain(ctx, user);
                        }
                        else{
                            Toast.makeText(ctx, R.string.invalid_password+" : ne doit contenir que des caractères alphanumerique et posséder plus de "+LOGIN_MINIMUM_LENGTH+" caractère(s)", Toast.LENGTH_LONG).show();
                        }
                    }
                    else{
                        Toast.makeText(ctx, R.string.invalid_login+" : ne doit contenir que des caractères alphanumerique et posséder plus de "+PASSWORD_MINIMUM_LENGTH+" caractère(s)", Toast.LENGTH_LONG).show();
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.d("test", "error");
            }
        };
        mDataBase.child("users").addValueEventListener(vel);
    }

    private Boolean isValidLogin(String user_login){
        Pattern p = Pattern.compile("[^a-zA-Z0-9]");
        boolean hasSpecialChar = p.matcher(user_login).find();

        boolean isTooShort = user_login.length() < LOGIN_MINIMUM_LENGTH;

        return !hasSpecialChar && !isTooShort;
    }

    private Boolean isValidPassword(String user_password){
        Pattern p = Pattern.compile("[^a-zA-Z0-9]");
        boolean hasSpecialChar = p.matcher(user_password).find();

        boolean isTooShort = user_password.length() < PASSWORD_MINIMUM_LENGTH;

        return !hasSpecialChar && !isTooShort;
    }

    private void goToMain(Context ctx, User user) {
        Intent intent = new Intent(ctx, MainActivity.class);
        intent.putExtra("user_info", user);
        startActivity(intent);
    }
}
