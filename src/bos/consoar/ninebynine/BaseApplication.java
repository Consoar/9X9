package bos.consoar.ninebynine;

import android.app.Application;
import android.content.res.Resources;
import android.content.res.TypedArray;
import bos.consoar.ninebynine.support.DisplayTool;

public final class BaseApplication extends Application {
	private int[] BlockColor;
	private int[] BlockColorDrawable;
	private static BaseApplication baseapplication;
	public static boolean isTable;

	public static BaseApplication getInstance() {
		return baseapplication;
	}

	public void onCreate() {
		super.onCreate();
		init();
	}

	private void init() {
		baseapplication = this;
		isTable=DisplayTool.isTablet(getApplicationContext());
		setTheme(R.style.AppTheme_Light);
	}

	public int getBlockColor(int i) {
		return this.BlockColor[i];
	}
	public int getBlockColorDrawable(int i) {
		return this.BlockColorDrawable[i];
	}
	public int[] getBlockColorDrawable() {
		return BlockColorDrawable;
	}

	public int[] getBlockColors() {
		return this.BlockColor;
	}

	public void setTheme(int resid) {
		super.setTheme(resid);
		Resources.Theme localTheme = this.getTheme();
		int[] tBlockColor = new int[6];
		TypedArray tTypedArray = localTheme.obtainStyledAttributes(resid,
				new int[] { R.attr.gridColor, R.attr.blueBlockColor,
						R.attr.redBlockColor, R.attr.yellowBlockColor,
						R.attr.greenBlockColor, R.attr.purpleBlockColor });
		for (int i = 0; i < tBlockColor.length; i++)
			tBlockColor[i] = tTypedArray.getColor(i, 0);
		this.BlockColor = tBlockColor;
		
		int[] tBlockColorDrawable = new int[6];
		TypedArray tTypedArrayDrawable = localTheme.obtainStyledAttributes(resid,
				new int[] { R.attr.emptyBlockDrawable, R.attr.blueBlockDrawable,
						R.attr.redBlockDrawable, R.attr.yellowBlockDrawable,
						R.attr.greenBlockDrawable, R.attr.purpleBlockDrawable });
		for (int i = 0; i < tBlockColor.length; i++)
			tBlockColorDrawable[i] = tTypedArrayDrawable.getResourceId(i, 0);
		this.BlockColorDrawable = tBlockColorDrawable;
	}
}
