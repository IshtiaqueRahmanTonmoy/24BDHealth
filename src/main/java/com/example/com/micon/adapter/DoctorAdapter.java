package com.example.com.micon.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.micon.entity.ListModel;
import com.example.micon.entity.ListModelDoctor;
import com.example.micron.myapplication.DoctorSearchActivity;
import com.example.micron.myapplication.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DoctorAdapter extends BaseAdapter {
    int x = 0;
    int le = 0;
    String s = null;
    String parts = null;
    String st;
    int l;
    String[] str;
    String[] ss;
    private ArrayList<ListModelDoctor> mOriginalValues;
    private Context context;
    ArrayList<ListModelDoctor> alist = null;
    LayoutInflater inflater;
    PopupMenu popupMenu;

    public DoctorAdapter(Context context, ArrayList<ListModelDoctor> alist) {
        this.context = context;
        this.alist = alist;
        this.mOriginalValues = new ArrayList<ListModelDoctor>();
        mOriginalValues.addAll(alist);
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
        // convert view = design

        View rowView = convertView;
        final ViewHolder holder;
        if (rowView == null) {

            holder = new ViewHolder();
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            rowView = inflater.inflate(R.layout.drappoint, parent, false);

            holder.name = (TextView) rowView.findViewById(R.id.dname);
            holder.hosname = (TextView) rowView.findViewById(R.id.hospital);
            holder.designation = (TextView) rowView.findViewById(R.id.designation);
            holder.address = (TextView) rowView.findViewById(R.id.add);
            holder.time = (TextView) rowView.findViewById(R.id.time);
            holder.day = (TextView) rowView.findViewById(R.id.date);
            holder.image = (ImageView) rowView.findViewById(R.id.imageview);

            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    popupMenu = new PopupMenu(context, v);
                    st = holder.address.getText().toString();
                    l = st.split("/").length;
                    str = st.split("/");
                    for(int i=0;i<l;i++) {
                        parts = str[i];
                    }
                    le = parts.split(",").length;
                    ss = parts.split(",");

                    if(parts.contains("01") || parts.contains("89") || parts.contains("96") || parts.contains("93")  || parts.contains("81") || parts.contains("90"))
                    {
                        String conf = "Select numbers for appoinment...";
                        SpannableStringBuilder builder = new SpannableStringBuilder();
                        SpannableString redSpannable= new SpannableString(conf);
                        redSpannable.setSpan(new ForegroundColorSpan(Color.RED), 0, conf.length(), 0);
                        builder.append(redSpannable);

                        popupMenu.getMenu().add(builder);

                        for(x=0; x<le; x++) {
                            popupMenu.getMenu().add(Menu.NONE, x, Menu.NONE, ss[x]);
                        }

                        popupMenu.show();
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                //Toast.makeText(context, item.getTitle(), Toast.LENGTH_SHORT).show();
                                //return true;
                                String phone = item.getTitle().toString();
                                Intent intent = new Intent(Intent.ACTION_CALL);
                                if(phone.length() == 12){
                                    intent.setData(Uri.parse("tel:" + phone));
                                    context.startActivity(intent);
                                }
                                else{
                                    intent.setData(Uri.parse("tel:"+"02"+phone));
                                    context.startActivity(intent);
                                }
                                return false;
                            }
                        });
                    }
                 }
            });
                    rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }


        ListModelDoctor ldoc = (ListModelDoctor) alist.get(position);

        holder.name.setText(ldoc.getName());
        holder.hosname.setText(ldoc.getHospital());
        holder.designation.setText(ldoc.getDesignation());
        holder.address.setText(ldoc.getAddress());
        holder.image.setImageResource(R.drawable.icon);
        holder.time.setText(ldoc.getTime());
        holder.day.setText(ldoc.getDay());

        return rowView;
    }


    public void filter(String text) {
        text = text.toLowerCase(Locale.getDefault());
        alist.clear();
        if (text.length() == 0) {
            alist.addAll(mOriginalValues);
        } else {
            for (ListModelDoctor wp : mOriginalValues) {
                if (wp.getAddress().toLowerCase(Locale.getDefault()).contains(text)) {
                    alist.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void filter1(String dists) {
        dists = dists.toLowerCase(Locale.getDefault());
        alist.clear();
        if (dists.length() == 0) {
            alist.addAll(mOriginalValues);
        } else {
            for (ListModelDoctor wp : mOriginalValues) {
                if (wp.getAddress().toLowerCase(Locale.getDefault()).contains(dists)) {
                    alist.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void filter3(String ext) {
        ext = ext.toLowerCase(Locale.getDefault());
        alist.clear();
        if (ext.length() == 0) {
            alist.addAll(mOriginalValues);
        }
        else {
            for (ListModelDoctor wp : mOriginalValues) {
                if (wp.getDesignation().toLowerCase(Locale.getDefault()).contains(ext) || wp.getAddress().toLowerCase(Locale.getDefault()).contains(ext)) {
                    alist.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void filterHos(String dist, String area) {

        dist = dist.toLowerCase(Locale.getDefault());
        area = area.toLowerCase(Locale.getDefault());

        alist.clear();
        if (dist.length() == 0) {
            alist.addAll(mOriginalValues);
        }
        else if(area.length() == 0)
        {
            alist.addAll(mOriginalValues);
        }
        else {
            for (ListModelDoctor wp : mOriginalValues) {
                if (wp.getName().toLowerCase(Locale.getDefault()).contains(dist)) {
                    alist.add(wp);
                }
                else if(wp.getAddress().toLowerCase(Locale.getDefault()).contains(area))
                {
                    alist.add(wp);
                }
            }
        }
        notifyDataSetChanged();

    }

    private class ViewHolder {

        TextView name;
        TextView hosname;
        TextView designation;
        TextView address;
        TextView time;
        TextView day;
        ImageView image;
    }
}


//