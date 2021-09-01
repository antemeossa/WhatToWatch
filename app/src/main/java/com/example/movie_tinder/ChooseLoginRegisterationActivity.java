package com.example.movie_tinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChooseLoginRegisterationActivity extends AppCompatActivity {


    private Button mLogin;
    private Button mRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_login_registeration);

        mLogin = (Button)findViewById(R.id.login);
        mRegister = (Button)findViewById(R.id.register);

        if(SettingsActivity.isTurkish == true){
            mLogin.setText("Giriş Yap");
            mRegister.setText("Üye Ol");

        }

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseLoginRegisterationActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseLoginRegisterationActivity.this, RegisterationActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });
    }
}