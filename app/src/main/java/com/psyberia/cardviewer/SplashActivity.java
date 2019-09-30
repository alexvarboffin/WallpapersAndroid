package com.psyberia.cardviewer;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.orhanobut.logger.Logger;
import com.psyberia.cardviewer.banner.AdsUtils;
import com.psyberia.cardviewer.banner.Banner;


public class SplashActivity extends AppCompatActivity
        implements OnClickListener,
        Loading.Callback {

    private Banner mBanner;
    public static int DELAY = 10000;


    private Loading loading;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        supportRequestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final TextView headLine = findViewById(R.id.headline);
        ImageView cancel = findViewById(R.id.button_cancel);

        final RelativeLayout container = findViewById(R.id.banner_container);
        headLine.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Scada.ttf"));
        container.setOnClickListener(this);
        cancel.setOnClickListener(this);

        new AdsUtils(this, new AdsUtils.Callback() {
            @Override
            public void requestError() {
                nextPage();
            }


            @Override
            public void requestSuccess(Banner data) {
                if (Banner.isEmpty(data)) {
                    nextPage();
                    return;
                }
                mBanner = data;
                container.setVisibility(View.VISIBLE);
                headLine.setText(mBanner.getMessage());

                SimpleTarget<Drawable> result = Glide.with(SplashActivity.this)
                        .load(mBanner.getThumbnail())
                        .into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    container.setBackground(resource);
                                }
                            }
                        });
            }
        }).execute();

        loading = new Loading(this);
        loading.execute();
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (Build.VERSION.SDK_INT >= 19 && hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(5126);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_cancel:
                nextPage();
                break;

            case R.id.banner_container:

                Logger.d(mBanner);

                if (Banner.isEmpty(mBanner)) {
                    nextPage();
                    break;
                }

                //==================================================================================
                nextPage();
                Intent i = new Intent("android.intent.action.VIEW");
                i.setData(Uri.parse(mBanner.getUrl()));
                SplashActivity.this.startActivity(i);
                break;
        }
    }

    @Override
    public void nextPage() {

        if (loading != null && !loading.isCancelled()) {
            loading.cancel(true);
        }


        startActivity(ViewActivity.getIntent(this));
        overridePendingTransition(R.anim.open_next, R.anim.close_main);
        SplashActivity.this.finish();
    }
}
