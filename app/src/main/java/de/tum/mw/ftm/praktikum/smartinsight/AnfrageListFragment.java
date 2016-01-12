package de.tum.mw.ftm.praktikum.smartinsight;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.os.Handler;
import android.widget.Toast;

import android.text.format.Time;
import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class AnfrageListFragment extends Fragment implements AnfrageListAdapter.customButtonListener{
    private SwipeRefreshLayout swipeContainer;
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    AnfrageListAdapter adapter;
    TextView txtIntroduction;
    private OnListFragmentInteractionListener mListener;
    ArrayList<AnfrageProvider> listAnfrageProvider = new ArrayList<AnfrageProvider>();
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
        listAnfrageProvider.clear();
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            listAnfrageProvider = (ArrayList<AnfrageProvider>)getArguments().get("requests");

        }

        adapter = new AnfrageListAdapter(anfrageProviders, this);

    }
    RecyclerView recyclerView = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_anfrage_list, container, false);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContRequestList);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshListView();
            }

        });
        txtIntroduction = (TextView) view.findViewById(R.id.txtInfo);
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        // Create the adapter to convert the array to views
        recyclerView.setAdapter(adapter);
        updateFragmentListView(listAnfrageProvider);
        return view;
    }

    private void refreshListView(){
        //Nur die Liste refreshen wenn auch anfragen vorhanden sind
        if(recyclerView != null && !anfrageProviders.isEmpty())
        {
            adapter.setStatusPositon(false);
            //Adapter für die Anfrage liste bescheid geben, dass sich daten geändert haben.
            adapter.notifyDataSetChanged();
        }
        swipeContainer.setRefreshing(false);
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
        adapter.setStatusPositon(false);
        if (requests.isEmpty()) {
            txtIntroduction.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        else{
            txtIntroduction.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            anfrageProviders.addAll(requests);
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onButtonClickListner(int position, AnfrageProvider value) {
        String msg = "Möchtest du die Anfrage zur Aufgabe " + value.getTaskNumber() + value.getTaskSubNumber() + " löschen?" ;
        String title = "Anfrage löschen?";

        finalDialog(title,msg, position,value).show();
    }

    @Override
    public void refreshListListener(int position, long timer) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshListView();
            }
        }, timer);
        // Todo scroll to position
        Log.d("test","Update in ms. " +timer);
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
