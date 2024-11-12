package com.aigirlfriend.virtuallove.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.aigirlfriend.virtuallove.HelperResizer;
import com.aigirlfriend.virtuallove.MainActivity;
import com.aigirlfriend.virtuallove.adapter.ChatListAdapter;
import com.aigirlfriend.virtuallove.databinding.FragmentChatBinding;
import com.aigirlfriend.virtuallove.db.HistoryLite;
import com.aigirlfriend.virtuallove.utils.ChatUtil;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;

public class ChatFragment extends Fragment {
    FragmentChatBinding binding;
    private ChatListAdapter mAdapter;
    private ImageView mImageCrown;
    List<String> obtainChats1;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public static ChatFragment newInstance() {
        return new ChatFragment();
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        ChatListAdapter chatListAdapter = this.mAdapter;
        if (chatListAdapter != null) {
            chatListAdapter.notifyDataSetChanged();
        }
        showChatList();
    }

    private void showChatList() {
        final List<HistoryLite> obtainChats = ChatUtil.obtainChats();
        this.obtainChats1 = new ArrayList();
        Log.d("TAG", "showChatList: " + obtainChats.size());
        if (this.mAdapter == null) {
            this.binding.mRvChats.setLayoutManager(new LinearLayoutManager(requireContext()));
            for (int i = 0; i < obtainChats.size(); i++) {
                if (obtainChats.get(i).mChats.size() > 1) {
                    if (this.obtainChats1.size() == 0) {
                        this.obtainChats1.add(obtainChats.get(i).mName);
                    } else {
                        for (int i2 = 0; i2 < this.obtainChats1.size(); i2++) {
                            if (!obtainChats.get(i).mName.equalsIgnoreCase(this.obtainChats1.get(i2))) {
                                this.obtainChats1.add(obtainChats.get(i).mName);
                            }
                        }
                    }
                }
            }
            Log.d("TAG", "showChatList: " + obtainChats.size());
            if (this.obtainChats1.size() == 0) {
                this.binding.mRvChats.setVisibility(View.GONE);
                this.binding.mRvChats.setVisibility(View.VISIBLE);
                HelperResizer.getheightandwidth(requireContext());
                HelperResizer.setSize(this.binding.imgLogo, 494, 499, true);
            } else {
                this.binding.mRvChats.setVisibility(View.VISIBLE);
                this.binding.conLoadLayout.setVisibility(View.GONE);
            }
            ChatListAdapter chatLadpter = new ChatListAdapter(obtainChats, requireContext(), this.obtainChats1) {
                @Override
                public void onItemClick(final int i) {
                    super.onItemClick(i);
                    ChatUtil.setEditHistory((HistoryLite) obtainChats.get(i));
                    ChatActivity.chat(ChatFragment.this.requireContext());
                    MainActivity.intentClick = 0;
                }
            };
            this.mAdapter = chatLadpter;
            this.binding.mRvChats.setAdapter(chatLadpter);
        }
    }

    @Override
    public void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        FragmentChatBinding inflate = FragmentChatBinding.inflate(layoutInflater, viewGroup, false);
        this.binding = inflate;
        return inflate.getRoot();
    }
}
