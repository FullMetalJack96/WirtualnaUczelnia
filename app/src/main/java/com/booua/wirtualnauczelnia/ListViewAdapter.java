package com.booua.wirtualnauczelnia;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class ListViewAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    JSONObject data;

    public ListViewAdapter(Context context, JSONObject arraylist) {
        this.context = context;
        data = arraylist;
    }

    @Override
    public int getCount() {
        return data.length();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        TextView from;
        TextView to;
        TextView subject;
        TextView lecturer;
        TextView room;
        TextView address;
        TextView type;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.listview_item, parent, false);


        from = (TextView) itemView.findViewById(R.id.from);
        to = (TextView) itemView.findViewById(R.id.to);
        subject = (TextView) itemView.findViewById(R.id.subject);
        lecturer = (TextView) itemView.findViewById(R.id.lecturer);
        room = (TextView) itemView.findViewById(R.id.room);
        address = (TextView) itemView.findViewById(R.id.address);
        type = (TextView) itemView.findViewById(R.id.type);


        try {
//            from.setText(data.getString(LoginActivity.FROM));
//        to.setText(data.getString(LoginActivity.TO));
        subject.setText(data.getString(LoginActivity.SUBJECT));
//        lecturer.setText(data.getString(LoginActivity.LECTURER));
//        room.setText(data.getString(LoginActivity.ROOM));
//        address.setText(data.getString(LoginActivity.ADDRESS));
//        type.setText(data.getString(LoginActivity.TYPE));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return itemView;
    }
}