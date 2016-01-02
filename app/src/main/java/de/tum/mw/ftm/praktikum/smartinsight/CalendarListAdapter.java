package de.tum.mw.ftm.praktikum.smartinsight;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Rebecca on 02.01.2016.
 */
public class CalendarListAdapter extends RecyclerView.Adapter<CalendarListAdapter.ViewHolder> {
    private final List<CalendarProvider> mValues;

    public interface customButtonListener {
        public void onButtonClickListner(int position,AnfrageProvider value);
    }

    public CalendarListAdapter(List<CalendarProvider> anfrageProvider) {
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
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public CalendarProvider mItem;

        public TextView name;
        public TextView room;
        public TextView date;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            name = (TextView) view.findViewById(R.id.calendar_name);
            room = (TextView) view.findViewById(R.id.calendar_place);
            date = (TextView) view.findViewById(R.id.calendar_date);
        }

    }
}
