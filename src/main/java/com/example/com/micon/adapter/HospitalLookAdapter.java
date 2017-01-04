package com.example.com.micon.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.micon.entity.ListModel;
import com.example.micron.myapplication.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Giasuddin on 9/1/2015.
 */
public class HospitalLookAdapter extends BaseAdapter {

    ArrayList<ListModel> mStringFilterList;
    private Context context;
    ArrayList<ListModel> alist = null;
    LayoutInflater inflater;

    public HospitalLookAdapter(Context context, ArrayList<ListModel> alist) {

        this.context = context;
        this.alist = alist;
        this.mStringFilterList = new ArrayList<ListModel>();
        mStringFilterList.addAll(alist);
    }

    @Override
    public int getCount() {
        return alist.size();
    }

    @Override
    public Object getItem(int position) {
        return alist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return alist.indexOf(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView area;
        TextView addressphone;
        ImageView image;

        ListModel l = (ListModel)alist.get(position);

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.hospitallook, parent, false);
        // Get the position
        //tempValues = alist.get(position);

        // Locate the TextViews in listview_item.xml

        area = (TextView) itemView.findViewById(R.id.area);
        addressphone = (TextView) itemView.findViewById(R.id.addressphn);


        // Locate the ImageView in listview_item.xml
        image = (ImageView) itemView.findViewById(R.id.imageviews);

        // Capture position and set results to the TextViews

        area.setText(l.getHname());
        addressphone.setText(l.getAddress());
        image.setImageResource(l.getImgid());

        return itemView;
    }

    public void filter(String text) {
        text = text.toLowerCase(Locale.getDefault());
        alist.clear();
        if (text.length() == 0) {
            alist.addAll(mStringFilterList);
        } else {
            for (ListModel wp : mStringFilterList) {
                if (wp.getAddress().toLowerCase(Locale.getDefault()).contains(text)) {
                    alist.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

   /*
    public void filter1(String dt) {
        dt = dt.toLowerCase(Locale.getDefault());
        alist.clear();
        if (dt.length() == 0) {
            alist.addAll(mStringFilterList);
        } else {
            for (ListModel wp : mStringFilterList) {
                if (wp.getAddress().toLowerCase(Locale.getDefault()).contains(dt)) {
                    alist.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
    */
}

