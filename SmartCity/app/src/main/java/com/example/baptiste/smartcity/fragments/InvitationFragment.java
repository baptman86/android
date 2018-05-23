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

import com.example.baptiste.smartcity.R;
import com.example.baptiste.smartcity.activities.LogInActivity;
import com.example.baptiste.smartcity.objects.Conversation;
import com.example.baptiste.smartcity.objects.Invitation;
import com.example.baptiste.smartcity.objects.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by ordinateur on 23/05/2018.
 */

public class InvitationFragment extends Fragment {
    private static DatabaseReference mDataBase = FirebaseDatabase.getInstance().getReference();
    View profil_view;
    String actual_user_id;
    private User actual_user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        profil_view = inflater.inflate(R.layout.invitation_fragment,container,false);

        profil_view.findViewById(R.id.invitiationButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            String invitation_topic = ((EditText) profil_view.findViewById(R.id.invitationTopic)).getText().toString();
            String invitation_user_login = ((EditText) profil_view.findViewById(R.id.invitationIdentifiant)).getText().toString();
            String invitation_description = ((EditText) profil_view.findViewById(R.id.invitationDescription)).getText().toString();
            sendInvitation(getContext(),invitation_user_login,invitation_topic,invitation_description);
            }
        });
        return profil_view;
    }

    private void sendInvitation(final Context ctx_param, final String user_login_param, final String topic_param, final String description_param){
        ValueEventListener vel = new ValueEventListener() {

            private User user;
            private String key;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = null;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user_found = snapshot.getValue(User.class);
                    if(user_found != null && user_found.getIdentifiant().equals(user_login_param)){
                        user = user_found;
                        key = snapshot.getKey();
                    }
                }
                mDataBase.child("users").removeEventListener(this);

                if(key != null){
                    String conv_id = mDataBase.child("conversation").push().getKey();
                    mDataBase.child("conversation").child(conv_id).setValue(new Conversation(topic_param,description_param));
                    String inv_id = mDataBase.child("invitation").push().getKey();
                    mDataBase.child("invitation").child(inv_id).setValue(new Invitation(actual_user_id,key,conv_id));

                    mDataBase.child("invitation").child(inv_id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Invitation invitation = dataSnapshot.getValue(Invitation.class);
                            if(invitation.getReponse() != null){
                                dataSnapshot.getRef().removeValue();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(ctx_param,getResources().getString(R.string.user_not_found),Toast.LENGTH_LONG).show();
                mDataBase.child("users").removeEventListener(this);
            }
        };
        mDataBase.child("users").addValueEventListener(vel);
    }

    @Override
    public void onAttach(Context context) {
        actual_user_id = getActivity().getIntent().getExtras().getString(getResources().getString(R.string.connected_user_id));
        super.onAttach(context);
    }
}
