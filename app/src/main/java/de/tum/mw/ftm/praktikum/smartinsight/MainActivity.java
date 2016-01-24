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
        implements NavigationView.OnNavigationItemSelectedListener, RequestsPhdListFragment.OnListFragmentInteractionListener {
    private UserLocalStore userLocalStore;
    private StudentRequestLocalStore anfrageLocalStore;
    private TaskListLocalStore taskListLocalStore;
    private User user = null;

    // Attribute, welche true ist wenn das Fragment AnfrageListe gelaen ist
    private boolean fragmentAnfrageListActive = false;
    // ist true wenn noch keine User login daten zum ersten mal geladen sind
    private boolean startActFirstTime = true;

    private TextView emailView;
    private TextView nameView;
    private NavigationView navigationView;

    // LIste mit den Anfragen, welcher dozent, wann kommt
    private ArrayList<RequestsPhd> requests = new ArrayList<>();
    // Liste, die die Klausureinsichtstermine speichert
    private ArrayList<Calendar> calendarList = new ArrayList<>();
    // Liste mit der Aufgaben für di eKklausur, wo der Student gerade eingeschrieben ist
    private ArrayList<Task> tasks = new ArrayList<>();

    // Interface funktion die aufgerufen wird, wenn eine Anfrage bearbeitet werden soll
    // Anfragen in der Zukunft können gelöscht werden
    // Anfragen in der Vergangenheit können erneuert weren
    @Override
    public void onListFragmentDeleteListItem(int position, RequestsPhd value, Boolean deleteNUpdate) {
        if(deleteNUpdate){
            deleteRequest(value);
            downloadRequests();
            Toast.makeText(MainActivity.this, "Anfrage wurde gelöscht!",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            //Todo update die aktuelle Anfrage indem sie nochmal neu gesendet wird --> wird nur als dummy implementiert
            Toast.makeText(MainActivity.this, "Anfrage wurde erneuert! Achtung Dummy Funktion!",
                    Toast.LENGTH_SHORT).show();
        }

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

    // methode di eAufgerufen werden soll, wenn das Fragment Anfragelsite geladen werden osll
    @SuppressWarnings("SpellCheckingInspection")
    private void setFragmentAnfrageliste() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment;
        // Wenn die Anfrageliste geladen wird, soll die Liste mit den Anfragen ovn der Studenten
        // Über ein bundle mit geladen werden
        Bundle bundle = new Bundle();
        bundle.putSerializable(String.valueOf(R.string.bundleRequests), requests);
        // In der Toolbar soll der titel geladen werden
        setTitle(R.string.capition_anfrageliste);
        fragment = new RequestsPhdListFragment();
        fragment.setArguments(bundle);

        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
        // true setzen, da das fragment active ist
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

                    RequestsPhd anfrage = new RequestsPhd(id,cutStart , cutEnd, task, subtask, type_of_question, phd);
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

        // Floating action button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // wenn der Floating action button aktiviert wurde, soll die Klasse
                // FloatingActivity geladen werden
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
        // in diesen zwei TextView ist das die E-Mail addresse und der Name des Users,
        // die in dem Naviationdrawer im header angezeigt werden
        emailView = (TextView) headerNavigation.findViewById(R.id.emailView);
        nameView = (TextView) headerNavigation.findViewById(R.id.nameView);

        taskListLocalStore = new TaskListLocalStore(this);
        userLocalStore = new UserLocalStore(this);
        anfrageLocalStore = new StudentRequestLocalStore(this);
        anfrageLocalStore.setStatusAnfrageClient(false);

        // Fragment zur Anfrageliste osll immer der startbildschirm sein
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
        // get user
        user = userLocalStore.getUserLogInUser();

        if (authenticate() && startActFirstTime) {
            // wenn der User eingeloggt ist und die App zum ersten mal gestarteter wird,
            // soll über ein Toast der User Willkommen geheißen werden
            Toast.makeText(MainActivity.this, "Willkommen, " + user.getName() + ".",
                    Toast.LENGTH_LONG).show();
            // setzen der email addresse und des namens im header des navigation drawer
            emailView.setText(user.getEmail());
            nameView.setText(user.getName());
            // soll danach nicht mehr geladen werden, daher wird das attribute false gesetzt
            startActFirstTime = false;
            // die  methoden downloaden vom server die aktuelel
            //  Klausureinsichtstermine geladen
            downloadExam();
            downloadCalendar();
            // setze im navtation drawer, dass die Anfrageliste ausgewählt ist
            navigationView.getMenu().getItem(0).setChecked(true);
            setFragmentAnfrageliste();
        } else if (startActFirstTime) {
            // wenn noch kein nutzer gespeichert ist, wird ein login activity geladen
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            startActFirstTime = true;
        }

        //Hier kommen updates nach dem Floating action button hin
        if (anfrageLocalStore.getStatusAnfrageClient()) {
            StudentRequest anfrage = anfrageLocalStore.getDataAnfrageClient();
            uploadData(anfrage);
        }
        anfrageLocalStore.setStatusAnfrageClient(false);
        // die methode downlaoded vom server die aktuelle Anfragelsite
        downloadRequests();
        // wenn sich etwas verändert aht, soll die User daten aktualisiert werden
        if(user.getDidChange()) {
            System.out.println("Change in user data detected!");
            refreshUserData();
        }
    }


    private boolean authenticate() {
        // ToDo überprüfen ob die Nutzerdaten noch aktuell sind und dementsprechens
        return userLocalStore.getUserLoggedIn();
    }

    // Methode soll die aktuelle Anfrage liste updaten, wenn sich etwas geändert hat
    @SuppressWarnings("SpellCheckingInspection")
    private void updateListView() {
        // hier uwrde noch keien andere möglcihkeit gefunden zu überprüfen, ob das Fragement
        // welches die Anfragen auflistet aktive ist und dementspreichend soll die Liste über
        // die methode updateFragmentListView geupdtet werden
        if (fragmentAnfrageListActive) {
            RequestsPhdListFragment anfrageListFragment = (RequestsPhdListFragment)
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
                bundle.putSerializable(String.valueOf(R.string.calendar), calendarList);
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
            case R.id.nav_profil:
                setTitle(R.string.caption_profil);
                fragment = new ProfilFragment();
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
        String url = getString(R.string.website)+"/download.php?intent=request&exam_name=" + user.getExam() + "&matrikelnummer=" + user.getMatrikelnummer() + "&pw=" + user.getPassword();
        System.out.println(url);
        client.execute(url);
    }

    private void downloadExam() {
        System.out.println("Trying exam download ...");
        JSONClient task_client = new JSONClient(this, examResultListener);
        String url = getString(R.string.website)+"/download.php?intent=exam&matrikelnummer="+user.getMatrikelnummer()+"&exam_name="+user.getExam()+ "&pw=" + user.getPassword();
        System.out.println(url);
        task_client.execute(url);
    }

    private void downloadCalendar() {
        System.out.println("Trying calendar download ...");
        JSONClient task_client = new JSONClient(this, calendarResultListener);
        String url = getString(R.string.website)+"/download.php?intent=calendar&exam_name="+user.getExam()+"&matrikelnummer="+user.getMatrikelnummer()+ "&pw=" + user.getPassword();
        System.out.println(url);
        task_client.execute(url);
    }

    @SuppressWarnings("SpellCheckingInspection")
    private void uploadData(StudentRequest anfrage) {
        System.out.println("Trying data upload ...");
        JSONClient uploader = new JSONClient(this, uploadResultListener);
        String url = getString(R.string.website)+"/upload.php?intent=request&exam_name="+user.getExam()+"&matrikelnummer=" + user.getMatrikelnummer() + "&task_id=" + anfrage.getLinked_task() + "&subtask_id=" + anfrage.getLinked_subtask() + "&pw=" + user.getPassword() + "&type_of_question=" + anfrage.getType_of_question();
        System.out.println(url);
        uploader.execute(url);
    }

    @SuppressWarnings("SpellCheckingInspection")
    private void refreshUserData() {
        System.out.println("Trying user data update ...");
        JSONClient uploader = new JSONClient(this, uploadResultListener);
        String url = getString(R.string.website)+"/upload.php?intent=userdata&exam_name="+user.getExam()+"&matrikelnummer=" + user.getMatrikelnummer() + "&pw=" + user.getPassword() + "&seat=" + user.getSitNumb();
        System.out.println(url);
        uploader.execute(url);
        user.setDidChange(false);
        userLocalStore.storeUserData(user);
    }

    @SuppressWarnings("SpellCheckingInspection")
    private void deleteRequest(RequestsPhd anfrage) {
        System.out.println("Trying data delete ...");
        JSONClient uploader = new JSONClient(this, uploadResultListener);
        String url = getString(R.string.website)+"/upload.php?intent=delete_request&exam_name="+user.getExam()+"&request_id=" + anfrage.id + "&pw=" + user.getPassword();
        System.out.println(url);
        uploader.execute(url);
    }

}
