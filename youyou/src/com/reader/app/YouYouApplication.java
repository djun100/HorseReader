package com.reader.app;

import java.io.File;

import android.app.Application;
import android.os.Environment;

public class YouYouApplication extends Application {
	public static String configDir = Environment.getExternalStorageDirectory()
			.getPath() + "/YouYou/";;
	public static String configPath = configDir + "config.xml";
	public static String cachePath = configDir + ".cache/";
	public static YouYouApplication Instance;

	@Override
	public void onCreate() {
		super.onCreate();
		Instance = this;
		File dir = new File(configDir);
		if (!dir.exists()) {
			dir.mkdir();
		}
	}

}
