package de.tum.mw.ftm.praktikum.smartinsight;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Button btnLogOut;
    UserLocalStore userLocalStore;
    AnfrageAdapter adapter;
    ListView listviewAnfrage;
    String [] startTime = {"9:00", "10:00", "10.20"};
    String [] endTime = {"9:10", "10:10", "10.30"};
    String [] taskNumber = {"1", "3", "1"};
    String [] taskSubNumber = {"a", "b", "c"};
    String [] frageArt = {"Inhalt", "Inhalt + Punkte", "Punkte"};
    String [] frageBearbeiter = {"Hand Peter Wurst", "Klaus Heinz", "Waltraud Franz"};
    private GetJSONListener l = new GetJSONListener(){

        @Override
        public void onRemoteCallComplete(JSONObject jsonFromNet) {
            System.out.println(jsonFromNet);
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), FloatingActivity.class);
                startActivity(myIntent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Generate button Logout
        btnLogOut = (Button) findViewById(R.id.btnLogOut);

        //generate lsitview für anfragen
        // Construct the data source
        ArrayList<Anfrage> arrayOfUsers = new ArrayList<Anfrage>();
        // Create the adapter to convert the array to views
        adapter = new AnfrageAdapter(this, arrayOfUsers);
        // Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.listViewAnfrangen);
        listView.setAdapter(adapter);


        userLocalStore = new UserLocalStore(this);

        //Execute JSON
        JSONClient client = new JSONClient(this, l);
        String url = "http://www.marcengelmann.com/smart/download.php";
        client.execute(url);
    }

    public void clearAnfragen(View view){
        adapter.clear();
    }


    public void addAnfrage(View view){
        // Add item to adapter
        Anfrage newAnfrage = new Anfrage(startTime[0], endTime[0], taskNumber[0], taskSubNumber[0], frageArt[0], frageBearbeiter[0]);
        adapter.add(newAnfrage);
    }

    public void addAllAnfrage(View view){
        adapter.clear();
        for(int i=0; i < startTime.length; i++){

        Anfrage newAnfrage = new Anfrage(startTime[i], endTime[i], taskNumber[i], taskSubNumber[i], frageArt[i], frageBearbeiter[i]);
        adapter.add(newAnfrage);

        }
 /*

// Or even append an entire new collection
// Fetching some data, data has now returned
// If data was JSON, convert to ArrayList of User objects.
JSONArray jsonArray = ...;
ArrayList<Anfrage> newAnfrage = Anfrage.fromJson(jsonArray)
adapter.addAll(newAnfrage);


*/
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void logOut(View view){
        userLocalStore.clearUserData();
        userLocalStore.setUserLoggedIn(false);
        Intent myIntent = new Intent(view.getContext(), LoginActivity.class);
        startActivity(myIntent);
    }

    @Override
    protected void onStart(){
        super.onStart();

        if(authenticate() == true){
            updateListView();
        }
        else{
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }

    }

    private boolean authenticate(){
        return userLocalStore.getUserLoggedIn();
    }

    private  void updateListView(){

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
