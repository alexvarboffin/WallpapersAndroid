package com.psyberia.cardviewer;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

/**
 * Created by combo on 11/13/2017.

 */

public class Application extends android.app.Application {
    public Application() {}

    @Override
    public void onCreate() {
        super.onCreate();

        Logger.addLogAdapter(new AndroidLogAdapter());
    }
}
