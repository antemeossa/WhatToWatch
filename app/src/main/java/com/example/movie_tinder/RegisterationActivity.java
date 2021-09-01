package com.example.movie_tinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class RegisterationActivity extends AppCompatActivity {

    private Button mRegisterBtn, backBtn;

    private EditText mEmail, mPassword, mUsername;

    private RadioGroup mRadioGroup;

    private RadioButton male,female;

    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);

        backBtn =  (Button)findViewById(R.id.backbtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterationActivity.this, ChooseLoginRegisterationActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });

        mAuth = FirebaseAuth.getInstance();
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user != null){
                    Intent intent = new Intent(RegisterationActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };

        mRegisterBtn = (Button)findViewById(R.id.register);

        mEmail = (EditText)findViewById(R.id.email);
        mPassword = (EditText)findViewById(R.id.password);
        mUsername = (EditText)findViewById(R.id.username);

        mRadioGroup = (RadioGroup)findViewById(R.id.radiogroup);
        male = (RadioButton)findViewById(R.id.maleBtnId);
        female = (RadioButton)findViewById(R.id.femaleBtnId);

        if(SettingsActivity.isTurkish){
            mRegisterBtn.setText("Üyel Ol");
            mPassword.setHint("Parola");
            mUsername.setHint("Kullanıcı adı");
            male.setText("Erkek");
            female.setText("Kadın");


        }

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedId = mRadioGroup.getCheckedRadioButtonId();

                final RadioButton radioButton = (RadioButton)findViewById(selectedId);
                if(radioButton.getText() == null){
                    Toast.makeText(RegisterationActivity.this, "You must select your gender.", Toast.LENGTH_SHORT).show();
                    return;
                }

                final String email = mEmail.getText().toString();
                final String password = mPassword.getText().toString();
                final String username = mUsername.getText().toString();

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(RegisterationActivity.this, "Sign Up Error!", Toast.LENGTH_SHORT).show();
                        }else{
                            String userId = mAuth.getCurrentUser().getUid();
                            final DatabaseReference currentUserDb;
                            DatabaseReference DB;

                            currentUserDb = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("Name");
                            DB = FirebaseDatabase.getInstance().getReference().child("users");


                            final Map userInfo = new HashMap<>();

                            userInfo.put("username", username);
                            userInfo.put("sex", radioButton.getText().toString());
                            userInfo.put("profileImageUrl","default");
                            currentUserDb.updateChildren(userInfo);

                            currentUserDb.setValue(username);

                            Intent intent = new Intent(RegisterationActivity.this, SelectionActivity.class);
                            startActivity(intent);
                            finish();
                            return;




                        }

                    }
                });
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthStateListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthStateListener);
    }
}