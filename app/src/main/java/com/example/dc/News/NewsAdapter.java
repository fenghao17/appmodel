package com.example.dc.News;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.dc.R;
import java.util.List;

public class NewsAdapter extends ArrayAdapter<NewsTitle> {
    int resourceId;
    class NewsHolder{
        ImageView newsPic;

        TextView newsTitle;
    }


   public NewsAdapter(Context context,int resourceId, List<NewsTitle> objects){
        super(context,resourceId,objects);
        this.resourceId = resourceId;
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        NewsTitle newsTitle = getItem(position);
        NewsHolder holder;
        if (convertView==null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            holder = new NewsHolder();
            holder.newsPic = view.findViewById(R.id.news_title_pic);
            holder.newsTitle = view.findViewById(R.id.news_title_content);
            view.setTag(holder);
        }else {
            view = convertView;
            holder = (NewsHolder)view.getTag();
        }

        Glide.with(getContext()).load(newsTitle.getImageUri()).into(holder.newsPic);
        holder.newsTitle.setText(newsTitle.getTitle());

        return view;
    }
}
