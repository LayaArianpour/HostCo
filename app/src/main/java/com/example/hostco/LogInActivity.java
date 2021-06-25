package com.example.hostco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hostco.Model.Hospitals;
import com.example.hostco.Model.Patients;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class LogInActivity extends AppCompatActivity {

    private MaterialButton btnLoginActivity;
    private ImageView imgBackActivityLogin;
    private TextInputEditText txtEmailLogin,txtPasswordLogin;
    private TextView txtCreateAccountLogin ,txtForgotPasswordLogin;
    private String email,password;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private ArrayList<Hospitals> mHospitals;
    private ArrayList<Patients> mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        imgBackActivityLogin=findViewById(R.id.imgBackActivityLogin);
        txtCreateAccountLogin=findViewById(R.id.txtCreateAccountLogin);
        txtForgotPasswordLogin=findViewById(R.id.txtForgotPasswordLogin);
        btnLoginActivity=findViewById(R.id.btnLoginActivity);

        txtEmailLogin=findViewById(R.id.txtEmailLogin);
        txtPasswordLogin=findViewById(R.id.txtPasswordLogin);

        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference=FirebaseDatabase.getInstance().getReference();

        mHospitals=new ArrayList<Hospitals>();
        mUser=new ArrayList<Patients>();



        txtCreateAccountLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogInActivity.this,StartActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        imgBackActivityLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogInActivity.this,StartActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        btnLoginActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login_Email=txtEmailLogin.getText().toString();
                String login_pass=txtPasswordLogin.getText().toString();

                if(login_Email.isEmpty()||login_pass.isEmpty()){
                    Toast.makeText(LogInActivity.this,"Empty Fields!",Toast.LENGTH_SHORT).show();
                }
                else{
                    email=txtEmailLogin.getText().toString();
                    password=txtPasswordLogin.getText().toString();
                    login(email,password);
                }
            }
        });

        txtForgotPasswordLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogInActivity.this,ForgotPasswordActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        });

    }

    private void login(String emailOfLogin, String passwordOfLogin) {
        firebaseAuth.signInWithEmailAndPassword(emailOfLogin,passwordOfLogin).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LogInActivity.this,"Login is Successful!",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LogInActivity.this,UserTypeSelectorActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LogInActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }



}