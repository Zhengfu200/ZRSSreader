package com.example.rssreader;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DetailActivity extends AppCompatActivity {

    private TextView titleText;
    private TextView descriptionText;
    private TextView linkText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        titleText = findViewById(R.id.titleText);
        descriptionText = findViewById(R.id.descriptionText);
        linkText = findViewById(R.id.linkText);

        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        final String link = getIntent().getStringExtra("link");

        titleText.setText(title);

        if(link != null){
            linkText.setText("Link:"+link);
            linkText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                    startActivity(browserIntent);
                }
            });// 支持 HTML 中的链接点击
        }else{
            linkText.setVisibility(View.GONE); // 如果没有链接，隐藏链接的TextView
        }
        //Html转富文本
        if(description != null){
            descriptionText.setText(Html.fromHtml(description, Html.FROM_HTML_MODE_LEGACY));
            descriptionText.setMovementMethod(LinkMovementMethod.getInstance());// 支持 HTML 中的链接点击
        }
    }
}