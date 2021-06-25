package com.example.hostco.Model;

public class Hospitals {

    String email;
    String name;
    String improved;
    String death;
    String hospitalization;
    String capacity;
    String phone;
    String country;
    String city;
    String location;
    String type;
    String id;

    public Hospitals() {
    }

    public Hospitals(String email, String name, String improved, String death, String hospitalization, String capacity, String phone, String country, String city, String location, String type, String id) {
        this.email = email;
        this.name = name;
        this.improved = improved;
        this.death = death;
        this.hospitalization = hospitalization;
        this.capacity = capacity;
        this.phone = phone;
        this.country = country;
        this.city = city;
        this.location = location;
        this.type = type;
        this.id = id;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImproved() {
        return improved;
    }

    public void setImproved(String improved) {
        this.improved = improved;
    }

    public String getDeath() {
        return death;
    }

    public void setDeath(String death) {
        this.death = death;
    }

    public String getHospitalization() {
        return hospitalization;
    }

    public void setHospitalization(String hospitalization) {
        this.hospitalization = hospitalization;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
