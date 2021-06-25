package com.example.hostco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.hostco.Adapter.UserHistoryAdapter;
import com.example.hostco.Adapter.UserSendRequestAdapter;
import com.example.hostco.Model.Histories;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserHistoryActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private RecyclerView rvUserHistoryActivity;
    private ImageView imgBackUserHistoryActivity;
    private ProgressBar pbUserHistoryActivity;
    private ArrayList<Histories> mHistories;
    private UserHistoryAdapter userHistoryAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_history);

        pbUserHistoryActivity=findViewById(R.id.pbUserHistoryActivity);
        rvUserHistoryActivity=findViewById(R.id.rvUserHistoryActivity);
        imgBackUserHistoryActivity=findViewById(R.id.imgBackUserHistoryActivity);
        pbUserHistoryActivity.setVisibility(View.VISIBLE);
        mHistories=new ArrayList<Histories>();
        rvUserHistoryActivity.setLayoutManager(new LinearLayoutManager(this));
        
        readPatientRequests();

        imgBackUserHistoryActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserHistoryActivity.this,MainUserActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        });
    }

    private void readPatientRequests() {
        databaseReference= FirebaseDatabase.getInstance().getReference().child("PatientRequests")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        mHistories.clear();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Histories history=dataSnapshot.getValue(Histories.class);
                    mHistories.add(history);
                }
                userHistoryAdapter=new UserHistoryAdapter(UserHistoryActivity.this,mHistories);
                rvUserHistoryActivity.setAdapter(userHistoryAdapter);
                userHistoryAdapter.notifyDataSetChanged();
                pbUserHistoryActivity.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}