package com.psyberia.cardviewer;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import com.google.android.material.tabs.TabLayout;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.ybq.parallaxviewpager.ParallaxViewPager;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

public class ViewActivity extends AppCompatActivity {

    private static final String IMG_LOC = "pages";
    private static final String TAG = "@@@";

    private ParallaxViewPager mParallaxViewPager;
    private ProgressBar mProgressBar;

    private boolean doubleBackToExitPressedOnce = false;

    public static Intent getIntent(Context mContext) {

        Intent intent = new Intent(mContext, ViewActivity.class);
        return intent;
    }


    //Drawable
//    private void displayWallpaper(){
//        WallpaperManager myWallpaperManager =
//                WallpaperManager.getInstance(getApplicationContext());
//        Drawable drawableWallpaper = myWallpaperManager.getDrawable();
//        imageWallpaper.setImageDrawable(drawableWallpaper);
//
//        Toast.makeText(this,
//                drawableWallpaper.getMinimumWidth() + " x " + drawableWallpaper.getMinimumHeight()
//                        + "\n" +
//                        drawableWallpaper.getIntrinsicWidth() + " x " + drawableWallpaper.getIntrinsicHeight(),
//                Toast.LENGTH_LONG).show();
//    }


    /**
     * Reason: Input dispatching timed
     * out (Waiting to send non-key event because the touched window has not finished processi
     */


    private void reloadWallpaper(Bitmap bm) {
        if (bm != null) {
            WallpaperManager myWallpaperManager =
                    WallpaperManager.getInstance(getApplicationContext());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (myWallpaperManager.isWallpaperSupported()) {
                    try {
                        myWallpaperManager.setBitmap(bm);
                        Toast.makeText(this, getString(R.string.state_installed), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(ViewActivity.this, "isWallpaperSupported() NOT SUPPORTED", Toast.LENGTH_LONG).show();
                }
            } else {
                try {
                    myWallpaperManager.setBitmap(bm);
                    Toast.makeText(this, getString(R.string.state_installed), Toast.LENGTH_SHORT).show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Toast.makeText(ViewActivity.this, "bm == null", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Нажмите ещё раз, чтобы выйти", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        this.mProgressBar = findViewById(R.id.progress);
        this.mParallaxViewPager = findViewById(R.id.viewpager);

        TabLayout tabLayout = findViewById(R.id.tab_dots);
        tabLayout.setupWithViewPager(mParallaxViewPager, true);


        final String[] images = initImages();
        this.mParallaxViewPager.setAdapter(new WallpaperAdapter(this, images));
    }

    private String[] initImages() {

        String[] strings;
        try {
            String[] arr = getAssets().list(IMG_LOC);
            strings = new String[arr.length];

            int index = 0;

            for (String name : arr) {
                strings[index] = //ASSET_SCHEME +
                        IMG_LOC + "/" + name;
                index++;
            }
            return strings;
        } catch (IOException e) {
            e.printStackTrace();
            return new String[]{};
        }

//        Field[] fields = R.assets.class.getFields();//raw
//        for (int count = 0; count < fields.length; count++) {
//
//            String name = fields[count].getName();
//
//            Logger.i("@@" + name);
////            this.strings[0] = R.drawable.img1;
//            try {
//                //asset
//                getAssets().open(fields[count].getName());
//                //getResources().openRawResource(fields[count].getName());
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }


        /**
         * Old payload
         */
//        this.strings[0] = R.drawable.img01;
//        this.strings[1] = R.drawable.img02;
//        this.strings[2] = R.drawable.img03;
//        this.strings[3] = R.drawable.img04;
//        this.strings[4] = R.drawable.img05;
    }


    private static class WallpaperAdapter extends PagerAdapter {

        private final WeakReference<ViewActivity> mWeakReference;
        private final String[] images;

        WallpaperAdapter(ViewActivity viewActivity, String[] images) {
            this.mWeakReference = new WeakReference<>(viewActivity);
            this.images = images;
        }

        public boolean isViewFromObject(@NonNull View arg0, @NonNull Object arg1) {
            return arg0 == arg1;
        }

        public void destroyItem(
                @NonNull ViewGroup container,
                int position,
                @NonNull Object obj) {
            container.removeView((View) obj);
        }

        @NonNull
        public Object instantiateItem(@NonNull ViewGroup container, final int position) {
            View view = View.inflate(container.getContext(), R.layout.item_pager, null);

            final ImageView thumbnail = view.findViewById(R.id.item_img);
            thumbnail.setDrawingCacheEnabled(true);


            ViewActivity activity = mWeakReference.get();

            Glide.with(activity)
                    .asBitmap()
                    .load(Const.ASSET_SCHEME + images[position])
                    //.error(R.drawable.ic_cancel)
                    .into(thumbnail);

            final Button btnSetWallpaper = view.findViewById(R.id.action_set_as_wallpaper);
            final Button btnSaveWallpaper = view.findViewById(R.id.action_save_wallpaper);


            btnSetWallpaper.setOnClickListener(view1 -> {

                Toast.makeText(activity, activity.getString(R.string.state_preparing), Toast.LENGTH_SHORT).show();

//                    String path = mPic[position];

//                    freezy
//                    Drawable drawable = getResources().getDrawable(path);
//                    Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();


                Bitmap bitmap = thumbnail.getDrawingCache();
                //false>>> Bitmap bitmap = BitmapFactory.decodeResource(ViewActivity.this.getResources(), path);
                activity.reloadWallpaper(bitmap);
            });


            btnSaveWallpaper.setOnClickListener(view12 -> {

                ViewActivity viewActivity = mWeakReference.get();
                if (viewActivity != null && !viewActivity.isStoragePermissionGranted()) {
                    return;
                }

                activity.mProgressBar.setVisibility(View.VISIBLE);

                String path = images[position];
                //Drawable drawable = getResources().getDrawable(path);
                // Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                try {
                    InputStream stream = activity.getAssets().open(path);
                    Bitmap bitmap = BitmapFactory.decodeStream(stream);

                    String imagePath = MediaStore.Images.Media.insertImage(
                            activity.getContentResolver(),
                            bitmap,
                            "Wallpaper_" + position,
                            "Wallpaper_" + position
                    );

                    Uri uri = Uri.parse(imagePath);

                    activity.mProgressBar.setVisibility(View.GONE);
                    Toast.makeText(activity,
                            String.format(activity.getString(R.string.state_saved_successfully), uri.getPath()),
                            Toast.LENGTH_LONG).show();


                } catch (IOException e) {
                    e.printStackTrace();
                    activity.mProgressBar.setVisibility(View.GONE);
                    Toast.makeText(activity, "Error: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }


            });


            container.addView(view, -1, -1);
            return view;
        }

        public int getCount() {
            return images.length;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
            //resume tasks needing this permission
        }
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {

                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }
}
