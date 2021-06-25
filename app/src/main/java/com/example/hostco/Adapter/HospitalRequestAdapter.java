package com.example.hostco.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hostco.Model.Histories;
import com.example.hostco.Model.Hospitals;
import com.example.hostco.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HospitalRequestAdapter extends RecyclerView.Adapter<HospitalRequestAdapter.ViewHolder> {

    DatabaseReference databaseReference;
    Context context;
    ArrayList<Histories> mHstories;

    public HospitalRequestAdapter(Context context, ArrayList<Histories> mHstories) {
        this.context = context;
        this.mHstories = mHstories;
    }

    @NonNull
    @Override
    public HospitalRequestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.hospital_get_request_item,parent,false);
        return new HospitalRequestAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HospitalRequestAdapter.ViewHolder holder, int position) {
        databaseReference= FirebaseDatabase.getInstance().getReference();
        Histories history=mHstories.get(position);

        Query queryGetPatientInfo=databaseReference.child("Patients").orderByChild("id").equalTo(history.getPatientId());
        queryGetPatientInfo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    holder.txtPatientFNameHospitalRequest.setText(" "+dataSnapshot.child("firstName").getValue().toString());
                    holder.txtPatientLNameHospitalRequest.setText(" "+dataSnapshot.child("lastName").getValue().toString());
                    holder.txtPatientAgeHospitalRequest.setText(" "+dataSnapshot.child("age").getValue().toString());
                    holder.txtPatientTelHospitalRequest.setText(" "+dataSnapshot.child("phone").getValue().toString());
                    holder.txtPatientAdressHospitalRequest.setText(" "+dataSnapshot.child("city").getValue().toString()+"_"+dataSnapshot.child("location").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.txtDateHospitalRequest.setText(history.getDate());
        holder.txtTimeHospitalRequest.setText(history.getTime());

        holder.btnAcceptHospitalRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query queryChangeStateHospitalRequest=databaseReference.child("HospitalRequests")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).orderByChild("id").equalTo(history.getId());

                queryChangeStateHospitalRequest.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                            dataSnapshot.getRef().child("state").setValue("Accepted");
                        }
                        Log.i("change state HospitalRequests", "onComplete: yes ");
                        mHstories.clear();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.i("change state HospitalRequests", "onFailure: "+error.getMessage());
                    }
                });

                Query queryChangeStatePatientRequest=databaseReference.child("PatientRequests")
                        .child(history.getPatientId()).orderByChild("id").equalTo(history.getId());

                queryChangeStatePatientRequest.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                            dataSnapshot.getRef().child("state").setValue("Accepted");
                        }
                        //notifyDataSetChanged();
                        //mHstories.clear();
                        Toast.makeText(context,"Request Accepted Successfully",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(context,"Request Accepted Failed!!!!",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        holder.btnRejectHospitalRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query queryChangeStateHospitalRequest=databaseReference.child("HospitalRequests")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).orderByChild("id").equalTo(history.getId());

                queryChangeStateHospitalRequest.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                            dataSnapshot.getRef().child("state").setValue("Rejected");
                        }
                        Log.i("change state HospitalRequests", "onComplete: yes ");
                        mHstories.clear();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.i("change state HospitalRequests", "onFailure: "+error.getMessage());
                    }
                });

                Query queryChangeStatePatientRequest=databaseReference.child("PatientRequests")
                        .child(history.getPatientId()).orderByChild("id").equalTo(history.getId());

                queryChangeStatePatientRequest.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                            dataSnapshot.getRef().child("state").setValue("Rejected");
                        }
                        //notifyDataSetChanged();
                        //mHstories.clear();
                        Toast.makeText(context,"Request Rejected Successfully",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(context,"Request Rejected Failed!!!!",Toast.LENGTH_SHORT).show();
                    }
                });

                Query queryForCapacityHospital=databaseReference.child("Hospitals").orderByChild("id").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
                queryForCapacityHospital.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                            int hospitalCapacity=Integer.parseInt(dataSnapshot.child("capacity").getValue().toString())+1;
                            dataSnapshot.getRef().child("capacity").setValue(hospitalCapacity);
                        }
                        Log.i("change Capacity Hospital", "onComplete: yes ");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.i("change Capacity Hospital", "onFailure: "+error.getMessage());
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return mHstories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtPatientFNameHospitalRequest;
        public TextView txtPatientLNameHospitalRequest;
        public TextView txtDateHospitalRequest;
        public TextView txtTimeHospitalRequest;
        public TextView txtPatientAgeHospitalRequest;
        public TextView txtPatientTelHospitalRequest;
        public TextView txtPatientAdressHospitalRequest;
        public MaterialButton btnAcceptHospitalRequest;
        public MaterialButton btnRejectHospitalRequest;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtPatientFNameHospitalRequest=(itemView).findViewById(R.id.txtPatientFNameHospitalRequest);
            txtPatientLNameHospitalRequest=(itemView).findViewById(R.id.txtPatientLNameHospitalRequest);
            txtDateHospitalRequest=(itemView).findViewById(R.id.txtDateHospitalRequest);
            txtTimeHospitalRequest=(itemView).findViewById(R.id.txtTimeHospitalRequest);
            txtPatientAgeHospitalRequest=(itemView).findViewById(R.id.txtPatientAgeHospitalRequest);
            txtPatientTelHospitalRequest=(itemView).findViewById(R.id.txtPatientTelHospitalRequest);
            txtPatientAdressHospitalRequest=(itemView).findViewById(R.id.txtPatientAdressHospitalRequest);
            btnAcceptHospitalRequest=(itemView).findViewById(R.id.btnAcceptHospitalRequest);
            btnRejectHospitalRequest=(itemView).findViewById(R.id.btnRejectHospitalRequest);
        }
    }
}
