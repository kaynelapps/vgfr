package com.aigirlfriend.virtuallove.utils;

import com.aigirlfriend.virtuallove.event.UpdateChatLiteEvent;

public final class ChatUtilRunnable1 implements Runnable {
    public static final ChatUtilRunnable1 INSTANCE = new ChatUtilRunnable1();

    private ChatUtilRunnable1() {
    }

    @Override
    public void run() {
        UpdateChatLiteEvent.post();
    }
}
