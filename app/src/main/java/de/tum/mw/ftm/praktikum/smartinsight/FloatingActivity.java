package de.tum.mw.ftm.praktikum.smartinsight;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

public class FloatingActivity extends AppCompatActivity {

    private Spinner spinnerTaskNumber;
    private String[]stringTaskNumber = {"1", "2", "3"};
    private Spinner spinnerTaskSubNumber;
    private String[]stringTaskSubNumber = {"a", "b", "c"};
    final Context context = this;

    private RadioButton radioBtnQuestionA;
    private RadioButton radioBtnQuestionB;
    private RadioButton radioBtnQuestionC;
    private RadioGroup radioGroupQuestion;

    //Aufgaben Informationen fürs senden
    private String artOfQuestion = "Inhalt";
    private String taskNumber = "1";
    private String taskSubNumber = "a";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floating);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        radioBtnQuestionA = (RadioButton) findViewById(R.id.radioBtnQuestionA);
        radioBtnQuestionB = (RadioButton) findViewById(R.id.radioBtnQuestionB);
        radioBtnQuestionC = (RadioButton) findViewById(R.id.radioBtnQuestionC);
        radioGroupQuestion = (RadioGroup) findViewById(R.id.radioGroupQuestion);


        spinnerTaskNumber = (Spinner)findViewById(R.id.spinnerTaskNumber);
        ArrayAdapter<String> adapterTaskNumber = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, stringTaskNumber);
        spinnerTaskNumber.setAdapter(adapterTaskNumber);

        spinnerTaskSubNumber = (Spinner)findViewById(R.id.spinnerTaskSubNumber);
        ArrayAdapter<String> adapterSubTaskNumber = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, stringTaskSubNumber);
        spinnerTaskSubNumber.setAdapter(adapterSubTaskNumber);

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
        //String msg = "Zur Aufgabe: " + taskNumber + taskSubNumber + "hast du eine Frage zu: " + artOfQuestion;

        finalDialog("Anfrage senden","Zur Aufgabe: " + taskNumber + taskSubNumber + " hast du eine Frage zu: " + artOfQuestion).show();
    }
    private Dialog finalDialog(String title,String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg);
        builder.setTitle(title);
        builder.setPositiveButton("Senden", new DialogInterface.OnClickListener
                () {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
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
