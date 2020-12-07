package com.example.lyes.parking;

import android.app.ActivityOptions;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;

import android.transition.Explode;
import android.util.Pair;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.widget.Button;
import android.widget.ImageView;


import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;


public class Agent extends AppCompatActivity {

        CardView btn_voir_les_reservation;
        CardView btn_les_facture;
        Button Logout;
        ImageView image_shared;

        String last_notifications="";

        List<Notification_exemple> list_notifications;

    NotificationManager notifManager ;
    static  int int_id=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent);

        //////////////////////////////////////////////Animations

        Explode explode=new Explode();
        explode.setDuration(500);
        explode.setInterpolator( new AnticipateInterpolator());
        getWindow().setEnterTransition(explode);

        ////////////////////////////////////////////

        FirebaseMessaging.getInstance().subscribeToTopic("test");
        FirebaseInstanceId.getInstance().getToken();


        btn_voir_les_reservation=(CardView)findViewById(R.id.btn_voir_les_reservations);
        btn_les_facture=(CardView)findViewById(R.id.btn_payement_et_facture);
        Logout=(Button)findViewById(R.id.button_logout_agent);
        image_shared=(ImageView)findViewById(R.id.img_sh);

        btn_voir_les_reservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pair[] pair =new Pair[1];
                pair[0]= new Pair<View,String>(image_shared,"demande_shared");

                ActivityOptions activityOptions=ActivityOptions.makeSceneTransitionAnimation(Agent.this,pair);
                startActivity(new Intent(Agent.this, Les_Reservations.class),activityOptions.toBundle());


            }
        });

      Logout.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              SharedPreferences sharedPreferences=getSharedPreferences("Variable",MODE_PRIVATE);
              SharedPreferences.Editor editor=sharedPreferences.edit();

              editor.putString("Connexion_existe","Non_CONNECT");
              editor.apply();
              startActivity(new Intent(Agent.this, SplachScreen.class));
          }
      });

      btn_les_facture.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              startActivity(new Intent(Agent.this, Payer.class));
          }
      });


      list_notifications=new ArrayList<Notification_exemple>();

    }

    @Override
    public void onBackPressed() {

        SharedPreferences sharedPreferences=getSharedPreferences("Variable",MODE_PRIVATE);
         String Connex =sharedPreferences.getString("Connexion_existe", "");
        if(Connex.equals("CONNECT")){
            startActivity(new Intent(Agent.this,SplachScreen.class));
        }
    }




    //////////create_notifications

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
