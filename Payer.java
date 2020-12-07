package com.example.lyes.parking;

import android.Manifest;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Payer extends AppCompatActivity {

    Toolbar toolbar;
    LinearLayout linear_list_non_payer;
    ProgressBar progressBar_non_payed;
    List<exemple_reservation> list_non_payer;
    CardView btn_payer;
    TextView is_list_is_empty;

    int price_calculated=0;

    private static final int SEND_SMS_CODE = 23;
    private static final int REQUEST_CALL = 1;
    String phone,id_reservation;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payer);
        requestSmsSendPermission();

        toolbar=(Toolbar)findViewById(R.id.toolbar_payer);
        toolbar.setTitle("Les Dettes ");
        toolbar.inflateMenu(R.menu.menu_agent_sign_in);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.log_out:
                        SharedPreferences sharedPreferences=getSharedPreferences("Variable",MODE_PRIVATE);
                        SharedPreferences.Editor editor=sharedPreferences.edit();

                        editor.putString("Connexion_existe","Non_CONNECT");
                        editor.apply();
                        startActivity(new Intent(Payer.this, SplachScreen.class));
                        break;

                    case R.id.refresh_reservation:
                        ActivityOptions activityOptions =ActivityOptions.makeSceneTransitionAnimation(Payer.this);
                        startActivity(new Intent(Payer.this,Payer.class),activityOptions.toBundle());
                        break;

                }
                return  true;
            }


});
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        linear_list_non_payer=(LinearLayout)findViewById(R.id.Linear_list_non_payed);
        progressBar_non_payed=(ProgressBar)findViewById(R.id.progress_payer);
        list_non_payer= new ArrayList<exemple_reservation>();
        is_list_is_empty=(TextView)findViewById(R.id.is_list_is_empty);

        non_payer_list();



    }





    /////list non payer

    public void non_payer_list(){

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.Base_url)
                .addConverterFactory( GsonConverterFactory.create(gson)) //Here we are using the GsonConverterFactory to directly convert json data to object
                .build();

        //creating the api interface
        final Api api = retrofit.create(Api.class);

        //now making the call object
        //Here we are using the api method that we created inside the api interface
        Call<List<exemple_reservation>> non_payed = api.list_non_payer();

        non_payed.enqueue(new Callback<List<exemple_reservation>>() {
            @Override
            public void onResponse(Call<List<exemple_reservation>> call, Response<List<exemple_reservation>> response) {

                list_non_payer=response.body();


                if(list_non_payer.size()==0){

                    progressBar_non_payed.setEnabled(false);
                    progressBar_non_payed.setVisibility(View.GONE);
                    is_list_is_empty.setVisibility(View.VISIBLE);
                }

                else {

                    progressBar_non_payed.setEnabled(false);
                    progressBar_non_payed.setVisibility(View.GONE);
                    linear_list_non_payer.removeAllViews();

                    for (int i = 0; i < list_non_payer.size(); i++) {

                        LayoutInflater inflater = getLayoutInflater();
                        View View_payer = inflater.inflate(R.layout.exemple_payer, null);

                        TextView full_name_payer = (TextView) View_payer.findViewById(R.id.full_name_payer);
                        TextView duration_payer = (TextView) View_payer.findViewById(R.id.duration_payer);
                        TextView montant_payer = (TextView) View_payer.findViewById(R.id.price_payer);
                        final TextView phone_resrvator = (TextView) View_payer.findViewById(R.id.phone_non_payer);
                        final TextView id_resvator_no_payer = (TextView) View_payer.findViewById(R.id.id_reservation_non_payer);


                        final Button detail = (Button) View_payer.findViewById(R.id.detail_payer);
                        CardView Delete_btn = (CardView) View_payer.findViewById(R.id.delete_payer);
                        btn_payer = (CardView) View_payer.findViewById(R.id.btn_payer);

                        full_name_payer.setText(list_non_payer.get(i).getFull_name());
                        duration_payer.setText(list_non_payer.get(i).getNumber_of_days());


                        /////pour calculer les tarif:
                        if (list_non_payer.get(i).getType_car().equals("leger")) {

                            price_calculated = Integer.parseInt(duration_payer.getText().toString()) * 50;
                        } else if (list_non_payer.get(i).getType_car().equals("lourde")) {

                            price_calculated = Integer.parseInt(duration_payer.getText().toString()) * 100;
                        } else if (list_non_payer.get(i).getType_car().equals("Service")) {

                            price_calculated = Integer.parseInt(duration_payer.getText().toString()) * 70;
                        } else {
                            try {
                                price_calculated = Integer.parseInt(duration_payer.getText().toString()) * 80;
                            }catch (NumberFormatException e){
                                //
                            }

                        }

                        montant_payer.setText(""+price_calculated + " DZD");
                        phone_resrvator.setText(list_non_payer.get(i).getPhone_number());
                        id_resvator_no_payer.setText(list_non_payer.get(i).getId_reservation());


                        //////////// clicker sur le details button

                        detail.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                PopupMenu popupMenu = new PopupMenu(Payer.this, detail);
                                popupMenu.inflate(R.menu.pop_up_menu_non_payed);

                                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem menuItem) {

                                        switch (menuItem.getItemId()) {
                                            case R.id.call_non_payer:

                                                phone = phone_resrvator.getText().toString();
                                                call_reservator(phone);
                                                break;

                                            case R.id.avertir_non_payer:
                                                phone = phone_resrvator.getText().toString();
                                                String message = "Bonjour Veuillez payer votre dettes du Parking,merci";
                                                sendSms(phone, message);
                                                break;
                                        }

                                        return true;
                                    }
                                });
                                popupMenu.show();
                            }
                        });


                        /////////////////////////delete reservation


                        Delete_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                progressDialog = new ProgressDialog(Payer.this);
                                progressDialog.setMessage("waiting for deleting");
                                progressDialog.show();

                                id_reservation = id_resvator_no_payer.getText().toString();
                                delete_reservation(id_reservation);
                            }
                        });


                        ///// button payer les reservations:

                        btn_payer.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                id_reservation = id_resvator_no_payer.getText().toString();

                                progressDialog = new ProgressDialog(Payer.this);
                                progressDialog.setMessage("payement en cours");
                                progressDialog.show();
                                payer_dette(id_reservation);
                            }
                        });


                        linear_list_non_payer.addView(View_payer);

                    }
                }


            }

            @Override
            public void onFailure(Call<List<exemple_reservation>> call, Throwable t) {

            }
        });



    }
    //////////////////////////////////////////////////Suprimer les reservations

    public void delete_reservation(String id_reservation){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.Base_url)
                .addConverterFactory( GsonConverterFactory.create(gson)) //Here we are using the GsonConverterFactory to directly convert json data to object
                .build();

        //creating the api interface
        final Api api = retrofit.create(Api.class);

        //now making the call object
        //Here we are using the api method that we created inside the api interface
        Call<exemple_reservation> validez = api.delete_reservation(id_reservation);
        validez.enqueue(new Callback<exemple_reservation>() {
            @Override
            public void onResponse(Call<exemple_reservation> call, Response<exemple_reservation> response) {
                //  Toast.makeText(reservation_detail.this,"Well updated",Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
               startActivity(new Intent(Payer.this,Payer.class));

            }

            @Override
            public void onFailure(Call<exemple_reservation> call, Throwable t) {
                Snackbar.make(linear_list_non_payer,"Reservation is not deleted",Snackbar.LENGTH_LONG).show();
            }
        });


    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //payer les dette

    public  void payer_dette(String id_reservat){

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.Base_url)
                .addConverterFactory( GsonConverterFactory.create(gson)) //Here we are using the GsonConverterFactory to directly convert json data to object
                .build();

        //creating the api interface
        final Api api = retrofit.create(Api.class);

        //now making the call object
        //Here we are using the api method that we created inside the api interface

        Call<exemple_reservation> payer_dette=api.payer_dette(id_reservat);

        payer_dette.enqueue(new Callback<exemple_reservation>() {
            @Override
            public void onResponse(Call<exemple_reservation> call, Response<exemple_reservation> response) {
                progressDialog.dismiss();
                startActivity(new Intent(Payer.this,Payer.class));

            }

            @Override
            public void onFailure(Call<exemple_reservation> call, Throwable t) {

            }
        });

    }






    //////////call reservator
    public void  call_reservator(String phone){

        if (ContextCompat.checkSelfPermission(Payer.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Payer.this,
                    new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        } else {
            String dial = "tel:" + phone;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }

    }

    //// permission du telephone

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                call_reservator(phone);
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }


    /////////////////////////////////////////////SMS//////////////////////////////////////////////
    private void requestSmsSendPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.SEND_SMS },
                SEND_SMS_CODE);
    }

    private void sendSms(String number,String message){
        try{
            SmsManager sms = SmsManager.getDefault();
            ArrayList<String> parts = sms.divideMessage(message);
            sms.sendMultipartTextMessage(number, null, parts, null,
                    null);
            Snackbar.make(linear_list_non_payer,"avertissement message succefuly",Snackbar.LENGTH_LONG).show();

        }
        catch(Exception e){
            Snackbar.make(linear_list_non_payer,"Error of sending:verify your solde",Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
       startActivity(new Intent(Payer.this, Agent.class));
    }
}
