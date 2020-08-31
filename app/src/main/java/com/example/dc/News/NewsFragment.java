package com.example.dc.News;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.dc.R;
import com.example.dc.Utility.Util;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class NewsFragment extends Fragment implements View.OnClickListener {
    List<NewsTitle> titleList = new ArrayList<>();

    Button world;

    Button county;

    Button social;

    Button enjoy;

    Button heath;

    ListView listView;

    News news;

    NewsAdapter newsAdapter;

    SwipeRefreshLayout news_swipe;

    final int STATE = 0;

    int selected = 0;


    List<NewsContent> contentList = new ArrayList<>();

    static Handler handler;

    Toolbar toolbar;

    final String SELECTED_COLOR = "#F8F8FF";
    final String UNSELECTED_COLOR = "#FF69B4";


    @Nullable
    @Override
    //初始化布局文件
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_layout,container,false);
        listView = view.findViewById(R.id.listView);

        world = view.findViewById(R.id.world);

        county = view.findViewById(R.id.county);

        social = view.findViewById(R.id.social);

        enjoy = view.findViewById(R.id.enjoy);

        heath = view.findViewById(R.id.heath);

        news_swipe = view.findViewById(R.id.news_swipe);




        world.setOnClickListener(this);
        county.setOnClickListener(this);
        social.setOnClickListener(this);
        enjoy.setOnClickListener(this);
        heath.setOnClickListener(this);

        return view;
    }

    //设置初始界面
    @Override
    public void onStart() {
        super.onStart();
        String url = "http://api.tianapi.com/world/index?key=2faddb7774ae37e4023dface1583b82a&num=10";
        toolbar = getActivity().findViewById(R.id.toolbar);
        world.setBackgroundColor(Color.parseColor(SELECTED_COLOR));
        newsAdapter = new NewsAdapter(NewsFragment.this.getActivity(), R.layout.news_title, titleList);
        listView.setAdapter(newsAdapter);


        //为listview子菜单项添加监听事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                NewsTitle title = titleList.get(i);
                Intent intent = new Intent(NewsFragment.this.getActivity(), NewsContentActivity.class);
                intent.putExtra("title",title.getTitle());
                intent.putExtra("url",title.getUrl());
                startActivity(intent);
            }
        });
        //下拉刷新功能
        news_swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            String url;
            @Override
            public void onRefresh() {
                switch (selected){
                    case 0:
                        url = "http://api.tianapi.com/world/index?key=2faddb7774ae37e4023dface1583b82a&num=10";
                        break;
                    case 1:
                        url = "http://api.tianapi.com/guonei/index?key=2faddb7774ae37e4023dface1583b82a&num=10";
                        break;
                    case 2:
                        url = "http://api.tianapi.com/social/index?key=2faddb7774ae37e4023dface1583b82a&num=10";
                        break;
                    case 3:
                        url = "http://api.tianapi.com/huabian/index?key=2faddb7774ae37e4023dface1583b82a&num=10";
                        break;
                    case 4:
                        url = "http://api.tianapi.com/health/index?key=2faddb7774ae37e4023dface1583b82a&num=10";
                        break;
                }
                showNewsTitle(url);
                news_swipe.setRefreshing(false);
            }
        });
        showNewsTitle(url);

}
    //初始化新闻资源数据
    public void init(News news){
        contentList = news.newsList;
        titleList.clear();
        for (int i = 0; i < contentList.size(); i++){
            NewsContent newsContent = contentList.get(i);

            NewsTitle title = new NewsTitle(newsContent.getPicUrl(),newsContent.getTitle(),newsContent.getUrl());

            titleList.add(title);
        }

    }
    //为按钮设置监听事件
    @Override
    public void onClick(View view) {
        String url = null;
        switch (view.getId()){
            case R.id.world:
                selected = 0;
                toolbar.setTitle("国际新闻");
                url = "http://api.tianapi.com/world/index?key=2faddb7774ae37e4023dface1583b82a&num=10";
                break;
            case R.id.county:
                selected = 1;
                toolbar.setTitle("国内新闻");
                url = "http://api.tianapi.com/guonei/index?key=2faddb7774ae37e4023dface1583b82a&num=10";
                break;
            case R.id.social:
                selected = 2;
                toolbar.setTitle("社会新闻");
                url = "http://api.tianapi.com/social/index?key=2faddb7774ae37e4023dface1583b82a&num=10";
                break;
            case R.id.enjoy:
                selected = 3;
                toolbar.setTitle("娱乐新闻");
                url = "http://api.tianapi.com/huabian/index?key=2faddb7774ae37e4023dface1583b82a&num=10";
                break;
            case R.id.heath:
                selected = 4;
                toolbar.setTitle("健康新闻");
                url = "http://api.tianapi.com/health/index?key=2faddb7774ae37e4023dface1583b82a&num=10";
                break;
        }
        //更新按钮选中状态
        if (selected==0){
            world.setBackgroundColor(Color.parseColor(SELECTED_COLOR));
            county.setBackgroundColor(Color.parseColor(UNSELECTED_COLOR));
            social.setBackgroundColor(Color.parseColor(UNSELECTED_COLOR));
            enjoy.setBackgroundColor(Color.parseColor(UNSELECTED_COLOR));
            heath.setBackgroundColor(Color.parseColor(UNSELECTED_COLOR));
        }else if (selected==1){
            world.setBackgroundColor(Color.parseColor(UNSELECTED_COLOR));
            county.setBackgroundColor(Color.parseColor(SELECTED_COLOR));
            social.setBackgroundColor(Color.parseColor(UNSELECTED_COLOR));
            enjoy.setBackgroundColor(Color.parseColor(UNSELECTED_COLOR));
            heath.setBackgroundColor(Color.parseColor(UNSELECTED_COLOR));
        }else if (selected == 2){
            world.setBackgroundColor(Color.parseColor(UNSELECTED_COLOR));
            county.setBackgroundColor(Color.parseColor(UNSELECTED_COLOR));
            social.setBackgroundColor(Color.parseColor(SELECTED_COLOR));
            enjoy.setBackgroundColor(Color.parseColor(UNSELECTED_COLOR));
            heath.setBackgroundColor(Color.parseColor(UNSELECTED_COLOR));
        }else if (selected == 3){
            world.setBackgroundColor(Color.parseColor(UNSELECTED_COLOR));
            county.setBackgroundColor(Color.parseColor(UNSELECTED_COLOR));
            social.setBackgroundColor(Color.parseColor(UNSELECTED_COLOR));
            enjoy.setBackgroundColor(Color.parseColor(SELECTED_COLOR));
            heath.setBackgroundColor(Color.parseColor(UNSELECTED_COLOR));
        }else if (selected == 4){
            world.setBackgroundColor(Color.parseColor(UNSELECTED_COLOR));
            county.setBackgroundColor(Color.parseColor(UNSELECTED_COLOR));
            social.setBackgroundColor(Color.parseColor(UNSELECTED_COLOR));
            enjoy.setBackgroundColor(Color.parseColor(UNSELECTED_COLOR));
            heath.setBackgroundColor(Color.parseColor(SELECTED_COLOR));
        }
        showNewsTitle(url);
    }

    //显示新闻标题
    public void showNewsTitle(String url){
        Util.sendRequsetWithOkhttp(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String newsContent = response.body().string();
                Gson gson = new Gson();
                news = gson.fromJson(newsContent,News.class);
                init(news);
                Message message = new Message();

                message.what = STATE;

                handler.sendMessage(message);

            }
        });

        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what==STATE){
                    newsAdapter.notifyDataSetChanged();
                }
            }
        };
    }


}
