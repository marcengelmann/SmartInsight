package de.tum.mw.ftm.praktikum.smartinsight;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class CalendarFragment extends Fragment {
    private static final String ARG_COLUMN_COUNT = "column-count";
    private CalendarListAdapter adapter;
    private TextView txtIntroduction;
    private RecyclerView list_calendar;
    private ArrayList<Calendar> listCalendar = new ArrayList<Calendar>();

    public CalendarFragment() {
    }

    public static CalendarFragment newInstance(int columnCount) {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listCalendar.clear();
        if (getArguments() != null) {
            // get bundle
            listCalendar = (ArrayList<Calendar>)getArguments().get(String.valueOf(R.string.calendar));
        }

        adapter = new CalendarListAdapter(listCalendar);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        // Set the adapter
        Context context = view.getContext();
        txtIntroduction = (TextView) view.findViewById(R.id.txtInfo);
        list_calendar = (RecyclerView) view.findViewById(R.id.list);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        // Create the adapter to convert the array to views
        recyclerView.setAdapter(adapter);
        updateFragmentListView(listCalendar);

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void updateFragmentListView(ArrayList<Calendar> listItems) {
        // wenn keine Klausureinsichtstermine vorhanden ist, soll ein defualt text geladen werden
        if (listItems.isEmpty()) {
            txtIntroduction.setVisibility(View.VISIBLE);
            list_calendar.setVisibility(View.GONE);
        } else {
            txtIntroduction.setVisibility(View.GONE);
            list_calendar.setVisibility(View.VISIBLE);
            adapter.updateData(listItems);
            adapter.notifyDataSetChanged();
        }

    }


}