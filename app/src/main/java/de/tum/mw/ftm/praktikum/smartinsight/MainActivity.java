package de.tum.mw.ftm.praktikum.smartinsight;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AnfrageListFragment.OnListFragmentInteractionListener{
    UserLocalStore userLocalStore;
    AnfrageLocalStore anfrageLocalStore;
    TaskListLocalStore taskListLocalStore;
    User user = null;

    private boolean fragmentAnfrageListActive = false;
    private boolean fragmentAnfrageListDefaultActive = false;
    private boolean startActFirstTime = true;

    TextView emailView;
    TextView nameView;

    ArrayList<Anfrage> requests = new ArrayList<Anfrage>();
    ArrayList<Task> tasks = new ArrayList<Task>();

    @Override
    public void onListFragmentDeleteListItem(int position, AnfrageProvider value) {
        deleteRequest(value);
        downloadRequests();
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
                    String id = obj.getString("id");
                    Anfrage anfrage = new Anfrage(id,student, task, subtask, phd, exam);
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

    private GetJSONListener examResultListener = new GetJSONListener(){
        @Override
        public void onRemoteCallComplete(JSONObject jsonFromNet) {

            taskListLocalStore.clearTaskStore();

            try {
                tasks.clear();
                JSONArray jsonArray = jsonFromNet.getJSONArray("posts");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);

                    String name = obj.getString("name");
                    String linked_exam = obj.getString("linked_exam");
                    String linked_phd = obj.getString("linked_phd");
                    String id = obj.getString("id");
                    String number = obj.getString("number");

                    Task task = new Task(name,linked_exam,linked_phd,id,number);
                    tasks.add(task);
                    System.out.println(task.toString());
                }

                taskListLocalStore.storeTaskList(tasks);


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

        taskListLocalStore = new TaskListLocalStore(this);
        userLocalStore = new UserLocalStore(this);
        anfrageLocalStore = new AnfrageLocalStore(this);
        anfrageLocalStore.setStatusAnfrageClient(false);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = new AnfrageListDefault();
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
        fragmentAnfrageListDefaultActive = true;

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

        Toast.makeText(MainActivity.this,"Willkommen, "+user.name + ", Matrikelnummer: "+ user.matrikelnummer + ", Email: "+user.email+ ", Prüfung: "+user.exam,
                Toast.LENGTH_LONG).show();
        emailView.setText(user.email);
        nameView.setText(user.name);
        if(authenticate() == true && startActFirstTime){
            Toast.makeText(MainActivity.this,"Willkommen, "+user.name + ", Matrikelnummer: "+ user.matrikelnummer + ", Email: "+user.email+ ", Prüfung: "+user.exam,
                    Toast.LENGTH_LONG).show();
            emailView.setText(user.email);
            nameView.setText(user.name);
            startActFirstTime = false;
            updateListView();
            downloadRequests();
            downloadExam();
        }
        else if (startActFirstTime){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            startActFirstTime = true;
        }

        //Hier kommen updates nach dem Floating action button hin
        if(anfrageLocalStore.getStatusAnfrageClient())
        {
            Anfrage anfrage = anfrageLocalStore.getDataAnfrageClient();
            uploadData(anfrage);
            updateListView();
        }
        anfrageLocalStore.setStatusAnfrageClient(false);


    }


    private boolean authenticate(){
        return userLocalStore.getUserLoggedIn();
    }

    private  void updateListView() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment;
        if(fragmentAnfrageListDefaultActive && !requests.isEmpty()){
            fragment = new AnfrageListFragment();
            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
            fragmentAnfrageListActive = true;
            fragmentAnfrageListDefaultActive = false;
        }
        else if(fragmentAnfrageListActive && requests.isEmpty()){
            fragment = new AnfrageListDefault();
            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
            fragmentAnfrageListDefaultActive = true;
            fragmentAnfrageListActive = false;
        }
        else if (fragmentAnfrageListActive) {
            AnfrageListFragment anfrageListFragment = (AnfrageListFragment)
                    getSupportFragmentManager().findFragmentById(R.id.container);
            if (anfrageListFragment != null) {
                anfrageListFragment.updateFragmentListView(requests);
            }
        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment;
        int id = item.getItemId();
        fragmentAnfrageListActive = false;
        fragmentAnfrageListDefaultActive = false;
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
            if(!requests.isEmpty()) {
                fragment = new AnfrageListFragment();
                fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                fragmentAnfrageListActive = true;
            }
            else {
                fragment = new AnfrageListDefault();
                fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                fragmentAnfrageListDefaultActive = true;
            }
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
        JSONClient client = new JSONClient(this, examResultListener);
        // TODO: Website so konfigurieren, dass die Anfrage nur mit Passwort ausgegeben wird.
        // TODO: ACHTUNG KURZNAME!
        String url = "http://marcengelmann.com/smart/download.php?intent=exam&exam_name=AER";
        client.execute(url);
    }

    private void uploadData(Anfrage anfrage) {
        System.out.println("Trying data upload ...");
        System.out.println(anfrage.toString());
        JSONClient uploader = new JSONClient(this, uploadResultListener);
        // TODO: Website so konfigurieren, dass die Anfrage nur mit Passwort ausgegeben wird.
        String url = "http://www.marcengelmann.com/smart/upload.php?intent=request&matrikelnummer=" + user.matrikelnummer+"&task_id="+anfrage.linked_task+"&subtask_id="+anfrage.linked_subtask;
        uploader.execute(url);
    }

    private void deleteRequest(AnfrageProvider anfrage) {
        System.out.println("Trying data delete ...");
        JSONClient uploader = new JSONClient(this, uploadResultListener);
        // TODO: Website so konfigurieren, dass die Anfrage nur mit Passwort ausgegeben wird.
        String url = "http://www.marcengelmann.com/smart/upload.php?intent=delete_request&request_id="+anfrage.id;
        uploader.execute(url);
    }

}
