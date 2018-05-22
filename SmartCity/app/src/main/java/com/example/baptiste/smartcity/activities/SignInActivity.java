package com.example.baptiste.smartcity.activities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.example.baptiste.smartcity.utils.Function;
import com.example.baptiste.smartcity.R;
import com.example.baptiste.smartcity.objects.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class SignInActivity extends AppCompatActivity {

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
            private String key;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = null;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user_found = snapshot.getValue(User.class);
                    if(user_found != null && user_found.getIdentifiant().equals(user_login)){
                        user = user_found;
                        key = snapshot.getKey();
                    }
                }
                mDataBase.child("users").removeEventListener(this);

                if(user != null){
                    Toast.makeText(ctx,R.string.error_user_already_exist,Toast.LENGTH_LONG).show();
                }
                else {
                    if(Function.isValidLogin(ctx,user_login)) {
                        if(Function.isValidPassword(ctx,user_password)) {
                            if (user_password.equals(user_conf_password)) {
                                String salt = User.generateSalt();
                                mDataBase.child("users").push().setValue(new User(user_login, User.encrypt(user_password, salt), salt, user_name, user_surname, user_email));
                            } else {
                                Toast.makeText(ctx, R.string.error_conf_password, Toast.LENGTH_LONG).show();
                            }
                            Toast.makeText(ctx, R.string.account_created, Toast.LENGTH_LONG).show();
                            goToMain(ctx, key);
                        }
                        else{
                            Toast.makeText(ctx, ctx.getResources().getString(R.string.invalid_password)+" : ne doit contenir que des caractères alphanumerique et posséder plus de "+Function.LOGIN_MINIMUM_LENGTH+" caractère(s)", Toast.LENGTH_LONG).show();
                        }
                    }
                    else{
                        Toast.makeText(ctx, ctx.getResources().getString(R.string.invalid_login)+" : ne doit contenir que des caractères alphanumerique et posséder plus de "+Function.PASSWORD_MINIMUM_LENGTH+" caractère(s)", Toast.LENGTH_LONG).show();
                    }

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

    private void goToMain(Context ctx, String user_id) {
        Intent intent = new Intent(ctx, MainActivity.class);
        intent.putExtra(getResources().getString(R.string.connected_user_id), user_id);
        startActivity(intent);
    }
}
