package com.example.test3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class UrlAdapter extends ArrayAdapter<String> {
    public UrlAdapter(Context context, List<String> urls) {
        super(context, 0, urls);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String url = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.textView);
        textView.setText(url);

        return convertView;
    }
}
