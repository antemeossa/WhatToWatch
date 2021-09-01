package com.example.movie_tinder;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class friendAdapter extends ArrayAdapter<String> {

    public friendAdapter(Activity context, ArrayList<String> friendsList){
        super(context,0,friendsList);
    }

    public View getView(int position, View convertView, ViewGroup parent){

        View listItemView = convertView;
        if(listItemView==null){
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.friend_row, parent, false
            );
        }

        String item = getItem(position);

        TextView friendName = (TextView)listItemView.findViewById(R.id.friendsNameList);
        friendName.setText(item);

        return listItemView;
    }
}
