package com.aigirlfriend.virtuallove.utils;

import com.aigirlfriend.virtuallove.ads.MyApplication;
import com.aigirlfriend.virtuallove.constants.SPKeys;
import java.util.ArrayList;
import java.util.List;

public class SubsUtil {
    private static boolean mAddCountNoAd = false;
    private static final List<String> mCommodityIds;
    private static volatile boolean mLifetime = false;
    private static final List<String> mProductIds;
    private static boolean mShowSubsGpt35 = false;
    private static boolean mShowSubsGpt40 = false;
    private static String mSubsToken = "";
    private static volatile boolean mSubscribe;

    public static void refreshSubsStatus() {
    }

    static {
        ArrayList arrayList = new ArrayList();
        mProductIds = arrayList;
        arrayList.add("aigirlfriend_subs_monthly");
        arrayList.add("aigirlfriend_subs_annual");
        ArrayList arrayList2 = new ArrayList();
        mCommodityIds = arrayList2;
        arrayList2.add("aigirlfriend_goods_lifetime");
    }

    public static void setSubsStatus(boolean z) {
        mSubscribe = z;
    }

    public static boolean isSubscribe() {
        return mSubscribe || mLifetime;
    }

    public static void setSubsLifetime() {
        mSubscribe = true;
        mLifetime = true;
    }

    public static void setShowSubs(boolean z) {
        if (z) {
            mShowSubsGpt40 = true;
        } else {
            mShowSubsGpt35 = true;
        }
    }

    public static void addChatCount() {
        mShowSubsGpt35 = false;
        mAddCountNoAd = true;
    }

    public static boolean showAdByAddCount() {
        if (!mAddCountNoAd) {
            return false;
        }
        mAddCountNoAd = false;
        return true;
    }

    public static boolean showSubs(boolean z) {
        if (isSubscribe()) {
            return false;
        }
        return z ? mShowSubsGpt40 : mShowSubsGpt35;
    }

    public static List<String> obtainProductIds() {
        return mProductIds;
    }

    public static List<String> obtainCommodityIds() {
        return mCommodityIds;
    }

    public static String getMonthlyId() {
        try {
            return mProductIds.get(0);
        } catch (Exception unused) {
            return "0";
        }
    }

    public static String getAnnualId() {
        try {
            return mProductIds.get(1);
        } catch (Exception unused) {
            return "0";
        }
    }

    public static String getLifetimeGoodsId() {
        try {
            return mCommodityIds.get(0);
        } catch (Exception unused) {
            return "0";
        }
    }

    public static void saveSubsToken(String str) {
        mSubsToken = str;
        SPUtils.put(MyApplication.mContext, SPKeys.SUBS_TOKEN, str);
    }

    public static String getSubsToken() {
        if (StringUtil.isEmpty(mSubsToken)) {
            mSubsToken = (String) SPUtils.get(MyApplication.mContext, SPKeys.SUBS_TOKEN, "");
        }
        return mSubsToken;
    }
}
