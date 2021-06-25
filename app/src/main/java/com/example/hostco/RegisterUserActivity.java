package com.example.hostco;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

public class RegisterUserActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private RelativeLayout container1InfoAccountRegisterUser,container2InfoPersonalRegisterUser,container3InfoAddressRegisterUser;

    private TextInputEditText txtUserNameRegisterUser,txtEmailRegisterUser
            ,txtPasswordRegisterUser,txtRePasswordRegisterUser;
    private TextInputEditText txtFirstNameRegisterUser,txtLastNameRegisterUser,txtAgeRegisterUser
            ,txtHeightRegisterUser,txtWeightRegisterUser,txtPhoneRegisterUser
            ,txtLocationRegisterUser;

    private Spinner spinnerCityRegisterUser;

    private MaterialButton btnNextRegisterUser;
    private ImageView imgBackActivityRegisterUser;
    private int activeContainer=1;
    private String user_UserName ,user_Email,user_Password,user_RePassword
            ,user_FirstName,user_LastName,user_Age,user_Height,user_Weight,user_Phone
            ,user_Country="Iran",user_City,user_Location;

    private TextView txtHaveAccountLogin;
    ProgressBar progressBar;
	
	/**/
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);



        container1InfoAccountRegisterUser=findViewById(R.id.container1InfoAccountRegisterUser);
        txtUserNameRegisterUser=findViewById(R.id.txtUserNameRegisterUser);
        txtEmailRegisterUser=findViewById(R.id.txtEmailRegisterUser);
        txtPasswordRegisterUser=findViewById(R.id.txtPasswordRegisterUser);
        txtRePasswordRegisterUser=findViewById(R.id.txtRePasswordRegisterUser);



        container2InfoPersonalRegisterUser=findViewById(R.id.container2InfoPersonalRegisterUser);
        txtFirstNameRegisterUser=findViewById(R.id.txtFirstNameRegisterUser);
        txtLastNameRegisterUser=findViewById(R.id.txtLastNameRegisterUser);
        txtAgeRegisterUser=findViewById(R.id.txtAgeRegisterUser);
        txtHeightRegisterUser=findViewById(R.id.txtHeightRegisterUser);
        txtWeightRegisterUser=findViewById(R.id.txtWeightRegisterUser);
        txtPhoneRegisterUser=findViewById(R.id.txtPhoneRegisterUser);



        container3InfoAddressRegisterUser=findViewById(R.id.container3InfoAddressRegisterUser);
        spinnerCityRegisterUser=(Spinner) findViewById(R.id.spinnerCityRegisterUser);
        txtLocationRegisterUser=findViewById(R.id.txtLocationRegisterUser);


        
        ArrayAdapter<CharSequence> adapterCity=ArrayAdapter.createFromResource(this,R.array.city, android.R.layout.simple_spinner_dropdown_item);
        adapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCityRegisterUser.setAdapter(adapterCity);
        spinnerCityRegisterUser.setOnItemSelectedListener(this);



        btnNextRegisterUser=findViewById(R.id.btnNextRegisterUser);
        imgBackActivityRegisterUser=findViewById(R.id.imgBackActivityRegisterUser);
        txtHaveAccountLogin=findViewById(R.id.txtHaveAccountLogin);
        progressBar=(ProgressBar)findViewById(R.id.pbRegisterUser);

        databaseReference= FirebaseDatabase.getInstance().getReference();
        firebaseAuth=FirebaseAuth.getInstance();

		
		
        txtHaveAccountLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterUserActivity.this,LogInActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });


        btnNextRegisterUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activeContainer==1){
                    user_UserName=txtUserNameRegisterUser.getText().toString();
                    user_Email=txtEmailRegisterUser.getText().toString();
                    user_Password=txtPasswordRegisterUser.getText().toString();
                    user_RePassword=txtRePasswordRegisterUser.getText().toString();

                    if(user_UserName.isEmpty()||user_Email.isEmpty()||
                            user_Password.isEmpty()||user_RePassword.isEmpty()){
                        Toast.makeText(RegisterUserActivity.this,"Empty Fields!",Toast.LENGTH_SHORT).show();
                    }

                    else if(user_Password.length()<6){
                        Toast.makeText(RegisterUserActivity.this,"The password should not be less than 6 characters!!",Toast.LENGTH_SHORT).show();
                    }
                    else if(!user_Password.contentEquals(user_RePassword)){
                        Toast.makeText(RegisterUserActivity.this,"The password and re password are not the same !",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        container1InfoAccountRegisterUser.setVisibility(View.GONE);
                        container2InfoPersonalRegisterUser.setVisibility(View.VISIBLE);
                        activeContainer=2;
                    }
                }
                else if(activeContainer==2){
                    user_FirstName=txtFirstNameRegisterUser.getText().toString();
                    user_LastName=txtLastNameRegisterUser.getText().toString();
                    user_Age=txtAgeRegisterUser.getText().toString();
                    user_Height=txtHeightRegisterUser.getText().toString();
                    user_Weight=txtWeightRegisterUser.getText().toString();
                    user_Phone=txtPhoneRegisterUser.getText().toString();

                    if(user_FirstName.isEmpty()||user_LastName.isEmpty()||
                            user_Age.isEmpty()||user_Height.isEmpty()||
                            user_Weight.isEmpty()||user_Phone.isEmpty()){
                        Toast.makeText(RegisterUserActivity.this,"Empty Fields!",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        btnNextRegisterUser.setText("REGISTER");
                        container2InfoPersonalRegisterUser.setVisibility(View.GONE);
                        container3InfoAddressRegisterUser.setVisibility(View.VISIBLE);
                        activeContainer=3;
                    }
                }
                else if(activeContainer==3){
                    user_City=spinnerCityRegisterUser.getSelectedItem().toString();
                    user_Location=txtLocationRegisterUser.getText().toString();

                    if(user_City.isEmpty()|| user_Location.isEmpty()){
                        Toast.makeText(RegisterUserActivity.this,"Empty Fields!",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        registerUser(user_UserName,user_Email,user_Password,user_FirstName,user_LastName,user_Age,
                                user_Weight,user_Height,user_Phone,user_Country,user_City,user_Location);
                    }
                }

            }
        });

        imgBackActivityRegisterUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activeContainer==1){
                    btnNextRegisterUser.setText("NEXT");
                    startActivity(new Intent(RegisterUserActivity.this,StartActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
                else if(activeContainer==2){
                    btnNextRegisterUser.setText("NEXT");
                    container2InfoPersonalRegisterUser.setVisibility(View.GONE);
                    container1InfoAccountRegisterUser.setVisibility(View.VISIBLE);
                    activeContainer=1;
                }
                else if(activeContainer==3){
                    btnNextRegisterUser.setText("NEXT");
                    container3InfoAddressRegisterUser.setVisibility(View.GONE);
                    container2InfoPersonalRegisterUser.setVisibility(View.VISIBLE);
                    activeContainer=2;
                }
            }
        });

    }

	
	
	/**/
    private void registerUser(String userName, String email, String password, String firstName, String lastName, String age,
                              String weight, String height, String phone, String country, String city, String location) {
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                HashMap<String,Object> map=new HashMap<>();
                map.put("email",email);
                map.put("type","user");
                map.put("id",firebaseAuth.getCurrentUser().getUid());
                databaseReference.child("AllUsers").child(firebaseAuth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });

                HashMap<String,Object> map_user=new HashMap<>();
                map_user.put("userName",userName);
                map_user.put("email",email);
                map_user.put("firstName",firstName);
                map_user.put("lastName",lastName);
                map_user.put("age",age);
                map_user.put("weight",weight);
                map_user.put("height",height);
                map_user.put("phone",phone);
                map_user.put("country",country);
                map_user.put("city",city);
                map_user.put("location",location);
                map_user.put("type","user");
                map_user.put("id",firebaseAuth.getCurrentUser().getUid());
                databaseReference.child("Patients").child(firebaseAuth.getCurrentUser().getUid()).setValue(map_user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(RegisterUserActivity.this,"Register is Successful",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterUserActivity.this,MainUserActivity.class)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            finish();
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(RegisterUserActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        user_City=item;

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}