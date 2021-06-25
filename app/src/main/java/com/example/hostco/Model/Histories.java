package com.example.hostco.Model;

import com.google.firebase.auth.FirebaseAuth;

public class Histories {
    String patientId;
    String hospitalId;
    String date;
    String time;
    String state;
    String id;

    public Histories() {
    }

    public Histories(String patientId, String hospitalId, String date, String time, String state, String id) {
        this.patientId = patientId;
        this.hospitalId = hospitalId;
        this.date = date;
        this.time = time;
        this.state = state;
        this.id = id;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
