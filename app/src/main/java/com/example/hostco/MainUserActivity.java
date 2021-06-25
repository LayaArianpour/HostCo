package com.example.hostco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.hostco.Adapter.UserSendRequestAdapter;
import com.example.hostco.Model.Patients;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainUserActivity extends AppCompatActivity {

    private MaterialTextView txt_username;
    private CardView boxcoronatest,boxhospitallist;
    private Toolbar toolbar;
    private DatabaseReference databaseReference;
    private ArrayList<Patients> mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);

        mUser=new ArrayList<Patients>();

        txt_username=findViewById(R.id.txt_username);
        boxcoronatest=findViewById(R.id.boxcoronatest);
        boxhospitallist=findViewById(R.id.boxhospitallist);


        toolbar = (Toolbar) findViewById(R.id.toolbarmain);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        readUser();

        boxcoronatest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainUserActivity.this,CovidTestActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        boxhospitallist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainUserActivity.this, UserSendRequestActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });


    }


    private void readUser() {
        databaseReference=FirebaseDatabase.getInstance().getReference().child("Patients")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        mUser.clear();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Patients user= snapshot.getValue(Patients.class);
                    mUser.add(user);
                    txt_username.setText(user.getUserName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_user, menu);
        super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.EditUser:
                startActivity(new Intent(MainUserActivity.this,EditInfoUserActivity.class));
                return true;
            case R.id.LogoutUser:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainUserActivity.this,StartActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
                return true;
            case R.id.HistoryUser:
                startActivity(new Intent(MainUserActivity.this,UserHistoryActivity.class));
                return true;
        }

        return false;
    }


}