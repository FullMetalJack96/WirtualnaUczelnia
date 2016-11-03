package com.booua.wirtualnauczelnia;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter {

    // Declare Variables
    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    HashMap<String, String> resultp = new HashMap<String, String>();

    public ListViewAdapter(Context context,
                           ArrayList<HashMap<String, String>> arraylist) {
        this.context = context;
        data = arraylist;
    }

    @Override
    public int getCount() {
        return data.size();
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
        // Declare Variables
        TextView date;
        TextView from;
        TextView to;
        TextView subject;
        TextView lecturer;
        TextView room;
        TextView address;
        TextView type;
        TextView passForm;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.listview_item, parent, false);
        // Get the position
        resultp = data.get(position);

        // Locate the TextViews in listview_item.xml

        date = (TextView) itemView.findViewById(R.id.date);
        from = (TextView) itemView.findViewById(R.id.from);
        to = (TextView) itemView.findViewById(R.id.to);
        subject = (TextView) itemView.findViewById(R.id.subject);
        lecturer = (TextView) itemView.findViewById(R.id.lecturer);
        room = (TextView) itemView.findViewById(R.id.room);
        address = (TextView) itemView.findViewById(R.id.address);
        type = (TextView) itemView.findViewById(R.id.type);
        passForm = (TextView) itemView.findViewById(R.id.passForm);


        // Capture position and set results to the TextViews
        date.setText(resultp.get(LoginActivity.DATE));
        from.setText(resultp.get(LoginActivity.FROM));
        to.setText(resultp.get(LoginActivity.TO));
        subject.setText(resultp.get(LoginActivity.SUBJECT));
        lecturer.setText(resultp.get(LoginActivity.LECTURER));
        room.setText(resultp.get(LoginActivity.ROOM));
        address.setText(resultp.get(LoginActivity.ADDRESS));
        type.setText(resultp.get(LoginActivity.TYPE));
        passForm.setText(resultp.get(LoginActivity.PASSFORM));
        // Capture position and set results to the ImageView
        // Passes flag images URL into ImageLoader.class
        // Capture ListView item click
        itemView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
//                // Get the position
//                resultp = data.get(position);
//                Intent intent = new Intent(context, SingleItemView.class);
//                // Pass all data rank
//                intent.putExtra("rank", resultp.get(MainActivity.RANK));
//                // Pass all data country
//                intent.putExtra("country", resultp.get(MainActivity.COUNTRY));
//                // Pass all data population
//                intent.putExtra("population",resultp.get(MainActivity.POPULATION));
//                // Pass all data flag
//                intent.putExtra("flag", resultp.get(MainActivity.FLAG));
//                // Start SingleItemView Class
//                context.startActivity(intent);

            }
        });
        return itemView;
    }
}