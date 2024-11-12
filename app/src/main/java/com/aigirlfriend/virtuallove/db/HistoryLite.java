package com.aigirlfriend.virtuallove.db;

import java.util.List;
import org.litepal.crud.LitePalSupport;

public class HistoryLite extends LitePalSupport {
    public List<ChatLite> mChats;
    public boolean mHasNewMsg;
    public boolean mIsVip;
    public String mMsg;
    public String mName;
    public String mPic;
    public String mPromptId;
    public long mTime;

    public HistoryLite() {
    }

    public HistoryLite(List<ChatLite> list, boolean z, boolean z2, String str, String str2, String str3, String str4, long j) {
        this.mChats = list;
        this.mHasNewMsg = z;
        this.mIsVip = z2;
        this.mMsg = str;
        this.mName = str2;
        this.mPic = str3;
        this.mPromptId = str4;
        this.mTime = j;
    }

    @Override
    public long getBaseObjId() {
        return super.getBaseObjId();
    }
}
