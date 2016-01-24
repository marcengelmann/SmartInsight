package de.tum.mw.ftm.praktikum.smartinsight;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/*
* Fragment welches die Anfragen der Studente auflistet mit den Informatinen,
* wann die dozenten kommen
 */
public class RequestsPhdListFragment extends Fragment implements RequestsPhdListAdapter.customButtonListener{
    private SwipeRefreshLayout swipeContainer;
    private static final String ARG_COLUMN_COUNT = "column-count";
    private RequestsPhdListAdapter adapter;
    private TextView txtIntroduction;
    private OnListFragmentInteractionListener mListener;
    private ArrayList<RequestsPhd> listAnfrageProvider = new ArrayList<RequestsPhd>();
    private Handler handlerRefreshList = new Handler();
    private Runnable runnableRefreshList = null;

    public RequestsPhdListFragment() {
    }

    public static RequestsPhdListFragment newInstance(int columnCount) {
        RequestsPhdListFragment fragment = new RequestsPhdListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listAnfrageProvider.clear();
        // hol die Liste für die Anfragen
        if (getArguments() != null) {
            listAnfrageProvider = (ArrayList<RequestsPhd>)getArguments().get(String.valueOf(R.string.bundleRequests));

        }

        adapter = new RequestsPhdListAdapter(listAnfrageProvider, this);

    }
    RecyclerView recyclerView = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_requests_phd_list, container, false);
        // setzte den swip container und aktualisiere die Liste, wenn er betätigt wird
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

    // Update der Anfrage lsite
    private void refreshListView(){
        //Adapter für die Anfrage liste bescheid geben, dass sich daten geändert haben.
        adapter.notifyDataSetChanged();
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

    public void updateFragmentListView(ArrayList<RequestsPhd> requests) {
        // überprüfe ob keine Anfragen vorhanden sind, sind keien Anfragen vorhanden,
        // dann setz die Liste unsichtbar und einen Einleitungstext true
        if (requests.isEmpty()) {
            txtIntroduction.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        else{
            txtIntroduction.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter.updateData(requests);
            adapter.notifyDataSetChanged();
        }
    }

    // Nachfrage, ob die ausgewählte Anfrage wirklich gelöscht oder erneurt werden soll
    @Override
    public void onButtonClickListner(int position, RequestsPhd value, Boolean deleteNupdate) {
        String msg = "Möchtest du die Anfrage zur Aufgabe " + value.getTaskNumber() + value.getTaskSubNumber() + " löschen?" ;
        String title = "Anfrage löschen?";

        if(!deleteNupdate){
            msg = "Möchtest du eine neue Anfrage zur Aufgabe " + value.getTaskNumber() + value.getTaskSubNumber() + " senden?" ;
            title = "Anfrage erneuern?";
        }

        finalDialog(title,msg, position,value, deleteNupdate).show();
    }

    // Methode, die die post delay aktualisiert in abhängigkeit, wie langee es noch bis zur
    // nächsten Anfrage dauert
    @Override
    public void refreshListListener(int position, long timer) {
        handlerRefreshList.removeCallbacks(runnableRefreshList);
        runnableRefreshList = new Runnable() {
            @Override
            public void run() {
                adapter.setRefreshActive(false);
                refreshListView();
            }
        };
        handlerRefreshList.postDelayed(runnableRefreshList, timer);
        Log.d("test", "Update in ms. " + timer);
    }

    private Dialog finalDialog(String title,String msg, final int position, final RequestsPhd value, final Boolean deletNuPdate){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(msg);
        builder.setTitle(title);
        builder.setPositiveButton("Ja", new DialogInterface.OnClickListener
                () {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                mListener.onListFragmentDeleteListItem(position, value, deletNuPdate);
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
        void onListFragmentDeleteListItem(int position, RequestsPhd value, Boolean deleteNupdate);
    }
}
