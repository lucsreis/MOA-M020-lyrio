package com.example.lyrio.modules.noticia.view;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lyrio.R;

public class NoticiaActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticia);

        webView = findViewById(R.id.noticia_clicada_webview);

        WebSettings ws = webView.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setSupportZoom(false);
        webView.loadUrl("https://www.vagalume.com.br/news/2018/07/13/23.html");
    }
}
