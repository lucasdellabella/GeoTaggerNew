package com.hci.geotagger.common;
/**
 * Class detects whether network connectivity is present
 */
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtils {
	
	public static boolean isNetworkUp(Context context) {
		ConnectivityManager conMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = conMgr.getActiveNetworkInfo();
		if (ni != null) {
			if (ni.isAvailable() && ni.isConnected())
				return true;
		}
		return false;
	}

}
