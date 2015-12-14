package de.tum.mw.ftm.praktikum.smartinsight;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Rebecca on 13.12.2015.
 */
public class AnfrageAdapter extends ArrayAdapter<AnfrageProvider> {
    // View lookup cache
    private static class ViewHolder {
        TextView startTime;
        TextView endTime;
        TextView question;
        TextView editor;
        TextView taskNumber;
        TextView taskSubNumber;
    }

    public AnfrageAdapter(Context context, ArrayList<AnfrageProvider> users) {
        super(context, R.layout.item_anfrage, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        AnfrageProvider anfrage = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_anfrage, parent, false);
            viewHolder.editor = (TextView) convertView.findViewById(R.id.editor);
            viewHolder.endTime = (TextView) convertView.findViewById(R.id.endTime);
            viewHolder.question = (TextView) convertView.findViewById(R.id.question);
            viewHolder.startTime = (TextView) convertView.findViewById(R.id.startTime);
            viewHolder.taskNumber = (TextView) convertView.findViewById(R.id.taskNumber);
            viewHolder.taskSubNumber = (TextView) convertView.findViewById(R.id.taskSubNumber);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        viewHolder.editor.setText(anfrage.editor);
        viewHolder.endTime.setText(anfrage.endTime);
        viewHolder.startTime.setText(anfrage.startTime);
        viewHolder.question.setText(anfrage.question);
        viewHolder.taskNumber.setText(anfrage.taskNumber);
        viewHolder.taskSubNumber.setText(anfrage.taskSubNumber);
        // Return the completed view to render on screen
        return convertView;
    }
}
