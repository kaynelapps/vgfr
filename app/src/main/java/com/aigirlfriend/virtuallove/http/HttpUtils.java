package com.aigirlfriend.virtuallove.http;

import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;

import androidx.exifinterface.media.ExifInterface;

import com.aigirlfriend.virtuallove.ads.MyApplication;

import com.aigirlfriend.virtuallove.BuildConfig;
import com.aigirlfriend.virtuallove.callback.ChatRequestCallback;
import com.aigirlfriend.virtuallove.db.ChatLite;
import com.aigirlfriend.virtuallove.db.HistoryLite;
import com.aigirlfriend.virtuallove.utils.ChatUtil;
import com.aigirlfriend.virtuallove.utils.StringUtil;
import com.aigirlfriend.virtuallove.utils.SubsUtil;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

public class HttpUtils {
    public static final Handler mHandler = new Handler(Looper.getMainLooper());

    public static void chat(final String str, final boolean z, final ChatRequestCallback chatRequestCallback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpUtils.AiGirlChat(str, z, chatRequestCallback);
            }
        }).start();
    }

    public static void AiGirlChat(String str, boolean z, ChatRequestCallback chatRequestCallback) {
        HashMap hashMap = new HashMap();
        hashMap.put("p2", str);
        hashMap.put("chatModel", z ? "4" : "3");
        HistoryLite editHistory = ChatUtil.getEditHistory();
        hashMap.put("templateId", editHistory.mPromptId);
        List<ChatLite> list = editHistory.mChats;
        if (list != null && list.size() > 1) {
            if (list.size() > 2) {
                int i = 0;
                String str2 = "";
                for (int size = list.size() - 3; size >= 0; size--) {
                    ChatLite chatLite = list.get(size);
                    if (StringUtil.isEmpty(str2) && chatLite.mMsgType == 0) {
                        str2 = chatLite.mMessageId;
                    }
                    i++;
                    if (i > 2) {
                        break;
                    }
                }
                if (StringUtil.isNotEmpty(str2)) {
                    hashMap.put("preMsgId", str2);
                }
            }
            Log.d("TAG", "" + list.get(list.size() - 2).mMessageId);
            hashMap.put("messageId", list.get(list.size() - 2).mMessageId);
        }

        hashMap.put("phoneId", getRandomNumber());
        //hashMap.put("phoneId", getAndroidId());
        hashMap.put("packageName", BuildConfig.APPLICATION_ID);
        getJsonContent("http://api.chatai.click:80/v1/chat2/chatTemplate", hashMap, new callChat(chatRequestCallback, z));
    }

    private static String getRandomNumber() {

        final long min1 = 100000000;
        final int max1 =  999999999;
        final long random1 = new Random().nextInt((int) ((max1 - min1) + 1)) + min1;

        final long min2 = 10000;
        final int max2 =  99999;
        final long random2 = new Random().nextInt((int) ((max2 - min2) + 1)) + min2;

        String strNum1 = String.valueOf(random1);
        String strNum2 = String.valueOf(random2);

        StringBuilder sb = new StringBuilder();
        sb.append(strNum1);
        sb.append(strNum2);

        String numStr = String.valueOf(sb);

        return numStr;
    }

    public static class callChat implements ChatRequestCallback {
        final ChatRequestCallback valcallback;
        final boolean valGpt4;

        callChat(ChatRequestCallback chatRequestCallback, boolean z) {
            this.valcallback = chatRequestCallback;
            this.valGpt4 = z;
        }

        @Override
        public void onResponse(final String str) {
            Handler handl1 = HttpUtils.mHandler;
            final ChatRequestCallback chatRequestCallback = this.valcallback;
            final boolean z = this.valGpt4;
            handl1.post(new Runnable() {
                @Override
                public void run() {
                    callChat.onResponseCall(chatRequestCallback, str, z);
                }
            });
        }

        public static void onResponseCall(ChatRequestCallback chatRequestCallback, String str, boolean z) {
            boolean z2;
            int i;
            if (chatRequestCallback != null) {
                try {
                    if (str.startsWith("data: [DONE]")) {
                        chatRequestCallback.onFinish();
                        return;
                    }
                    String replace = str.trim().replace("data:", "");
                    if (replace.contains("[DONE]")) {
                        replace = replace.replace("[DONE]", "");
                        z2 = true;
                    } else {
                        z2 = false;
                    }
                    try {
                        JSONObject jSONObject = new JSONObject(replace);
                        try {
                            String string = jSONObject.getString("object");
                            if (string.equals("free")) {
                                SubsUtil.setShowSubs(z);
                                SubsUtil.refreshSubsStatus();
                            } else if (string.equals("often")) {
                                SubsUtil.refreshSubsStatus();
                            } else if (string.equals("sensitivity")) {
                                SubsUtil.refreshSubsStatus();
                            } else if (string.equals("order")) {
                                SubsUtil.refreshSubsStatus();
                            } else if (string.equals("serve_busy")) {
                                SubsUtil.refreshSubsStatus();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        JSONObject jSONObject2 = jSONObject.getJSONArray("choices").getJSONObject(0).getJSONObject("delta");
                        if (jSONObject2.has("content")) {
                            chatRequestCallback.onResponse(jSONObject2.getString("content"));
                        }
                        if (z2) {
                            chatRequestCallback.onFinish();
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        try {
                            i = replace.indexOf("choices");
                        } catch (Exception e3) {
                            e3.printStackTrace();
                            i = 0;
                        }
                        if (i >= 0) {
                            JSONObject jSONObject3 = new JSONArray(replace.substring(i + 9)).getJSONObject(0).getJSONObject("delta");
                            if (jSONObject3.has("content")) {
                                chatRequestCallback.onResponse(jSONObject3.getString("content"));
                            }
                            if (z2) {
                                chatRequestCallback.onFinish();
                            }
                        }
                    }
                } catch (Exception unused) {
                }
            }
        }

        @Override
        public void onFinish() {
            Handler handl1 = HttpUtils.mHandler;
            Objects.requireNonNull(this.valcallback);
            handl1.post(new Runnable() {
                @Override
                public void run() {
                    callChat.this.onFinish();
                }
            });
        }
    }

    private static void getJsonContent(String str, Map<String, String> map, ChatRequestCallback chatRequestCallback) {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            if (map != null && !map.isEmpty()) {
                Uri.Builder builder = new Uri.Builder();
                for (Map.Entry next : map.entrySet()) {
                    builder.appendQueryParameter((String) next.getKey(), (String) next.getValue());
                }
                new DataOutputStream(httpURLConnection.getOutputStream()).write(builder.build().getEncodedQuery().getBytes());
            }
            int responseCode = httpURLConnection.getResponseCode();
            Log.d("TAG", "getJsonContent: " + responseCode);
            if (responseCode == 200) {
                changeInputStream(httpURLConnection.getInputStream(), chatRequestCallback);
            } else if (chatRequestCallback != null) {
                chatRequestCallback.onFinish();
            }
        } catch (Exception unused) {
            if (chatRequestCallback != null) {
                chatRequestCallback.onFinish();
            }
        }
    }

    private static void changeInputStream(InputStream inputStream, ChatRequestCallback chatRequestCallback) throws IOException {
        byte[] bArr = new byte[1024];
        while (inputStream.read(bArr) != -1) {
            try {
                chatRequestCallback.onResponse(new String(bArr));
            } catch (Exception unused) {
                if (chatRequestCallback != null) {
                    chatRequestCallback.onFinish();
                    return;
                }
                return;
            }
        }
    }

    public static String getAndroidId() {
        return Settings.Secure.getString(MyApplication.mContext.getContentResolver(), "android_id");
    }
}
