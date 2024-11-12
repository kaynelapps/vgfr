package com.aigirlfriend.virtuallove.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.aigirlfriend.virtuallove.R;
import com.aigirlfriend.virtuallove.adapter.holder.ReceiveHolder;
import com.aigirlfriend.virtuallove.adapter.holder.SendOutHolder;
import com.aigirlfriend.virtuallove.db.ChatLite;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ChatLite> mChats;
    private final Context mContext;
    private boolean mReceiving;
    private static final String[] FLAG_OPTIONS = {
            "Inappropriate Content",
            "Harassment",
            "Spam",
            "Other"
    };

    public ChatAdapter(Context context, List<ChatLite> list) {
        this.mContext = context;
        this.mChats = list;
    }

    public void startReceive() {
        this.mReceiving = true;
        ChatLite chatLite = new ChatLite();
        chatLite.mMsg = "";
        chatLite.mMsgType = 1;
        this.mChats.add(chatLite);
        notifyDataSetChanged();
    }

    public boolean isReceiving() {
        return this.mReceiving;
    }

    public void endReceive() {
        this.mReceiving = false;
        if (this.mChats != null && this.mChats.size() > 0) {
            if (this.mChats.get(this.mChats.size() - 1).mMsgType == 1) {
                this.mChats.remove(this.mChats.size() - 1);
            }
        }
        notifyDataSetChanged();
    }

    public void updateLastItem(String str) {
        if (this.mReceiving && this.mChats != null && this.mChats.size() > 0) {
            int size = this.mChats.size() - 1;
            ChatLite chatLite = this.mChats.get(size);
            chatLite.mMsg = str;
            this.mChats.set(size, chatLite);
            notifyItemChanged(size);
        }
    }

    public void updateData(List<ChatLite> list) {
        if (list != null) {
            this.mChats = list;
            notifyDataSetChanged();
        }
    }

    public void addNewMsg(ChatLite chatLite) {
        if (this.mChats != null) {
            this.mChats.add(chatLite);
            notifyDataSetChanged();
        }
    }

    private void showFlagDialog(ChatLite message) {
        new AlertDialog.Builder(mContext)
                .setTitle("Report Message")
                .setItems(FLAG_OPTIONS, (dialog, which) -> {
                    message.mMessageFlag = which + 1;
                    message.save();
                    notifyDataSetChanged();
                })
                .show();
    }

    @Override
    public int getItemViewType(int i) {
        return this.mChats.get(i).mMsgType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == 0) {
            return new SendOutHolder(LayoutInflater.from(this.mContext).inflate(R.layout.holder_send_out, viewGroup, false));
        }
        return new ReceiveHolder(LayoutInflater.from(this.mContext).inflate(R.layout.holder_receive, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        ChatLite chatLite = this.mChats.get(i);

        if (chatLite.mMsgType == 0) {
            SendOutHolder holder = (SendOutHolder) viewHolder;
            holder.mTvContent.setText(chatLite.mMsg);
            holder.mTvContent.setOnLongClickListener(v -> {
                showFlagDialog(chatLite);
                return true;
            });
            holder.mTvContent.setBackgroundResource(
                    chatLite.mMessageFlag > 0 ?
                            R.drawable.bg_message_flagged :
                            R.drawable.bg_message_normal
            );
        } else {
            ReceiveHolder holder = (ReceiveHolder) viewHolder;
            holder.mTvContent.setText(chatLite.mMsg);
            holder.mTvContent.setOnLongClickListener(v -> {
                showFlagDialog(chatLite);
                return true;
            });
            holder.mTvContent.setBackgroundResource(
                    chatLite.mMessageFlag > 0 ?
                            R.drawable.bg_message_flagged :
                            R.drawable.bg_message_normal
            );
        }
    }


    @Override
    public int getItemCount() {
        return this.mChats.size();
    }
}
