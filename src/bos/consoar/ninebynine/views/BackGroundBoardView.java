package bos.consoar.ninebynine.views;

import java.util.Arrays;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import bos.consoar.ninebynine.BaseApplication;
import bos.consoar.ninebynine.R.color;
import bos.consoar.ninebynine.support.Common;

public class BackGroundBoardView extends View {
	private int[] mBlockColor;
	private int width;
	private int height;
	private int[] mNextBlockStates;
	private int mSpace;
	private Rect mRect;
	private Paint iPaint;
	private Rect mBackgroundRect;

	public BackGroundBoardView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init();
	}

	public BackGroundBoardView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	public BackGroundBoardView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}

	private void init() {
		if (isInEditMode()) {
			return;
		}
		setWillNotDraw(false);
		this.mBackgroundRect = new Rect();
		float f1 = getResources().getDisplayMetrics().density;
		this.width = Common.width;
		this.height = Common.height;
		this.mNextBlockStates = new int[width * height];
		this.mSpace = (int) (1.0F * f1);
		this.mRect = new Rect();
		this.iPaint = new Paint();
	}

	public int getChildHeight() {
		return getSize() / this.height;
	}

	public int getChildWidth() {
		return getSize() / this.width;
	}

	private int getSize() {
		return Math.min(getWidth() - getPaddingLeft() - getPaddingRight(),
				getHeight() - getPaddingTop() - getPaddingBottom());
	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int mSize = getHeight() / width;
		int cWidth = getChildWidth();
		int cHeight = getChildHeight();
		int cSize = getSize();
		this.iPaint.setStyle(Paint.Style.FILL);
		int tl = (cSize - cWidth * this.width) / 2;
		int tt = (cSize - cHeight * this.height) / 2;
		for (int j = 0; j < height; j++) {
			int k = 0;
			while (k < width) {
				int mState = this.mNextBlockStates[((k + 1 + j * this.width) - 1)];
				int mSpace = this.mSpace;
				if (mState == 0) {
					this.iPaint.setColor(color.board_grid_color_light);
				}
				int l = tl + mSpace + k * mSize;
				int t = tt + mSpace + j * mSize;
				int r = l + mSize - mSpace * 2;
				int b = t + mSize - mSpace * 2;
				// System.out
				// .println("l " + l + " t " + t + " r " + r + " b " + b);
				this.mRect.set(l, t, r, b);
				this.iPaint.setColor(BaseApplication.getInstance()
						.getBlockColor(mState));
				canvas.drawRect(this.mRect, this.iPaint);
				k++;
			}
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int measuredHeight = measureHeight(heightMeasureSpec);
		int measuredWidth = measureWidth(widthMeasureSpec);
		if (measuredWidth < measuredHeight)
			setMeasuredDimension(measuredWidth, measuredWidth);
		else
			setMeasuredDimension(measuredHeight, measuredHeight);
	}

	private int measureHeight(int measureSpec) {
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);
		// Default size if no limits are specified.
		int result = 200;
		if (specMode == MeasureSpec.AT_MOST) {
			result = specSize;
		} else if (specMode == MeasureSpec.EXACTLY) {
			// If your control can fit within these bounds return that value.
			result = specSize;
		}
		return result;
	}

	private int measureWidth(int measureSpec) {
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);
		// Default size if no limits are specified.
		int result = 200;
		if (specMode == MeasureSpec.AT_MOST) {
			result = specSize;
		} else if (specMode == MeasureSpec.EXACTLY) {
			// If your control can fit within these bounds return that value.
			result = specSize;
		}
		return result;
	}

	private String getModeStr(int mode) {
		String modeStr = null;
		switch (mode) {
		case MeasureSpec.UNSPECIFIED:
			modeStr = "UNSPECIFIED";
			break;
		case MeasureSpec.AT_MOST:
			modeStr = "AT_MOST";
			break;
		case MeasureSpec.EXACTLY:
			modeStr = "EXACTLY";
			break;
		}
		return modeStr;
	}

	public void setStates(int[] mStates) {
		if (mStates.length > mNextBlockStates.length)
			return;
		Arrays.fill(this.mNextBlockStates, 0);
		System.arraycopy(mStates, 0, this.mNextBlockStates, 0, mStates.length);
		invalidate();
	}
}