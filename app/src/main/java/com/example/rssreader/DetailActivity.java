package com.example.rssreader;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DetailActivity extends AppCompatActivity {

    private TextView titleText;
    private TextView descriptionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        titleText = findViewById(R.id.titleText);
        descriptionText = findViewById(R.id.descriptionText);

        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");

        titleText.setText(title);
        descriptionText.setText(description);
    }
}