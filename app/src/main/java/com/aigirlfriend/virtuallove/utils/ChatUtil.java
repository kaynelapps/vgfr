package com.aigirlfriend.virtuallove.utils;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import com.aigirlfriend.virtuallove.R;
import com.aigirlfriend.virtuallove.db.ChatLite;
import com.aigirlfriend.virtuallove.db.HistoryLite;
import com.aigirlfriend.virtuallove.event.UpdateChatLiteEvent;
import com.aigirlfriend.virtuallove.model.RoleModel;
import java.util.ArrayList;
import java.util.List;
import org.litepal.LitePal;

public class ChatUtil {
    private static final String TAG = "ChatUtil";
    private static HistoryLite mEditHistory;
    private static List<HistoryLite> mHistories;
    public static final int MESSAGE_FLAG_NORMAL = 0;
    public static final int MESSAGE_FLAG_SENSITIVE = 1;
    public static final int MESSAGE_FLAG_BLOCKED = 2;

    public static void initChats() {
        if (mHistories == null) {
            mHistories = new ArrayList();
        }
        List<HistoryLite> findAll = LitePal.findAll(HistoryLite.class, true, new long[0]);
        if (findAll != null) {
            mHistories.addAll(findAll);
        }
    }

    public static List<HistoryLite> obtainChats() {
        List<HistoryLite> list = mHistories;
        if (list != null) {
            list.sort(ChatUtilComparator1.INSTANCE);
        }
        return mHistories;
    }

    public static int ChatUtilComparator1Call(HistoryLite historyLite, HistoryLite historyLite2) {
        return historyLite2.mTime > historyLite.mTime ? 1 : -1;
    }

    public static void setEditHistory(HistoryLite historyLite) {
        mEditHistory = historyLite;
        if (historyLite.mHasNewMsg) {
            mEditHistory.mHasNewMsg = false;
            mEditHistory.save();
            new Handler().postDelayed(ChatUtilRunnable1.INSTANCE, 500);
        }
    }

    public static void setEditHistory(String str) {
        if (mHistories != null && !TextUtils.isEmpty(str)) {
            for (int i = 0; i < mHistories.size(); i++) {
                if (mHistories.get(i).mPromptId.equals(str)) {
                    HistoryLite historyLite = mHistories.get(i);
                    mEditHistory = historyLite;
                    if (historyLite.mHasNewMsg) {
                        mEditHistory.mHasNewMsg = false;
                        mEditHistory.save();
                        new Handler().postDelayed(ChatUtilRunnable1.INSTANCE, 500);
                        return;
                    }
                    return;
                }
            }
        }
    }

    public static HistoryLite getEditHistory() {
        return mEditHistory;
    }

    public static int getMessageFlag(String message) {
        message = message.toLowerCase();
        if (message.contains("nsfw") || message.contains("explicit") || message.contains("adult")) {
            return MESSAGE_FLAG_BLOCKED;
        }
        if (message.contains("date") || message.contains("meet") || message.contains("personal")) {
            return MESSAGE_FLAG_SENSITIVE;
        }
        return MESSAGE_FLAG_NORMAL;
    }

    public static void saveChatMsg(String str, int i) {
        if (!StringUtil.isEmpty(str)) {
            HistoryLite editHistory = getEditHistory();
            long currentTimeMillis = System.currentTimeMillis();
            editHistory.mMsg = str;
            editHistory.mTime = currentTimeMillis;
            List<ChatLite> list = editHistory.mChats;
            if (list == null) {
                list = new ArrayList<>();
            }
            ChatLite chatLite = new ChatLite();
            chatLite.mMsg = str;
            chatLite.mTime = currentTimeMillis;
            chatLite.mMsgType = i;
            chatLite.mMessageFlag = getMessageFlag(str);
            chatLite.mMessageId = StringUtil.obtainMsgId();
            chatLite.save();
            list.add(chatLite);
            editHistory.mChats = list;
            editHistory.save();
            if (!mHistories.contains(editHistory)) {
                mHistories.add(0, editHistory);
            }
            UpdateChatLiteEvent.post();
        }
    }

    public static void sendDefaultMsg() {
        Log.d(TAG, "sendDefaultMsg: " + mHistories);
        if (mHistories != null) {
            long currentTimeMillis = System.currentTimeMillis();
            HistoryLite historyLite = new HistoryLite();
            historyLite.mIsVip = true;
            historyLite.mName = "Violet";
            historyLite.mHasNewMsg = true;
            historyLite.mPic = "pic_role_violet_20_vip";
            historyLite.mMsg = "Can we have a movie night? I've got the popcorn!";
            historyLite.mTime = currentTimeMillis;
            historyLite.mPromptId = "653b5fd5003600001b00576c";
            ArrayList arrayList = new ArrayList();
            ChatLite chatLite = new ChatLite();
            chatLite.mMsg = "Can we have a movie night? I've got the popcorn!";
            chatLite.mTime = currentTimeMillis;
            chatLite.mMsgType = 1;
            chatLite.save();
            arrayList.add(chatLite);
            historyLite.mChats = arrayList;
            historyLite.save();
            mHistories.add(historyLite);
            long currentTimeMillis2 = System.currentTimeMillis();
            HistoryLite historyLite2 = new HistoryLite();
            historyLite2.mIsVip = true;
            historyLite2.mName = "Scarlett";
            historyLite2.mHasNewMsg = true;
            historyLite2.mPic = "pic_role_scarlett_19";
            historyLite2.mMsg = "Let's go out tonight, I want everyone to see you're mine.";
            historyLite2.mTime = currentTimeMillis2;
            historyLite2.mPromptId = "653b5fd4003600001b00576b";
            ArrayList arrayList2 = new ArrayList();
            ChatLite chatLite2 = new ChatLite();
            chatLite2.mMsg = "Let's go out tonight, I want everyone to see you're mine.";
            chatLite2.mTime = currentTimeMillis2;
            chatLite2.mMsgType = 1;
            chatLite2.save();
            arrayList2.add(chatLite2);
            historyLite2.mChats = arrayList2;
            historyLite2.save();
            mHistories.add(historyLite2);
            long currentTimeMillis3 = System.currentTimeMillis();
            HistoryLite historyLite3 = new HistoryLite();
            historyLite3.mIsVip = true;
            historyLite3.mName = "Grace";
            historyLite3.mHasNewMsg = true;
            historyLite3.mPic = "pic_role_grace_17_vip";
            historyLite3.mMsg = "How do you feel about a night in, just you and me?";
            historyLite3.mTime = currentTimeMillis3;
            historyLite3.mPromptId = "653b5fda003600001b005770";
            ArrayList arrayList3 = new ArrayList();
            ChatLite chatLite3 = new ChatLite();
            chatLite3.mMsg = "How do you feel about a night in, just you and me?";
            chatLite3.mTime = currentTimeMillis3;
            chatLite3.mMsgType = 1;
            chatLite3.save();
            arrayList3.add(chatLite3);
            historyLite3.mChats = arrayList3;
            historyLite3.save();
            mHistories.add(historyLite3);
            long currentTimeMillis4 = System.currentTimeMillis();
            HistoryLite historyLite4 = new HistoryLite();
            historyLite4.mIsVip = true;
            historyLite4.mName = "Lola";
            historyLite4.mHasNewMsg = true;
            historyLite4.mPic = "pic_role_lola_16";
            historyLite4.mMsg = "I’ve got that fluttery feeling again, and it's all because of you. What's new?";
            historyLite4.mTime = currentTimeMillis4;
            historyLite4.mPromptId = "653b5fd3003600001b00576a";
            ArrayList arrayList4 = new ArrayList();
            ChatLite chatLite4 = new ChatLite();
            chatLite4.mMsg = "I’ve got that fluttery feeling again, and it's all because of you. What's new?";
            chatLite4.mTime = currentTimeMillis4;
            chatLite4.mMsgType = 1;
            chatLite4.save();
            arrayList4.add(chatLite4);
            historyLite4.mChats = arrayList4;
            historyLite4.save();
            mHistories.add(historyLite4);
            long currentTimeMillis5 = System.currentTimeMillis();
            HistoryLite historyLite5 = new HistoryLite();
            historyLite5.mIsVip = true;
            historyLite5.mName = "Audrey";
            historyLite5.mHasNewMsg = true;
            historyLite5.mPic = "pic_role_audrey_15_vip";
            historyLite5.mMsg = "Hello! Care for some company tonight?";
            historyLite5.mTime = currentTimeMillis5;
            historyLite5.mPromptId = "653b5fd1003600001b005769";
            ArrayList arrayList5 = new ArrayList();
            ChatLite chatLite5 = new ChatLite();
            chatLite5.mMsg = "Hello! Care for some company tonight?";
            chatLite5.mTime = currentTimeMillis5;
            chatLite5.mMsgType = 1;
            chatLite5.save();
            arrayList5.add(chatLite5);
            historyLite5.mChats = arrayList5;
            historyLite5.save();
            mHistories.add(historyLite5);
            long currentTimeMillis6 = System.currentTimeMillis();
            HistoryLite historyLite6 = new HistoryLite();
            historyLite6.mIsVip = true;
            historyLite6.mName = "Amelia";
            historyLite6.mHasNewMsg = true;
            historyLite6.mPic = "pic_role_amelia_14";
            historyLite6.mMsg = "Thinking about chatting with you, it still brings a blush to my cheeks. Care to do it again?";
            historyLite6.mTime = currentTimeMillis6;
            historyLite6.mPromptId = "653b5fcf003600001b005768";
            ArrayList arrayList6 = new ArrayList();
            ChatLite chatLite6 = new ChatLite();
            chatLite6.mMsg = "Thinking about chatting with you, it still brings a blush to my cheeks. Care to do it again?";
            chatLite6.mTime = currentTimeMillis6;
            chatLite6.mMsgType = 1;
            chatLite6.save();
            arrayList6.add(chatLite6);
            historyLite6.mChats = arrayList6;
            historyLite6.save();
            mHistories.add(historyLite6);
            long currentTimeMillis7 = System.currentTimeMillis();
            HistoryLite historyLite7 = new HistoryLite();
            historyLite7.mIsVip = true;
            historyLite7.mName = "Lyric";
            historyLite7.mHasNewMsg = true;
            historyLite7.mPic = "pic_role_lyric_12";
            historyLite7.mMsg = "I’ve been waiting for your message. Make my day and say hi?";
            historyLite7.mTime = currentTimeMillis7;
            historyLite7.mPromptId = "653b5fce003600001b005767";
            ArrayList arrayList7 = new ArrayList();
            ChatLite chatLite7 = new ChatLite();
            chatLite7.mMsg = "I’ve been waiting for your message. Make my day and say hi?";
            chatLite7.mTime = currentTimeMillis7;
            chatLite7.mMsgType = 1;
            chatLite7.save();
            arrayList7.add(chatLite7);
            historyLite7.mChats = arrayList7;
            historyLite7.save();
            mHistories.add(historyLite7);
            long currentTimeMillis8 = System.currentTimeMillis();
            HistoryLite historyLite8 = new HistoryLite();
            historyLite8.mIsVip = true;
            historyLite8.mName = "Daisy";
            historyLite8.mHasNewMsg = true;
            historyLite8.mPic = "pic_role_daisy_11_vip";
            historyLite8.mMsg = "I've freshly made your bed. Anything else I can assist with?";
            historyLite8.mTime = currentTimeMillis8;
            historyLite8.mPromptId = "653b5fd6003600001b00576d";
            ArrayList arrayList8 = new ArrayList();
            ChatLite chatLite8 = new ChatLite();
            chatLite8.mMsg = "I've freshly made your bed. Anything else I can assist with?";
            chatLite8.mTime = currentTimeMillis8;
            chatLite8.mMsgType = 1;
            chatLite8.save();
            arrayList8.add(chatLite8);
            historyLite8.mChats = arrayList8;
            historyLite8.save();
            mHistories.add(historyLite8);
            long currentTimeMillis9 = System.currentTimeMillis();
            HistoryLite historyLite9 = new HistoryLite();
            historyLite9.mIsVip = true;
            historyLite9.mName = "Lila";
            historyLite9.mHasNewMsg = true;
            historyLite9.mPic = "pic_role_lila_10_vip";
            historyLite9.mMsg = "Missed you more than I thought I would. How have you been?";
            historyLite9.mTime = currentTimeMillis9;
            historyLite9.mPromptId = "653b5fe4003600001b005773";
            ArrayList arrayList9 = new ArrayList();
            ChatLite chatLite9 = new ChatLite();
            chatLite9.mMsg = "Missed you more than I thought I would. How have you been?";
            chatLite9.mTime = currentTimeMillis9;
            chatLite9.mMsgType = 1;
            chatLite9.save();
            arrayList9.add(chatLite9);
            historyLite9.mChats = arrayList9;
            historyLite9.save();
            mHistories.add(historyLite9);
            long currentTimeMillis10 = System.currentTimeMillis();
            HistoryLite historyLite10 = new HistoryLite();
            historyLite10.mIsVip = true;
            historyLite10.mName = "Hannah";
            historyLite10.mHasNewMsg = true;
            historyLite10.mPic = "pic_role_hannah_8_vip";
            historyLite10.mMsg = "Imagine us, cuddled up with a warm drink, chatting away. Sound good?";
            historyLite10.mTime = currentTimeMillis10;
            historyLite10.mPromptId = "653b5fd9003600001b00576f";
            ArrayList arrayList10 = new ArrayList();
            ChatLite chatLite10 = new ChatLite();
            chatLite10.mMsg = "Imagine us, cuddled up with a warm drink, chatting away. Sound good?";
            chatLite10.mTime = currentTimeMillis10;
            chatLite10.mMsgType = 1;
            chatLite10.save();
            arrayList10.add(chatLite10);
            historyLite10.mChats = arrayList10;
            historyLite10.save();
            mHistories.add(historyLite10);
            long currentTimeMillis11 = System.currentTimeMillis();
            HistoryLite historyLite11 = new HistoryLite();
            historyLite11.mIsVip = true;
            historyLite11.mName = "Ellie";
            historyLite11.mHasNewMsg = true;
            historyLite11.mPic = "pic_role_ellie_7";
            historyLite11.mMsg = "I had an amazing dream last night, and you were in it. Want to hear more?";
            historyLite11.mTime = currentTimeMillis11;
            historyLite11.mPromptId = "653b5fcc003600001b005766";
            ArrayList arrayList11 = new ArrayList();
            ChatLite chatLite11 = new ChatLite();
            chatLite11.mMsg = "I had an amazing dream last night, and you were in it. Want to hear more?";
            chatLite11.mTime = currentTimeMillis11;
            chatLite11.mMsgType = 1;
            chatLite11.save();
            arrayList11.add(chatLite11);
            historyLite11.mChats = arrayList11;
            historyLite11.save();
            mHistories.add(historyLite11);
            long currentTimeMillis12 = System.currentTimeMillis();
            HistoryLite historyLite12 = new HistoryLite();
            historyLite12.mIsVip = true;
            historyLite12.mName = "Emily";
            historyLite12.mHasNewMsg = true;
            historyLite12.mPic = "pic_role_emily_6";
            historyLite12.mMsg = "Planning to stay in tonight or heading out?";
            historyLite12.mTime = currentTimeMillis12;
            historyLite12.mPromptId = "653b5fe2003600001b005772";
            ArrayList arrayList12 = new ArrayList();
            ChatLite chatLite12 = new ChatLite();
            chatLite12.mMsg = "Planning to stay in tonight or heading out?";
            chatLite12.mTime = currentTimeMillis12;
            chatLite12.mMsgType = 1;
            chatLite12.save();
            arrayList12.add(chatLite12);
            historyLite12.mChats = arrayList12;
            historyLite12.save();
            mHistories.add(historyLite12);
            long currentTimeMillis13 = System.currentTimeMillis();
            HistoryLite historyLite13 = new HistoryLite();
            historyLite13.mIsVip = true;
            historyLite13.mName = "Clara";
            historyLite13.mHasNewMsg = true;
            historyLite13.mPic = "pic_role_clara_5";
            historyLite13.mMsg = "Every time my notification chimes, I hope it's you. Ready to dive into another conversation?";
            historyLite13.mTime = currentTimeMillis13;
            historyLite13.mPromptId = "653b5fca003600001b005765";
            ArrayList arrayList13 = new ArrayList();
            ChatLite chatLite13 = new ChatLite();
            chatLite13.mMsg = "Every time my notification chimes, I hope it's you. Ready to dive into another conversation?";
            chatLite13.mTime = currentTimeMillis13;
            chatLite13.mMsgType = 1;
            chatLite13.save();
            arrayList13.add(chatLite13);
            historyLite13.mChats = arrayList13;
            historyLite13.save();
            mHistories.add(historyLite13);
            long currentTimeMillis14 = System.currentTimeMillis();
            HistoryLite historyLite14 = new HistoryLite();
            historyLite14.mIsVip = true;
            historyLite14.mName = "Ava";
            historyLite14.mHasNewMsg = true;
            historyLite14.mPic = "pic_role_ava_3";
            historyLite14.mMsg = "Missed me already, darling?";
            historyLite14.mTime = currentTimeMillis14;
            historyLite14.mPromptId = "653b5fd7003600001b00576e";
            ArrayList arrayList14 = new ArrayList();
            ChatLite chatLite14 = new ChatLite();
            chatLite14.mMsg = "Missed me already, darling?";
            chatLite14.mTime = currentTimeMillis14;
            chatLite14.mMsgType = 1;
            chatLite14.save();
            arrayList14.add(chatLite14);
            historyLite14.mChats = arrayList14;
            historyLite14.save();
            mHistories.add(historyLite14);
            long currentTimeMillis15 = System.currentTimeMillis();
            HistoryLite historyLite15 = new HistoryLite();
            historyLite15.mIsVip = true;
            historyLite15.mName = "Faye";
            historyLite15.mHasNewMsg = true;
            historyLite15.mPic = "pic_role_faye_2";
            historyLite15.mMsg = "The world's filled with wonders. Want to explore it together?";
            historyLite15.mTime = currentTimeMillis15;
            historyLite15.mPromptId = "653b5fdd003600001b005771";
            ArrayList arrayList15 = new ArrayList();
            ChatLite chatLite15 = new ChatLite();
            chatLite15.mMsg = "The world's filled with wonders. Want to explore it together?";
            chatLite15.mTime = currentTimeMillis15;
            chatLite15.mMsgType = 1;
            chatLite15.save();
            arrayList15.add(chatLite15);
            historyLite15.mChats = arrayList15;
            historyLite15.save();
            mHistories.add(historyLite15);
            long currentTimeMillis16 = System.currentTimeMillis();
            HistoryLite historyLite16 = new HistoryLite();
            historyLite16.mIsVip = false;
            historyLite16.mName = "Ruby";
            historyLite16.mHasNewMsg = true;
            historyLite16.mPic = "pic_role_ruby_1";
            historyLite16.mMsg = "Feeling a little lonely without you. Care to keep me company?";
            historyLite16.mTime = currentTimeMillis16;
            historyLite16.mPromptId = "64f05911e052fa70b7843b9d";
            ArrayList arrayList16 = new ArrayList();
            ChatLite chatLite16 = new ChatLite();
            chatLite16.mMsg = "Feeling a little lonely without you. Care to keep me company?";
            chatLite16.mTime = currentTimeMillis16;
            chatLite16.mMsgType = 1;
            chatLite16.save();
            arrayList16.add(chatLite16);
            historyLite16.mChats = arrayList16;
            historyLite16.save();
            mHistories.add(historyLite16);
        }
    }

    public static List<RoleModel> obtainRoles() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(createRole("Ruby", R.drawable.pic_role_ruby_1, false, "64f05911e052fa70b7843b9d"));
        arrayList.add(createRole("Faye", R.drawable.pic_role_faye_2, true, "653b5fdd003600001b005771"));
        arrayList.add(createRole("Ava", R.drawable.pic_role_ava_3, true, "653b5fd7003600001b00576e"));
        arrayList.add(createRole("Clara", R.drawable.pic_role_clara_5, true, "653b5fca003600001b005765"));
        arrayList.add(createRole("Emily", R.drawable.pic_role_emily_6, true, "653b5fe2003600001b005772"));
        arrayList.add(createRole("Ellie", R.drawable.pic_role_ellie_7, true, "653b5fcc003600001b005766"));
        arrayList.add(createRole("Hannah", R.drawable.pic_role_hannah_8_vip, true, "653b5fd9003600001b00576f"));
        arrayList.add(createRole("Lila", R.drawable.pic_role_lila_10_vip, true, "653b5fe4003600001b005773"));
        arrayList.add(createRole("Daisy", R.drawable.pic_role_daisy_11_vip, true, "653b5fd6003600001b00576d"));
        arrayList.add(createRole("Lyric", R.drawable.pic_role_lyric_12, true, "653b5fce003600001b005767"));
        arrayList.add(createRole("Amelia", R.drawable.pic_role_amelia_14, true, "653b5fcf003600001b005768"));
        arrayList.add(createRole("Audrey", R.drawable.pic_role_audrey_15_vip, true, "653b5fd1003600001b005769"));
        arrayList.add(createRole("Lola", R.drawable.pic_role_lola_16, true, "653b5fd3003600001b00576a"));
        arrayList.add(createRole("Grace", R.drawable.pic_role_grace_17_vip, true, "653b5fda003600001b005770"));
        arrayList.add(createRole("Scarlett", R.drawable.pic_role_scarlett_19, true, "653b5fd4003600001b00576b"));
        arrayList.add(createRole("Violet", R.drawable.pic_role_violet_20_vip, true, "653b5fd5003600001b00576c"));
        return arrayList;
    }

    public static List<RoleModel> obtainRoles1() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(createRole("Ruby", R.drawable.icon_role_head_1, false, "64f05911e052fa70b7843b9d"));
        arrayList.add(createRole("Faye", R.drawable.icon_role_head_2, false, "653b5fdd003600001b005771"));
        arrayList.add(createRole("Ava", R.drawable.icon_role_head_3, false, "653b5fd7003600001b00576e"));
        arrayList.add(createRole("Clara", R.drawable.icon_role_head_5, false, "653b5fca003600001b005765"));
        arrayList.add(createRole("Emily", R.drawable.icon_role_head_6, false, "653b5fe2003600001b005772"));
        arrayList.add(createRole("Ellie", R.drawable.icon_role_head_7, false, "653b5fcc003600001b005766"));
        arrayList.add(createRole("Hannah", R.drawable.icon_role_head_8, true, "653b5fd9003600001b00576f"));
        arrayList.add(createRole("Lila", R.drawable.icon_role_head_10, true, "653b5fe4003600001b005773"));
        arrayList.add(createRole("Daisy", R.drawable.icon_role_head_11, true, "653b5fd6003600001b00576d"));
        arrayList.add(createRole("Lyric", R.drawable.icon_role_head_12, false, "653b5fce003600001b005767"));
        arrayList.add(createRole("Amelia", R.drawable.icon_role_head_14, false, "653b5fcf003600001b005768"));
        arrayList.add(createRole("Audrey", R.drawable.icon_role_head_15, true, "653b5fd1003600001b005769"));
        arrayList.add(createRole("Lola", R.drawable.icon_role_head_16, false, "653b5fd3003600001b00576a"));
        arrayList.add(createRole("Grace", R.drawable.icon_role_head_17, true, "653b5fda003600001b005770"));
        arrayList.add(createRole("Scarlett", R.drawable.icon_role_head_19, false, "653b5fd4003600001b00576b"));
        arrayList.add(createRole("Violet", R.drawable.icon_role_head_20, true, "653b5fd5003600001b00576c"));
        return arrayList;
    }

    private static RoleModel createRole(String str, int i, boolean z, String str2) {
        RoleModel roleModel = new RoleModel();
        roleModel.mName = str;
        roleModel.mResId = i;
        roleModel.mIsVip = z;
        roleModel.mPromptId = str2;
        return roleModel;
    }

    public static HistoryLite getHistoryById(String str) {
        List<HistoryLite> list = mHistories;
        if (list == null) {
            return null;
        }
        for (HistoryLite next : list) {
            if (next.mPromptId.equals(str)) {
                return next;
            }
        }
        return null;
    }

    public static void recycle() {
        List<HistoryLite> list = mHistories;
        if (list != null) {
            list.clear();
            mHistories = null;
        }
    }
}