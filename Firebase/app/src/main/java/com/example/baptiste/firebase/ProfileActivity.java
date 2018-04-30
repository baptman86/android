package com.example.baptiste.firebase;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Baptiste on 28/04/2018.
 */

public class ProfileActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            this.configureToolbar();
        }

        @Override
        public int getFragmentLayout() { return R.layout.activity_profile; }

        // --------------------
        // ACTIONS
        // --------------------

        @OnClick(R.id.profile_activity_button_update)
        public void onClickUpdateButton() { }

        @OnClick(R.id.profile_activity_button_sign_out)
        public void onClickSignOutButton() { }

        @OnClick(R.id.profile_activity_button_delete)
        public void onClickDeleteButton() { }
    }
}
