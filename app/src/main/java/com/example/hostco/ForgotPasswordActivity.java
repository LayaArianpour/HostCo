package com.example.hostco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private TextInputEditText txtEmailForgotPasswordActivity;
    private MaterialButton btnForgotPasswordActivity;
    private ImageView imgBackForgotPasswordActivity;
    private ProgressBar pbForgotPasswordActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        txtEmailForgotPasswordActivity=findViewById(R.id.txtEmailForgotPasswordActivity);
        btnForgotPasswordActivity=findViewById(R.id.btnForgotPasswordActivity);
        imgBackForgotPasswordActivity=findViewById(R.id.imgBackForgotPasswordActivity);
        pbForgotPasswordActivity=findViewById(R.id.pbForgotPasswordActivity);

        imgBackForgotPasswordActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLoginActivity();
            }
        });

        btnForgotPasswordActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail=txtEmailForgotPasswordActivity.getText().toString();
                if(TextUtils.isEmpty(mail)){
                    Toast.makeText(ForgotPasswordActivity.this,"Email Is Empty!!!",Toast.LENGTH_SHORT).show();
                }
                else{
                    pbForgotPasswordActivity.setVisibility(View.VISIBLE);
                    FirebaseAuth.getInstance().sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                pbForgotPasswordActivity.setVisibility(View.GONE);
                                Toast.makeText(ForgotPasswordActivity.this, "Please Check Your Email", Toast.LENGTH_SHORT).show();
                                goToLoginActivity();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ForgotPasswordActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            }
        });
    }

    private void goToLoginActivity() {
        startActivity(new Intent(ForgotPasswordActivity.this,LogInActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
        finish();
    }
}