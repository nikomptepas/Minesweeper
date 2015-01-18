package com.example.ancelnicolas91.minesweeper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Justine on 18/01/2015.
 */
public class ScoresAdapter extends BaseAdapter {

    List<Scores> myScores;

    LayoutInflater inflater;

    public ScoresAdapter(Context context,List<Scores> myScores) {
        inflater = LayoutInflater.from(context);
        this.myScores = myScores;
    }

    @Override
    public int getCount() {
        return myScores.size();
    }

    @Override
    public Object getItem(int position) {
        return myScores.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView textView_pseudo;
        TextView textView_cases;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if(convertView == null) {
            holder = new ViewHolder();
          //  convertView = inflater.inflate(R.layout.itemlivre, null);

            holder.textView_pseudo = (TextView)convertView.findViewById(R.id.textView_pseudo);
            holder.textView_cases = (TextView)convertView.findViewById(R.id.textView_cases);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

       // holder.textView_pseudo.setText(myScores.get(position).getTitre());
       // holder.textView_cases.setText(myScores.get(position).getAuteur());

        return convertView;

    }

}
