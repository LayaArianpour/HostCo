package com.example.hostco.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hostco.MainHospitalActivity;
import com.example.hostco.MainUserActivity;
import com.example.hostco.Model.Hospitals;
import com.example.hostco.R;
import com.example.hostco.StartActivity;
import com.example.hostco.UserHistoryActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class UserSendRequestAdapter extends RecyclerView.Adapter<UserSendRequestAdapter.ViewHolder> {

    DatabaseReference databaseReference;
    Context context;
    ArrayList<Hospitals> mHospitals;
    public UserSendRequestAdapter(Context context,ArrayList<Hospitals> mHospitals) {
        this.context=context;
        this.mHospitals=mHospitals;
    }

    @NonNull
    @Override
    public UserSendRequestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.user_send_request_item,parent,false);
        return new  UserSendRequestAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserSendRequestAdapter.ViewHolder holder, int position) {
        Hospitals hospital=mHospitals.get(position);
        databaseReference= FirebaseDatabase.getInstance().getReference();

        holder.txtHospitalNameHospitalItem.setText(hospital.getName()+" Hospital");
        holder.txtHospitalTelHospitalItem.setText(hospital.getPhone());
        holder.txtCapacityHospitalItem.setText("Capacity : "+hospital.getCapacity());
        holder.txtImprovedHospitalItem.setText("Improved : "+hospital.getImproved());
        holder.txtDeathHospitalItem.setText("Death : "+hospital.getDeath());
        holder.txtHospitalizationHospitalItem.setText("Hospitalization : "+hospital.getHospitalization());
        holder.txtHospitalAdressHospitalItem.setText("Address : "+hospital.getCity()+"_"+hospital.getLocation());

        holder.btnRequestHospitalItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hospitalCapacity=Integer.parseInt(hospital.getCapacity())-1;
                String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

                HashMap<String,Object> mapRequests=new HashMap<>();
                mapRequests.put("patientId", FirebaseAuth.getInstance().getCurrentUser().getUid());
                mapRequests.put("hospitalId",hospital.getId());
                mapRequests.put("date",currentDate);
                mapRequests.put("time",currentTime);
                mapRequests.put("state","Waiting for status to be determined...");
                mapRequests.put("id",databaseReference.push().getKey());


                //add to HospitalRequests
                databaseReference.child("HospitalRequests").child(hospital.getId())
                        .child(databaseReference.push().getKey()).setValue(mapRequests).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.i("AddToHospitalRequests", "onComplete: yes ");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("AddToHospitalRequests", "onFailure: "+e.getMessage());
                    }
                });


                //add to PatientRequests
                databaseReference.child("PatientRequests").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child(databaseReference.push().getKey()).setValue(mapRequests).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Query query=databaseReference.child("Hospitals").orderByChild("id").equalTo(hospital.getId());
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                                        dataSnapshot.getRef().child("capacity").setValue(hospitalCapacity);
                                    }
                                    mHospitals.clear();
                                    Toast.makeText(context, "The request was sent successfully", Toast.LENGTH_SHORT).show();
                                    context.startActivity(new Intent(context, UserHistoryActivity.class));
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(context,"The request could not be sent!!",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context,"The request could not be sent!!",Toast.LENGTH_SHORT).show();
                    }
                });



            }
        });




    }



    @Override
    public int getItemCount() {
        return mHospitals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtHospitalNameHospitalItem;
        public TextView txtHospitalTelHospitalItem;
        public TextView txtCapacityHospitalItem;
        public TextView txtImprovedHospitalItem;
        public TextView txtDeathHospitalItem;
        public TextView txtHospitalizationHospitalItem;
        public TextView txtHospitalAdressHospitalItem;
        public MaterialButton btnRequestHospitalItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtHospitalNameHospitalItem=(itemView).findViewById(R.id.txtHospitalNameHospitalItem);
            txtHospitalTelHospitalItem=(itemView).findViewById(R.id.txtHospitalTelHospitalItem);
            txtCapacityHospitalItem=(itemView).findViewById(R.id.txtCapacityHospitalItem);
            txtImprovedHospitalItem=(itemView).findViewById(R.id.txtImprovedHospitalItem);
            txtDeathHospitalItem=(itemView).findViewById(R.id.txtDeathHospitalItem);
            txtHospitalizationHospitalItem=(itemView).findViewById(R.id.txtHospitalizationHospitalItem);
            txtHospitalAdressHospitalItem=(itemView).findViewById(R.id.txtHospitalAdressHospitalItem);
            btnRequestHospitalItem=(itemView).findViewById(R.id.btnRequestHospitalItem);
        }
    }
}
