package ru.fagci.tuihome;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.*;
import ru.fagci.tuihome.action.ModelAction;

import java.util.ArrayList;
import java.util.List;

public class SuggestRow extends RelativeLayout {
    List<ModelAction> actions = new ArrayList<>();

    private ImageButton mButton;
    private ImageView mIcon;
    private TextView mTitle;
    private ProgressBar mProgress;
    private TextView mSubTitle;
    private TextView mDesc;


    public SuggestRow(Context context, AttributeSet attrs) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.suggest_item, this);
        mProgress = findViewById(R.id.suggest_itemProgressBar);
        mIcon = findViewById(R.id.suggest_itemImageView);
        mTitle = findViewById(R.id.suggest_itemTextView);
        mSubTitle = findViewById(R.id.suggest_itemSubtitle);

        mDesc = findViewById(R.id.suggest_itemDesc);

        mButton = findViewById(R.id.suggest_itemMenuButton);
    }

    public void resetIcon() {
        mIcon.setImageResource(0);
    }

    public void setIcon(Bitmap b) {
        mIcon.setAlpha(0.0f);
        mIcon.setImageBitmap(b);
        mIcon.animate().setDuration(50).alpha(1.0f);
    }

    public void setTitle(String text) {
        mTitle.setText(text);
    }

    public void setSubTitle(String text) {
        mSubTitle.setText(text);
    }

    public void setDesc(String desc) {
        mDesc.setText(desc);
    }

    public void hideProgress() {
        mProgress.animate().alpha(0.0f)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator a) {
                        super.onAnimationEnd(a);
                        mProgress.setVisibility(GONE);
                    }
                });
    }

    public void showProgress() {
        mProgress.setAlpha(0.0f);
        mProgress.setVisibility(VISIBLE);
        mProgress.animate().setStartDelay(250).setDuration(150).alpha(1.0f);
    }
}
 
