package com.psyberia.cardviewer.banner;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import com.orhanobut.logger.Logger;
import com.psyberia.cardviewer.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by combo on 11/13/2017.
 */

public class AdsUtils {

    private final Callback mCallback;


    public interface Callback {
        void requestError();

        void requestSuccess(Banner banner);
    }

    private final Context mContext;

    public AdsUtils(Context context, Callback callback) {
        this.mCallback = callback;
        this.mContext = context;
    }

    public boolean isConnected() {

        ConnectivityManager manager = ((ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE));
        NetworkInfo activeNetwork = null;

        if (manager != null) {
            activeNetwork = manager.getActiveNetworkInfo();
        }
        return activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
    }

    public void execute() {
        new ParseTask(mContext.getString(R.string.banner_url), mCallback).execute();
    }

    private static class ParseTask extends AsyncTask<Void, Void, String> {

        private final String bannerRotator;
        private final Callback mCallback;

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";

        ParseTask(String b, Callback c) {
            mCallback = c;
            bannerRotator = b;
        }


        @Override
        protected String doInBackground(Void... params) {

            try {
                URL url = new URL(bannerRotator);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder buffer = new StringBuilder();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                resultJson = buffer.toString();

            } catch (Exception e) {
                Logger.e(e.getMessage());
                if (mCallback != null) {
                    mCallback.requestError();
                }
            }
            return resultJson;
        }

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);

            Logger.d(strJson);


            strJson = "{\"url\":\"http:\\/\\/yandex.ru\",\"message\":\"\\u042d\\u0442\\u043e \\u043f\\u0440\\u0438\\u043c\\u0435\\u0440 \\u0431\\u0430\\u043d\\u043d\\u0435\\u0440\\u0430. \\u0415\\u0433\\u043e \\u043c\\u043e\\u0436\\u043d\\u043e \\u0437\\u0430\\u043c\\u0435\\u043d\\u0438\\u0442\\u044c, \\u043a\\u0430\\u043a \\u0438 \\u044d\\u0442\\u043e\\u0442 \\u0442\\u0435\\u043a\\u0441\\u0442\"," +
                    "\"thumbnail\":\"https:\\/\\/spark.adobe.com\\/images\\/landing\\/examples\\/fall-sale-banner.jpg\"}";


            Banner banner = new Banner();

            try {
                JSONObject dataJsonObj = new JSONObject(strJson);
//                JSONArray friends = dataJsonObj.getJSONArray("friends");
//

//                JSONObject secondFriend = friends.getJSONObject(1);
//                secondName = secondFriend.getString("name");
//                Log.d(LOG_TAG, "Второе имя: " + secondName);

//                for (int i = 0; i < friends.length(); i++) {
//                    JSONObject friend = friends.getJSONObject(i);
//
//                    JSONObject contacts = friend.getJSONObject("contacts");

                String url = dataJsonObj.getString("url");
                String message = dataJsonObj.getString("message");
                String thumbnail = dataJsonObj.getString("thumbnail");

                Logger.d("url: " + url);
                Logger.d("message: " + message);
                Logger.d("thumbnail: " + thumbnail);
//                }

                banner.setMessage(message);
                banner.setUrl(url);
                banner.setThumbnail(thumbnail);


                if (mCallback != null) {
                    mCallback.requestSuccess(banner);
                }

            } catch (Exception e) {
                Logger.e(e.getMessage());
                if (mCallback != null) {
                    mCallback.requestError();
                }
            }

        }
    }
}
