package com.example.focus2;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MyAdapter extends ArrayAdapter<Task> {

    Context context;
    ArrayList<Task> task;

    public MyAdapter (Context context, ArrayList<Task> taskList) {
        super(context, R.layout.row, taskList);
        this.task = taskList;
        this.context = context;

    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View row = layoutInflater.inflate(R.layout.row, parent, false);
        TextView myTitle = row.findViewById(R.id.title);
        TextView myDescription = row.findViewById(R.id.subTitle);

        myTitle.setText(task.get(position).getName());
        myDescription.setText(task.get(position).getAlarm());


        return row;
    }

}
