package com.example.movie_tinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private RequestQueue mQueue;

    private int z = 0;
    private TextView movieTitle;
    private TextView movieYear;
    private TextView movieGenre;
    private ImageView moviePoster;

    private Button endBtn;
    private Button yepBtn;
    private Button nopeBtn;
    private Button goToList;

    private Cards cards_data[];


    private String title;
    private String year ;
    private String jsonCurrentMovieId;
    private String poster_url ;


    private static String currentUid;
    private String currentMovieId;

    private static DatabaseReference userDb;
    private DatabaseReference friendsDb;
    private FirebaseAuth mAuth;

    private static ArrayList<String> fyeppedMovies;
    private static ArrayList<String> yeppedMovies;
    public static ArrayList<String> matchedMovies;
    public static ArrayList<Cards> matchedMoviesArry;


    private String friendsUsername;
    private static String friendsUid;

    private String fMovie;

    //private static int userYepCounter = 0;
    //private static int userNopeCounter = 0;
    private static int fYepCounter = 0;
    private static int fNopeCounter = 0;


    private static boolean gameFinished;
    private boolean userGameFinished = false;
    private String userGameFinishedS;
    private boolean friendGameFinished = false;
    private String friendGameFinishedS;
    private boolean isGameFinished = false;
    private static boolean isMatchingFinished;

    private JSONArray jsonArrayG;
    private String JsonUrl;

    private static int finishCounter = 0;

    private ImageView yepBtnImg;
    private ImageView nopeBtnImg;

    private CardView cardView;
    private Button popUpMenu;

    private FloatingActionButton fab;

    private int userYepCounter = 0;
    private  int userNopeCounter = 0;
    private int friendYepCounter = 0;
    private int friendNopeCounter = 0;

    private LinearLayout ll;

    private String oyunbitti = "Game finished.";






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        matchedMoviesArry = new ArrayList<Cards>();



        mAuth = FirebaseAuth.getInstance();

        userDb = FirebaseDatabase.getInstance().getReference().child("users");
        friendsDb = FirebaseDatabase.getInstance().getReference().child("users");

        yepBtnImg = (ImageView)findViewById(R.id.yepBtnImg);
        nopeBtnImg = (ImageView)findViewById(R.id.nopeBtnImg);
        popUpMenu = (Button)findViewById(R.id.popUpMenuBtn);

        cardView = (CardView)findViewById(R.id.cardView);

        ll = (LinearLayout)findViewById(R.id.ll);

        currentUid = mAuth.getCurrentUser().getUid();

        SelectPlayType type = new SelectPlayType();

        JsonUrl = type.getJSON_URL();
        userDb.child(currentUid).child("gamefinished").setValue(false);

        mQueue = Volley.newRequestQueue(this);
        //endBtn = (Button) findViewById(R.id.button_end);

        yepBtn = (Button)findViewById(R.id.yep);
        nopeBtn = (Button)findViewById(R.id.nope);



        movieTitle = (TextView)findViewById(R.id.movieTit);
        movieYear = (TextView)findViewById(R.id.movieYear);
        //movieGenre = (TextView)findViewById(R.id.movieGenre);
        moviePoster = (ImageView)findViewById(R.id.moviePoster);

        yeppedMovies = new ArrayList<String>();
        fyeppedMovies = new ArrayList<String>();
        matchedMovies = new ArrayList<String>();

        fab = (FloatingActionButton)findViewById(R.id.goToListFab);





        SelectionActivity selectedFriend = new SelectionActivity();

        friendsUsername = selectedFriend.getFusername();

        yepBtn.setEnabled(true);
        nopeBtn.setEnabled(true);





        userDb.orderByChild("Name").equalTo(friendsUsername).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


               for(DataSnapshot childSnapshot: snapshot.getChildren()){
                   String parent = childSnapshot.getKey();


                    friendsUid = parent;


               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        //clickNopes(finishCounter);





        for(int i = 0; i < 1; i++){
            clickNopes(finishCounter);
            finishCounter++;
        }

        final int first_color = yepBtnImg.getDrawingCacheBackgroundColor();





        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    //userDb.child(currentUid).child("random").setValue(z);
                friendCounter();
                isMatched();
                //afterFinish();
                if(finishCounter== jsonArrayG.length()){


                    userGameFinished=true;
                }


                    Intent intent = new Intent(MainActivity.this, ListMatches.class);
                    startActivity(intent);
                    return;


            }
        });

        yepBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userDb.child(currentUid).child("random").setValue(z);

                Random rnd = new Random();
                final int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                Random rnd2 = new Random();
                final int color2 = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

                if(finishCounter== jsonArrayG.length()){
                    fab.setVisibility(view.VISIBLE);
                    yepBtn.setVisibility(view.GONE);
                    nopeBtn.setVisibility(view.GONE);
                    yepBtnImg.setVisibility(view.GONE);
                    nopeBtnImg.setVisibility(view.GONE);
                    ll.setBackgroundColor(color2);
                    userGameFinished=true;
                }
                friendCounter();
                getfMovie();
                afterFinish();



                z++;



                yepBtn.setBackgroundColor(color);
                cardView.setCardBackgroundColor(color2);
                nopeBtn.setBackgroundColor(first_color);

                clickNopes(finishCounter);
                userDb.child(currentUid).child("yeps").child(currentMovieId).setValue(currentMovieId);
                yeppedMovies.add(currentMovieId);



                finishCounter++;









            }
        });

        nopeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userDb.child(currentUid).child("random").setValue(z);
                Random rnd = new Random();
                final int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                nopeBtn.setBackgroundColor(color);
                Random rnd2 = new Random();
                final int color2 = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

                if(finishCounter== jsonArrayG.length()){
                    fab.setVisibility(view.VISIBLE);
                    nopeBtn.setVisibility(view.GONE);
                    yepBtn.setVisibility(view.GONE);
                    nopeBtnImg.setVisibility(view.GONE);
                    yepBtnImg.setVisibility(view.GONE);
                    ll.setBackgroundColor(color2);
                    userGameFinished=true;
                }
                friendCounter();
                getfMovie();
                //isGameFinished();
                afterFinish();
                //isMatched();




                z++;



                cardView.setCardBackgroundColor(color2);
                yepBtn.setBackgroundColor(first_color);
              //  goToList();
                clickNopes(finishCounter);
                userDb.child(currentUid).child("nopes").child(currentMovieId).setValue(true);




                finishCounter++;





            }
        });








    }

    @Override
    protected void onStart() {
        super.onStart();
        goToList();
    }



    public void friendCounter(){

        friendsDb.child(friendsUid).child("yeps").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    fYepCounter = (int)snapshot.getChildrenCount();
                    friendYepCounter++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        friendsDb.child(friendsUid).child("nopes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    fNopeCounter = (int)snapshot.getChildrenCount();
                    friendNopeCounter++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        userDb.child(currentUid).child("yeps").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    userYepCounter = (int)snapshot.getChildrenCount();


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        userDb.child(currentUid).child("nopes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    userNopeCounter = (int)snapshot.getChildrenCount();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        friendsDb.child(friendsUid).child("gamefinished").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String friendsGameStatus = String.valueOf(snapshot.getValue());
                if(friendsGameStatus.equals("true")){
                    friendGameFinished = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if(userYepCounter + userNopeCounter == jsonArrayG.length()){
            userGameFinished = true;
            Toast.makeText(this,oyunbitti,Toast.LENGTH_SHORT).show();

            userDb.child(currentUid).child("gamefinished").setValue(true);
        }
        if(friendYepCounter + friendNopeCounter == jsonArrayG.length()){

            friendGameFinished = true;
        }

    }

    public void isGameFinished(){
         friendsDb.child(friendsUid).child("gamefinished").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String friendsGameStatus = String.valueOf(snapshot.getValue());
                if(friendsGameStatus.equals("true")){
                    friendGameFinished = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if(userYepCounter + userNopeCounter == jsonArrayG.length()){
            userGameFinished = true;
            Toast.makeText(this,"Game finished!",Toast.LENGTH_SHORT).show();
            userDb.child(currentUid).child("gamefinished").setValue(true);
        }
        if(fYepCounter + fNopeCounter == jsonArrayG.length()){

            friendGameFinished = true;
        }
    }

    public void afterFinish(){




                userDb.child(currentUid).child("matches").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(userGameFinished == true && friendGameFinished == true){
                            for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                String child = childSnapshot.getKey();
                                if(!matchedMovies.contains(child)){
                                    matchedMovies.add(child);
                                }

                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



    }







    public void getfMovie(){
        if(friendsDb.child(friendsUid).child("yeps") != null){
            friendsDb.child(friendsUid).child("yeps").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    String fCurrentMovie = String.valueOf(snapshot.getKey());
                    if(!fyeppedMovies.contains(fCurrentMovie)){
                        fyeppedMovies.add(fCurrentMovie);
                    }

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }





    }


    public void isMatched(){


            for(int i = 0; i < fyeppedMovies.size(); i++){
                for(int j = 0; j < yeppedMovies.size(); j++){
                    if(fyeppedMovies.get(i).equals(yeppedMovies.get(j))){
                        userDb.child(currentUid).child("matches").child(fyeppedMovies.get(i)).setValue(true);
                        friendsDb.child(friendsUid).child("matches").child(yeppedMovies.get(j)).setValue(true);
                        //matchedMovies.add(fyeppedMovies.get(i));


                    }
                    if(yeppedMovies.get(j).equals(fyeppedMovies.get(i))){
                        userDb.child(currentUid).child("matches").child(fyeppedMovies.get(i)).setValue(true);
                        userDb.child(friendsUid).child("matches").child(yeppedMovies.get(j)).setValue(true);


                    }

                }
    }}


    public void clickNopes(final int counter){
        final String JSON_URL = JsonUrl;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, JSON_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {


                    JSONArray jsonArray = response.getJSONArray("results");
                        if(counter < jsonArray.length()){
                            JSONObject results = jsonArray.getJSONObject(counter);
                            title = String.valueOf(results.getString("original_title"));
                            year = String.valueOf(results.getString("release_date"));
                            currentMovieId = results.getString("id");
                            poster_url = String.valueOf(results.getString("poster_path"));
                            movieTitle.setText(title);
                            movieYear.setText(year);
                            Cards item = new Cards(currentMovieId,title,"https://image.tmdb.org/t/p/w500"+poster_url,year);
                            matchedMoviesArry.add(item);


                            Glide.with(MainActivity.this).load("https://image.tmdb.org/t/p/w500"+results.getString("poster_path")).into(moviePoster);
                        }else{
                           // goToList();
                        }








                    jsonArrayG = jsonArray;




                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }

    public static ArrayList<Cards> getMatchedMoviesArry() {
        return matchedMoviesArry;
    }

    public void goToList(){
        if(gameFinished==true && isMatchingFinished==true){
            Intent intent = new Intent(MainActivity.this, ListMatches.class);
            startActivity(intent);
            return;
        }

    }
    public void goToSettings() {

        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
        return;
    }


    public static ArrayList<String> getMatchedMovies() {
        return matchedMovies;
    }







    public void end(View view) {
        restart();
        Intent intent = new Intent(MainActivity.this, startPage.class);
        startActivity(intent);
        return;
    }

    public static void restart(){

        userDb.child(currentUid).child("yeps").removeValue();
        userDb.child(currentUid).child("nopes").removeValue();
        userDb.child(currentUid).child("friendsMail").removeValue();
        userDb.child(currentUid).child("friends_username").removeValue();
        userDb.child(currentUid).child("gamefinished").removeValue();
        userDb.child(currentUid).child("gamefinished").setValue(false);
        userDb.child(currentUid).child("matches").removeValue();
        fyeppedMovies.clear();
        yeppedMovies.clear();
        //userYepCounter = 0;
        //userNopeCounter = 0;
        fYepCounter = 0;
        fNopeCounter = 0;
        finishCounter = 0;
        gameFinished=false;
        isMatchingFinished = false;

    }


    public void popUpMenu(View view) {
        PopupMenu popup = new PopupMenu(this,view);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.popup_menu);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.popUpFriends:
                Toast.makeText(this,"To be implemented",Toast.LENGTH_SHORT).show();
                //Intent intent1 = new Intent(MainActivity.this, Friends.class);
                //startActivity(intent1);
                //finish();
                return true;
            case R.id.popUpSettings:
                goToSettings();
                return true;
            case R.id.popUpSıgnOut:
                mAuth.signOut();
                Intent intent = new Intent(MainActivity.this, ChooseLoginRegisterationActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return false;


        }
    }

    public static String getFriendsUid() {
        return friendsUid;
    }

    public static ArrayList<String> getFyeppedMovies() {
        return fyeppedMovies;
    }

    public static ArrayList<String> getYeppedMovies() {
        return yeppedMovies;
    }
}