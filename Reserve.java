package com.example.lyes.parking;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Reserve extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    Toolbar toolbar;
    String name,car,durationn,phone,date,type_car,typed_car;
    EditText full_name,car_number,phone_number,duration;
    TextView show_date;

             CardView btn_reserve;
             ConstraintLayout constraintLayout;
             ProgressDialog pg;
             List<token_exemple> list_token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve);
        constraintLayout=(ConstraintLayout)findViewById(R.id.layout_reserve);
        full_name=(EditText)findViewById(R.id.full_name);
        car_number=(EditText)findViewById(R.id.car_number);
        phone_number=(EditText)findViewById(R.id.number_phone);

        duration=(EditText)findViewById(R.id.duration);

        btn_reserve=(CardView)findViewById(R.id.btn_reserve);


        list_token=new ArrayList<token_exemple>();
        pg=new ProgressDialog(this);


        /////////////obtenir la liste des tokens

        obtenir_la_list_des_tokens();



/////////////////////toolbar///////////////////////////////////////////////////////

        toolbar=(Toolbar)findViewById(R.id.tool_reserve);
        toolbar.setTitle("Take a resarvation");
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.inflateMenu(R.menu.menu_sign_in);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                if(menuItem.getItemId()==R.id.back_to_splash){
                    startActivity(new Intent(Reserve.this, SplachScreen.class));
                }
                return true;
            }
        });
//////////////////////////////////////////////////////////////////////////////////////////////////

        ////spinner
        ////spinner

        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.numbers, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                type_car = adapterView.getItemAtPosition(i).toString();
                //Toast.makeText(Reserve.this,""+type_car,Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ////////////////////////////////////////////////////////////////////////////////////////
        //////btn date

        Button btn_selecet_date= (Button)findViewById(R.id.btn_select_date);

        btn_selecet_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });



        /////////////////////////////////////////////////////////////////////////////////

        ///////btn reserver


        btn_reserve.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                 name=full_name.getText().toString();
                 car=car_number.getText().toString();
                 phone=phone_number.getText().toString();
                 durationn=duration.getText().toString();
                 date= show_date.getText().toString();
                 typed_car=type_car;

                Toast.makeText(Reserve.this,date+" "+name+" "+car,Toast.LENGTH_LONG).show();



                    if (name.isEmpty() || car.isEmpty() || phone.isEmpty() || durationn.isEmpty() || date.isEmpty() || type_car.equals("Select type")) {

                        Snackbar.make(constraintLayout, "verifier les champs", Snackbar.LENGTH_LONG).show();
                    } else {

                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Reserve.this);
                        LayoutInflater inflater = getLayoutInflater();
                        View view1 = inflater.inflate(R.layout.dialogue_confirm_reservation, null);

                        TextView textView = (TextView) view1.findViewById(R.id.text_dialogue);
                        CardView reserve_dialogue = (CardView) view1.findViewById(R.id.btn_dialogue_reserve);


                        textView.setText("Do you really want to reserve this place");
                        reserve_dialogue.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                notifier_les_agent();
                                add_reservation();


                            }
                        });


                        alertDialog.setView(view1);
                        alertDialog.show();

                    }





            }
        });









    }

    ////////////////////////////Add reservation
    public void add_reservation() {




            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();


            /////// connexion database

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Api.Base_url)
                    .addConverterFactory(GsonConverterFactory.create(gson)) //Here we are using the GsonConverterFactory to directly convert json data to object
                    .build();


            Api api = retrofit.create(Api.class);
            Call<exemple_reservation> exemple_reservationCall = api.addReservation(name, car, phone, durationn, date, typed_car);

            exemple_reservationCall.enqueue(new Callback<exemple_reservation>() {
                @Override
                public void onResponse(Call<exemple_reservation> call, Response<exemple_reservation> response) {
                    startActivity(new Intent(Reserve.this, Success_add_reservation.class));
                }

                @Override
                public void onFailure(Call<exemple_reservation> call, Throwable t) {
                    Toast.makeText(Reserve.this, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }



    public void obtenir_la_list_des_tokens(){
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

        Call<List<token_exemple>> tokens=api.list_des_tokens();
        tokens.enqueue(new Callback<List<token_exemple>>() {
            @Override
            public void onResponse(Call<List<token_exemple>> call, Response<List<token_exemple>> response) {
                list_token=response.body();
            }

            @Override
            public void onFailure(Call<List<token_exemple>> call, Throwable t) {

            }
        });


    }


    ////////////////////////////////Notifier les agents:

    public void notifier_les_agent() {
        pg.setMessage("adding reservations....");
        pg.show();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.Base_url)
                .addConverterFactory(GsonConverterFactory.create(gson)) //Here we are using the GsonConverterFactory to directly convert json data to object
                .build();

        //creating the api interface
        final Api api = retrofit.create(Api.class);

        //now making the call object
        //Here we are using the api method that we created inside the api interface

        for (int i = 0; i < list_token.size(); i++) {
            Call<token_exemple> notifier = api.sending_notifications(list_token.get(i).getToken().toString());
            notifier.enqueue(new Callback<token_exemple>() {
                @Override
                public void onResponse(Call<token_exemple> call, Response<token_exemple> response) {
                    pg.dismiss();

                }

                @Override
                public void onFailure(Call<token_exemple> call, Throwable t) {

                }
            });

        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

        show_date = (TextView)findViewById(R.id.show_date);
        show_date.setText(currentDateString);
    }
}
