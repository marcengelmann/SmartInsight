package de.tum.mw.ftm.praktikum.smartinsight;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Adapter für die KLausureinsichtstermin eliste
 */
public class CalendarListAdapter extends RecyclerView.Adapter<CalendarListAdapter.ViewHolder> {
    private final ArrayList<Calendar> mValues = new ArrayList<Calendar>();
    public CalendarListAdapter(List<Calendar> anfrageProvider) {
        mValues.addAll(anfrageProvider);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_list_calendar, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        String date = mValues.get(position).getDate();
        viewHolder.mItem = mValues.get(position);
        // get the acutal date
        SimpleDateFormat curFormater = new SimpleDateFormat("dd.MM.yyyy");
        Date examDate = null;
        try {
            examDate = curFormater.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date currentDay = new Date();
        if(examDate != null){
            // wenn an dem heutigen Tag eine Klausureinsicht ist, soll der Termin einen grünen
            // HIntergrund haben, sonst normal einen weißen
            if(examDate.getYear() == currentDay.getYear()
                    && examDate.getMonth() == currentDay.getMonth()
                    && examDate.getDay() == currentDay.getDay()){
                viewHolder.card.setCardBackgroundColor(Color.GREEN);
            } else{
                viewHolder.card.setCardBackgroundColor(Color.WHITE);

            }
        }
        // von dem aktuellen Klausureinsichttermin sollen die Werte gesetzt werden
        viewHolder.name.setText(mValues.get(position).getName());
        viewHolder.room.setText(mValues.get(position).getRoom());
        viewHolder.date.setText(date);
        viewHolder.numbOfReg.setText(mValues.get(position).getNumbOfRegistration());
        viewHolder.responsiblePerson.setText(mValues.get(position).getResponsiblePerson());
        viewHolder.mean.setText(mValues.get(position).getMean_grade());

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }
    public void updateData(ArrayList<Calendar> calendarArrayList){
        mValues.clear();
        mValues.addAll(calendarArrayList);
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public Calendar mItem;

        public TextView name;
        public TextView room;
        public TextView date;
        public TextView numbOfReg;
        public TextView responsiblePerson;
        public TextView mean;
        private CardView card;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            name = (TextView) view.findViewById(R.id.calendar_name);
            room = (TextView) view.findViewById(R.id.calendar_place);
            date = (TextView) view.findViewById(R.id.calendar_date);
            numbOfReg = (TextView) view.findViewById(R.id.calendar_numbOfReg);
            responsiblePerson = (TextView) view.findViewById(R.id.calendar_responsiblePerson);
            mean = (TextView) view.findViewById(R.id.calendar_mean);
            card = (CardView) view.findViewById(R.id.card_view_calendar_list);

        }

    }
}

