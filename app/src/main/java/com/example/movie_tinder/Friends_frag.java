package com.example.movie_tinder;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Friends_frag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Friends_frag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static ListView lw;

    public static ArrayList<String> friendNames;

    private FloatingActionButton fab;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ArrayAdapter basicAdapter;
    private static friendAdapter adapter;

    public Friends_frag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Friends_frag.
     */
    // TODO: Rename and change types and number of parameters
    public static Friends_frag newInstance(String param1, String param2) {
        Friends_frag fragment = new Friends_frag();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friends_frag, container, false);
        friendNames = start_frag.friendsNames;
        lw = (ListView)view.findViewById(R.id.friendList);
        basicAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,friendNames);
        adapter =  new friendAdapter(getActivity(),friendNames);
        start_frag.userDb.child("friends").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                lw.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        lwClick();
        Intent intent = new Intent(getActivity(),MyService.class);
        Intent intent1 = new Intent(getActivity(),MyService.class);
      //  onCreate(intent);
      //  stopService(intent1);

        return view;

    }

    private void onCreate(Intent intent){
        startService(intent);
    }

    private void startService(Intent intent) {


    }

    private void stopService(Intent intent){
        startService(intent);
    }

    public void lwClick(){
        lw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                start_frag.userDb.child("invited_friend").setValue(friendNames.get(position));
                if(friendNames.get(position).equals("DON'T CLICK, JUST A CHECKER!")){
                    Toast.makeText(getActivity(),"You can't click that!",Toast.LENGTH_SHORT).show();
                }else{
                    start_frag.setFusername(friendNames.get(position));
                    Intent intent = new Intent(getActivity(),SelectPlayType.class);
                    startActivity(intent);
                    return;
                }

            }
        });

    }


    public ArrayList<String> getFriendNames() {
        return friendNames;
    }
}