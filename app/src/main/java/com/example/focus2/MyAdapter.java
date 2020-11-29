package com.example.focus2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyAdapter extends ArrayAdapter<String> {

    Context context;
    String[] rTitle;
    String[] rDescription;

    public MyAdapter(Context context, String[] title, String[] description) {
        super(context, R.layout.row, R.id.title, title);
        this.context = context;
        this.rTitle = title;
        this.rDescription = description;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View row = layoutInflater.inflate(R.layout.row, parent, false);
        TextView myTitle = row.findViewById(R.id.title);
        TextView myDescription = row.findViewById(R.id.subTitle);

        myTitle.setText(rTitle[position]);
        myDescription.setText(rDescription[position]);


        return row;
    }
}
