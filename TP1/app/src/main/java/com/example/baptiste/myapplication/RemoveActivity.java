package com.example.baptiste.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Baptiste on 12/03/2018.
 */

public class RemoveActivity extends Activity {

    static UsersBDD usersBDD;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        usersBDD = new UsersBDD(this);
        usersBDD.open();
        setContentView(R.layout.activity_remove);
        findViewById(R.id.confirmDeleteUser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_name = ((EditText)findViewById(R.id.User_name)).getText().toString();
                User user = usersBDD.getUserWithName(user_name);
                if(user != null) {
                    usersBDD.removeUser(user.getId());
                    Toast.makeText(view.getContext(), "suppression de " + user_name + " bien effectué", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(view.getContext(), "cet utilisateur n'existe pas", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //usersBDD.close();
    }
}
