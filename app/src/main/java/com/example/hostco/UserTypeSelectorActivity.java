package com.example.hostco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserTypeSelectorActivity extends AppCompatActivity {

    private ProgressBar pbUserTypeActivity;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type_selector);

        pbUserTypeActivity=findViewById(R.id.pbUserTypeActivity);
        pbUserTypeActivity.setVisibility(View.VISIBLE);

        databaseReference= FirebaseDatabase.getInstance().getReference().child("AllUsers")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("type").exists()){
                    if(snapshot.child("type").getValue().toString().equals("user")){
                        pbUserTypeActivity.setVisibility(View.GONE);
                        startActivity(new Intent(UserTypeSelectorActivity.this,MainUserActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                    }
                    else{
                        pbUserTypeActivity.setVisibility(View.GONE);
                        startActivity(new Intent(UserTypeSelectorActivity.this,MainHospitalActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}