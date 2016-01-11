package de.tum.mw.ftm.praktikum.smartinsight;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Rebecca on 02.01.2016.
 */
public class CalendarListAdapter extends RecyclerView.Adapter<CalendarListAdapter.ViewHolder> {
    private final List<Calendar> mValues;

    public CalendarListAdapter(List<Calendar> anfrageProvider) {
        mValues = anfrageProvider;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_list_calendar, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.mItem = mValues.get(position);
        viewHolder.name.setText(mValues.get(position).name);
        viewHolder.room.setText(mValues.get(position).room);
        viewHolder.date.setText(mValues.get(position).date);
        viewHolder.numbOfReg.setText(mValues.get(position).numbOfRegistration);
        viewHolder.responsiblePerson.setText(mValues.get(position).responsiblePerson);
        viewHolder.meanGrade.setText(mValues.get(position).mean_grade);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public Calendar mItem;

        public TextView name;
        public TextView room;
        public TextView date;
        public TextView numbOfReg;
        public TextView responsiblePerson;
        public TextView meanGrade;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            name = (TextView) view.findViewById(R.id.calendar_name);
            room = (TextView) view.findViewById(R.id.calendar_place);
            date = (TextView) view.findViewById(R.id.calendar_date);
            numbOfReg = (TextView) view.findViewById(R.id.calendar_numbOfReg);
            responsiblePerson = (TextView) view.findViewById(R.id.calendar_responsiblePerson);
            meanGrade = (TextView) view.findViewById(R.id.calendar_mean);
        }

    }
}
