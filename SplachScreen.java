package com.example.lyes.parking;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.transition.Slide;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class SplachScreen extends AppCompatActivity {

    ImageView image_sign;
    TextView text_sign;


/////////////////////////////////////////////////////////////////
    CardView reserve_btn;
    CardView sigin_btn;
    CardView location_btn;
    CardView about_btn;
    String Connex;



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splach_screen);


        ///////////////////////////////////////////////////////

        Slide slide=new Slide();
        slide.setSlideEdge(Gravity.LEFT);
        slide.setDuration(500);
        getWindow().setEnterTransition(slide);


        ////////////////////////////////////////////////////////////////////////////////
        ///for animation this

              image_sign=(ImageView)findViewById(R.id.image_signin_shared);
              text_sign=(TextView)findViewById(R.id.text_signin_shared);
          ////    /////////////////////////////////////////////////////////////////////////////

        reserve_btn=(CardView)findViewById(R.id.ajouter_resevation);
        sigin_btn=(CardView)findViewById(R.id.sign_in);
        location_btn=(CardView)findViewById(R.id.location);
        about_btn=(CardView)findViewById(R.id.about_us);

        sigin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 SharedPreferences sharedPreferences = getSharedPreferences("Variable", MODE_PRIVATE);
                 Connex =sharedPreferences.getString("Connexion_existe", "");

                 if(Connex.equals("CONNECT")){
                     ActivityOptions activityOptions=ActivityOptions.makeSceneTransitionAnimation(SplachScreen.this);
                     startActivity(new Intent(SplachScreen.this, Agent.class),activityOptions.toBundle());

                 }else {


                     Pair[] pair = new Pair[2];
                     pair[0] = new Pair<View, String>(image_sign, "image_sign_shared");
                     pair[1] = new Pair<View, String>(text_sign, "text_sign_shared");

                     ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(SplachScreen.this, pair);
                     Intent intent = new Intent(SplachScreen.this, Sign_in.class);
                     startActivity(intent, activityOptions.toBundle());
                 }

            }
        });

        reserve_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SplachScreen.this, Reserve.class));
            }
        });

        location_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SplachScreen.this, GeoLocalisation.class));
            }
        });

        about_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SplachScreen.this, About_us.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
     startActivity(new Intent(SplachScreen.this,SplachScreen.class));
    }
}
