package com.aigirlfriend.virtuallove.gallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.Transformation;
import com.aigirlfriend.virtuallove.HelperResizer;
import com.aigirlfriend.virtuallove.R;
import com.aigirlfriend.virtuallove.constants.IntentKeys;
import com.aigirlfriend.virtuallove.model.RoleModel;
import com.aigirlfriend.virtuallove.transform.CornerTransform;
import com.aigirlfriend.virtuallove.utils.ScreenUtil;

public class ItemFragment extends Fragment {
    ConstraintLayout conImageText;
    private Context mContext;
    private ImageView mImageRole;
    private int mIndex;
    private RoleModel mRole;

    public void setLockGone() {
    }

    public static Fragment create(RoleModel roleModel, int i) {
        ItemFragment itemFragment = new ItemFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(IntentKeys.ROLE_ATTR, roleModel);
        bundle.putInt(IntentKeys.INDEX, i);
        itemFragment.setArguments(bundle);
        return itemFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mRole = (RoleModel) arguments.getSerializable(IntentKeys.ROLE_ATTR);
            this.mIndex = arguments.getInt(IntentKeys.INDEX);
        }
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_item, viewGroup, false);
        this.mImageRole = (ImageView) inflate.findViewById(R.id.mImageRole);
        this.conImageText = (ConstraintLayout) inflate.findViewById(R.id.conImageText);
        HelperResizer.setSize(this.conImageText, 426, 123, true);
        TextView textView = (TextView) inflate.findViewById(R.id.txtImage);
        RoleModel roleModel = this.mRole;
        if (roleModel != null) {
            RequestBuilder<Drawable> load = Glide.with(this.mContext).load(Integer.valueOf(roleModel.mResId));
            Context context = this.mContext;
            ((RequestBuilder) load.transform((Transformation<Bitmap>) new CornerTransform(context, (float) ScreenUtil.dip2px(context, 20.0f)))).into(this.mImageRole);
            textView.setText(this.mRole.mName);
            if (this.mIndex == 0) {
                setSelected(true);
            }
            Log.d("TAG", "onCreateView: " + this.mRole.mIsVip + "::" + this.mRole.mName);

        }
        return inflate;
    }

    public void setSelected(boolean z) {
        ImageView imageView = this.mImageRole;
        if (imageView != null) {
            imageView.setSelected(z);
        }
    }
}
