package com.example.nav.nav_electri;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Attraction_Adapter extends BaseAdapter {

    Context context;
    ArrayList<AttractionPosition> data;
    private static LayoutInflater inflater = null;

    public Attraction_Adapter(Context context, ArrayList<AttractionPosition> data) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.attractions_item_list, null);
        TextView attr_item_title = (TextView) vi.findViewById(R.id.attr_item_title);
        attr_item_title.setText(data.get(position).getTitle());


        if(data.get(position).getSponsor() == true){
            TextView attr_sponsor = (TextView) vi.findViewById(R.id.attr_sponsor);
            attr_sponsor.setText(this.context.getResources().getString(R.string.sponsor_holder));
        } else {
            TextView attr_sponsor = (TextView) vi.findViewById(R.id.attr_sponsor);
            attr_sponsor.setText("");
        }


        TextView attr_item_snip = (TextView) vi.findViewById(R.id.attr_item_snip);
        attr_item_snip.setText(data.get(position).getSnip());

        TextView attr_item_distance = (TextView) vi.findViewById(R.id.attr_item_distance);
        double tmp_dist = data.get(position).getDistance();
        if(tmp_dist == -1){
            attr_item_distance.setText(this.context.getResources().getString(R.string.distance)+" ??");
        } else if(tmp_dist < 1000) {
            attr_item_distance.setText(this.context.getResources().getString(R.string.distance)+" "+String.format("%.0f",tmp_dist)+"m");
        } else {
            tmp_dist = tmp_dist / 1000;
            attr_item_distance.setText(this.context.getResources().getString(R.string.distance)+" "+String.format("%.2f",tmp_dist)+"km");
        }


        TextView attr_item_open_hours = (TextView) vi.findViewById(R.id.attr_item_open_hours);
        attr_item_open_hours.setText(this.context.getResources().getString(R.string.no_open_hours_holder)+" "+ data.get(position).getToday_open_hours());

        TextView attr_item_telephone = (TextView) vi.findViewById(R.id.attr_item_telephone);
        attr_item_telephone.setText(this.context.getResources().getString(R.string.phone_number_holder)+" "+data.get(position).getTelephone());
        return vi;
    }
}