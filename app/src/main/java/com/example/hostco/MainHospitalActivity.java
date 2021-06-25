package com.example.hostco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.hostco.Adapter.HospitalRequestAdapter;
import com.example.hostco.Model.Histories;
import com.example.hostco.Model.Hospitals;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class MainHospitalActivity extends AppCompatActivity {

    private Toolbar toolbarHospitalMain;
    private MaterialTextView txtHospitalizationNumberHospitalMainActivity,txtImprovedNumberHospitalMainActivity
            ,txtDeathNumberHospitalMainActivity,txtHopitalNameHospitalMainActivity,txtCapacityNumberHospitalMainActivity
            ,txtCounter;
    private CardView boxCounter;

    private DatabaseReference databaseReference;
    private ArrayList<Hospitals> mHospitals;

    private ArrayList<Histories> mHistoriesCounter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_hospital);


        txtHopitalNameHospitalMainActivity=findViewById(R.id.txtHopitalNameHospitalMainActivity);
        txtHospitalizationNumberHospitalMainActivity=findViewById(R.id.txtHospitalizationNumberHospitalMainActivity);
        txtImprovedNumberHospitalMainActivity=findViewById(R.id.txtImprovedNumberHospitalMainActivity);
        txtDeathNumberHospitalMainActivity=findViewById(R.id.txtDeathNumberHospitalMainActivity);
        txtCapacityNumberHospitalMainActivity=findViewById(R.id.txtCapacityNumberHospitalMainActivity);

        boxCounter=findViewById(R.id.boxCounter);
        txtCounter=findViewById(R.id.txtCounter);


        toolbarHospitalMain=(Toolbar) findViewById(R.id.toolbarHospitalMain);
        setSupportActionBar(toolbarHospitalMain);
        getSupportActionBar().setTitle("");

        mHospitals=new ArrayList<Hospitals>();
        mHistoriesCounter=new ArrayList<Histories>();
        readHospitalRequestsCount();

        readHospitalDetail();


    }

    private void readHospitalRequestsCount() {
        databaseReference= FirebaseDatabase.getInstance().getReference().child("HospitalRequests")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        mHistoriesCounter.clear();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Histories history=dataSnapshot.getValue(Histories.class);
                    if(history.getState().equals("Waiting for status to be determined...")){
                        mHistoriesCounter.add(history);

                    }
                }
                int counter=mHistoriesCounter.size();
                Log.i("counter", String.valueOf(counter));
                if(counter>0){
                    if(counter>99){
                        ViewGroup.LayoutParams params = txtCounter.getLayoutParams();
                        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                        txtCounter.setLayoutParams(params);
                    }
                    boxCounter.setVisibility(View.VISIBLE);
                    txtCounter.setText(String.valueOf(counter));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void readHospitalDetail() {
        databaseReference=FirebaseDatabase.getInstance().getReference().child("Hospitals")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        mHospitals.clear();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Log.i("snapshot", String.valueOf(snapshot));

                    //Hospitals myHospital= snapshot.getValue(Hospitals.class);

                    Hospitals hospital= new Hospitals();
                    hospital.setCapacity(Objects.requireNonNull(snapshot.child("capacity").getValue().toString()));
                    hospital.setCity(Objects.requireNonNull(snapshot.child("city").getValue().toString()));
                    hospital.setEmail(Objects.requireNonNull(snapshot.child("email").getValue().toString()));
                    hospital.setName(Objects.requireNonNull(snapshot.child("name").getValue().toString()));
                    hospital.setImproved(Objects.requireNonNull(snapshot.child("improved").getValue().toString()));
                    hospital.setDeath(Objects.requireNonNull(snapshot.child("death").getValue().toString()));
                    hospital.setHospitalization(Objects.requireNonNull(snapshot.child("hospitalization").getValue().toString()));
                    hospital.setPhone(Objects.requireNonNull(snapshot.child("phone").getValue().toString()));
                    hospital.setCountry(Objects.requireNonNull(snapshot.child("country").getValue().toString()));
                    hospital.setLocation(Objects.requireNonNull(snapshot.child("location").getValue().toString()));
                    hospital.setType(Objects.requireNonNull(snapshot.child("type").getValue().toString()));
                    hospital.setId(Objects.requireNonNull(snapshot.child("id").getValue().toString()));

                    mHospitals.add(hospital);
                    txtHopitalNameHospitalMainActivity.setText(hospital.getName());
                    txtDeathNumberHospitalMainActivity.setText(hospital.getDeath());
                    txtHospitalizationNumberHospitalMainActivity.setText(hospital.getHospitalization());
                    txtImprovedNumberHospitalMainActivity.setText(hospital.getImproved());
                    txtCapacityNumberHospitalMainActivity.setText(hospital.getCapacity());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_hospital, menu);
        super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_hospital,menu);
        return true;    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.EditHospital:
                startActivity(new Intent(MainHospitalActivity.this,EditInfoHospitalActivity.class));
                return true;
            case R.id.LogoutHospital:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainHospitalActivity.this,StartActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
                return true;
            case R.id.RequestsHospital:
                startActivity(new Intent(MainHospitalActivity.this,HospitalGetRequestActivity.class));
                return true;
            case R.id.HistoryHospital:
                startActivity(new Intent(MainHospitalActivity.this,HospitalHistoryActivity.class));
                return true;
        }

        return false;    }

}