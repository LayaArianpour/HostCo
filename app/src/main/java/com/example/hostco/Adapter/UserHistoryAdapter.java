package com.example.hostco.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.example.hostco.UserHistoryActivity;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.hostco.R.color.gray;
import static com.example.hostco.R.color.green_light;
import static com.example.hostco.R.color.red;

public class UserHistoryAdapter extends RecyclerView.Adapter<UserHistoryAdapter.ViewHolder> {

    DatabaseReference databaseReference;
    Context context;
    ArrayList<Histories> mHstories;

    public UserHistoryAdapter(Context context, ArrayList<Histories> mHstories) {
        this.context = context;
        this.mHstories = mHstories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.user_history_item,parent,false);
        return new UserHistoryAdapter.ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        databaseReference= FirebaseDatabase.getInstance().getReference();
        Histories history=mHstories.get(position);
        Query queryGetHospitalInfo=databaseReference.child("Hospitals").orderByChild("id").equalTo(history.getHospitalId());
        queryGetHospitalInfo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    holder.txtHospitalNameUserHistory.setText(dataSnapshot.child("name").getValue().toString()+" Hospital");
                    holder.txtHospitalTelUserHistory.setText(dataSnapshot.child("phone").getValue().toString());
                    holder.txtHospitalAdressUserHistory.setText(" "+dataSnapshot.child("city").getValue().toString()+"_"+dataSnapshot.child("location").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.txtDateUserHistory.setText(" "+history.getDate());
        holder.txtTimeUserHistory.setText(" "+history.getTime());
        if(history.getState().equals("Accepted")){
            holder.txtStatusUserHistory.setTextColor(Color.parseColor("#85B52A"));
        }
        else if(history.getState().equals("Rejected")){
            holder.txtStatusUserHistory.setTextColor(Color.parseColor("#EA0C0C"));
            holder.btnCancelUserHistory.setVisibility(View.GONE);
        }
        else if(history.getState().equals("Waiting for status to be determined...")){
            holder.txtStatusUserHistory.setTextColor(Color.parseColor("#B8B8B8"));
        }
        holder.txtStatusUserHistory.setText(" "+history.getState());


        holder.btnCancelUserHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query queryDeleteHospitalRequest=databaseReference.child("HospitalRequests").child(history.getHospitalId()).orderByChild("id").equalTo(history.getId());
                queryDeleteHospitalRequest.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                            dataSnapshot.getRef().removeValue();
                        }
                        Log.i("Cancel request HospitalRequests", "onComplete: yes ");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.i("Cancel request HospitalRequests", "onFailure: "+error.getMessage());
                    }
                });

                Query queryDeletePatientRequest=databaseReference.child("PatientRequests").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).orderByChild("id").equalTo(history.getId());
                queryDeletePatientRequest.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                            dataSnapshot.getRef().removeValue();
                        }
                        Log.i("Cancel request PatientRequests", "onComplete: yes ");
                        mHstories.clear();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.i("Cancel request PatientRequests", "onFailure: "+error.getMessage());
                    }
                });


                Query queryForCapacityHospital=databaseReference.child("Hospitals").orderByChild("id").equalTo(history.getHospitalId());
                queryForCapacityHospital.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                            int hospitalCapacity=Integer.parseInt(dataSnapshot.child("capacity").getValue().toString())+1;
                            dataSnapshot.getRef().child("capacity").setValue(hospitalCapacity);
                        }
                        //mHstories.clear();
                        Toast.makeText(context,"Cancel request completed successfully",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(context,"Cancel request failed!!!!",Toast.LENGTH_SHORT).show();
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

        public TextView txtHospitalNameUserHistory;
        public TextView txtHospitalTelUserHistory;
        public TextView txtDateUserHistory;
        public TextView txtTimeUserHistory;
        public TextView txtStatusUserHistory;
        public TextView txtHospitalAdressUserHistory;
        public MaterialButton btnCancelUserHistory;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtHospitalNameUserHistory=(itemView).findViewById(R.id.txtHospitalNameUserHistory);
            txtHospitalTelUserHistory=(itemView).findViewById(R.id.txtHospitalTelUserHistory);
            txtDateUserHistory=(itemView).findViewById(R.id.txtDateUserHistory);
            txtTimeUserHistory=(itemView).findViewById(R.id.txtTimeUserHistory);
            txtStatusUserHistory=(itemView).findViewById(R.id.txtStatusUserHistory);
            txtHospitalAdressUserHistory=(itemView).findViewById(R.id.txtHospitalAdressUserHistory);
            btnCancelUserHistory=(itemView).findViewById(R.id.btnCancelUserHistory);
        }
    }
}
