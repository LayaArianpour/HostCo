package com.example.hostco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.hostco.Model.Hospitals;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class EditInfoHospitalActivity extends AppCompatActivity {

    private ImageView imgBackAHospitalEditActivity;
    private ProgressBar pbHospitalEditActivity;
    private MaterialButton btnEditHospitalEditActivity;
    private TextInputEditText txtNameHospitalEditActivity,txtPhoneHospitalEditActivity,txtCapacityHospitalEditActivity
            ,txtImprovedHospitalEditActivity,txtDeathHospitalEditActivity,txtHospitalizationHospitalEditActivity,txtLocationHospitalEditActivity;

    private DatabaseReference databaseReference;

    private String hospital_Name,hospital_Phone,hospital_improved,hospital_death,hospital_hospitalization
            ,hospital_Location,hospital_Capacity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info_hospital);

        imgBackAHospitalEditActivity=findViewById(R.id.imgBackAHospitalEditActivity);
        pbHospitalEditActivity=findViewById(R.id.pbHospitalEditActivity);
        btnEditHospitalEditActivity=findViewById(R.id.btnEditHospitalEditActivity);

        txtNameHospitalEditActivity=findViewById(R.id.txtNameHospitalEditActivity);
        txtPhoneHospitalEditActivity=findViewById(R.id.txtPhoneHospitalEditActivity);
        txtCapacityHospitalEditActivity=findViewById(R.id.txtCapacityHospitalEditActivity);
        txtImprovedHospitalEditActivity=findViewById(R.id.txtImprovedHospitalEditActivity);
        txtDeathHospitalEditActivity=findViewById(R.id.txtDeathHospitalEditActivity);
        txtHospitalizationHospitalEditActivity=findViewById(R.id.txtHospitalizationHospitalEditActivity);
        txtLocationHospitalEditActivity=findViewById(R.id.txtLocationHospitalEditActivity);


        imgBackAHospitalEditActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditInfoHospitalActivity.this,MainHospitalActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        });

        readHospitalInfo();

        //
        btnEditHospitalEditActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHospitalVariables();
                if(hospital_Capacity.isEmpty()|| hospital_Location.isEmpty()||
                        hospital_hospitalization.isEmpty()|| hospital_death.isEmpty()||
                        hospital_improved.isEmpty()|| hospital_Phone.isEmpty()||
                        hospital_Name.isEmpty()){
                    Toast.makeText(EditInfoHospitalActivity.this,"Empty Fields!",Toast.LENGTH_SHORT).show();
                }
                else {
                    databaseReference=FirebaseDatabase.getInstance().getReference().child("Hospitals");
                    Query query=databaseReference.orderByChild("id").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                                dataSnapshot.getRef().child("name").setValue(hospital_Name);
                                dataSnapshot.getRef().child("improved").setValue(hospital_improved);
                                dataSnapshot.getRef().child("death").setValue(hospital_death);
                                dataSnapshot.getRef().child("hospitalization").setValue(hospital_hospitalization);
                                dataSnapshot.getRef().child("capacity").setValue(hospital_Capacity);
                                dataSnapshot.getRef().child("phone").setValue(hospital_Phone);
                                dataSnapshot.getRef().child("location").setValue(hospital_Location);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    startActivity(new Intent(EditInfoHospitalActivity.this,MainHospitalActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }
            }
        });

    }

    private void readHospitalInfo() {
        pbHospitalEditActivity.setVisibility(View.VISIBLE);
        databaseReference=FirebaseDatabase.getInstance().getReference().child("Hospitals")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    //Hospitals hospital=snapshot.getValue(Hospitals.class);
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

                    txtNameHospitalEditActivity.setText(hospital.getName());
                    txtPhoneHospitalEditActivity.setText(hospital.getPhone());
                    txtCapacityHospitalEditActivity.setText(hospital.getCapacity());
                    txtImprovedHospitalEditActivity.setText(hospital.getImproved());
                    txtDeathHospitalEditActivity.setText(hospital.getDeath());
                    txtHospitalizationHospitalEditActivity.setText(hospital.getHospitalization());
                    txtLocationHospitalEditActivity.setText(hospital.getLocation());
                }
                pbHospitalEditActivity.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setHospitalVariables(){
        hospital_Name=txtNameHospitalEditActivity.getText().toString();
        hospital_Phone=txtPhoneHospitalEditActivity.getText().toString();
        hospital_Capacity=txtCapacityHospitalEditActivity.getText().toString();
        hospital_death=txtDeathHospitalEditActivity.getText().toString();
        hospital_hospitalization=txtHospitalizationHospitalEditActivity.getText().toString();
        hospital_improved=txtImprovedHospitalEditActivity.getText().toString();
        hospital_Location=txtLocationHospitalEditActivity.getText().toString();
    }


}