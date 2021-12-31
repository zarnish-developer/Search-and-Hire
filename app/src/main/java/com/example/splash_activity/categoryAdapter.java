package com.example.splash_activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class categoryAdapter  extends BaseAdapter {
    Context ctx;
    String[] workerType;
    int[] workerIcons;

    public categoryAdapter(Context ctx, String[] workerType, int[] workerIcons) {
        this.ctx = ctx;
        this.workerType = workerType;
        this.workerIcons = workerIcons;
    }

    @Override
    public int getCount() {
        return workerType.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView= LayoutInflater.from(ctx).inflate(R.layout.category_layout,parent,false);
        ImageView img=convertView.findViewById(R.id.image_icon);
        TextView textView=convertView.findViewById(R.id.tv);
        img.setImageResource(workerIcons[position]);
        textView.setText(workerType[position]);
        return convertView;
    }
}
