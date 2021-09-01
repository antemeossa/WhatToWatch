package com.example.movie_tinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;

public class ListMatches extends AppCompatActivity {

    ListView lw;
    MainActivity main;
    private RequestQueue mQueue;

    private ArrayList<Cards> allMovies;
    private ArrayList<String> matchedMoviesId;
    private ArrayList<Cards> matchesList;

    private MovieAdapter movieAdapter;

    private Button backBtn;

    private  String currentMovieId;
    private String movieID;

    private FloatingActionButton fab;
    private FloatingActionButton fabBack;

    private Toolbar tb;
    private AppBarLayout appBarLayout;

    private int counter=0;

    private TextView matchCount;

    private FirebaseAuth mAuth;

    private DatabaseReference db;

    private String currentUid;

    private static ArrayList<String> matchesdb;

    private boolean goMatch;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_matches);

//        final int first_color = fab.getDrawingCacheBackgroundColor();


        SelectPlayType type = new SelectPlayType();


        lw = (ListView) findViewById(R.id.matchList);
        matchCount = (TextView)findViewById(R.id.matchCount);


        matchesList = new ArrayList<Cards>();

        fab = (FloatingActionButton)findViewById(R.id.fab_list);
        fabBack = (FloatingActionButton)findViewById(R.id.fab_back);


        matchedMoviesId = MainActivity.matchedMovies;
        allMovies = MainActivity.matchedMoviesArry;

        mAuth = FirebaseAuth.getInstance();
        currentUid = mAuth.getUid();

        db = FirebaseDatabase.getInstance().getReference().child("users").child(currentUid);

        matchesdb = new ArrayList<String>();
        goMatch=false;

        db.child("matches").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot childSnapshot: snapshot.getChildren()){
                    if(!matchesdb.contains(String.valueOf(childSnapshot.getKey()))){
                        matchesdb.add(String.valueOf(childSnapshot.getKey()));



                    }
                    goMatch=true;
                    f();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


/*
        for(int i = 0; i < matchesdb.size(); i++){
            movieID= String.valueOf(matchesdb.get(i));

            for(int j = 0; j < allMovies.size(); j++){
                currentMovieId = String.valueOf(allMovies.get(j).getMovieId());
                if(movieID.equals(currentMovieId)){
                    Cards item = allMovies.get(j);
                    matchesList.add(item);
                    counter=matchesList.size();
                    matchCount.setText(""+counter);



                }
            }
        }

        */


        movieAdapter = new MovieAdapter(ListMatches.this, matchesList);







        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random rnd = new Random();
                final int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                fab.setBackgroundColor(color);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String shareBody = "Your body here";
                String shareSub = "Your subject here";
                intent.putExtra(intent.EXTRA_SUBJECT, shareSub);
                intent.putExtra(intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(intent,"Share using"));

            }
        });

        fabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random rnd = new Random();
                final int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                fabBack.setBackgroundColor(color);
                MainActivity.restart();
                matchesdb.clear();
                counter=0;
                Intent intent = new Intent(ListMatches.this, startPage.class);
                startActivity(intent);
                finish();
                return;
            }
        });







            }

            public void f(){
                if(goMatch){
                    for(int i = 0; i < matchesdb.size(); i++){
                        movieID = String.valueOf(matchesdb.get(i));
                        for(int j = 0; j < allMovies.size(); j++){
                            currentMovieId = String.valueOf(allMovies.get(j).getMovieId());
                            if(movieID.equals(currentMovieId)){
                                Cards item = allMovies.get(j);
                                if(!matchesList.contains(item)){
                                    matchesList.add(item);
                                    counter=matchesdb.size();
                                    matchCount.setText(""+counter);
                                    lw.setAdapter(movieAdapter);
                                }

                            }

                        }
                    }
                }
            }

        }







