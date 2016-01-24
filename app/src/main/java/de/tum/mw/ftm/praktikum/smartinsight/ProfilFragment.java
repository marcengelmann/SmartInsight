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



/**
 * Pofil klasse zum anzeigen der profildaten
 * Sitzplatznummer kann hier auch noch einmal geädnert werden
 */
public class ProfilFragment extends Fragment {
    private Spinner spinnerSitzNumber;
    private TextView txtMatrikelNum;
    private TextView txtName;
    private TextView txtEmail;
    private TextView txtExam;
    private UserLocalStore userLocalStore;
    public ProfilFragment() {
        // Required empty public constructor
    }

    public static ProfilFragment newInstance(String param1, String param2) {
        return new ProfilFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // holt die aktuellen gespeicherten User daten
        userLocalStore = new UserLocalStore(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profil, container, false);
        view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        spinnerSitzNumber = (Spinner) view.findViewById(R.id.profileSitzNumb);
        txtEmail = (TextView) view.findViewById(R.id.profileEmail);
        txtMatrikelNum = (TextView) view.findViewById(R.id.profilMatrikel);
        txtName = (TextView) view.findViewById(R.id.profileName);
        txtExam = (TextView) view.findViewById(R.id.profilExam);

        // laden der Sitzplatznummern in den Spinner und setzen
        int maxSitNumb = getResources().getInteger(R.integer.max_sitz_numb);
        String[] number = new String[maxSitNumb];
        for(int i=1; i <= number.length; i++){
            number[i-1] = String.valueOf(i);
        }
        ArrayAdapter<String> adapterSitNumber = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, number);
        adapterSitNumber.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSitzNumber.setAdapter(adapterSitNumber);

        // holen der user daten
        final User user = userLocalStore.getUserLogInUser();
        // setzen der Sitzplatznummer vom student
        spinnerSitzNumber.setSelection(Integer.valueOf(user.getSitNumb())-1);
        spinnerSitzNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // schauen ob sich die Daten geändert haben von der Sitzplatznummer
                // und speichern, dass die daten sich geändert haben
                if(!user.getSitNumb().equals(String.valueOf(position))) {
                    user.setDidChange(true);
                    user.setPassword(String.valueOf(position + 1));
                    userLocalStore.storeUserData(user);
                    System.out.println("Locally stored!");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        // Laden der restlichen Profildaten
        txtEmail.setText(user.getEmail());
        txtName.setText(user.getName());
        txtMatrikelNum.setText(user.getMatrikelnummer());
        txtExam.setText(user.getExam());
        return view;
    }


}

