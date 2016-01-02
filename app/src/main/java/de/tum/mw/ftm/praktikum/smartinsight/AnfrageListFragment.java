package de.tum.mw.ftm.praktikum.smartinsight;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class AnfrageListFragment extends Fragment implements AnfrageListAdapter.customButtonListener{

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    AnfrageListAdapter adapter;
    TextView txtIntroduction;
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AnfrageListFragment() {
    }

    public static AnfrageListFragment newInstance(int columnCount) {
        AnfrageListFragment fragment = new AnfrageListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

        adapter = new AnfrageListAdapter(anfrageProviders, this);

    }
    RecyclerView recyclerView = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_anfrage_list, container, false);

        // Set the adapter
            Context context = view.getContext();
            txtIntroduction = (TextView) view.findViewById(R.id.txtInfo);
            recyclerView = (RecyclerView) view.findViewById(R.id.list);
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            // Create the adapter to convert the array to views
            recyclerView.setAdapter(adapter);


        return view;
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
    static  ArrayList<AnfrageProvider> anfrageProviders =  new ArrayList<AnfrageProvider>();

    public void updateFragmentListView(ArrayList<AnfrageProvider> requests) {
        anfrageProviders.clear();
        if (requests.isEmpty()) {
            txtIntroduction.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        else{
            txtIntroduction.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            for (AnfrageProvider request : requests) {
                anfrageProviders.add(new AnfrageProvider(request.id, request.startTime, request.endTime, request.taskNumber, request.taskSubNumber, request.question, request.editor));
            }
        }
        if (recyclerView != null){
            // Create the adapter to convert the array to views
            recyclerView.setAdapter(adapter);
        }


    }

    @Override
    public void onButtonClickListner(int position, AnfrageProvider value) {
        String msg = "Möchtest du die Anfrage zur Aufgabe " + value.getTaskNumber() + value.getTaskSubNumber() + " löschen?" ;
        String title = "Anfrage löschen?";

        finalDialog(title,msg, position,value).show();
    }

    private Dialog finalDialog(String title,String msg, final int position, final AnfrageProvider value){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(msg);
        builder.setTitle(title);
        builder.setPositiveButton("Ja", new DialogInterface.OnClickListener
                () {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                mListener.onListFragmentDeleteListItem(position, value);
            }
        });
        builder.setNegativeButton("Nein", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.setCancelable(false);
        return builder.create();    }


    public interface OnListFragmentInteractionListener {
        void onListFragmentDeleteListItem(int position, AnfrageProvider value);
    }
}
