package com.aigirlfriend.virtuallove.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.aigirlfriend.virtuallove.HelperResizer;
import com.aigirlfriend.virtuallove.MainActivity;
import com.aigirlfriend.virtuallove.databinding.FragmentLovelistBinding;
import com.aigirlfriend.virtuallove.gallery.DepthPageTransformer;
import com.aigirlfriend.virtuallove.gallery.ItemFragment;
import com.aigirlfriend.virtuallove.model.RoleModel;
import com.aigirlfriend.virtuallove.utils.ChatUtil;
import java.util.ArrayList;
import java.util.List;

public class LovelistFragment extends Fragment {
    FragmentLovelistBinding binding;
    
    public GalleryAdapter mGalleryAdapter;
    List<RoleModel> mRoles;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        new ArrayList();
        resize();
        this.mRoles = ChatUtil.obtainRoles();
        GalleryAdapter galleryAdapter = new GalleryAdapter(getChildFragmentManager(), this.mRoles);
        this.mGalleryAdapter = galleryAdapter;
        this.binding.viewPager.setAdapter(galleryAdapter);
        this.binding.viewPager.setPageMargin(20);
        this.binding.viewPager.setOffscreenPageLimit(3);
        if (this.mRoles.size() > 1) {
            this.binding.viewPager.setCurrentItem(this.mRoles.size() * 100);
        }
        this.binding.viewPager.setPageTransformer(true, new DepthPageTransformer());
        this.binding.viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int i) {
                super.onPageSelected(i);
                int size = i % LovelistFragment.this.mRoles.size();
                if (LovelistFragment.this.mGalleryAdapter != null) {
                    Fragment[] fragments = LovelistFragment.this.mGalleryAdapter.getFragments();
                    int i2 = 0;
                    while (i2 < fragments.length) {
                        ItemFragment itemFragment = (ItemFragment) fragments[i2];
                        if (itemFragment != null) {
                            itemFragment.setSelected(i2 == size);
                        }
                        i2++;
                    }
                }
            }
        });
        this.binding.btnStartChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int currentItem = LovelistFragment.this.binding.viewPager.getCurrentItem() % LovelistFragment.this.mRoles.size();
                ChatUtil.setEditHistory(LovelistFragment.this.mRoles.get(currentItem).mPromptId);
                ChatActivity.chat(LovelistFragment.this.requireContext());
                MainActivity.intentClick = 1;
            }
        });
    }

    private void resize() {
        HelperResizer.setSize(this.binding.btnStartChat, 650, 173);
    }

    public static LovelistFragment newInstance() {
        return new LovelistFragment();
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        FragmentLovelistBinding inflate = FragmentLovelistBinding.inflate(layoutInflater, viewGroup, false);
        this.binding = inflate;
        return inflate.getRoot();
    }

    public static class GalleryAdapter extends FragmentPagerAdapter {
        private final Fragment[] mFragments;
        private final List<RoleModel> mRoles1;

        public int getItemPosition(Object obj) {
            return -2;
        }

        public GalleryAdapter(FragmentManager fragmentManager, List<RoleModel> list) {
            super(fragmentManager);
            this.mRoles1 = list;
            this.mFragments = new Fragment[list.size()];
        }

        @Override
        public Fragment getItem(int i) {
            int size = i % this.mRoles1.size();
            ItemFragment itemFragment = (ItemFragment) ItemFragment.create(this.mRoles1.get(size), size);
            this.mFragments[size] = itemFragment;
            return itemFragment;
        }

        @Override
        public Object instantiateItem(ViewGroup viewGroup, int i) {
            return (ItemFragment) super.instantiateItem(viewGroup, i);
        }

        @Override
        public int getCount() {
            if (this.mRoles1.size() <= 1) {
                return this.mRoles1.size();
            }
            return Integer.MAX_VALUE;
        }

        public Fragment[] getFragments() {
            return this.mFragments;
        }
    }
}
