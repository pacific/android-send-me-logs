package org.focuser.sendmelogs.demo;

import android.app.Application;
import android.util.Log;

public class DemoApp extends Application {
	
	private static final String LOG_TAG = DemoApp.class.getSimpleName();

	@Override
	public void onTerminate() {
		super.onTerminate();
		Log.d(LOG_TAG, "terminated");
	}

}
