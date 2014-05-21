package bos.consoar.ninebynine.support;

import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

public class DisplayTool {
	public static boolean isTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}

	public static boolean isTablet2(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		dm = context.getResources().getDisplayMetrics();
		int mWidth = dm.widthPixels;
		int mHeight = dm.heightPixels;

		if (mHeight > mWidth) {// layout port
			return false;
			// ÊúÆÁ .......
		} else {// layout land
			return true;
			// ºáÆÁ .......
		}
	}
}
