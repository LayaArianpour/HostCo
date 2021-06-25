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

import com.example.hostco.Adapter.HospitalRequestAdapter;
import com.example.hostco.Model.Histories;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HospitalGetRequestActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private RecyclerView rvHospitalGetRequestActivity;
    private ProgressBar pbHospitalGetRequestActivity;
    private ImageView imgBackHospitalGetRequestActivity;
    private ArrayList<Histories> mHistories;
    private HospitalRequestAdapter hospitalRequestAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_get_request);

        rvHospitalGetRequestActivity=findViewById(R.id.rvHospitalGetRequestActivity);
        pbHospitalGetRequestActivity=findViewById(R.id.pbHospitalGetRequestActivity);
        imgBackHospitalGetRequestActivity=findViewById(R.id.imgBackHospitalGetRequestActivity);

        pbHospitalGetRequestActivity.setVisibility(View.VISIBLE);
        mHistories=new ArrayList<Histories>();
        rvHospitalGetRequestActivity.setLayoutManager(new LinearLayoutManager(this));

        readHospitalRequests();

        imgBackHospitalGetRequestActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HospitalGetRequestActivity.this,MainHospitalActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        });
    }

    private void readHospitalRequests() {
        databaseReference= FirebaseDatabase.getInstance().getReference().child("HospitalRequests")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        mHistories.clear();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Histories history=dataSnapshot.getValue(Histories.class);
                    if(history.getState().equals("Waiting for status to be determined...")){
                        mHistories.add(history);
                    }
                }
                hospitalRequestAdapter=new HospitalRequestAdapter(HospitalGetRequestActivity.this,mHistories);
                rvHospitalGetRequestActivity.setAdapter(hospitalRequestAdapter);
                hospitalRequestAdapter.notifyDataSetChanged();
                pbHospitalGetRequestActivity.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}