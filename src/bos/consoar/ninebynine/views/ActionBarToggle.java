package bos.consoar.ninebynine.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.widget.ImageButton;
import bos.consoar.ninebynine.support.GBTools;

public class ActionBarToggle extends ImageButton {
	private boolean isChecked;

	public ActionBarToggle(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init();
	}

	public ActionBarToggle(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	public ActionBarToggle(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}

	private void init() {
		setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
		if (isChecked)
			GBTools.GBsetAlpha(this, 0.5f);
		else GBTools.GBsetAlpha(this, 1.0f);
	}
}
