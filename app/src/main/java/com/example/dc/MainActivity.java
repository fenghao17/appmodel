package com.example.dc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.dc.News.NewsFragment;
import com.example.dc.Weather.WeatherActivity;

import org.litepal.tablemanager.Connector;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    FrameLayout frameLayout;

    final int GAMEFRAGMENT = 0;

    final int MUSICFRAGMENT = 1;

    final int NEWSFRAGMENT = 2;

    final int SHOWFRAGMENT = 3;

    final int WEATHERFRAGMENT = 4;

    int flag_fragment = 0;

    LinearLayout layout_game;

    LinearLayout layout_music;

    LinearLayout layout_show;

    LinearLayout layout_news;

    LinearLayout layout_weather;

    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);


        frameLayout = findViewById(R.id.frameLayout);

        layout_game = findViewById(R.id.layout_game);

        layout_music = findViewById(R.id.layout_music);

        layout_show = findViewById(R.id.layout_show);

        layout_news = findViewById(R.id.layout_news);

        layout_weather = findViewById(R.id.layout_weather);



        layout_game.setOnClickListener(this);
        layout_music.setOnClickListener(this);
        layout_show.setOnClickListener(this);
        layout_news.setOnClickListener(this);
        layout_weather.setOnClickListener(this);

        Connector.getWritableDatabase();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //游戏功能代码
            case R.id.layout_game:

                break;
                //音乐功能代码
            case R.id.layout_music:

                break;
                //直播功能代码
            case R.id.layout_show:
                break;
                //新闻功能代码
            case R.id.layout_news:
                toolbar.setTitle("国际新闻");
                Fragment fragment_news = new NewsFragment();
                replaceFragment(fragment_news);
                break;
                //天气功能代码
            case R.id.layout_weather:
                Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout,fragment);
        transaction.commit();
    }
}
