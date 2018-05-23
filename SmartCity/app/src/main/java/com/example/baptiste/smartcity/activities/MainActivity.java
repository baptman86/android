package com.example.baptiste.smartcity.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.baptiste.smartcity.R;
import com.example.baptiste.smartcity.fragments.AnnoncesFragment;
import com.example.baptiste.smartcity.fragments.ConversationsFragment;
import com.example.baptiste.smartcity.fragments.InvitationFragment;
import com.example.baptiste.smartcity.fragments.MapFragment;
import com.example.baptiste.smartcity.fragments.NewsFragment;
import com.example.baptiste.smartcity.fragments.ProfilFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {



    private Fragment FragmentMap;
    private Fragment FragmentNews;
    private Fragment FragmentProfil;
    private Fragment FragmentConversation;
    private Fragment FragmentAnnonces;
    private Fragment FragmentInvitation;

    //FOR DESIGN
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.navigation_drawer);

        this.configureToolBar();

        this.configureDrawerLayout();

        this.configureNavigationView();

        this.showMapFragment();
    }



    @Override
    public void onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        // 6 - Show fragment after user clicked on a menu item
        switch (id){
            case R.id.activity_main_drawer_map :
                this.showMapFragment();
                break;
            case R.id.activity_main_drawer_news :
                this.showNewsFragment();
                break;
            case R.id.activity_main_drawer_profil :
                this.showProfilFragment();
                break;
            case R.id.activity_main_drawer_conversation :
                this.showConversationFragment();
                break;
            case R.id.activity_main_drawer_invitation :
                this.showInvitationFragment();
                break;
            case R.id.activity_main_drawer_annonces :
                this.showAnnoncesFragment();
                break;
            case R.id.activity_main_drawer_disconnect :
                this.disconnect();
                break;
            default:
                break;
        }

        this.drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }







    private void showAnnoncesFragment(){
        if (this.FragmentAnnonces == null) this.FragmentAnnonces = new AnnoncesFragment();
        this.startTransactionFragment(this.FragmentAnnonces);
    }


    // ---------------------
    // FUNCTION USE ON CLICK
    // ---------------------

    private void showMapFragment(){
        if (this.FragmentMap == null) this.FragmentMap = new MapFragment();
        this.startTransactionFragment(this.FragmentMap);
    }

    private void showProfilFragment(){
        if (this.FragmentProfil == null) this.FragmentProfil = new ProfilFragment();
        this.startTransactionFragment(this.FragmentProfil);
    }

    private void showNewsFragment(){
        if (this.FragmentNews == null) this.FragmentNews = new NewsFragment();
        this.startTransactionFragment(this.FragmentNews);
    }

    private void showConversationFragment(){
        if (this.FragmentConversation == null) this.FragmentConversation = new ConversationsFragment();
        this.startTransactionFragment(this.FragmentConversation);
    }

    private void showInvitationFragment(){
        if (this.FragmentInvitation == null) this.FragmentInvitation = new InvitationFragment();
        this.startTransactionFragment(this.FragmentInvitation);
    }

    private void disconnect(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(getResources().getString(R.string.storedLogin));
        editor.remove(getResources().getString(R.string.storedPassword));
        editor.apply();

        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
    }

    private void startTransactionFragment(Fragment fragment){
        if (!fragment.isVisible()){
            getSupportFragmentManager().beginTransaction().replace(R.id.navidation_drawer_frame_layout, fragment).commit();
        }
    }




    // ---------------------
    // CONFIGURATION
    // ---------------------

    private void configureToolBar(){
        this.toolbar = (Toolbar) findViewById(R.id.navigation_drawer_toolbar);
        setSupportActionBar(toolbar);
    }

    private void configureDrawerLayout(){
        this.drawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer_main_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void configureNavigationView(){
        this.navigationView = (NavigationView) findViewById(R.id.navigation_drawer_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
}
