package com.example.lyes.parking;

import android.app.ActivityOptions;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Sign_in extends AppCompatActivity {

    Toolbar toolbar;
    CardView btn_sign_in;
    EditText user_name;
    EditText user_pass;
    private static String var;
    ConstraintLayout constraintLayout;
    ProgressDialog pg_dialogue;
    NotificationManager notifManager ;
    static  int int_id=0;

    TextView new_compte;

    //////////////////////////////// verifie connexion by Shared prefernces;

    /////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        btn_sign_in=(CardView)findViewById(R.id.btn_signin);
        user_name=(EditText)findViewById(R.id.user);
        user_pass=(EditText)findViewById(R.id.password);
        constraintLayout=(ConstraintLayout)findViewById(R.id.sign_in_page);
        pg_dialogue= new ProgressDialog(this);

        //todo:=========================create compte===============================//
        new_compte=(TextView)findViewById(R.id.new_compte);
        new_compte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Sign_in.this, Client_compte.class));
            }
        });
       //todo===========================================================//



///////////////////////////////////////////////tool bar///////////////////////////
        toolbar=(Toolbar)findViewById(R.id.tool_bar_sign_in);
        toolbar.inflateMenu(R.menu.menu_sign_in);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.getItemId()==R.id.back_to_splash){

                    startActivity(new Intent(Sign_in.this, SplachScreen.class));
                }

                return true;            }
        });



/////////////////////////////////////////////////////////////////////////////////////////

        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pg_dialogue.setMessage("Authentification.....");
                pg_dialogue.show();
                login_agent(user_name.getText().toString(),user_pass.getText().toString());
            }
        });
////////////////////////////////////////////////////////////////////////////////////////////////





    }





    ///////////////fonctionLogin
    public void login_agent(final String name, String password){

        if(name.isEmpty() || password.isEmpty()){
            pg_dialogue.dismiss();
            Snackbar.make(constraintLayout,"user name or password is empty",Snackbar.LENGTH_LONG).show();
            return;
        }else{

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();


            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Api.Base_url)
                    .addConverterFactory( GsonConverterFactory.create(gson)) //Here we are using the GsonConverterFactory to directly convert json data to object
                    .build();

            Api api= retrofit.create( Api.class );
            Call<Login_exemple> conenct_agent= api.verifier_user(name,password );

            conenct_agent.enqueue(new Callback<Login_exemple>() {
                @Override
                public void onResponse(Call<Login_exemple> call, Response<Login_exemple> response) {
                     pg_dialogue.dismiss();
                    variable_shared_preferences_is_connected();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        createNotification_when_android_version_superieur_a_8("Bienvenue Msr: " + name, Sign_in.this);
                    }

                    ActivityOptions activityOptions=ActivityOptions.makeSceneTransitionAnimation(Sign_in.this);
                    startActivity(new Intent(Sign_in.this, Agent.class),activityOptions.toBundle());
                    //Snackbar.make(constraintLayout,"Connexion avec success",Snackbar.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Call<Login_exemple> call, Throwable t) {
                    Snackbar.make(constraintLayout,"Erreur de connexion ",Snackbar.LENGTH_LONG).show();
                }
            });


        }

    }


    ///////fonction pour verifier levariabe de connexion

    public void variable_shared_preferences_is_connected(){
        SharedPreferences sharedPreferences=getSharedPreferences("Variable",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();

        editor.putString("Connexion_existe","CONNECT");
        editor.apply();
    }

    public String load_variable_de_connexion(){
        SharedPreferences sharedPreferences = getSharedPreferences("Variable", MODE_PRIVATE);
        var = sharedPreferences.getString("Connexion_existe", "");
        return var;
    }


    /////// create Notification

    public void createNotification_when_android_version_superieur_a_8(String aMessage, Context context) {
        final int NOTIFY_ID = 0; // ID of notification
        String id = "1"; // default_channel_id
        String title = "parking mob"; // Default Channel
        Intent intent;
        PendingIntent pendingIntent;
        NotificationCompat.Builder builder = null;
        if (notifManager == null) {
            notifManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notifManager.getNotificationChannel(id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, title, importance);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notifManager.createNotificationChannel(mChannel);
            }
            builder = new NotificationCompat.Builder(context, id);
            intent = new Intent(context, Les_Reservations.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            builder.setContentTitle(aMessage)                            // required
                    .setSmallIcon(android.R.drawable.ic_btn_speak_now)   // required
                    .setContentText(context.getString(R.string.app_name)) // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setTicker(aMessage)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        }
    
        Notification notification = builder.build();
        notifManager.notify(NOTIFY_ID, notification);
    }








}
