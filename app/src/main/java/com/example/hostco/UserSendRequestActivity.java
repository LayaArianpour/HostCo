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

import com.example.hostco.Adapter.UserSendRequestAdapter;
import com.example.hostco.Model.Hospitals;
import com.example.hostco.Model.Patients;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class UserSendRequestActivity extends AppCompatActivity {

    private RecyclerView rvUserSendRequestActivity;
    private ProgressBar pbUserSendRequestActivity;
    private ImageView imgBackUserSendRequestActivity;
    private DatabaseReference databaseReferenceUser,databaseReferenceHospital;
    private ArrayList<Hospitals> mHospitals;
    private UserSendRequestAdapter userSendRequestAdapter;
    private String userCity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_send_request);

        pbUserSendRequestActivity=findViewById(R.id.pbUserSendRequestActivity);
        rvUserSendRequestActivity=findViewById(R.id.rvUserSendRequestActivity);
        imgBackUserSendRequestActivity=findViewById(R.id.imgBackUserSendRequestActivity);
        pbUserSendRequestActivity.setVisibility(View.VISIBLE);

        mHospitals=new ArrayList<Hospitals>();
        rvUserSendRequestActivity.setLayoutManager(new LinearLayoutManager(this));
        
        readHospitalForRequest();

        imgBackUserSendRequestActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserSendRequestActivity.this,MainUserActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        });
    }

    private void readHospitalForRequest() {
        databaseReferenceUser= FirebaseDatabase.getInstance().getReference().child("Patients")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        databaseReferenceUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Patients patient=snapshot.getValue(Patients.class);
                    userCity=patient.getCity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReferenceHospital=FirebaseDatabase.getInstance().getReference().child("Hospitals");
        mHospitals.clear();
        databaseReferenceHospital.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Log.i("snapshot", String.valueOf(snapshot));
                    //Hospitals hospital=dataSnapshot.getValue(Hospitals.class);

                    Hospitals hospital=new Hospitals();
                    hospital.setCapacity(Objects.requireNonNull(dataSnapshot.child("capacity").getValue().toString()));
                    hospital.setCity(Objects.requireNonNull(dataSnapshot.child("city").getValue().toString()));
                    hospital.setEmail(Objects.requireNonNull(dataSnapshot.child("email").getValue().toString()));
                    hospital.setName(Objects.requireNonNull(dataSnapshot.child("name").getValue().toString()));
                    hospital.setImproved(Objects.requireNonNull(dataSnapshot.child("improved").getValue().toString()));
                    hospital.setDeath(Objects.requireNonNull(dataSnapshot.child("death").getValue().toString()));
                    hospital.setHospitalization(Objects.requireNonNull(dataSnapshot.child("hospitalization").getValue().toString()));
                    hospital.setPhone(Objects.requireNonNull(dataSnapshot.child("phone").getValue().toString()));
                    hospital.setCountry(Objects.requireNonNull(dataSnapshot.child("country").getValue().toString()));
                    hospital.setLocation(Objects.requireNonNull(dataSnapshot.child("location").getValue().toString()));
                    hospital.setType(Objects.requireNonNull(dataSnapshot.child("type").getValue().toString()));
                    hospital.setId(Objects.requireNonNull(dataSnapshot.child("id").getValue().toString()));

                    int hospitalCapacity=Integer.parseInt(hospital.getCapacity());
                    String hospitalCity=hospital.getCity();

                    if(hospitalCapacity>0 && hospitalCity.equals(userCity)){
                        mHospitals.add(hospital);

                    }
                }
                Log.i("mHospitalsEndCount", String.valueOf(mHospitals.size()));
                userSendRequestAdapter=new UserSendRequestAdapter(UserSendRequestActivity.this,mHospitals);
                rvUserSendRequestActivity.setAdapter(userSendRequestAdapter);
                userSendRequestAdapter.notifyDataSetChanged();
                pbUserSendRequestActivity.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}