package de.tum.mw.ftm.praktikum.smartinsight;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;


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
    public static SettingsFragment newInstance(String param1, String param2) {
        return new SettingsFragment();
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
        view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        spinnerSitzNumber = (Spinner) view.findViewById(R.id.profileSitzNumb);
        txtEmail = (TextView) view.findViewById(R.id.profileEmail);
        txtMatrikelNum = (TextView) view.findViewById(R.id.profilMatrikel);
        txtName = (TextView) view.findViewById(R.id.profileName);
        txtExam = (TextView) view.findViewById(R.id.profilExam);

        int maxSitNumb = getResources().getInteger(R.integer.max_sitz_numb);
        String[] number = new String[maxSitNumb];
        for(int i=1; i <= number.length; i++){
            number[i-1] = String.valueOf(i);
        }
        ArrayAdapter<String> adapterSitNumber = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, number);
        adapterSitNumber.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSitzNumber.setAdapter(adapterSitNumber);
        final User user = userLocalStore.getUserLogInUser();
        spinnerSitzNumber.setSelection(Integer.valueOf(user.sitNumb)-1);
        spinnerSitzNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if(!user.sitNumb.equals(String.valueOf(position))) {
                    user.didChange = true;
                    user.sitNumb = String.valueOf(position+1);
                    userLocalStore.storeUserData(user);
                    System.out.println("Locally stored!");
                }
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            userLocalStore.setUserStatusProfilPic(false);
            if (requestCode == 1) {

                File f = new File(Environment.getExternalStorageDirectory().toString());

                for (File temp : f.listFiles()) {

                    if (temp.getName().equals("temp.jpg")) {

                        f = temp;
                        Uri uri = Uri.fromFile(f);
                        userLocalStore.setUserProfilPic(uri);
                        userLocalStore.setUserStatusProfilPic(true);

                        break;

                    }

                }

            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();

                userLocalStore.setUserProfilPic(selectedImage);
                userLocalStore.setUserStatusProfilPic(true);
            }
        }
    }

}
