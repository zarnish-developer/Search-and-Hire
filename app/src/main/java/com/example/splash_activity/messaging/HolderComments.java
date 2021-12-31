package com.example.splash_activity.messaging;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splash_activity.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class HolderComments extends RecyclerView.ViewHolder {
    public View view;
    public CircleImageView commenterUserImageIv;
    public TextView commenterUserNameTv, commentsTextUserTv;

    public HolderComments(@NonNull View itemView) {
        super(itemView);
        view = itemView;
        commenterUserImageIv = itemView.findViewById(R.id.commenterUserImageIvId);
        commenterUserNameTv = itemView.findViewById(R.id.commenterUserNameTvId);
        commentsTextUserTv = itemView.findViewById(R.id.commentsTextUserTvId);

    }

}
