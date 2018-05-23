package com.example.baptiste.smartcity.adapters;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.baptiste.smartcity.fragments.NewsFragment;

import com.example.baptiste.smartcity.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;


public class ListConversationAdapter extends BaseAdapter {
    private Fragment fragment;
    private ArrayList<HashMap<String, String>> data;

    public ListConversationAdapter(Fragment f, ArrayList<HashMap<String, String>> d) {
        fragment = f;
        data=d;
    }
    public int getCount() {
        return data.size();
    }
    public Object getItem(int position) {
        return position;
    }
    public long getItemId(int position) {
        return position;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        ListNewsViewHolder holder = null;
        if (convertView == null) {
            holder = new ListNewsViewHolder();
            convertView = LayoutInflater.from(fragment.getContext()).inflate(R.layout.conversations_list, parent, false);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.sdetails = (TextView) convertView.findViewById(R.id.sdetails);
            convertView.setTag(holder);
        } else {
            holder = (ListNewsViewHolder) convertView.getTag();
        }
        holder.title.setId(position);
        holder.sdetails.setId(position);

        HashMap<String, String> song = data.get(position);

        holder.title.setText(song.get(NewsFragment.KEY_TITLE));
        holder.sdetails.setText(song.get(NewsFragment.KEY_DESCRIPTION));
        return convertView;
    }

    public static class ListNewsViewHolder {
        TextView title, sdetails;
    }
}

