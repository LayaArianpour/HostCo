package com.example.hostco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.hostco.Adapter.HospitalHistoryAdapter;
import com.example.hostco.Adapter.HospitalRequestAdapter;
import com.example.hostco.Model.Histories;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HospitalHistoryActivity extends AppCompatActivity {

    private ImageView imgBackHospitalHistory;
    private ProgressBar pbHospitalHistory;
    private RecyclerView rvlistOfHospitalHistory;
    private HospitalHistoryAdapter hospitalHistoryAdapter;

    private DatabaseReference databaseReference;
    ArrayList<Histories> mHistories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_history);

        imgBackHospitalHistory=findViewById(R.id.imgBackHospitalHistory);
        pbHospitalHistory=findViewById(R.id.pbHospitalHistory);
        rvlistOfHospitalHistory=findViewById(R.id.rvlistOfHospitalHistory);

        mHistories=new ArrayList<Histories>();
        pbHospitalHistory.setVisibility(View.VISIBLE);
        rvlistOfHospitalHistory.setLayoutManager(new LinearLayoutManager(this));


        readHistoryPatient();


        imgBackHospitalHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HospitalHistoryActivity.this,MainHospitalActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        });




    }

    private void readHistoryPatient() {
        databaseReference= FirebaseDatabase.getInstance().getReference().child("HospitalRequests")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        mHistories.clear();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Histories history=dataSnapshot.getValue(Histories.class);
                    if(!(history.getState().equals("Waiting for status to be determined..."))){
                        mHistories.add(history);
                        Log.i("hospitalHistory", String.valueOf(mHistories.size()));
                    }
                }
                hospitalHistoryAdapter=new HospitalHistoryAdapter(HospitalHistoryActivity.this,mHistories);
                rvlistOfHospitalHistory.setAdapter(hospitalHistoryAdapter);
                hospitalHistoryAdapter.notifyDataSetChanged();
                pbHospitalHistory.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}