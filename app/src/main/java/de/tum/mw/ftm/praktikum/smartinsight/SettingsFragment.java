package de.tum.mw.ftm.praktikum.smartinsight;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class SettingsFragment extends Fragment {
    private Spinner spinnerSitzNumber;
    private TextView txtMatrikelNum;
    private TextView txtName;
    private TextView txtEmail;
    private TextView txtExam;
    private Button btnUploadFoto;
    CircleImageView profileImage;
    UserLocalStore userLocalStore;
    private OnListFragmentInteractionListener mListener;
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
        view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        spinnerSitzNumber = (Spinner) view.findViewById(R.id.profileSitzNumb);
        txtEmail = (TextView) view.findViewById(R.id.profileEmail);
        txtMatrikelNum = (TextView) view.findViewById(R.id.profilMatrikel);
        txtName = (TextView) view.findViewById(R.id.profileName);
        txtExam = (TextView) view.findViewById(R.id.profilExam);
        profileImage = (CircleImageView) view.findViewById(R.id.profileImage);
        btnUploadFoto = (Button) view.findViewById(R.id.btnUploadFoto);
        btnUploadFoto.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                onClickUploadFoto(v);

            }

        });
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
        updateProfilPic();
        return view;
    }

    public void onClickUploadFoto(View view){
        final CharSequence[] options = { "Foto aufnehmen", "Foto aus Gallerie auswählen","Abbrechen" };

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Neues Profilbild Hochladen!");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Foto aufnehmen"))

                {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");

                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));

                    startActivityForResult(intent, 1);

                } else if (options[item].equals("Foto aus Gallerie auswählen"))

                {

                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(intent, 2);


                } else if (options[item].equals("Abbrechen")) {

                    dialog.dismiss();

                }

            }

        });

        builder.show();
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
        updateProfilPic();

    }

    private void updateProfilPic() {
        if (userLocalStore.getUserStatusProfilPic()){
            User user = userLocalStore.getUserLogInUser();
            profileImage.setImageURI(userLocalStore.getUserProfilPic());
            mListener.onListFragmentUpdateProfilePic();
        }
        else {
            profileImage.setImageResource(R.mipmap.ic_launcher_fernrohr);
        }

    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentUpdateProfilePic();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
