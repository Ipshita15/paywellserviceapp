package com.cloudwell.paywell.services.utils;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
public class IsOnlineReceiver extends BroadcastReceiver {
	Context mContext;

	@SuppressWarnings("deprecation")
	@Override
	public void onReceive(Context context, Intent intent) {
		this.mContext = context;
		if (isOnline())
			if (!isAutoTimeEnabled()) {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
					Intent intent1 = new Intent(Settings.ACTION_DATE_SETTINGS);
					intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					mContext.getApplicationContext().startActivity(intent1);
				} else {
					Settings.System.putInt(mContext.getContentResolver(),
							Settings.System.AUTO_TIME, 1);
				}

			}
	}

	@SuppressWarnings("deprecation")
	private boolean isAutoTimeEnabled() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
			// For JB+
			return Settings.Global.getInt(mContext.getContentResolver(),
					Settings.Global.AUTO_TIME, 0) > 0;
		}
		// For older Android versions
		return Settings.System.getInt(mContext.getContentResolver(),
				Settings.System.AUTO_TIME, 0) > 0;
	}

	public boolean isOnline() {
		try {
			ConnectionDetector cd = new ConnectionDetector(mContext);
			cd.isConnectingToInternet();
			return cd.isConnectingToInternet();
		} catch (Exception e) {
			return false;
		}
	}

}
