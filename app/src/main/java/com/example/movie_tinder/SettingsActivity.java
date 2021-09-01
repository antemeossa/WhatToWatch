package com.example.movie_tinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SettingsActivity extends AppCompatActivity {

    private EditText mNameField, mPhoneField;

    private Button mBack, mConfirm, signOut;

    private ImageView mPP;

    private FirebaseAuth mAuth;

    private DatabaseReference mCustomerDatabase;

    private RadioGroup rg;
    private RadioButton türkçe;
    private RadioButton english;

    private String userId, name, phone, ppUrl;

    private Uri resultUri;

    private Button engLanguage;
    private Button trLanguage;
    public static boolean isTurkish;

    private RadioButton male;
    private RadioButton female;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);



        //Bundle extras = getIntent().getExtras();

        //String uSex = String.valueOf(extras.getString("userSex"));

        mNameField = (EditText)findViewById(R.id.name);
        mPhoneField = (EditText)findViewById(R.id.phone);

        mBack= (Button)findViewById(R.id.back);
        mConfirm = (Button)findViewById(R.id.confirm);
        signOut = (Button)findViewById(R.id.signoutbtn);

        mPP = (ImageView)findViewById(R.id.profileImage);

        trLanguage = (Button)findViewById(R.id.trLanguage);
        engLanguage = (Button)findViewById(R.id.engLanguage);

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        mCustomerDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        male = (RadioButton)findViewById(R.id.maleBtn);
        female = (RadioButton)findViewById(R.id.femaleBtn);

        getUserInfo();

        if(isTurkish == true){

            mNameField.setHint("İsim");
            mPhoneField.setHint("Telefon");
            mBack.setText("Geri");
            mConfirm.setText("Onayla");
            signOut.setText("Çıkış Yap");
            male.setText("Erkek");
            female.setText("Kadın");

        }


        mPP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);

            }
        });
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInfo();
            }
        });

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this,startPage.class);
                startActivity(intent);
                finish();
                return;
            }
        });



    }

    private void getUserInfo() {

        mCustomerDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.getChildrenCount() > 0){

                    Map<String, Object> map = (Map<String, Object>) snapshot.getValue();

                    if(map.get("name") != null){

                        name = map.get("name").toString();
                        mNameField.setText(name);

                    }

                    if(map.get("phone") != null){

                        phone = map.get("phone").toString();
                        mPhoneField.setText(phone);

                    }

                    if(map.get("profileImageUrl") != null){

                        ppUrl = map.get("profileImageUrl").toString();
                        Glide.with(getApplication()).load(ppUrl).into(mPP);

                    }



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    


    private void saveUserInfo() {

        name = mNameField.getText().toString();
        phone = mPhoneField.getText().toString();

        Map userInfo = new HashMap();
        userInfo.put("name", name);
        userInfo.put("phone", phone);
        mCustomerDatabase.updateChildren(userInfo);

        if(resultUri != null){

            StorageReference filepath = FirebaseStorage.getInstance().getReference().child("profileImages").child(userId);
            Bitmap bitmap = null;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = filepath.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    finish();
                }
            });

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Uri downloadUrl = Uri.parse(taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());
                    Map userInfo = new HashMap();
                    userInfo.put("profileImageUrl", downloadUrl.toString());
                    mCustomerDatabase.updateChildren(userInfo);

                    finish();
                    return;

                }
            });

        }else{
            finish();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            final Uri imageUri = data.getData();
            resultUri = imageUri;
            mPP.setImageURI(resultUri);
        }
    }


    public void trSelected(View view) {
        isTurkish = true;
    }

    public void engSelected(View view) {
        isTurkish = false;
    }

    public void signout(View view) {
        mAuth.signOut();
        Intent intent = new Intent(SettingsActivity.this, ChooseLoginRegisterationActivity.class);
        startActivity(intent);
        finish();
        return;
    }
}