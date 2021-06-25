package com.example.hostco.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hostco.Model.Histories;
import com.example.hostco.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HospitalHistoryAdapter extends RecyclerView.Adapter<HospitalHistoryAdapter.ViewHolder> {

    DatabaseReference databaseReference;
    Context context;
    ArrayList<Histories> mHistories;

    public HospitalHistoryAdapter(Context context, ArrayList<Histories> mHistories) {
        this.context = context;
        this.mHistories = mHistories;
    }

    @NonNull
    @Override
    public HospitalHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.hospital_history_item,parent,false);
        return new HospitalHistoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HospitalHistoryAdapter.ViewHolder holder, int position) {
        Histories history=mHistories.get(position);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        Query query=databaseReference.child("Patients").orderByChild("id").equalTo(history.getPatientId());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    holder.txtPatientFNameHospitalHistory.setText(" "+dataSnapshot.child("firstName").getValue().toString());
                    holder.txtPatientLNameHospitalHistory.setText(" "+dataSnapshot.child("lastName").getValue().toString());
                    holder.txtPatientAgeHospitalHistory.setText(" "+dataSnapshot.child("age").getValue().toString());
                    holder.txtPatientTelHospitalHistory.setText(" "+dataSnapshot.child("phone").getValue().toString());
                    holder.txtPatientAdressHospitalHistory.setText(" "+dataSnapshot.child("city").getValue().toString()+"_"+dataSnapshot.child("location").getValue().toString());
                    holder.txtPatientHeightHospitalHistory.setText(" "+dataSnapshot.child("height").getValue().toString()+" cm");
                    holder.txtPatientWeightHospitalHistory.setText(" "+dataSnapshot.child("weight").getValue().toString()+" kg");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.txtDateHospitalHistory.setText(" "+history.getDate());
        holder.txtTimeHospitalHistory.setText(" "+history.getTime());
        if(history.getState().equals("Accepted")){
            holder.txtStatusHospitalHistory.setTextColor(Color.parseColor("#85B52A"));
        }
        else if(history.getState().equals("Rejected")){
            holder.txtStatusHospitalHistory.setTextColor(Color.parseColor("#EA0C0C"));
        }
        holder.txtStatusHospitalHistory.setText(" "+history.getState());

    }

    @Override
    public int getItemCount() {
        return mHistories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtPatientFNameHospitalHistory;
        public TextView txtPatientLNameHospitalHistory;
        public TextView txtDateHospitalHistory;
        public TextView txtTimeHospitalHistory;
        public TextView txtPatientAgeHospitalHistory;
        public TextView txtPatientTelHospitalHistory;
        public TextView txtPatientWeightHospitalHistory;
        public TextView txtPatientHeightHospitalHistory;
        public TextView txtPatientAdressHospitalHistory;
        public TextView txtStatusHospitalHistory;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtPatientFNameHospitalHistory=(itemView).findViewById(R.id.txtPatientFNameHospitalHistory);
            txtPatientLNameHospitalHistory=(itemView).findViewById(R.id.txtPatientLNameHospitalHistory);
            txtDateHospitalHistory=(itemView).findViewById(R.id.txtDateHospitalHistory);
            txtTimeHospitalHistory=(itemView).findViewById(R.id.txtTimeHospitalHistory);
            txtPatientAgeHospitalHistory=(itemView).findViewById(R.id.txtPatientAgeHospitalHistory);
            txtPatientTelHospitalHistory=(itemView).findViewById(R.id.txtPatientTelHospitalHistory);
            txtPatientWeightHospitalHistory=(itemView).findViewById(R.id.txtPatientWeightHospitalHistory);
            txtPatientHeightHospitalHistory=(itemView).findViewById(R.id.txtPatientHeightHospitalHistory);
            txtPatientAdressHospitalHistory=(itemView).findViewById(R.id.txtPatientAdressHospitalHistory);
            txtStatusHospitalHistory=(itemView).findViewById(R.id.txtStatusHospitalHistory);

        }
    }
}
