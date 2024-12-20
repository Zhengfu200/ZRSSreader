package com.example.rssreader;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Url;

public class MainActivity extends AppCompatActivity {

    private EditText rssUrl;
    private  Button btnFetch;
    private Button btnPreset1, btnPreset2, btnPreset3;
    private  RecyclerView recyclerView;
    private RssAdapter rssAdapter;
    private LinearLayout buttonLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rssUrl = findViewById(R.id.rssUrl);
        btnFetch = findViewById(R.id.btnFetch);
        recyclerView = findViewById(R.id.recyclerView);
        btnPreset1 = findViewById(R.id.btnPreset1);
        btnPreset2 = findViewById(R.id.btnPreset2);
        btnPreset3 = findViewById(R.id.btnPreset3);
        buttonLayout = findViewById(R.id.buttonLayout);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnPreset1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rssUrl.setText("https://plink.anyfeeder.com/zaobao/realtime/china");
            }
        });

        btnPreset2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rssUrl.setText("https://plink.anyfeeder.com/people");
            }
        });

        btnPreset3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rssUrl.setText("https://plink.anyfeeder.com/infzm/news");
            }
        });


        btnFetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = rssUrl.getText().toString().trim();
                fetchRssFeed(url);
                buttonLayout.setVisibility(View.GONE);  // 隐藏预设按钮布局
            }
        });
    }

    private void fetchRssFeed(String url){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://example.com")
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        RssService service = retrofit.create(RssService.class);
        Call<RssFeed> call = service.getRssFeed(url);

        call.enqueue(new Callback<RssFeed>() {
            @Override
            public void onResponse(Call<RssFeed> call, Response<RssFeed> response) {
                if(response.isSuccessful() && response.body() != null){
                    List<RssItem> items = response.body().channel.items;
                    rssAdapter = new RssAdapter(items, new RssAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(RssItem item) {
                            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                            intent.putExtra("title", item.title);
                            intent.putExtra("description", item.description);
                            intent.putExtra("link", item.link);
                            startActivity(intent);
                        }
                    });
                    recyclerView.setAdapter(rssAdapter);
                }
            }

            @Override
            public void onFailure(Call<RssFeed> call, Throwable t) {
                Log.e("Rss Error", t.getMessage());
            }
        });
    }

    interface  RssService{
        @GET
        Call<RssFeed> getRssFeed(@Url String url);
    }

    @Root(name = "rss", strict = false)
    public static class RssFeed {
        @Element(name = "channel")
        public Channel channel;
    }

    @Root(name = "channel", strict = false)
    public static class Channel {
        @ElementList(name = "item", inline = true)
        public List<RssItem> items;
    }

    @Root(name = "item", strict = false)
    public static class RssItem {
        @Element(name = "title")
        public String title;

        @Element(name = "description" , required = false)
        public String description;

        @Element(name = "link", required = false)
        public  String link;
    }
}