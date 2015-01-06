package com.konifar.activitytransitionsample;

import android.animation.TimeInterpolator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class DetailActivity extends Activity {

    private static final String EXTRA_ORIENTATION = "extra_orientation";
    private static final String EXTRA_RESOURCE_ID = "extra_resource_id";
    private static final String EXTRA_LEFT = "extra_left";
    private static final String EXTRA_TOP = "extra_top";
    private static final String EXTRA_WIDTH = "extra_width";
    private static final String EXTRA_HEIGHT = "extra_height";

    private static final TimeInterpolator sDecelerator = new DecelerateInterpolator();

    private static final int ANIM_DURATION = 800;

    @InjectView(R.id.img_preview)
    ImageView mImgPreview;

    private int mLeftDelta;
    private int mTopDelta;
    private float mWidthScale;
    private float mHeightScale;
    private int mOriginalOrientation;

    public static void start(Activity activity, View transitionView, PhotoModel info) {
        int[] screenLocation = new int[2];
        transitionView.getLocationOnScreen(screenLocation);
        Intent intent = new Intent(activity, DetailActivity.class);
        int orientation = activity.getResources().getConfiguration().orientation;

        intent.putExtra(EXTRA_ORIENTATION, orientation);
        intent.putExtra(EXTRA_RESOURCE_ID, info.resId);
        intent.putExtra(EXTRA_LEFT, screenLocation[0]);
        intent.putExtra(EXTRA_TOP, screenLocation[1]);
        intent.putExtra(EXTRA_WIDTH, transitionView.getWidth());
        intent.putExtra(EXTRA_HEIGHT, transitionView.getHeight());
        activity.startActivity(intent);
        activity.overridePendingTransition(0, 0);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.inject(this);

        Bundle bundle = getIntent().getExtras();
        final int resId = bundle.getInt(EXTRA_RESOURCE_ID);
        final int thumbnailTop = bundle.getInt(EXTRA_TOP);
        final int thumbnailLeft = bundle.getInt(EXTRA_LEFT);
        final int thumbnailWidth = bundle.getInt(EXTRA_WIDTH);
        final int thumbnailHeight = bundle.getInt(EXTRA_HEIGHT);
        mOriginalOrientation = bundle.getInt(EXTRA_ORIENTATION);

        mImgPreview.setImageResource(resId);

        if (savedInstanceState == null) {
            ViewTreeObserver observer = mImgPreview.getViewTreeObserver();
            observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    mImgPreview.getViewTreeObserver().removeOnPreDrawListener(this);

                    int[] screenLocation = new int[2];
                    mImgPreview.getLocationOnScreen(screenLocation);
                    mLeftDelta = thumbnailLeft - screenLocation[0];
                    mTopDelta = thumbnailTop - screenLocation[1];
                    mWidthScale = (float) thumbnailWidth / (mImgPreview.getWidth());
                    mHeightScale = (float) thumbnailHeight / mImgPreview.getHeight();

                    runEnterAnimation();

                    return true;
                }
            });
        }
    }

    public void runEnterAnimation() {
        final long duration = (long) (ANIM_DURATION);

        mImgPreview.setPivotX(0);
        mImgPreview.setPivotY(0);
        mImgPreview.setScaleX(mWidthScale);
        mImgPreview.setScaleY(mHeightScale);
        mImgPreview.setTranslationX(mLeftDelta);
        mImgPreview.setTranslationY(mTopDelta);

        mImgPreview.animate().setDuration(duration).
                scaleX(1).scaleY(1).
                translationX(0).translationY(0).
                setInterpolator(sDecelerator);
    }

    public void runExitAnimation(final Runnable endAction) {
        final long duration = (long) (ANIM_DURATION);

        final boolean fadeOut;
        if (getResources().getConfiguration().orientation != mOriginalOrientation) {
            mImgPreview.setPivotX(mImgPreview.getWidth() / 2);
            mImgPreview.setPivotY(mImgPreview.getHeight() / 2);
            mLeftDelta = 0;
            mTopDelta = 0;
            fadeOut = true;
        } else {
            fadeOut = false;
        }

        mImgPreview.animate().setDuration(duration).
                scaleX(mWidthScale).scaleY(mHeightScale).
                translationX(mLeftDelta).translationY(mTopDelta).
                withEndAction(endAction);
        if (fadeOut) {
            mImgPreview.animate().alpha(0);
        }
    }

    @Override
    public void onBackPressed() {
        runExitAnimation(new Runnable() {
            public void run() {
                finish();
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

}
