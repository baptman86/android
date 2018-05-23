package com.example.baptiste.smartcity.adapters;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.baptiste.smartcity.R;
import com.example.baptiste.smartcity.fragments.AnnoncesFragment;

import java.util.ArrayList;
import java.util.HashMap;

public class ListAnnoncesAdapter extends BaseAdapter {
    private Fragment fragment;
    private ArrayList<HashMap<String, String>> data;

    public ListAnnoncesAdapter(Fragment f, ArrayList<HashMap<String, String>> d) {
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
        ListAnnoncesAdapter.ListAnnoncesViewHolder holder = null;
        if (convertView == null) {
            holder = new ListAnnoncesAdapter.ListAnnoncesViewHolder();
            convertView = LayoutInflater.from(fragment.getContext()).inflate(R.layout.annonces_list, parent, false);
            holder.title = (TextView) convertView.findViewById(R.id.titleListAnn);
            holder.description = (TextView) convertView.findViewById(R.id.descListAnn);
            holder.date_debut = (TextView) convertView.findViewById(R.id.dateDebListAnn);
            holder.date_fin = (TextView) convertView.findViewById(R.id.dateFinListAnn);
            holder.prix = (TextView) convertView.findViewById(R.id.priceListAnn);
            holder.lieu = (TextView) convertView.findViewById(R.id.lieuListAnn);
            convertView.setTag(holder);
        } else {
            holder = (ListAnnoncesAdapter.ListAnnoncesViewHolder) convertView.getTag();
        }
        holder.title.setId(position);
        holder.description.setId(position);
        holder.date_debut.setId(position);
        holder.date_fin.setId(position);
        holder.prix.setId(position);
        holder.lieu.setId(position);

        HashMap<String, String> song = data.get(position);

        holder.title.setText(song.get(AnnoncesFragment.KEY_TITLE));
        holder.description.setText(song.get(AnnoncesFragment.KEY_DESCRIPTION));
        holder.date_debut.setText(song.get(AnnoncesFragment.KEY_DATE_DEBUT));
        holder.date_fin.setText(song.get(AnnoncesFragment.KEY_DATE_FIN));
        holder.prix.setText(song.get(AnnoncesFragment.KEY_PRIX));
        holder.lieu.setText(song.get(AnnoncesFragment.KEY_LIEU));
        return convertView;
    }

    public static class ListAnnoncesViewHolder {
        TextView title, description, date_debut, date_fin, prix, lieu;
    }
}
