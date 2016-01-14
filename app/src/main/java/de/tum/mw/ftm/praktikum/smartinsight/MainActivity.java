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
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AnfrageListFragment.OnListFragmentInteractionListener {
    UserLocalStore userLocalStore;
    AnfrageLocalStore anfrageLocalStore;
    TaskListLocalStore taskListLocalStore;
    User user = null;

    private boolean fragmentAnfrageListActive = false;
    private boolean startActFirstTime = true;

    TextView emailView;
    TextView nameView;
    CircleImageView profilPicView;
    NavigationView navigationView;

    ArrayList<AnfrageProvider> requests = new ArrayList<>();
    ArrayList<Calendar> calendarList = new ArrayList<>();
    ArrayList<Task> tasks = new ArrayList<>();

    @Override
    public void onListFragmentDeleteListItem(int position, AnfrageProvider value, Boolean deleteNUpdate) {
        if(deleteNUpdate){
            deleteRequest(value);
            downloadRequests();
            Toast.makeText(MainActivity.this, "Anfrage wurde gelöscht!",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            //Todo update die aktuelle Anfrage indem sie nochmal neu gesendet wird --> wird nur als dummy implementiert
            Toast.makeText(MainActivity.this, "Anfrage wurde erneuert! Achtung Dummy Funktion!",
                    Toast.LENGTH_SHORT).show();        }

    }

    private GetJSONListener uploadResultListener = new GetJSONListener() {
        @Override
        public void onRemoteCallComplete(JSONObject jsonFromNet) {
            try {
                String result = jsonFromNet.getString("result");
                if (result.contains("true")) {
                    System.out.println("Upload successful!");
                    downloadRequests();
                } else {
                    Toast.makeText(MainActivity.this, result,
                            Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    };

    @SuppressWarnings("SpellCheckingInspection")
    private void setFragmentAnfrageliste() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment;

        Bundle bundle = new Bundle();
        bundle.putSerializable("requests", requests);

        setTitle(R.string.capition_anfrageliste);
        fragment = new AnfrageListFragment();
        fragment.setArguments(bundle);

        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
        fragmentAnfrageListActive = true;

    }

    private GetJSONListener requestResultListener = new GetJSONListener() {
        @Override
        public void onRemoteCallComplete(JSONObject jsonFromNet) {
            try {
                requests.clear();
                JSONArray jsonArray = jsonFromNet.getJSONArray("posts");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    String subtask = obj.getString("subtask_name");
                    String task = obj.getString("task_name");
                    String phd = obj.getString("linked_phd");
                    String id = obj.getString("id");
                    String startTime = obj.getString("start_time");
                    String endTime = obj.getString("end_time");
                    String type_of_question = obj.getString("type_of_question");

                    String cutStart = startTime.substring(11, startTime.length() - 3);
                    String cutEnd = endTime.substring(11, endTime.length() - 3);

                    AnfrageProvider anfrage = new AnfrageProvider(id,cutStart , cutEnd, task, subtask, type_of_question, phd);
                    requests.add(anfrage);
                }


                /*if (customIntFragListView != null) {
                    customIntFragListView.updateFragmentListView(requests);
                }*/

            } catch (JSONException e) {
                requests.clear();
                Toast.makeText(MainActivity.this, "Keine Anfragen verfügbar!",
                        Toast.LENGTH_SHORT).show();
            }
            updateListView();
        }

    };

    private GetJSONListener calendarResultListener = new GetJSONListener() {
        @Override
        public void onRemoteCallComplete(JSONObject jsonFromNet) {
            try {
                calendarList.clear();
                JSONArray jsonArray = jsonFromNet.getJSONArray("posts");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);

                    String date = obj.getString("date");
                    String name = obj.getString("name");
                    String room = obj.getString("room");
                    String responsible_person = obj.getString("responsible_person");
                    String number_of_students = obj.getString("number_of_students");
                    String mean_grade = obj.getString("mean_grade");
                    Calendar calendar = new Calendar(date,name,room,number_of_students, responsible_person,mean_grade);
                    calendarList.add(calendar);
                    System.out.println(calendar.toString());
                }
            } catch (JSONException e) {
                Toast.makeText(MainActivity.this, "Serververbindung fehlgeschlagen!",
                        Toast.LENGTH_SHORT).show();
            }
        }
    };

    private GetJSONListener examResultListener = new GetJSONListener() {
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

                    Task task = new Task(name, linked_exam, linked_phd, id, number);

                    JSONArray subtasks = obj.getJSONArray("subtasks");

                    for (int j = 0; j < subtasks.length(); j++) {
                        JSONObject sub_obj = subtasks.getJSONObject(j);

                        String sub_name = sub_obj.getString("name");
                        String sub_letter = sub_obj.getString("letter");
                        String sub_id = sub_obj.getString("id");
                        String sub_linked_phd = sub_obj.getString("linked_phd");

                        SubTask subtask = new SubTask(sub_name, sub_letter, sub_id, sub_linked_phd);
                        task.addSubtask(subtask);
                    }
                    tasks.add(task);
                }
                taskListLocalStore.storeTaskList(tasks);
            } catch (JSONException e) {
                Toast.makeText(MainActivity.this, "Fehler beim Laden der Prüfungsstruktur!",
                        Toast.LENGTH_SHORT).show();
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

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerNavigation = navigationView.getHeaderView(0);
        headerNavigation.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        emailView = (TextView) headerNavigation.findViewById(R.id.emailView);
        nameView = (TextView) headerNavigation.findViewById(R.id.nameView);
        profilPicView = (CircleImageView) headerNavigation.findViewById(R.id.imageView);

        //generate list view for requests
        // Construct the data source
        //ArrayList<AnfrageProvider> arrayOfUsers = new ArrayList<>();
        // Create the adapter to convert the array to views

        taskListLocalStore = new TaskListLocalStore(this);
        userLocalStore = new UserLocalStore(this);
        anfrageLocalStore = new AnfrageLocalStore(this);
        anfrageLocalStore.setStatusAnfrageClient(false);

        setFragmentAnfrageliste();

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
    protected void onStart() {
        super.onStart();

        user = userLocalStore.getUserLogInUser();

        if (authenticate() && startActFirstTime) {
            Toast.makeText(MainActivity.this, "Willkommen, " + user.name + ".",
                    Toast.LENGTH_LONG).show();
            emailView.setText(user.email);
            nameView.setText(user.name);
            startActFirstTime = false;
            downloadExam();
            downloadCalendar();
            navigationView.getMenu().getItem(0).setChecked(true);
            setFragmentAnfrageliste();
        } else if (startActFirstTime) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            startActFirstTime = true;
        }

        //Hier kommen updates nach dem Floating action button hin
        if (anfrageLocalStore.getStatusAnfrageClient()) {
            Anfrage anfrage = anfrageLocalStore.getDataAnfrageClient();
            uploadData(anfrage);
        }
        anfrageLocalStore.setStatusAnfrageClient(false);

        downloadRequests();


        if(user.didChange) {
            System.out.println("Change in user data detected!");
            refreshUserData();
        }
    }


    private boolean authenticate() {
        return userLocalStore.getUserLoggedIn();
    }

    @SuppressWarnings("SpellCheckingInspection")
    private void updateListView() {
        if (fragmentAnfrageListActive) {
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
        fragmentAnfrageListActive = false;

        switch (item.getItemId()) {
            case R.id.nav_calendar:
                Bundle bundle = new Bundle();
                bundle.putSerializable("calendar", calendarList);
                fragment = new CalendarFragment();
                fragment.setArguments(bundle);
                setTitle(R.string.caption_klausur);
                fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                break;
            case R.id.nav_abmelden:
                userLocalStore.clearUserData();
                userLocalStore.setUserLoggedIn(false);
                setTitle(R.string.caption);
                View view;
                view = new View(this);
                Intent myIntent = new Intent(view.getContext(), LoginActivity.class);
                startActivity(myIntent);
                startActFirstTime = true;
                break;
            case R.id.nav_anfragen:
                setFragmentAnfrageliste();
                break;
            case R.id.nav_settings:
                setTitle(R.string.caption_settings);
                fragment = new SettingsFragment();
                fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void downloadRequests() {

        System.out.println("Trying requests download ...");
        JSONClient client = new JSONClient(this, requestResultListener);
        String url = "http://www.marcengelmann.com/smart/download.php?intent=request&exam_name=" + user.exam + "&matrikelnummer=" + user.matrikelnummer + "&pw=" + user.password;
        client.execute(url);
    }

    private void downloadExam() {
        System.out.println("Trying exam download ...");
        JSONClient task_client = new JSONClient(this, examResultListener);
        String url = "http://marcengelmann.com/smart/download.php?intent=exam&matrikelnummer="+user.matrikelnummer+"&exam_name="+user.exam+ "&pw=" + user.password;
        System.out.println(url);
        task_client.execute(url);
    }

    private void downloadCalendar() {
        System.out.println("Trying calendar download ...");
        JSONClient task_client = new JSONClient(this, calendarResultListener);
        String url = "http://marcengelmann.com/smart/download.php?intent=calendar&exam_name="+user.exam+"&matrikelnummer="+user.matrikelnummer+ "&pw=" + user.password;
        task_client.execute(url);
    }

    @SuppressWarnings("SpellCheckingInspection")
    private void uploadData(Anfrage anfrage) {
        System.out.println("Trying data upload ...");
        JSONClient uploader = new JSONClient(this, uploadResultListener);
        String url = "http://www.marcengelmann.com/smart/upload.php?intent=request&exam_name="+user.exam+"&matrikelnummer=" + user.matrikelnummer + "&task_id=" + anfrage.linked_task + "&subtask_id=" + anfrage.linked_subtask + "&pw=" + user.password + "&type_of_question=" + anfrage.type_of_question;
        uploader.execute(url);
    }

    @SuppressWarnings("SpellCheckingInspection")
    private void refreshUserData() {
        System.out.println("Trying user data update ...");
        JSONClient uploader = new JSONClient(this, uploadResultListener);
        String url = "http://www.marcengelmann.com/smart/upload.php?intent=userdata&exam_name="+user.exam+"&matrikelnummer=" + user.matrikelnummer + "&pw=" + user.password + "&seat=" + user.sitNumb;
        uploader.execute(url);
        user.didChange = false;
        userLocalStore.storeUserData(user);
    }

    @SuppressWarnings("SpellCheckingInspection")
    private void deleteRequest(AnfrageProvider anfrage) {
        System.out.println("Trying data delete ...");
        JSONClient uploader = new JSONClient(this, uploadResultListener);
        String url = "http://www.marcengelmann.com/smart/upload.php?intent=delete_request&exam_name="+user.exam+"&request_id=" + anfrage.id + "&pw=" + user.password;
        uploader.execute(url);
    }

}
