package com.example.lyes.parking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class About_us extends AppCompatActivity {

    Toolbar toolbar;
    Button btn_fb;
    Button btn_gmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.abus);

        toolbar=(Toolbar)findViewById(R.id.tool_abus);
        toolbar.inflateMenu(R.menu.menu_sign_in);
        toolbar.setTitle("About us");
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()){
                    case R.id.back_to_splash:
                        startActivity(new Intent(About_us.this, SplachScreen.class));
                }
                return true;
            }
        });
        //////////////////////////////////////////////////////////////////////////////////
        btn_fb=(Button)findViewById(R.id.btn_fb);
        btn_gmail=(Button)findViewById(R.id.btn_gmail);

        btn_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             go_to_fb();
            }
        });


        btn_gmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send_gmail();
            }
        });


    }

    //////////////////////////////////////////////////////////////
  public  void send_gmail(){

      String recipientList = "madani.amar02@gmail.com";
      String[] recipients = recipientList.split(",");

      Intent intent = new Intent(Intent.ACTION_SEND);
      intent.putExtra(Intent.EXTRA_EMAIL, recipients);

      intent.setType("message/rfc822");
      startActivity(Intent.createChooser(intent, "Choose an email client"));
  }

   public void go_to_fb(){

       startActivity(new Intent( About_us.this, contact_us_fb.class )  );

   }
}
