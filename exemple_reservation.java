package com.example.lyes.parking;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lyes on 12-09-2019.
 */

public class exemple_reservation {


    @SerializedName("is_confirmed")
    String is_confirmed;

    public String getIs_confirmed() {
        return is_confirmed;
    }

    public void setIs_confirmed(String is_confirmed) {
        this.is_confirmed = is_confirmed;
    }

    public String getId_reservation() {
        return id_reservation;
    }

    public void setId_reservation(String id_reservation) {
        this.id_reservation = id_reservation;
    }

    @SerializedName("id_reservation")
    String id_reservation;

    @SerializedName("type_car")
    String type_car;

    @SerializedName("full_name")
    String full_name;

    @SerializedName("car_number")
    String car_number;

    @SerializedName("phone_number")
    String phone_number;


    @SerializedName("date_de_reservation")
    String Date_de_reservation;


    @SerializedName("number_of_days")
    String number_of_days;


    public String getType_car() {
        return type_car;
    }

    public void setType_car(String type_car) {
        this.type_car = type_car;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getCar_number() {
        return car_number;
    }

    public void setCar_number(String car_number) {
        this.car_number = car_number;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getDate_de_reservation() {
        return Date_de_reservation;
    }

    public void setDate_de_reservation(String date_de_reservation) {
        Date_de_reservation = date_de_reservation;
    }

    public String getNumber_of_days() {
        return number_of_days;
    }

    public void setNumber_of_days(String number_of_days) {
        this.number_of_days = number_of_days;
    }
}
