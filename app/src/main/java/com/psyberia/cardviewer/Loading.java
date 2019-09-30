package com.psyberia.cardviewer;

import android.os.AsyncTask;

/**
 * Created by combo on 11/14/2017.

 */

public class Loading extends AsyncTask<Void, Void, Void> {


    interface Callback {
        void nextPage();
    }


    private final Callback mCallback;

    Loading(Callback callback) {
        this.mCallback = callback;
    }

    protected Void doInBackground(Void... voids) {
        try {
            Thread.sleep(SplashActivity.DELAY);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        mCallback.nextPage();
    }
}
