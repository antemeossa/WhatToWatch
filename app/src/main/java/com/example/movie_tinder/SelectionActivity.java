package com.example.movie_tinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SelectionActivity extends AppCompatActivity {


    private Button playBtn;
    private EditText friendsUsername;
    private Button inviteBtn;
    private Button signout;

    private String currentUid;
    private String friendUsername;
    public static String fusername;

    DatabaseReference userDb;
    private FirebaseAuth mAuth;

    private boolean isInvited;
    private Friends_frag frag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        mAuth = FirebaseAuth.getInstance();
        playBtn = (Button)findViewById(R.id.playBtn);
        inviteBtn = (Button)findViewById(R.id.inviteBtn);

        friendsUsername = (EditText)findViewById(R.id.friendsId);
        frag = new Friends_frag();

        currentUid = mAuth.getCurrentUser().getUid();
        userDb = FirebaseDatabase.getInstance().getReference().child("users").child(currentUid);
        signout = (Button)findViewById(R.id.signoutbtn);

        if(SettingsActivity.isTurkish){
            playBtn.setText("Oyna");
            inviteBtn.setText("Davet Et");
            signout.setText("İptal Et");
            friendsUsername.setHint("Arkadaşının Kullanıcı Adı");
        }



        //MainActivity.restart();



            playBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(isInvited==false){
                        Toast.makeText(SelectionActivity.this,"You must invite a friend!",Toast.LENGTH_SHORT).show();
                    }else{

                        Intent intent = new Intent(SelectionActivity.this, SelectPlayType.class);
                        startActivity(intent);
                        finish();
                        return;
                    }

                }
            });








        inviteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fusername = friendsUsername.getText().toString();
                userDb.child("invited_friend").setValue(fusername);


                isInvited=true;
                Toast.makeText(SelectionActivity.this,"Invited.",Toast.LENGTH_SHORT).show();
                if(!frag.friendNames.contains(fusername)){
                    frag.friendNames.add(fusername);
                }else{
                    Toast.makeText(SelectionActivity.this,"You are already friends",Toast.LENGTH_SHORT).show();
                }


            }
        });
    }


    public String getFusername() {
        return fusername;
    }

    public void signout(View view) {
        mAuth.signOut();
        Intent intent = new Intent(SelectionActivity.this, ChooseLoginRegisterationActivity.class);
        startActivity(intent);
        finish();
        return;
    }
}