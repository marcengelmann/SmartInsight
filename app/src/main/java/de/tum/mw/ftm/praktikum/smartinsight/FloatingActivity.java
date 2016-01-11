package de.tum.mw.ftm.praktikum.smartinsight;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.util.ArrayList;

public class FloatingActivity extends AppCompatActivity {

    private Spinner spinnerTaskNumber;
    private String[] stringTaskNumber;
    private Spinner spinnerTaskSubNumber;
    private ArrayList<String[]> stringTaskSubNumber;
    final Context context = this;

    private RadioButton radioBtnQuestionA;
    private RadioButton radioBtnQuestionB;
    private RadioButton radioBtnQuestionC;
    private RadioGroup radioGroupQuestion;

    //Aufgaben Informationen fürs senden
    private String artOfQuestion = "Inhalt";
    private String taskNumber = "1";
    private String taskSubNumber = "a";

    AnfrageLocalStore anfrageLocalStore;
    UserLocalStore userLocalStore;
    TaskListLocalStore taskListLocalStore;

    private ArrayList<Task> tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floating);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.caption_addTask);
        radioBtnQuestionA = (RadioButton) findViewById(R.id.radioBtnQuestionA);
        radioBtnQuestionB = (RadioButton) findViewById(R.id.radioBtnQuestionB);
        radioBtnQuestionC = (RadioButton) findViewById(R.id.radioBtnQuestionC);
        radioGroupQuestion = (RadioGroup) findViewById(R.id.radioGroupQuestion);

        taskListLocalStore = new TaskListLocalStore(this);
        tasks = taskListLocalStore.getTaskList();
        stringTaskNumber = new String[tasks.size()];

        int pos = 0;
        stringTaskSubNumber = new ArrayList<String[]>();

        for(Task task:tasks) {
            int sub_pos = 0;
            stringTaskNumber[pos] = task.number +". "+task.name;
            pos ++;
            String[] subStringHelper = new String[task.getSubtasks().size()];
            for(SubTask subtask:task.getSubtasks()) {
                subStringHelper[sub_pos] = subtask.letter + ") "+subtask.name;
                sub_pos ++;
            }
            if(subStringHelper.length == 0) {
                subStringHelper = new String[1];
                subStringHelper[0] = "gesamt";
            }
            stringTaskSubNumber.add(subStringHelper);
        }

        spinnerTaskNumber = (Spinner)findViewById(R.id.spinnerTaskNumber);
        spinnerTaskSubNumber = (Spinner)findViewById(R.id.spinnerTaskSubNumber);

        ArrayAdapter<String> adapterTaskNumber = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, stringTaskNumber);
        adapterTaskNumber.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTaskNumber.setAdapter(adapterTaskNumber);
        spinnerTaskNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                ArrayAdapter<String> newAdapter = new ArrayAdapter<>(FloatingActivity.this, android.R.layout.simple_spinner_dropdown_item, stringTaskSubNumber.get(position));
                newAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerTaskSubNumber.setAdapter(newAdapter);
                newAdapter.notifyDataSetChanged();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
               // do nothing
            }
        });

        ArrayAdapter<String> adapterSubTaskNumber = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, stringTaskSubNumber.get(0));
        adapterSubTaskNumber.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTaskSubNumber.setAdapter(adapterSubTaskNumber);

        anfrageLocalStore = new AnfrageLocalStore(this);
        userLocalStore = new UserLocalStore(this);

        anfrageLocalStore.setStatusAnfrageClient(false);
        anfrageLocalStore.clearDataAnfrageClient();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onButtonSendQuestion(View view){
        String[] arrayQuestion = getResources().getStringArray(R.array.art_of_question);
        int radioButtonID = radioGroupQuestion.getCheckedRadioButtonId();
        View radioButton = radioGroupQuestion.findViewById(radioButtonID);
        int idx = radioGroupQuestion.indexOfChild(radioButton);
        artOfQuestion = arrayQuestion[idx];
        taskNumber = String.valueOf(spinnerTaskNumber.getSelectedItem());
        taskSubNumber = String.valueOf(spinnerTaskSubNumber.getSelectedItem());

        finalDialog("Anfrage senden","Zur Aufgabe " + taskNumber +" "+ taskSubNumber + " hast du eine Frage zu: " + artOfQuestion).show();
    }
    private Dialog finalDialog(String title,String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg);
        builder.setTitle(title);
        builder.setPositiveButton("Senden", new DialogInterface.OnClickListener
                () {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                User user = userLocalStore.getUserLogInUser();
                String subtask_string;
                try {
                    subtask_string = tasks.get(spinnerTaskNumber.getSelectedItemPosition()).getSubtasks().get(spinnerTaskSubNumber.getSelectedItemPosition()).id;
                }
                catch (IndexOutOfBoundsException e) {
                    subtask_string = null;
                }
                Anfrage anfrage = new Anfrage("0",user.matrikelnummer,tasks.get(spinnerTaskNumber.getSelectedItemPosition()).id,subtask_string,"linked_phd","linked_exam",artOfQuestion,false);
                anfrageLocalStore.storeAnfrageData(anfrage);
                anfrageLocalStore.setStatusAnfrageClient(true);
                finish();
            }
        });
        builder.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
// Canel was clicked
            }
        });

        builder.setCancelable(false);
        return builder.create();    }
}

