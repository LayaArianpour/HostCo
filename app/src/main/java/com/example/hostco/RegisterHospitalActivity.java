package com.example.hostco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterHospitalActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private RelativeLayout container1InfoAccountRegisterHospital,container2InfoPersonalRegisterHospital,container3InfoAddressRegisterHospital;
    private TextInputEditText txtEmailRegisterHospital,txtPasswordRegisterHospital,txtRePasswordRegisterHospital,
            txtNameRegisterHospital,txtPhoneRegisterHospital,txtImproved,txtdeath,txtHospitalization,txtCapacity,
            txtLocationRegisterHospital;
    private TextView txtHaveAccountLoginHospital;
    private Spinner spinnerCityRegisterHospital;

    private ProgressBar pbRegisterHospital;
    private ImageView imgBackActivityRegisterHospital;
    private MaterialButton btnNextRegisterHospital;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    private int activeContainer=1;

    private String hospital_Email,hospital_Password,hospital_RePassword
            ,hospital_Name,hospital_Phone,hospital_improved,hospital_death,hospital_hospitalization
            ,hospital_Country="Iran",hospital_City,hospital_Location,hospital_Capacity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_hospital);

        container1InfoAccountRegisterHospital=findViewById(R.id.container1InfoAccountRegisterHospital);
        txtEmailRegisterHospital=findViewById(R.id.txtEmailRegisterHospital);
        txtPasswordRegisterHospital=findViewById(R.id.txtPasswordRegisterHospital);
        txtRePasswordRegisterHospital=findViewById(R.id.txtRePasswordRegisterHospital);

        container2InfoPersonalRegisterHospital=findViewById(R.id.container2InfoPersonalRegisterHospital);
        txtNameRegisterHospital=findViewById(R.id.txtNameRegisterHospital);
        txtPhoneRegisterHospital=findViewById(R.id.txtPhoneRegisterHospital);
        txtImproved=findViewById(R.id.Improved);
        txtdeath=findViewById(R.id.death);
        txtHospitalization=findViewById(R.id.Hospitalization);
        txtCapacity=findViewById(R.id.Capacity);


        container3InfoAddressRegisterHospital=findViewById(R.id.container3InfoAddressRegisterHospital);
        spinnerCityRegisterHospital=(Spinner) findViewById(R.id.spinnerCityRegisterHospital);
        txtLocationRegisterHospital=findViewById(R.id.txtLocationRegisterHospital);



        ArrayAdapter<CharSequence> adapterCity=ArrayAdapter.createFromResource(this,R.array.city, android.R.layout.simple_spinner_dropdown_item);
        adapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCityRegisterHospital.setAdapter(adapterCity);
        spinnerCityRegisterHospital.setOnItemSelectedListener(this);



        btnNextRegisterHospital=findViewById(R.id.btnNextRegisterHospital);
        imgBackActivityRegisterHospital=findViewById(R.id.imgBackActivityRegisterHospital);
        txtHaveAccountLoginHospital=findViewById(R.id.txtHaveAccountLoginHospital);
        pbRegisterHospital=(ProgressBar)findViewById(R.id.pbRegisterHospital);


        databaseReference= FirebaseDatabase.getInstance().getReference();
        firebaseAuth= FirebaseAuth.getInstance();

        txtHaveAccountLoginHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterHospitalActivity.this,LogInActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        btnNextRegisterHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activeContainer==1){
                    hospital_Email=txtEmailRegisterHospital.getText().toString();
                    hospital_Password=txtPasswordRegisterHospital.getText().toString();
                    hospital_RePassword=txtRePasswordRegisterHospital.getText().toString();

                    if(hospital_Email.isEmpty()||hospital_Password.isEmpty()||
                            hospital_RePassword.isEmpty()){
                        Toast.makeText(RegisterHospitalActivity.this,"Empty Fields!",Toast.LENGTH_SHORT).show();
                    }

                    else if(hospital_Password.length()<6){
                        Toast.makeText(RegisterHospitalActivity.this,"The password should not be less than 6 characters!!",Toast.LENGTH_SHORT).show();
                    }
                    else if(!hospital_Password.contentEquals(hospital_RePassword)){
                        Toast.makeText(RegisterHospitalActivity.this,"The password and re password are not the same !",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        container1InfoAccountRegisterHospital.setVisibility(View.GONE);
                        container2InfoPersonalRegisterHospital.setVisibility(View.VISIBLE);
                        activeContainer=2;
                    }
                }
                else if(activeContainer==2){
                    hospital_Name=txtNameRegisterHospital.getText().toString();
                    hospital_Phone=txtPhoneRegisterHospital.getText().toString();
                    hospital_improved=txtImproved.getText().toString();
                    hospital_death=txtdeath.getText().toString();
                    hospital_hospitalization=txtHospitalization.getText().toString();
                    hospital_Capacity=txtCapacity.getText().toString();

                    if(hospital_Name.isEmpty()||hospital_Phone.isEmpty()||
                            hospital_improved.isEmpty()||hospital_death.isEmpty()||
                            hospital_hospitalization.isEmpty()|| hospital_Capacity.isEmpty()){
                        Toast.makeText(RegisterHospitalActivity.this,"Empty Fields!",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        btnNextRegisterHospital.setText("REGISTER");
                        container2InfoPersonalRegisterHospital.setVisibility(View.GONE);
                        container3InfoAddressRegisterHospital.setVisibility(View.VISIBLE);
                        activeContainer=3;
                    }
                }
                else if(activeContainer==3){
                    hospital_City=spinnerCityRegisterHospital.getSelectedItem().toString();
                    hospital_Location=txtLocationRegisterHospital.getText().toString();

                    if(hospital_City.isEmpty()|| hospital_Location.isEmpty()){
                        Toast.makeText(RegisterHospitalActivity.this,"Empty Fields!",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        registerHospital(hospital_Email,hospital_Password,hospital_Name,hospital_Phone,hospital_improved,hospital_death,
                                hospital_hospitalization,hospital_Capacity,hospital_City,hospital_Country,hospital_Location);
                    }
                }

            }
        });

        imgBackActivityRegisterHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activeContainer==1){
                    btnNextRegisterHospital.setText("NEXT");
                    startActivity(new Intent(RegisterHospitalActivity.this,StartActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
                else if(activeContainer==2){
                    btnNextRegisterHospital.setText("NEXT");
                    container2InfoPersonalRegisterHospital.setVisibility(View.GONE);
                    container1InfoAccountRegisterHospital.setVisibility(View.VISIBLE);
                    activeContainer=1;
                }
                else if(activeContainer==3){
                    btnNextRegisterHospital.setText("NEXT");
                    container3InfoAddressRegisterHospital.setVisibility(View.GONE);
                    container2InfoPersonalRegisterHospital.setVisibility(View.VISIBLE);
                    activeContainer=2;
                }
            }
        });
    }

    private void registerHospital(String email, String password, String name, String phone, String improved, String death,
                                  String hospitalization,String capacity, String city,String country, String location) {
        pbRegisterHospital.setVisibility(View.VISIBLE);
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                HashMap<String,Object> map=new HashMap<>();
                map.put("email",email);
                map.put("type","hospital");
                map.put("id",firebaseAuth.getCurrentUser().getUid());
                databaseReference.child("AllUsers").child(firebaseAuth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
                HashMap<String,Object> map_hospital=new HashMap<>();
                map_hospital.put("email",email);
                map_hospital.put("name",name);
                map_hospital.put("improved",improved);
                map_hospital.put("death",death);
                map_hospital.put("hospitalization",hospitalization);
                map_hospital.put("capacity",capacity);
                map_hospital.put("phone",phone);
                map_hospital.put("country",country);
                map_hospital.put("city",city);
                map_hospital.put("location",location);
                map_hospital.put("type","hospital");
                map_hospital.put("id",firebaseAuth.getCurrentUser().getUid());
                databaseReference.child("Hospitals").child(firebaseAuth.getCurrentUser().getUid()).setValue(map_hospital).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        pbRegisterHospital.setVisibility(View.GONE);
                        Toast.makeText(RegisterHospitalActivity.this,"Register is Successful",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterHospitalActivity.this,MainHospitalActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pbRegisterHospital.setVisibility(View.GONE);
                Toast.makeText(RegisterHospitalActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item=parent.getItemAtPosition(position).toString();
        hospital_Country=item;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}