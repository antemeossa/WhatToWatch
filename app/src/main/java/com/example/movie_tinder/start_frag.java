package com.example.movie_tinder;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link start_frag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class start_frag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Button playBtn;
    private EditText friendsUsername;
    private Button inviteBtn;
    private Button signout;

    private String currentUid;
    private String friendUsername;
    public static String fusername;

    public static DatabaseReference userDb;
    private FirebaseAuth mAuth;

    private boolean isInvited;
    private Friends_frag frag;
    public static ArrayList<String> friendsNames;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FloatingActionButton helpFab;
    private String helpText;
    private ConstraintLayout cl;
    public start_frag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment start_frag.
     */
    // TODO: Rename and change types and number of parameters
    public static start_frag newInstance(String param1, String param2) {
        start_frag fragment = new start_frag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start_frag, container, false);

        friendsNames = new  ArrayList<String>();
        mAuth = FirebaseAuth.getInstance();
        playBtn = (Button)view.findViewById(R.id.playBtn);
        inviteBtn = (Button)view.findViewById(R.id.inviteBtn);
        helpFab = (FloatingActionButton)view.findViewById(R.id.helpFab);
        cl = (ConstraintLayout)view.findViewById(R.id.start_frag_layout);

        friendsUsername = (EditText)view.findViewById(R.id.friendsId);
        frag = new Friends_frag();

        currentUid = mAuth.getCurrentUser().getUid();
        userDb = FirebaseDatabase.getInstance().getReference().child("users").child(currentUid);
        signout = (Button)view.findViewById(R.id.signoutbtn);
        userDb.child("friends").child("DON'T CLICK, JUST A CHECKER!").setValue(true);
        if(SettingsActivity.isTurkish){
            playBtn.setText("Oyna");
            inviteBtn.setText("Davet Et");
            signout.setText("İptal Et");
            friendsUsername.setHint("Arkadaşının Kullanıcı Adı");
        }

        helpText = (String)getString(R.string.HELP);

        //MainActivity.restart();
        userDb.child("friends").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot childSnapshot: snapshot.getChildren()){
                    if(!friendsNames.contains(String.valueOf(childSnapshot.getKey()))){
                        friendsNames.add(String.valueOf(childSnapshot.getKey()));
                        if(String.valueOf(childSnapshot.getKey()).equals("DON'T CLICK, JUST A CHECKER!")){
                            userDb.child("friends").child(childSnapshot.getKey()).removeValue();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isInvited==false){
                    Toast.makeText(getActivity(),"You must invite a friend!",Toast.LENGTH_SHORT).show();
                }else{

                    Intent intent = new Intent(getActivity(), SelectPlayType.class);
                    startActivity(intent);

                    return;
                }

            }
        });








        inviteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(String.valueOf(userDb.child("invited_friend")).isEmpty()){

                }else{
                    fusername = friendsUsername.getText().toString();
                    userDb.child("invited_friend").setValue(fusername);
                    isInvited=true;
                    userDb.child("friends").child(fusername).setValue(true);

                    Snackbar snackbar = Snackbar.make(cl,"Invited",Snackbar.LENGTH_SHORT);
                    snackbar.show();

                    userDb.child("friends").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for(DataSnapshot childSnapshot: snapshot.getChildren()){
                                if(!friendsNames.contains(String.valueOf(childSnapshot.getKey()))){
                                    friendsNames.add(String.valueOf(childSnapshot.getKey()));
                                    if(String.valueOf(childSnapshot.getKey()).equals("DON'T CLICK, JUST A CHECKER!")){
                                        userDb.child("friends").child(childSnapshot.getKey()).removeValue();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }





            }
        });

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(getActivity(), ChooseLoginRegisterationActivity.class);
                startActivity(intent);
                return;
            }
        });

        helpFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDialog();
            }
        });

        return view;
    }

    public void openDialog(){

        helpDialog helpDialog = new helpDialog();
        helpDialog.show(getFragmentManager(),"HELP: ");

    }

    public String getFusername() {
        return fusername;
    }

    public static void setFusername(String fusername) {
        start_frag.fusername = fusername;
    }







    }
