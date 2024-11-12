package com.aigirlfriend.virtuallove;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.aigirlfriend.virtuallove.constants.SPKeys;
import com.aigirlfriend.virtuallove.databinding.ActivityMainBinding;
import com.aigirlfriend.virtuallove.ui.BaseActivity;
import com.aigirlfriend.virtuallove.ui.ChatFragment;
import com.aigirlfriend.virtuallove.ui.LovelistFragment;
import com.aigirlfriend.virtuallove.ui.SettingActivity;
import com.aigirlfriend.virtuallove.utils.ChatUtil;
import com.aigirlfriend.virtuallove.utils.SPUtils;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.appupdate.AppUpdateOptions;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.aigirlfriend.virtuallove.ads.MyApplication;
import android.util.Log;
import com.google.android.gms.ads.AdRequest;




public class MainActivity extends BaseActivity {
    private AppUpdateManager appUpdateManager;
    private ActivityResultLauncher<IntentSenderRequest> updateResultLauncher;
    private ReviewManager reviewManager;
    public static int intentClick = 2;
    ActivityMainBinding binding;
    private int mCurrentFragmentIndex = -1;
    private final Fragment[] mFragments = new Fragment[2];

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        String testDeviceId = AdRequest.DEVICE_ID_EMULATOR;
        Log.d("AdMob", "Test Device ID: " + testDeviceId);

        getWindow().setFlags(1024, 1024);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupInAppUpdate();
        showInAppReview();

        binding.mRbTabChat.setOnClickListener(view -> clickTabChat());
        binding.mRbTabList.setOnClickListener(view -> clickTabList());

        ChatUtil.initChats();
        if (((Boolean) SPUtils.get(MyApplication.mContext, SPKeys.FIRST_OPEN, true))) {
            SPUtils.put(MyApplication.mContext, SPKeys.FIRST_OPEN, false);
            ChatUtil.sendDefaultMsg();
            clickTabList();
        } else {
            clickTabList();
        }
        resize();
        binding.btnSetting.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(intent);
        });
    }
    private void setupInAppUpdate() {
        appUpdateManager = AppUpdateManagerFactory.create(this);

        updateResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartIntentSenderForResult(),
                result -> {
                    if (result.getResultCode() != RESULT_OK) {
                        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
                        appUpdateInfoTask.addOnSuccessListener(this::startUpdate);
                    }
                }
        );

        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(this::startUpdate);
    }

    private void startUpdate(AppUpdateInfo appUpdateInfo) {
        if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
            AppUpdateOptions updateOptions = AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE)
                    .setAllowAssetPackDeletion(true)
                    .build();
            try {
                appUpdateManager.startUpdateFlowForResult(appUpdateInfo, updateResultLauncher, updateOptions);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showInAppReview() {
        reviewManager = ReviewManagerFactory.create(this);
        reviewManager.requestReviewFlow().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                reviewManager.launchReviewFlow(this, task.getResult())
                        .addOnCompleteListener(flowTask -> {});
            }
        });
    }

    private void resize() {
        HelperResizer.getheightandwidth(this);
        HelperResizer.setSize(binding.mImageTabChat, 120, 120);
        HelperResizer.setSize(binding.mImageTabList, 120, 120);
        HelperResizer.setSize(binding.btnSetting, 120, 120);
        HelperResizer.setSize(binding.conTopBar, 1080, 150);
        HelperResizer.setSize(binding.conBottom, 1080, ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION);
    }

    public void clickTabChat() {
        binding.titleTopBar.setText(getResources().getString(R.string.Chat));
        binding.titleTopBar.setGravity(17);
        mCurrentFragmentIndex = 0;
        binding.mImageTabChat.setImageResource(R.drawable.chat_press);
        binding.mTvTabChat.setTextColor(getResources().getColor(R.color.color_FF3E71));
        binding.mImageTabList.setImageResource(R.drawable.love_unpress);
        binding.mTvTabList.setTextColor(getResources().getColor(R.color.white));
        loadFragment(new ChatFragment());
    }

    public void clickTabList() {
        binding.titleTopBar.setText(getResources().getString(R.string.ai_gf_anime_girl));
        binding.titleTopBar.setGravity(16);
        mCurrentFragmentIndex = 1;
        binding.mImageTabChat.setImageResource(R.drawable.chat_unpress);
        binding.mTvTabChat.setTextColor(getResources().getColor(R.color.white));
        binding.mImageTabList.setImageResource(R.drawable.love_press);
        binding.mTvTabList.setTextColor(getResources().getColor(R.color.color_FF3E71));
        loadFragment(new LovelistFragment());
    }

    private void replaceFragment(int i) {
        newCurrentFragment(i);
    }

    private void newCurrentFragment(int i) {
        Fragment fragment = i == 1 ? LovelistFragment.newInstance() : ChatFragment.newInstance();
        mFragments[i] = fragment;
        addAndShowFragment(fragment);
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        beginTransaction.replace(R.id.mFlContent, fragment);
        beginTransaction.commit();
    }

    private void addAndShowFragment(Fragment fragment) {
        if (fragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().show(fragment).commitAllowingStateLoss();
        } else {
            getSupportFragmentManager().beginTransaction().add(R.id.mFlContent, fragment).commitAllowingStateLoss();
        }
    }

    private void hideFragment(Fragment fragment) {
        if (fragment.isVisible()) {
            getSupportFragmentManager().beginTransaction().hide(fragment).commitAllowingStateLoss();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (intentClick == 0) {
            clickTabChat();
        } else if (intentClick == 1) {
            clickTabList();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ChatUtil.recycle();
    }

    @Override
    public void onBackPressed() {
        ExitDialog();
    }

    private void ExitDialog() {
        final Dialog dialog = new Dialog(MainActivity.this, R.style.DialogTheme);
        dialog.setContentView(R.layout.popup_exit_dialog);
        dialog.setCancelable(false);

        RelativeLayout no = dialog.findViewById(R.id.no);
        RelativeLayout rate = dialog.findViewById(R.id.rate);
        RelativeLayout yes = dialog.findViewById(R.id.yes);

        no.setOnClickListener(v -> dialog.dismiss());

        rate.setOnClickListener(v -> {
            String rateapp = getPackageName();
            Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + rateapp));
            startActivity(intent1);
        });

        yes.setOnClickListener(v -> {
            dialog.dismiss();
            finish();
            System.exit(0);
        });

        dialog.show();
    }
}
