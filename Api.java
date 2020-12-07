package com.example.lyes.parking;

import com.example.lyes.parking.Login_exemple;
import com.example.lyes.parking.Notification_exemple;
import com.example.lyes.parking.exemple_reservation;
import com.example.lyes.parking.token_exemple;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Amar on 12-06-2020.
 */

public interface Api {

    String Base_url="https://parkingghraba2013.000webhostapp.com/";

    @FormUrlEncoded
    @POST("login_retrofit.php")
    Call<Login_exemple> verifier_user(@Field("user") String user, @Field("pass") String pass);

    @FormUrlEncoded
    @POST("add_reservation.php")
    Call<exemple_reservation> addReservation(@Field("full_name") String name,
                                             @Field("car_number") String number,
                                             @Field("phone_number") String phone_numbre,
                                             @Field("number_of_days") String duree,
                                             @Field("date_de_reservation") String date,
                                             @Field("type_car") String type);

    @GET("get_reservations.php")
    Call<List<exemple_reservation>> get_reservation();


    @FormUrlEncoded
    @POST("validez_reservation.php")
    Call<exemple_reservation> validez_reservation(@Field("id_reservation") String id_reservation);

    @FormUrlEncoded
    @POST("delete_reservation.php")
    Call<exemple_reservation> delete_reservation(@Field("id_reservation") String id_reservation);

    @FormUrlEncoded
    @POST("search_about_reservation.php")
    Call<List<exemple_reservation>> get_search_reservation(@Field("phone_number") String phone_number);

    @GET("list_non_payed.php")
    Call<List<exemple_reservation>> list_non_payer();


    @FormUrlEncoded
    @POST("payer_dettes.php")
    Call<exemple_reservation> payer_dette(@Field("id_reservation") String id_reservation);


    @GET("les_notifications.php")
    Call<List<Notification_exemple>> getNotifications();

    ///////////////////////////////////// les useres (Tokens)

    @GET("list_token.php")
    Call<List<token_exemple>> list_des_tokens();

    @FormUrlEncoded
    @POST("sending_notifications.php")
    Call<token_exemple> sending_notifications(@Field("token") String token);



}
