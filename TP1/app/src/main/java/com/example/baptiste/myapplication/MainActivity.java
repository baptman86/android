package com.example.baptiste.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    static UsersBDD usersBDD;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Création d'une instance de ma classe com.example.baptiste.myapplication.UsersBDD
        usersBDD = new UsersBDD(this);
        usersBDD.open();

        setContentView(R.layout.activity_main);
        findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_name = ((EditText)findViewById(R.id.User_name)).getText().toString();
                String user_surname = ((EditText)findViewById(R.id.User_surname)).getText().toString();
                User user = new User(user_name,user_surname);
                usersBDD.addUser(user);
                Toast.makeText(view.getContext(),"ajout de "+user_name+" "+user_surname+" bien effectué",Toast.LENGTH_LONG).show();
            }
        });

        findViewById(R.id.askUser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<User> users = usersBDD.getUsers();
                for(User u : users){
                    Toast.makeText(view.getContext(),u.getId()+" : "+u.getName()+" "+u.getSurname(),Toast.LENGTH_LONG).show();
                }
            }
        });

        findViewById(R.id.deleteUser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), RemoveActivity.class);
                startActivity(intent);
            }
        });
    }

    protected void OnDestroy(){
        super.onDestroy();
        //usersBDD.close();
    }
}
