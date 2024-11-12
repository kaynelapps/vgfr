package com.aigirlfriend.virtuallove.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.aigirlfriend.virtuallove.HelperResizer;
import com.aigirlfriend.virtuallove.R;
import com.aigirlfriend.virtuallove.adapter.ChatAdapter;
import com.aigirlfriend.virtuallove.callback.ChatRequestCallback;
import com.aigirlfriend.virtuallove.callback.SimpleTextWatcher;
import com.aigirlfriend.virtuallove.databinding.ActivityChatBinding;
import com.aigirlfriend.virtuallove.databinding.DialogLimitPremBinding;
import com.aigirlfriend.virtuallove.db.ChatLite;
import com.aigirlfriend.virtuallove.db.HistoryLite;
import com.aigirlfriend.virtuallove.http.HttpUtils;
import com.aigirlfriend.virtuallove.utils.ChatUtil;
import com.aigirlfriend.virtuallove.utils.ClickUtil;
import com.aigirlfriend.virtuallove.utils.ScreenUtil;
import com.aigirlfriend.virtuallove.utils.StringUtil;
import java.util.List;

public class ChatActivity extends BaseActivity implements View.OnLayoutChangeListener {
    ActivityChatBinding binding;
    Dialog dialog;

    DialogLimitPremBinding dialogLimitPremBinding;
    Dialog limitDialog;

    public ChatAdapter mChatAdapter;
    private boolean mEjectKeyboard;
    private int mGptLevel;
    public Handler mHandler = new Handler(Looper.getMainLooper());
    private HistoryLite mHistory;
    private long mLastHideKeyboardTime;

    public int mNestedScrollY;

    public StringBuilder mRespondStr;

    public Runnable mTimeoutRunnable = new Runnable() {
        @Override
        public void run() {
            if (ChatActivity.this.mChatAdapter != null && ChatActivity.this.mChatAdapter.isReceiving()) {
                ChatActivity.this.mChatAdapter.endReceive();
            }
        }
    };

    public static void chat(Context context) {
        Intent intent = new Intent(context, ChatActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setFlags(1024, 1024);
        ActivityChatBinding inflate = ActivityChatBinding.inflate(getLayoutInflater());
        this.binding = inflate;
        setContentView((View) inflate.getRoot());



        resize();
        setViewLis();
        selectGpt35();
        initData();

    }

    private void resize() {
        HelperResizer.getheightandwidth(this);
        HelperResizer.setSize(this.binding.conTopBar, 1080, 150);
        HelperResizer.setSize(this.binding.constraintLayout2, 360, 95);
        HelperResizer.setSize(this.binding.lGPT35, 160, 80);
        HelperResizer.setSize(this.binding.lGPT40, 160, 80);
        HelperResizer.setSize(this.binding.btnBack, 108, 108);
        HelperResizer.setSize(this.binding.mBtnSend, 100, 100);
        HelperResizer.setSize(this.binding.btnMsgSend, 64, 64);
        HelperResizer.setSize(this.binding.emohiIcon, 68, 68);
    }

    private void initData() {
        HistoryLite editHistory = ChatUtil.getEditHistory();
        this.mHistory = editHistory;
        Log.d("TAG", "showChats: " + editHistory.mName + "::" + editHistory);
        if (editHistory != null) {
            this.binding.mTvRoleName.setText(editHistory.mName);
            showChats();
        }
    }

    public void showChats() {
        List<ChatLite> list = this.mHistory.mChats;
        Log.d("TAG", "showChats: " + list.size());
        ChatAdapter chatAdapter = this.mChatAdapter;
        if (chatAdapter == null) {
            this.mChatAdapter = new ChatAdapter(this, list);
            this.binding.mRvChats.setLayoutManager(new LinearLayoutManager(this));
            this.binding.mRvChats.setAdapter(this.mChatAdapter);
        } else {
            chatAdapter.updateData(list);
        }
        this.mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ChatActivity.this.binding.mNestedScrollView.fullScroll(130);
            }
        }, 300);
    }

    private void setViewLis() {
        this.binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatActivity.this.hideKeyboard();
                ChatActivity.this.finish();
            }
        });
        this.binding.lGPT35.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatActivity.this.selectGpt35();
            }
        });
        this.binding.lGPT40.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatActivity.this.selectGpt40();
            }
        });
        this.binding.mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatActivity.this.sendQuestion();
            }
        });
        this.mHandler.postDelayed(new Runnable() {
            @Override
            public final void run() {
                ChatActivity.this.binding.mRootView.addOnLayoutChangeListener(ChatActivity.this);
            }
        }, 1000);
        if (Build.VERSION.SDK_INT >= 23) {
            this.binding.mNestedScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View view, int i, int i2, int i3, int i4) {
                    ChatActivity.this.onChangeScrollCall(view, i, i2, i3, i4);
                }
            });
        }
        this.binding.mEditInput.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                String trim = editable.toString().trim();
                if (ChatActivity.this.mChatAdapter == null || !ChatActivity.this.mChatAdapter.isReceiving()) {
                    ChatActivity.this.binding.mBtnSend.setEnabled(!StringUtil.isEmpty(trim));
                } else {
                    ChatActivity.this.binding.mBtnSend.setEnabled(false);
                }
                StringUtil.isEmpty(trim);
            }
        });
    }


    public void selectGpt35() {
        this.mGptLevel = 100;
        this.binding.lGPT35.setBackgroundResource(R.drawable.gpt_press);
        this.binding.mTvGpt35.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        this.binding.lGPT40.setBackgroundResource(R.drawable.gpt_pro_unpress);
        this.binding.mTvGpt40.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
    }

    public void onChangeScrollCall(View view, int i, int i2, int i3, int i4) {
        this.mNestedScrollY = i2;
        ChatAdapter chatAdapter = this.mChatAdapter;
        if ((chatAdapter == null || !chatAdapter.isReceiving()) && i4 - i2 > 12) {
            long currentTimeMillis = System.currentTimeMillis();
            long j = this.mLastHideKeyboardTime;
            if (j == 0 || currentTimeMillis - j >= 500) {
                this.mLastHideKeyboardTime = currentTimeMillis;
                hideKeyboard();
                return;
            }
            this.mLastHideKeyboardTime = currentTimeMillis;
        }
    }

    public void hideKeyboard() {
        if (this.mEjectKeyboard) {
            this.mEjectKeyboard = false;
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

            if (inputMethodManager.isActive() && getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 2);
            }
        }
    }


    public void selectGpt40() {
        this.mGptLevel = 2005;
        this.binding.lGPT35.setBackgroundResource(R.drawable.gpt_unpress);
        this.binding.mTvGpt40.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        this.binding.lGPT40.setBackgroundResource(R.drawable.gpt_pro_press);
        this.binding.mTvGpt35.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
    }


    public void sendQuestion() {
        if (!ClickUtil.isTooFast()) {
            String obj = this.binding.mEditInput.getText().toString();
            this.binding.mEditInput.setText("");
            sendQuestion(obj);
        }
    }

    private void sendQuestion(String str) {
        boolean z = false;
        ChatUtil.saveChatMsg(str, 0);
        hideKeyboard();
        showChats();
        this.mRespondStr = new StringBuilder();
        ChatAdapter chatAdapter = this.mChatAdapter;
        if (chatAdapter != null) {
            chatAdapter.startReceive();
        }
        this.mHandler.postDelayed(new Runnable() {
            public final void run() {
                ChatActivity.this.binding.mNestedScrollView.fullScroll(130);
            }
        }, 200);
        if (this.mGptLevel == 200) {
            z = true;
        }
        HttpUtils.chat(str, z, new chatCall1());
    }

    @Override
    public void onResume() {
        super.onResume();
        int i = this.mGptLevel;
        if (i == 100) {
            selectGpt35();
        } else if (i == 2005) {
            selectGpt40();
        }
    }

    public class chatCall1 implements ChatRequestCallback {
        chatCall1() {
        }

        @Override
        public void onResponse(String str) {
            try {
                if (!ChatActivity.this.isFinishing()) {
                    if (ChatActivity.this.mHandler != null) {
                        ChatActivity.this.mHandler.removeCallbacks(ChatActivity.this.mTimeoutRunnable);
                    }
                    if (((!str.equals("\n") && !str.equals("\n\n")) || !ChatActivity.this.mRespondStr.toString().isEmpty()) && !ChatActivity.this.mRespondStr.toString().endsWith(str)) {
                        ChatActivity.this.mRespondStr.append(str);
                        String sb = ChatActivity.this.mRespondStr.toString();
                        if (StringUtil.isNotEmpty(sb)) {
                            int indexOf = sb.indexOf("「");
                            int indexOf2 = sb.indexOf("」");
                            Log.d("ChatActivity", "onResponse:12 " + indexOf + "::" + indexOf2);
                            if (indexOf > 0 && indexOf2 > 0 && indexOf2 > indexOf) {
                                String substring = sb.substring(0, indexOf);
                                ChatActivity.this.mRespondStr.delete(0, ChatActivity.this.mRespondStr.toString().length());
                                ChatActivity.this.mRespondStr.append(substring);
                                Log.d("ChatActivity", "onResponse:1 " + ChatActivity.this.mRespondStr.toString());
                            }
                        }
                        if (ChatActivity.this.mChatAdapter != null) {
                            ChatActivity.this.mChatAdapter.updateLastItem(ChatActivity.this.mRespondStr.toString());
                        }
                        ChatActivity.this.binding.mNestedScrollView.fullScroll(130);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFinish() {
            try {
                if (!ChatActivity.this.isFinishing()) {
                    if (ChatActivity.this.mRespondStr.toString().contains("You can either wait for tomorrow for 5 more chats or subscribe now to continue our conversation")) {
                        ChatActivity.this.limitDialog = new Dialog(ChatActivity.this, R.style.TransparentBackground);
                        ChatActivity.this.limitDialog.requestWindowFeature(1);
                        ChatActivity chatActivity = ChatActivity.this;
                        chatActivity.dialogLimitPremBinding = DialogLimitPremBinding.inflate(chatActivity.getLayoutInflater());
                        ChatActivity.this.limitDialog.setContentView(ChatActivity.this.dialogLimitPremBinding.getRoot());
                        ChatActivity.this.limitDialog.getWindow().setFlags(1024, 1024);
                        ChatActivity.this.limitDialog.setCancelable(true);
                        HelperResizer.getheightandwidth(ChatActivity.this);
                        HelperResizer.setSize(ChatActivity.this.dialogLimitPremBinding.conSubBg, 850, ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION);
                        HelperResizer.setSize(ChatActivity.this.dialogLimitPremBinding.iconPrem, 62, 62);
                        HelperResizer.setSize(ChatActivity.this.dialogLimitPremBinding.btnSub, 280, 100);
                        ChatActivity.this.limitDialog.show();
                        ChatActivity.this.limitDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                ChatActivity.this.limitDialog.dismiss();
                            }
                        });
                        ChatActivity.this.dialogLimitPremBinding.modelName.setText(ChatActivity.this.binding.mTvRoleName.getText().toString());
                        ChatActivity.this.dialogLimitPremBinding.btnSub.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ChatActivity.this.limitDialog.dismiss();
                            }
                        });
                    }
                    if (ChatActivity.this.mChatAdapter != null) {
                        if (ChatActivity.this.mChatAdapter.isReceiving()) {
                            ChatActivity.this.mChatAdapter.endReceive();
                            ChatActivity.this.mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    ChatActivity.this.binding.mNestedScrollView.fullScroll(130);
                                }
                            }, 300);
                        } else {
                            return;
                        }
                    }
                    String sb = ChatActivity.this.mRespondStr.toString();
                    if (StringUtil.isEmpty(sb)) {
                        ChatActivity.this.resultIsEmpty();
                    } else {
                        ChatUtil.saveChatMsg(sb, 1);
                    }
                    ChatActivity.this.binding.mBtnSend.setEnabled(!StringUtil.isEmpty(ChatActivity.this.binding.mEditInput.getText().toString().trim()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void resultIsEmpty() {
        if (this.mChatAdapter != null) {
            ChatLite chatLite = new ChatLite();
            chatLite.mMsgType = 1;
            chatLite.mMsg = "Connection failed.";
            this.mChatAdapter.addNewMsg(chatLite);
            this.binding.mNestedScrollView.fullScroll(130);
        }
    }

    @Override
    public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        if (i4 == 0 || i8 == 0 || i4 - rect.bottom > 0) {
            if (!this.mEjectKeyboard) {
                this.mEjectKeyboard = true;
            } else {
                return;
            }
        } else if (this.mEjectKeyboard) {
            this.mEjectKeyboard = false;
        } else {
            return;
        }
        handlerKeyboardStatus();
    }

    private void handlerKeyboardStatus() {
        if (this.mEjectKeyboard) {
            this.binding.mNestedScrollView.fullScroll(130);
            this.binding.mEditInput.setFocusable(true);
            this.binding.mEditInput.setFocusableInTouchMode(true);
            this.binding.mEditInput.requestFocus();
            return;
        }
        this.mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int acce1 = ChatActivity.this.mNestedScrollY + ScreenUtil.dip2px(ChatActivity.this, 54.0f);
                int unused = ChatActivity.this.mNestedScrollY = acce1;
                ChatActivity.this.binding.mNestedScrollView.scrollTo(0, acce1);
            }
        }, 20);
    }
}
