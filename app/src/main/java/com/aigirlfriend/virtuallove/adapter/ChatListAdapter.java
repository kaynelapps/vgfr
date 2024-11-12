package com.aigirlfriend.virtuallove.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.aigirlfriend.virtuallove.HelperResizer;
import com.aigirlfriend.virtuallove.R;
import com.aigirlfriend.virtuallove.db.HistoryLite;
import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {
    Context context;
    List<HistoryLite> list;
    List<String> listsize;

    public void onItemClick(int i) {
    }

    public ChatListAdapter(List<HistoryLite> list2, Context context2, List<String> list3) {
        this.context = context2;
        this.list = list2;
        this.listsize = list3;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat_list, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        viewHolder.mTvName.setText(this.list.get(i).mName);
        String str = this.list.get(i).mPic;
        Log.d("TAG", "onBindViewHolder+Pos: " + i);
        int identifier = this.context.getResources().getIdentifier(str, "drawable", this.context.getPackageName());
        Log.d("TAG", "onBindViewHolder: " + identifier + "::" + R.drawable.icon_role_head_2);
        if (i == 0) {
            viewHolder.imgRecentIcon.setVisibility(0);
        } else {
            viewHolder.imgRecentIcon.setVisibility(8);
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatListAdapter.this.onItemClick(i);
            }
        });
        Glide.with(this.context).load(Integer.valueOf(identifier)).into(viewHolder.mImageHead);
        viewHolder.mTvMsg.setText(this.list.get(i).mMsg);
        HelperResizer.getheightandwidth(this.context);
        HelperResizer.setSize(viewHolder.conChat, 980, 220);
        HelperResizer.setSize(viewHolder.mImageHead, 153, 152, true);
        HelperResizer.setSize(viewHolder.cardHead, 153, 152, true);
        HelperResizer.setSize(viewHolder.imgRecentIcon, 50, 49, true);
        Log.d("GEtData", "onBindViewHolder: " + this.list.get(i).mMsg);
        Log.d("GEtData", "onBindViewHolder: " + this.list.get(i).mPromptId);
        Log.d("GEtData", "onBindViewHolder: " + this.list.get(i).mName);
        Log.d("GEtData", "onBindViewHolder: " + this.list.get(i).mChats);
        Log.d("GEtData", "onBindViewHolder: " + this.list.get(i).mPic);
        Log.d("GEtData", "onBindViewHolder: " + this.list.get(i).mHasNewMsg);
        Log.d("GEtData", "onBindViewHolder: " + this.list.get(i).mIsVip);
        Log.d("GEtData", "onBindViewHolder: " + this.list.get(i).mTime);
    }

    @Override
    public int getItemCount() {
        Log.d("TAG", "getItemCount: ::" + this.listsize.size());
        return this.listsize.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardHead;
        ConstraintLayout conChat;
        ImageView imgRecentIcon;
        ImageView mImageHead;
        TextView mTvMsg;
        TextView mTvName;

        public ViewHolder(View view) {
            super(view);
            this.mImageHead = (ImageView) view.findViewById(R.id.mImageHead);
            this.imgRecentIcon = (ImageView) view.findViewById(R.id.imgRecentIcon);
            this.mTvName = (TextView) view.findViewById(R.id.mTvName);
            this.mTvMsg = (TextView) view.findViewById(R.id.mTvMsg);
            this.conChat = (ConstraintLayout) view.findViewById(R.id.conChat);
            this.cardHead = (CardView) view.findViewById(R.id.cardHead);
        }
    }
}
