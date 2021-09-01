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

import static java.lang.System.load;

public class MovieAdapter extends ArrayAdapter<Cards> {

    public MovieAdapter(Activity context, ArrayList<Cards> movieDetails){
        super(context,0,movieDetails);
    }

    public View getView(int position, View convertView, ViewGroup parent){

        View listItemView = convertView;
        if(listItemView==null){
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.match_list_row, parent, false
            );
        }

        Cards item = getItem(position);

        TextView movieId = (TextView)listItemView.findViewById(R.id.txtMovieHeader);
        movieId.setText(item.getName());
        TextView movieyear = (TextView)listItemView.findViewById(R.id.txtMovieYear);
        movieyear.setText(item.getYear());
        ImageView poster = (ImageView)listItemView.findViewById(R.id.listImagePoster);
        String posterPath = (String)item.getPosterUrl();
        Glide.with(listItemView).load(posterPath).into(poster);
        return listItemView;
    }
}
