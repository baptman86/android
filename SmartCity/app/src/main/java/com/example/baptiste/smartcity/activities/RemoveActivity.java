package com.example.baptiste.smartcity.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baptiste.smartcity.R;
import com.example.baptiste.smartcity.bdd.UsersBDD;
import com.example.baptiste.smartcity.object.User;

import java.util.List;

public class RemoveActivity extends Activity {

    static UsersBDD usersBDD;
    private static final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        usersBDD = new UsersBDD(this);
        usersBDD.open();
        setContentView(R.layout.activity_remove);
        findViewById(R.id.confirmDeleteUser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_name = ((EditText) findViewById(R.id.User_login)).getText().toString();
                User user = usersBDD.getUserWithLogin(user_name);
                if (user != null) {
                    usersBDD.removeUser(user.getId());
                    Toast.makeText(view.getContext(), "suppression de " + user_name + " bien effectué", Toast.LENGTH_LONG).show();
                    finish();
                    startActivity(getIntent());
                } else {
                    Toast.makeText(view.getContext(), "cet utilisateur n'existe pas", Toast.LENGTH_LONG).show();
                }
            }
        });
        LinearLayout layout_list_user = findViewById(R.id.list_user);
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        List<User> list_user = usersBDD.getUsers();
        for (User user : list_user) {
            TextView tv = new TextView(this);
            tv.setLayoutParams(lparams);
            tv.setText(user.getIdentifiant());
            layout_list_user.addView(tv);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //usersBDD.close();
    }
}
