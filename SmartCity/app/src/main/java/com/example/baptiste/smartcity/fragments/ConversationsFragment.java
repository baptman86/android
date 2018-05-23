package com.example.baptiste.smartcity.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.baptiste.smartcity.R;
import com.example.baptiste.smartcity.adapters.ListAnnoncesAdapter;
import com.example.baptiste.smartcity.adapters.ListConversationAdapter;
import com.example.baptiste.smartcity.objects.Conversation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.HashMap;
public class ConversationsFragment extends Fragment {

    private static DatabaseReference mDataBase = FirebaseDatabase.getInstance().getReference();

    private ListView listConversation;
    private ProgressBar loader;

    private EditText searchBar;

    private Button searchButton;

    private View v;
    private String actual_user_id;
    private ArrayList<String> conversations_id = new ArrayList<>();

    private ArrayList<HashMap<String, String>> dataList = new ArrayList<>();

    public static final String KEY_TITLE = "title";
    public static final String KEY_DESCRIPTION = "description";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.conversations_fragment, container, false);

        searchBar = v.findViewById(R.id.searchBarConversation);
        searchButton = v.findViewById(R.id.searchButtonConversation);
        listConversation = (ListView) v.findViewById(R.id.listConversations);
        loader = (ProgressBar) v.findViewById(R.id.loader);
        listConversation.setEmptyView(loader);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("test","bonjour");
            }
        });


        if(isNetworkAvailable(v.getContext())){
            //displayConversations();
            generateListConversation();
        }
        else{
            Toast.makeText(v.getContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }
        return v;
    }

    public void generateListConversation(){
        dataList = new ArrayList<>();

        //exemple
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(KEY_TITLE, "titre1");
        map.put(KEY_DESCRIPTION, "desc1");
        dataList.add(map);

        ListConversationAdapter adapter = new ListConversationAdapter(ConversationsFragment.this, dataList);
        listConversation.setAdapter(adapter);
    }

    public void displayConversations(){
        ValueEventListener vel = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("test","mdr1");
                GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {};
                ArrayList<String> conversations_id = dataSnapshot.getValue(t);
                mDataBase.child("users").child(actual_user_id).child("conversations_id").removeEventListener(this);

                if(!conversations_id.isEmpty()){
                    for(String conversation_id : conversations_id){
                        displayConversation(conversation_id);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(getContext(),getResources().getString(R.string.error_reading_database),Toast.LENGTH_LONG).show();
                mDataBase.child("users").child(actual_user_id).child("conversations_id").removeEventListener(this);
            }
        };
        Log.e("test",actual_user_id);
        mDataBase.child("users").child(actual_user_id).child("conversations_id").addValueEventListener(vel);
    }

    public void displayConversation(final String conversation_id){
        ValueEventListener vel = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Conversation conversation = dataSnapshot.getValue(Conversation.class);
                mDataBase.child("conversations").child(conversation_id).removeEventListener(this);

                HashMap<String, String> map = new HashMap<>();
                map.put(KEY_TITLE, conversation.getTopic()); //null pointer execption here
                map.put(KEY_DESCRIPTION, conversation.getDescription());
                dataList.add(map);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(getContext(),getResources().getString(R.string.error_reading_database),Toast.LENGTH_LONG).show();
                mDataBase.child("conversations").child(conversation_id).removeEventListener(this);
            }
        };
        mDataBase.child("conversations").child(conversation_id).addValueEventListener(vel);
    }

    @Override
    public void onAttach(Context context) {
        actual_user_id = getActivity().getIntent().getExtras().getString(getResources().getString(R.string.connected_user_id));
        super.onAttach(context);
    }

    public static boolean isNetworkAvailable(Context context)
    {
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }
}
