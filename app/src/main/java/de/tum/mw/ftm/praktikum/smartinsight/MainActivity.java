package de.tum.mw.ftm.praktikum.smartinsight;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AnfrageListFragment.OnListFragmentInteractionListener{
    UserLocalStore userLocalStore;
    AnfrageClientLocalStore anfrageClientLocalStore;
    User user = null;

    private boolean fragmentAnfrageListActive = false;
    private boolean startActFirstTime = true;
    TextView emailView;
    TextView nameView;

    ArrayList<Anfrage> requests = new ArrayList<Anfrage>();

    @Override
    public void onListFragmentDeleteListItem(int position, AnfrageProvider value) {
        // TODO: hier müsste die Anfrage an der Position gelöscht werden. Und danach müsste die Liste neu geupdatet werden
        Toast.makeText(MainActivity.this, "Anfrage ... wurde gelöscht!",
                Toast.LENGTH_SHORT).show();
    }

    private GetJSONListener uploadResultListener = new GetJSONListener(){
        @Override
        public void onRemoteCallComplete(JSONObject jsonFromNet) {
            try {
                String result = jsonFromNet.getString("result");

                if(result.contains("true")) {
                    System.out.println("Upload successful!");
                    downloadRequests();
                }

                //TODO: Receive repsonse from server!

            } catch(JSONException e) {
                System.out.println(e);
            }catch (NullPointerException e) {
                System.out.println(e);
            }
        }

    };

    private GetJSONListener requestResultListener = new GetJSONListener(){
        @Override
        public void onRemoteCallComplete(JSONObject jsonFromNet) {
            try {
                requests.clear();
                JSONArray jsonArray = jsonFromNet.getJSONArray("posts");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    String student = obj.getString("linked_student");
                    String exam = obj.getString("linked_exam");
                    String subtask = obj.getString("linked_subtask");
                    String task = obj.getString("linked_task");
                    String phd = obj.getString("linked_phd");
                    Anfrage anfrage = new Anfrage(student, task, subtask, phd, exam);
                    requests.add(anfrage);
                    System.out.println(anfrage.toString());
                }
                updateListView();

                /*if (customIntFragListView != null) {
                    customIntFragListView.updateFragmentListView(requests);
                }*/

            } catch (JSONException e) {
                System.out.println(e);
            }catch (NullPointerException e) {
                System.out.println(e);
            }

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
        View headerNavigation = navigationView.getHeaderView(0);
        emailView = (TextView) headerNavigation.findViewById(R.id.emailView);
        nameView = (TextView) headerNavigation.findViewById(R.id.nameView);
        //generate lsitview für anfragen
        // Construct the data source
        ArrayList<AnfrageProvider> arrayOfUsers = new ArrayList<AnfrageProvider>();
        // Create the adapter to convert the array to views

        userLocalStore = new UserLocalStore(this);
        anfrageClientLocalStore = new AnfrageClientLocalStore(this);
        anfrageClientLocalStore.setStatusAnfrageClient(false);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, new AnfrageListFragment()).commit();
        fragmentAnfrageListActive = true;

        user = userLocalStore.getUserLogInUser();

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

    @Override
    protected void onStart(){
        super.onStart();

        user = userLocalStore.getUserLogInUser();


        if(authenticate() == true && startActFirstTime){
            downloadRequests();
            Toast.makeText(MainActivity.this,"Willkommen, "+user.name + ", Matrikelnummer: "+ user.matrikelnummer + ", Email: "+user.email+ ", Prüfung: "+user.exam,
                    Toast.LENGTH_LONG).show();
            emailView.setText(user.email);
            nameView.setText(user.name);
            startActFirstTime = false;
            updateListView();
        }
        else if (startActFirstTime){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            startActFirstTime = true;
        }

        //Hier kommen updates nach dem Floating action button hin
        if(anfrageClientLocalStore.getStatusAnfrageClient())
        {

            //TODO: upload data to server
            Anfrage anfrage = anfrageClientLocalStore.getDataAnfrageClient();
            uploadData(anfrage);
            updateListView();
        }
        anfrageClientLocalStore.setStatusAnfrageClient(false);


    }


    private boolean authenticate(){
        return userLocalStore.getUserLoggedIn();
    }

    private  void updateListView() {


        if (fragmentAnfrageListActive) {
            AnfrageListFragment listFrag = (AnfrageListFragment)
                    getSupportFragmentManager().findFragmentById(R.id.container);
            if(listFrag != null) {
                listFrag.updateFragmentListView(requests);
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment;
        fragmentAnfrageListActive = false;
        int id = item.getItemId();

        if (id == R.id.nav_calendar) {
            fragment = new CalendarFragment();
            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
        }
        else if (id == R.id.nav_abmelden) {
            userLocalStore.clearUserData();
            userLocalStore.setUserLoggedIn(false);
            View view;
            view = new View(this);
            Intent myIntent = new Intent(view.getContext(), LoginActivity.class);
            startActivity(myIntent);
        }
        else if (id == R.id.nav_anfragen) {
            fragment = new AnfrageListFragment();
            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
            fragmentAnfrageListActive = true;
        }
        else if (id == R.id.nav_settings) {
            fragment = new SettingsFragment();
            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
        }
        else if (id == R.id.nav_statistic) {
            fragment = new StatisticFragment();
            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void downloadRequests() {
        JSONClient client = new JSONClient(this, requestResultListener);
        // TODO: Website so konfigurieren, dass die Anfrage nur mit Passwort ausgegeben wird.
        String url = "http://www.marcengelmann.com/smart/download.php?intent=request&matrikelnummer=" + user.matrikelnummer;
        client.execute(url);
    }

    private void downloadExam() {
       // JSONClient client = new JSONClient(this, examResultListener);
        // TODO: Website so konfigurieren, dass die Anfrage nur mit Passwort ausgegeben wird.
       // String url = "http://www.marcengelmann.com/smart/download.php?intent=exam&exam_name=" + user.exam;
       // client.execute(url);
    }

    private void uploadData(Anfrage anfrage) {
        System.out.println("Trying data upload ...");
        JSONClient uploader = new JSONClient(this, uploadResultListener);
        // TODO: Website so konfigurieren, dass die Anfrage nur mit Passwort ausgegeben wird.
        String url = "http://www.marcengelmann.com/smart/upload.php?intent=request&matrikelnummer=" + user.matrikelnummer+"&task_id="+anfrage.linked_task+"&subtask_id="+anfrage.linked_subtask;
        uploader.execute(url);
    }


}
