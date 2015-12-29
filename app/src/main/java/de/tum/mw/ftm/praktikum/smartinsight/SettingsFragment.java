package de.tum.mw.ftm.praktikum.smartinsight;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;


public class SettingsFragment extends Fragment {
    private Spinner spinnerSitzNumber;
    private TextView txtMatrikelNum;
    private TextView txtName;
    private TextView txtEmail;
    private TextView txtExam;

    UserLocalStore userLocalStore;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userLocalStore = new UserLocalStore(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        spinnerSitzNumber = (Spinner) view.findViewById(R.id.profileSitzNumb);
        txtEmail = (TextView) view.findViewById(R.id.profileEmail);
        txtMatrikelNum = (TextView) view.findViewById(R.id.profilMatrikel);
        txtName = (TextView) view.findViewById(R.id.profileName);
        txtExam = (TextView) view.findViewById(R.id.profilExam);

        int maxSitNumb = getResources().getInteger(R.integer.max_sitz_numb);
        String[] number = new String[maxSitNumb];
        for(int i=0; i < number.length; i++){
            number[i] = String.valueOf(i);
        }
        ArrayAdapter<String> adapterSitNumber = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, number);
        adapterSitNumber.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSitzNumber.setAdapter(adapterSitNumber);
        final User user = userLocalStore.getUserLogInUser();
        spinnerSitzNumber.setSelection(Integer.valueOf(user.sitNumb));
        spinnerSitzNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                user.sitNumb = String.valueOf(position);
                userLocalStore.storeUserData(user);
                //Todo interface erstellen, was die Sitzpostion updated oder bei UserLocalStore eine Variable  hinzufügen, die besagt, dass das ganze upgedated werden muss
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        txtEmail.setText(user.email);
        txtName.setText(user.name);
        txtMatrikelNum.setText(user.matrikelnummer);
        txtExam.setText(user.exam);
        return view;
    }

    public void btnUploadFoto(View view){

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
