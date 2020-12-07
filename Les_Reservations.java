package com.example.lyes.parking;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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


public class Les_Reservations extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    Toolbar toolbar;
    LinearLayout list_reservations;
    ArrayList<exemple_reservation> liste_de_reservation,liste_du_reservation_search;
    ProgressBar progressBar;
    TextView empty_liste;
    EditText full_name,car_number,phone_number,duration;
    EditText day,month,year;
    String name,car,durationn,phone,date,type_car,typed_car,dayy,monthh,yearr;
    AlertDialog.Builder search_dialogue;
    TextView textView_search;
    TextView show_date;
    View View_add;


    @Override
    public void onBackPressed() {
        startActivity(new Intent(Les_Reservations.this, Agent.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_les__reservations);

        progressBar=(ProgressBar)findViewById(R.id.progress);
        empty_liste=(TextView)findViewById(R.id.empty_liste);
        FloatingActionButton fab_add= (FloatingActionButton)findViewById(R.id.fab_Add);

        /////////////////////////////////////////////////////// refresh animation

       /*  Slide slide=new Slide();
        slide.setDuration(400);
        slide.setSlideEdge(Gravity.BOTTOM);
        getWindow().setEnterTransition(slide);*/

///////////////////////////////////////////////////////////////////////////////////////////
        toolbar=(Toolbar)findViewById(R.id.tool_reservation);
        toolbar.setTitle("Les Reservations ");
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
                        startActivity(new Intent(Les_Reservations.this, SplachScreen.class));
                        break;

                    case R.id.refresh_reservation:
                        ActivityOptions activityOptions =ActivityOptions.makeSceneTransitionAnimation(Les_Reservations.this);
                        startActivity(new Intent(Les_Reservations.this,Les_Reservations.class),activityOptions.toBundle());
                        break;

                    case R.id.chercher_reservation:

                         search_dialogue= new AlertDialog.Builder(Les_Reservations.this);
                        LayoutInflater layoutInflater=getLayoutInflater();
                        final View search_view=layoutInflater.inflate(R.layout.recherche_dialog,null);


                        Button btn_search=(Button) search_view.findViewById(R.id.btn_search);
                        final EditText editText_search=(EditText)search_view.findViewById(R.id.number_phone_search);
                        textView_search=(TextView) search_view.findViewById(R.id.message_error);

                        btn_search.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                String phone_search_number=editText_search.getText().toString();
                                chercher_une_reservation(phone_search_number);
                               // chercher_une_reservation();
                            }
                        });

                        search_dialogue.setView(search_view);
                        search_dialogue.show();


                           break;

                }
                return true;
            }
        });




    //////////////////////////////////////////////////////////////////////////////////////////
        liste_de_reservation=new ArrayList<exemple_reservation>();
        liste_du_reservation_search=new ArrayList<exemple_reservation>();
        list_reservations=(LinearLayout)findViewById(R.id.list_reservation);
       // list_reservations.removeAllViews();

              get_all_reservation();

////////////////////////////////////////////////////////////////////////////////// Ajouter une reservation ///////////////
         fab_add.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {


                 ///// formulaire dans une costume alert dialogue


                 AlertDialog.Builder add_reservation= new AlertDialog.Builder(Les_Reservations.this);
                 LayoutInflater layoutInflater_2=getLayoutInflater();
                  View_add=layoutInflater_2.inflate(R.layout.add_reservation_dialog,null);
                 add_reservation.setView(View_add);

                 full_name=(EditText)View_add.findViewById(R.id.full_name_alert);
                 car_number=(EditText)View_add.findViewById(R.id.car_number_alert);
                 phone_number=(EditText)View_add.findViewById(R.id.number_phone_alert);
                 show_date=(TextView)View_add.findViewById(R.id.show_date_dialog);

                  duration=(EditText)View_add.findViewById(R.id.duration_alert);
                  CardView add_reserv =(CardView)View_add.findViewById(R.id.btn_reserve_alert);


                  /////Select date
                 Button button_select_date_dialog=(Button)View_add.findViewById(R.id.btn_select_date_dialog);

                 button_select_date_dialog.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {

                         DialogFragment datePicker = new DatePickerFragment();
                         datePicker.show(getSupportFragmentManager(), "date picker");
                     }
                 });


                 add_reserv.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {


                         name=full_name.getText().toString();
                         car=car_number.getText().toString();
                         phone=phone_number.getText().toString();
                         durationn=duration.getText().toString();
                         date=show_date.getText().toString();

                         // Toast.makeText(Les_Reservations.this,name+" "+car+" "+phone+" "+durationn+" "+date,Toast.LENGTH_LONG).show();
                         add_reservation(name,car,phone,durationn,date);
                     }
                 });

                 add_reservation.show();
             }
         });
 /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    }



    public void get_all_reservation(){

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
                      Call<List<exemple_reservation>> call_fil = api.get_reservation();

                      //then finallly we are making the call using enqueue()
                      //it takes callback interface as an argument
                      //and callback is having two methods onRespnose() and onFailure
                      //if the request is successfull we will get the correct response and onResponse will b

                      call_fil.enqueue(new Callback<List<exemple_reservation>>() {
                          @Override
                          public void onResponse(Call<List<exemple_reservation>> call, Response<List<exemple_reservation>> response) {

                              liste_de_reservation= (ArrayList<exemple_reservation>) response.body();
                              progressBar.setVisibility(View.GONE);
                              progressBar.setEnabled(false);

                              if(liste_de_reservation.size()==0){

                                  empty_liste.getText();

                              }else {

                                  empty_liste.setEnabled(false);
                                  empty_liste.setVisibility(View.GONE);

                                  for (int i = 0; i < liste_de_reservation.size(); i++) {
                                      LayoutInflater layoutInflater = getLayoutInflater();
                                      View view = layoutInflater.inflate(R.layout.reservation_exemple, null, false);

                                      final TextView type_car = (TextView) view.findViewById(R.id.type_car_view);
                                      final TextView full_name = (TextView) view.findViewById(R.id.full_name_view);
                                      final TextView car_number = (TextView) view.findViewById(R.id.car_number_view);
                                      final TextView phone_number = (TextView) view.findViewById(R.id.phone_number_view);
                                      final TextView days = (TextView) view.findViewById(R.id.number_days_view);
                                      final TextView id_reservation = (TextView) view.findViewById(R.id.id_reservation);
                                      final TextView date = (TextView) view.findViewById(R.id.date_view);
                                      final TextView is_confirmed = (TextView) view.findViewById(R.id.is_confirmed_view);
                                      ImageView imageView = (ImageView) view.findViewById(R.id.indice);


                                      type_car.setText(liste_de_reservation.get(i).getType_car());
                                      full_name.setText(liste_de_reservation.get(i).getFull_name());
                                      car_number.setText(liste_de_reservation.get(i).getCar_number());
                                      phone_number.setText(liste_de_reservation.get(i).getPhone_number());
                                      id_reservation.setText(liste_de_reservation.get(i).getId_reservation());
                                      days.setText(liste_de_reservation.get(i).getNumber_of_days());
                                      date.setText(liste_de_reservation.get(i).getDate_de_reservation());
                                      is_confirmed.setText(liste_de_reservation.get(i).getIs_confirmed());

                                      if (is_confirmed.getText().equals("1")) {
                                          imageView.setBackgroundResource(R.drawable.checked);
                                      } else {
                                          imageView.setBackgroundResource(R.drawable.hourglass);
                                      }


                                      ///////////////go to details:

                                      view.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View view) {
                                              ActivityOptions activityOpt = ActivityOptions.makeSceneTransitionAnimation(Les_Reservations.this);
                                              Intent intent = new Intent(Les_Reservations.this, reservation_detail.class);
                                              intent.putExtra("type_car", type_car.getText());
                                              intent.putExtra("full_name", full_name.getText());
                                              intent.putExtra("car_number", car_number.getText());
                                              intent.putExtra("phone_number", phone_number.getText());
                                              intent.putExtra("id_reservation", id_reservation.getText());
                                              intent.putExtra("date", date.getText());
                                              intent.putExtra("is_confirmed", is_confirmed.getText());
                                              startActivity(intent, activityOpt.toBundle());
                                          }
                                      });

                                      list_reservations.addView(view);
                                  }
                  }
            }

            @Override
            public void onFailure(Call<List<exemple_reservation>> call, Throwable t) {

            }
        });

    }


    //////////////add_reservation

    public void add_reservation(String name,String car,String phone,String durationn,String date) {



        Gson gson = new GsonBuilder()
                .setLenient()
                .create();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.Base_url)
                .addConverterFactory(GsonConverterFactory.create(gson)) //Here we are using the GsonConverterFactory to directly convert json data to object
                .build();

        Api api = retrofit.create(Api.class);
        Call<exemple_reservation> exemple_reservationCall = api.addReservation(name, car, phone, durationn, date, "agent_acces");

        exemple_reservationCall.enqueue(new Callback<exemple_reservation>() {
            @Override
            public void onResponse(Call<exemple_reservation> call, Response<exemple_reservation> response) {
                startActivity(new Intent(Les_Reservations.this, Les_Reservations.class));
            }

            @Override
            public void onFailure(Call<exemple_reservation> call, Throwable t) {
                Toast.makeText(Les_Reservations.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


  ////////////////////////////////////////////////////////chercher une reservation

    public void chercher_une_reservation(String number){

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
        Call<List<exemple_reservation>> search = api.get_search_reservation(number);

        //then finallly we are making the call using enqueue()
        //it takes callback interface as an argument
        //and callback is having two methods onRespnose() and onFailure
        //if the request is successfull we will get the correct response and onResponse will b

        search.enqueue(new Callback<List<exemple_reservation>>() {
            @Override
            public void onResponse(Call<List<exemple_reservation>> call, Response<List<exemple_reservation>> response) {


                liste_du_reservation_search= (ArrayList<exemple_reservation>) response.body();

                 if(liste_du_reservation_search.size()==0){
                     textView_search.setText("Reservator not found");
                     textView_search.setTextColor(Color.RED);
                 }
                for (int i = 0; i < liste_du_reservation_search.size(); i++) {

                    ActivityOptions activityOpt = ActivityOptions.makeSceneTransitionAnimation(Les_Reservations.this);
                    Intent intent = new Intent(Les_Reservations.this, reservation_detail.class);
                    intent.putExtra("full_name", liste_du_reservation_search.get(i).getFull_name());
                    intent.putExtra("car_number", liste_du_reservation_search.get(i).getCar_number());
                    intent.putExtra("phone_number", liste_du_reservation_search.get(i).getPhone_number());
                    intent.putExtra("id_reservation",liste_du_reservation_search.get(i).getId_reservation());
                    intent.putExtra("date", liste_du_reservation_search.get(i).getDate_de_reservation());
                    intent.putExtra("is_confirmed", liste_du_reservation_search.get(i).getIs_confirmed());
                    startActivity(intent, activityOpt.toBundle());
                }

                             }


            @Override
            public void onFailure(Call<List<exemple_reservation>> call, Throwable t) {

                Toast.makeText(Les_Reservations.this,t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

        ///////////////////
        show_date = (TextView)View_add.findViewById(R.id.show_date_dialog);
        show_date.setText(currentDateString);
    }
}
