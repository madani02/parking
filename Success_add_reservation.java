package com.example.lyes.parking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Success_add_reservation extends AppCompatActivity {

    Button btn_back;
    TextView mssg;

    private static final int SEND_SMS_CODE = 23;

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Success_add_reservation.this, Reserve.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_add_reservation);



        btn_back=(Button)findViewById(R.id.go_to_back);
        mssg=(TextView)findViewById(R.id.msgg);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Success_add_reservation.this, SplachScreen.class));
            }
        });
    }







}
