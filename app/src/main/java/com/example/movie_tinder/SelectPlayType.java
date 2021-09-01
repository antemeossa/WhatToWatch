package com.example.movie_tinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class SelectPlayType extends AppCompatActivity {

    private Button popularBtn;
    private Button topRatedBtn;
    private Button latestBtn;
    private Button nowPlayingBtn;
    private Button upComingBtn;
    private Button backBtn;
    private String listType;
    static private String JSON_URL;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_play_type);

        popularBtn = (Button) findViewById(R.id.popularBtn);
        topRatedBtn = (Button) findViewById(R.id.topRatedBtn);

        nowPlayingBtn = (Button)findViewById(R.id.nowPlayingBtn);
        upComingBtn = (Button)findViewById(R.id.upcomingBtn);
        backBtn = (Button)findViewById(R.id.backBtnSelectPlayType);

        if(SettingsActivity.isTurkish){
            popularBtn.setText("Popüler");
            topRatedBtn.setText("En İyiler");
            nowPlayingBtn.setText("Vizyondakiler");
            upComingBtn.setText("Pek Yakında");
            backBtn.setText("Geri");
        }


        popularBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listType = "popular";
                JSON_URL = "https://api.themoviedb.org/3/movie/"+listType+"?api_key=9f1d229c1a82490afe903ee4b19c97c0";
                Intent intent = new Intent(SelectPlayType.this, MainActivity.class);
                startActivity(intent);
                finish();
                return;

            }
        });

        topRatedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listType = "top_rated";
                JSON_URL = "https://api.themoviedb.org/3/movie/"+listType+"?api_key=9f1d229c1a82490afe903ee4b19c97c0";
                Intent intent = new Intent(SelectPlayType.this, MainActivity.class);
                startActivity(intent);
                finish();
                return;

            }
        });



        nowPlayingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listType = "now_playing";
                JSON_URL = "https://api.themoviedb.org/3/movie/"+listType+"?api_key=9f1d229c1a82490afe903ee4b19c97c0";
                Intent intent = new Intent(SelectPlayType.this, MainActivity.class);
                startActivity(intent);
                finish();
                return;

            }
        });

        upComingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listType = "upcoming";
                JSON_URL = "https://api.themoviedb.org/3/movie/"+listType+"?api_key=9f1d229c1a82490afe903ee4b19c97c0";
                Intent intent = new Intent(SelectPlayType.this, MainActivity.class);
                startActivity(intent);
                finish();
                return;

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SelectPlayType.this, startPage.class);
                start_frag.userDb.child("invited_friend").removeValue();
                startActivity(intent);
                finish();
                return;
            }
        });



    }


    public String getJSON_URL() {
        return JSON_URL;
    }
}