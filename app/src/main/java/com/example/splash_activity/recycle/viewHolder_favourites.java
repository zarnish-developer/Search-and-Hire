package com.example.splash_activity.recycle;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splash_activity.R;

public class viewHolder_favourites extends RecyclerView.ViewHolder {
  public   ImageView urlImage,onlineStatusView;
   public TextView category,name,details,wage;
    public viewHolder_favourites(@NonNull View itemView)
    {
        super(itemView);
        urlImage=itemView.findViewById(R.id.profileImage);
        //  onlineStatusView=itemView.findViewById(R.id.all_users_online_status);
        category=itemView.findViewById(R.id.profession1);
        name =itemView.findViewById(R.id.name1);
        details=itemView.findViewById(R.id.description1);
        wage=itemView.findViewById(R.id.wage);
    }






}
