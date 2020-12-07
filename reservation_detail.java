package com.example.lyes.parking;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.transition.Slide;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class reservation_detail extends AppCompatActivity {
    ConstraintLayout constraintLayout;
    Toolbar toolbar;
    TextView full_name_detail;
    TextView car_number_detail;
    TextView phone_number_detail;
    TextView date_detail;
    String id_reservation;
    Button delete_btn;
    Button validate_btn;
    LinearLayout linearLayout;

    String phone_number;
    String full_name;
    String date;
    String is_confirmed;
    String car_number;
    String type_car;
    private static final int SEND_SMS_CODE = 23;
    private static final int REQUEST_CALL = 1;

    @Override
    public void onBackPressed() {
        startActivity(new Intent(reservation_detail.this, Les_Reservations.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_detail);

        /////////////////request permission SmS

        ///////Animation
        Slide explode =new Slide();
        explode.setSlideEdge(Gravity.RIGHT);
        explode.setDuration(800);
        getWindow().setEnterTransition(explode);



        requestSmsSendPermission();

        //////////////////////////////////////////

         type_car=getIntent().getStringExtra("type_car");
         full_name=getIntent().getStringExtra("full_name");
          car_number=getIntent().getStringExtra("car_number");
           phone_number=getIntent().getStringExtra("phone_number");
           id_reservation=getIntent().getStringExtra("id_reservation");
            date=getIntent().getStringExtra("date");
            is_confirmed=getIntent().getStringExtra("is_confirmed");


       constraintLayout=(ConstraintLayout)findViewById(R.id.context);
        full_name_detail=(TextView)findViewById(R.id.full_name_detail);
        car_number_detail=(TextView)findViewById(R.id.number_car_detail);
        phone_number_detail=(TextView)findViewById(R.id.phone_number_detail);
        date_detail=(TextView)findViewById(R.id.date_reservation_detail);
         delete_btn=(Button)findViewById(R.id.suprimer);
         validate_btn=(Button)findViewById(R.id.validate_btn);
         linearLayout=(LinearLayout)findViewById(R.id.linear_horizental);






        /////////////////////////////////////////////////////////////////
       toolbar=(Toolbar)findViewById(R.id.tool_bar_reservation);
       toolbar.setTitle("Reservation Details");
       toolbar.inflateMenu(R.menu.menu_agent_detail_reservation);
       toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
           @Override
           public boolean onMenuItemClick(MenuItem menuItem) {
               return false;
           }
       });

       toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
           @Override
           public boolean onMenuItemClick(MenuItem item) {

               switch(item.getItemId()){
                   case R.id.back_to_list_reservation:
                       startActivity(new Intent(reservation_detail.this,Les_Reservations.class));
                       break;
                   case R.id.log_out_detail:
                       SharedPreferences sharedPreferences=getSharedPreferences("Variable",MODE_PRIVATE);
                       SharedPreferences.Editor editor=sharedPreferences.edit();

                       editor.putString("Connexion_existe","Non_CONNECT");
                       editor.apply();
                       startActivity(new Intent(reservation_detail.this, SplachScreen.class));
                       break;

                   case  R.id.call_reservator:
                         make_call(phone_number);

                       break;
               }

               return true;
           }
       });

       //////////////////////////////////////////////////////////////////////////////////

      // Toast.makeText(reservation_detail.this,""+type_car+" "+full_name+" "+car_number+" "+phone_number+" "+id_reservation+" "+date+" "+is_confirmed+"",Toast.LENGTH_LONG).show();

        full_name_detail.setText(full_name);
        car_number_detail.setText(car_number);
        phone_number_detail.setText(phone_number);
        date_detail.setText(date);

   ///////////////////////////////////////////////////////////////////////////////////////

        if(is_confirmed.equals("1")){

            validate_btn.setVisibility(View.INVISIBLE);
            validate_btn.setEnabled(false);
            linearLayout.setGravity(Gravity.CENTER);


        }

        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                delete_reservation();
            }
        });


    }


    public void validez_reservation(){
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
        Call<exemple_reservation> validez = api.validez_reservation(id_reservation);
        validez.enqueue(new Callback<exemple_reservation>() {
            @Override
            public void onResponse(Call<exemple_reservation> call, Response<exemple_reservation> response) {
              //  Toast.makeText(reservation_detail.this,"Well updated",Toast.LENGTH_LONG).show();
                String message="Bonjour "+full_name+",votre demande du reservation pour la date"+date+"a été validé veuillez respecter la date du reservation,merci";
                sendSms(phone_number,message);

            }

            @Override
            public void onFailure(Call<exemple_reservation> call, Throwable t) {
                Toast.makeText(reservation_detail.this,"not updated",Toast.LENGTH_LONG).show();
            }
        });


    }


    public void delete_reservation(){
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
                startActivity(new Intent(reservation_detail.this,Les_Reservations.class));

            }

            @Override
            public void onFailure(Call<exemple_reservation> call, Throwable t) {
                Snackbar.make(constraintLayout,"Reservation is not deleted",Snackbar.LENGTH_LONG).show();
            }
        });


    }


    public void validez_la_reservation(View view) {
        validez_reservation();


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
            Snackbar.make(constraintLayout,"sending validate message succefuly",Snackbar.LENGTH_LONG).show();

        }
        catch(Exception e){
            Snackbar.make(constraintLayout,"Error of sending:verify your solde",Snackbar.LENGTH_LONG).show();
        }
    }




    /////////////////////////////////phone_call
    public void make_call(String number){

            if (ContextCompat.checkSelfPermission(reservation_detail.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(reservation_detail.this,
                        new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            } else {
                String dial = "tel:" + number;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
               make_call(phone_number);
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
}



