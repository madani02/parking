package com.example.lyes.parking;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by DIRILIS on 17-04-2019.
 */

public class contact_us_fb extends AppCompatActivity {

    Toolbar toolbar;@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_web );

        WebView myWebView = findViewById(R.id.webview);
        myWebView.getSettings().setJavaScriptEnabled(true);

        myWebView.setWebViewClient(new WebViewClient());

        myWebView.loadUrl("https://www.facebook.com/jakob.menez");

        toolbar=(Toolbar)findViewById(R.id.tool_reserve);
        toolbar.setTitle("Take a resarvation");
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.inflateMenu(R.menu.menu_sign_in);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                if(menuItem.getItemId()==R.id.back_to_splash){
                    startActivity(new Intent(contact_us_fb.this, SplachScreen.class));
                }
                return true;
            }
        });



    }

}
