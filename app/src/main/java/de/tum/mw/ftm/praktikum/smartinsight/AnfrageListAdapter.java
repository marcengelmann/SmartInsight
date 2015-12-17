package de.tum.mw.ftm.praktikum.smartinsight;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.List;


public class AnfrageListAdapter extends RecyclerView.Adapter<AnfrageListAdapter.ViewHolder> {

    private final List<AnfrageProvider> mValues;
    customButtonListener customListner;


    public interface customButtonListener {
        public void onButtonClickListner(int position,AnfrageProvider value);
    }

    public AnfrageListAdapter(List<AnfrageProvider> anfrageProvider , customButtonListener listener) {
        mValues = anfrageProvider;
        customListner = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_list_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder viewHolder,final int position) {
        viewHolder.mItem = mValues.get(position);
        viewHolder.editor.setText(mValues.get(position).editor);
        viewHolder.endTime.setText(mValues.get(position).endTime);
        viewHolder.startTime.setText(mValues.get(position).startTime);
        viewHolder.question.setText(mValues.get(position).question);
        viewHolder.taskNumber.setText(mValues.get(position).taskNumber);
        viewHolder.taskSubNumber.setText(mValues.get(position).taskSubNumber);
        viewHolder.listViewButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (customListner != null) {
                    customListner.onButtonClickListner(position, viewHolder.mItem);
                }
            }
        });
    }



    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public AnfrageProvider mItem;

        public TextView startTime;
        public TextView endTime;
        public TextView question;
        public TextView editor;
        public TextView taskNumber;
        public TextView taskSubNumber;
        public FloatingActionButton listViewButton;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            //mIdView = (TextView) view.findViewById(R.id.id);
            //mContentView = (TextView) view.findViewById(R.id.content);
            editor = (TextView) view.findViewById(R.id.editor);
            endTime = (TextView) view.findViewById(R.id.endTime);
            question = (TextView) view.findViewById(R.id.question);
            startTime = (TextView) view.findViewById(R.id.startTime);
            taskNumber = (TextView) view.findViewById(R.id.taskNumber);
            taskSubNumber = (TextView) view.findViewById(R.id.taskSubNumber);
            listViewButton = (FloatingActionButton) view.findViewById(R.id.listViewButton);
        }

    }
}
