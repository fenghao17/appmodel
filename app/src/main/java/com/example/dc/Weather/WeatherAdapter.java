package com.example.dc.Weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dc.R;

import java.util.List;

public class WeatherAdapter extends ArrayAdapter<String> {
    int resourceId;
    WeatherAdapter(Context context,int resourceId,List<String> areas){
        super(context,resourceId,areas);
        this.resourceId = resourceId;
    }

    class WeatherHolder{
        TextView areaTextView;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;
        WeatherHolder holder;
        String name = getItem(position);
        if (convertView==null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            holder = new WeatherHolder();
            holder.areaTextView = view.findViewById(R.id.area_text);
            view.setTag(holder);
        }else {
            view = convertView;
            holder = (WeatherHolder)view.getTag();
        }
        holder.areaTextView.setText(name);
        return view;
    }
}
