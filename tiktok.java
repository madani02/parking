package com.example.lyes.parking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.lyes.parking.R;
import com.google.firebase.iid.FirebaseInstanceId;

public class tiktok extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiktok);


         /* String token= FirebaseInstanceId.getInstance().getToken();
          Toast.makeText(tiktok.this,token,Toast.LENGTH_LONG).show();*/

    }
}
