package com.example.hostco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.hostco.Model.Patients;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class EditInfoUserActivity extends AppCompatActivity {

    private ProgressBar pbEditUser;
    private TextInputEditText txtUserNameEditUser,txtFirstNameEditUser,txtLastNameEditUser,txtAgeEditUser
            ,txtHeightEditUser,txtWeightEditUser,txtPhoneEditUser,txtLocationEditUser;
    private MaterialButton btnEditEditUser;
    private ImageView imgBackActivityEditUser;

    private DatabaseReference databaseReference;
    private String user_UserName , user_FirstName,user_LastName,user_Age,user_Height,user_Weight,user_Phone
            ,user_Location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info_user);

        pbEditUser=findViewById(R.id.pbEditUser);
        btnEditEditUser=findViewById(R.id.btnEditEditUser);
        imgBackActivityEditUser=findViewById(R.id.imgBackActivityEditUser);

        txtUserNameEditUser=findViewById(R.id.txtUserNameEditUser);
        txtFirstNameEditUser=findViewById(R.id.txtFirstNameEditUser);
        txtLastNameEditUser=findViewById(R.id.txtLastNameEditUser);
        txtAgeEditUser=findViewById(R.id.txtAgeEditUser);
        txtHeightEditUser=findViewById(R.id.txtHeightEditUser);
        txtWeightEditUser=findViewById(R.id.txtWeightEditUser);
        txtPhoneEditUser=findViewById(R.id.txtPhoneEditUser);
        txtLocationEditUser=findViewById(R.id.txtLocationEditUser);


        readUserInfo();

        imgBackActivityEditUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(EditInfoUserActivity.this,MainUserActivity.class)
                       .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        });


        btnEditEditUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setVariables();
                if(user_UserName.isEmpty()|| user_FirstName.isEmpty()||
                user_LastName.isEmpty()|| user_Age.isEmpty()||
                user_Height.isEmpty()|| user_Height.isEmpty()||
                user_Phone.isEmpty()|| user_Location.isEmpty()){
                    Toast.makeText(EditInfoUserActivity.this,"Empty Fields!",Toast.LENGTH_SHORT).show();
                }
                else{
                    databaseReference=FirebaseDatabase.getInstance().getReference().child("Patients");
                    Query query=databaseReference.orderByChild("id").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                                dataSnapshot.getRef().child("userName").setValue(user_UserName);
                                dataSnapshot.getRef().child("firstName").setValue(user_FirstName);
                                dataSnapshot.getRef().child("lastName").setValue(user_LastName);
                                dataSnapshot.getRef().child("age").setValue(user_Age);
                                dataSnapshot.getRef().child("weight").setValue(user_Weight);
                                dataSnapshot.getRef().child("height").setValue(user_Height);
                                dataSnapshot.getRef().child("phone").setValue(user_Phone);
                                dataSnapshot.getRef().child("location").setValue(user_Location);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    startActivity(new Intent(EditInfoUserActivity.this,MainUserActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }


            }
        });

    }

    private void setVariables() {
        user_UserName=txtUserNameEditUser.getText().toString();
        user_FirstName=txtFirstNameEditUser.getText().toString();
        user_LastName=txtLastNameEditUser.getText().toString();
        user_Age=txtAgeEditUser.getText().toString();
        user_Height=txtHeightEditUser.getText().toString();
        user_Weight=txtWeightEditUser.getText().toString();
        user_Phone=txtPhoneEditUser.getText().toString();
        user_Location=txtLocationEditUser.getText().toString();
    }

    private void readUserInfo() {
        pbEditUser.setVisibility(View.VISIBLE);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Patients").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Patients user=snapshot.getValue(Patients.class);
                    txtUserNameEditUser.setText(user.getUserName());
                    txtFirstNameEditUser.setText(user.getFirstName());
                    txtLastNameEditUser.setText(user.getLastName());
                    txtAgeEditUser.setText(user.getAge());
                    txtHeightEditUser.setText(user.getHeight());
                    txtWeightEditUser.setText(user.getWeight());
                    txtPhoneEditUser.setText(user.getPhone());
                    txtLocationEditUser.setText(user.getLocation());
                }
                pbEditUser.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}