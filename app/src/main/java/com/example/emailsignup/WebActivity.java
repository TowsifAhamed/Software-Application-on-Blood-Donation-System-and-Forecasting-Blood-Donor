package com.example.emailsignup;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.webkit.WebView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class WebActivity extends AppCompatActivity {
    private WebView mwebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        mwebView = findViewById(R.id.webView_act_viewer);
        mwebView.loadUrl("http://192.168.1.107:8000/prediction/");
        mwebView.getSettings().setAppCacheEnabled(false);
    }
}