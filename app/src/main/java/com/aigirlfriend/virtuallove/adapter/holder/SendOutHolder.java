package com.aigirlfriend.virtuallove.adapter.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.aigirlfriend.virtuallove.HelperResizer;
import com.aigirlfriend.virtuallove.R;

public class SendOutHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    public TextView mTvContent;
    public ImageView btnFlag;

    public SendOutHolder(View view) {
        super(view);
        this.mTvContent = (TextView) view.findViewById(R.id.mTvContent);
        this.imageView = (ImageView) view.findViewById(R.id.imageView);
        this.btnFlag = (ImageView) view.findViewById(R.id.btnFlag);
        HelperResizer.getheightandwidth(view.getContext());
        HelperResizer.setSize(this.imageView, 122, 122, true);
    }
}
