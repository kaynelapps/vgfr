package com.aigirlfriend.virtuallove.db;

import org.litepal.crud.LitePalSupport;

public class ChatLite extends LitePalSupport {
    public String mMessageId;
    public String mMsg;
    public int mMsgType;
    public long mTime;
    public int mMessageFlag;

    @Override
    public long getBaseObjId() {
        return super.getBaseObjId();
    }
}
