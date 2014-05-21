package bos.consoar.ninebynine.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import bos.consoar.ninebynine.BaseApplication;
import bos.consoar.ninebynine.support.Common;
import bos.consoar.ninebynine.support.entity.Block;

public class UpNextView extends View {
	private int[] mBlockColor;
	private int width;
	private int height;
	private Block[] mNextBlockStates;
	private int mSpace;
	private Rect mRect;
	private Paint iPaint;
	private Paint textPaint;
	private float textSize = 19f;

	public UpNextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init();
	}

	public UpNextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	public UpNextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}

	private void init() {
		if (isInEditMode()) {
			return;
		}
		float f1 = getResources().getDisplayMetrics().density;
		this.width = 3;
		this.height = 2;
		this.mNextBlockStates = new Block[width * height];
		for (int i = 0; i < width * height; i++)
			mNextBlockStates[i] = new Block();
		this.mSpace = (int) (1.0F * f1);
		this.mRect = new Rect();
		this.iPaint = new Paint();
		this.iPaint.setStrokeWidth(f1 * 2.0F);
		this.textPaint = new Paint(Paint.FAKE_BOLD_TEXT_FLAG
				| Paint.ANTI_ALIAS_FLAG);
		this.textPaint.setColor(Color.WHITE);
		this.textPaint.setStyle(Paint.Style.FILL);
		textSize = textSize * getResources().getDisplayMetrics().density;
		this.textPaint.setTextSize(textSize);
	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int mSize = getHeight() / 2;
		for (int j = 0; j < height; j++) {
			int k = 0;
			while (k < this.width) {
				int mState = this.mNextBlockStates[((k + 1 + j * this.width) - 1)]
						.getState();
				int mSpace = this.mSpace;
				if (mState == 0) {
					this.iPaint.setStyle(Paint.Style.STROKE);
					mSpace += (int) (this.iPaint.getStrokeWidth() / 2.0F);
				}
				int l = mSpace + k * mSize;
				int t = mSpace + j * mSize;
				int r = l + mSize - mSpace * 2;
				int b = t + mSize - mSpace * 2;
				this.mRect.set(l, t, r, b);
				this.iPaint.setColor(BaseApplication.getInstance()
						.getBlockColor(mState));
				canvas.drawRect(this.mRect, this.iPaint);
				this.iPaint.setStyle(Paint.Style.FILL);

				// draw center num
				if (this.mNextBlockStates[((k + 1 + j * this.width) - 1)]
						.getNum() != 0) {
					FontMetrics fontMetrics = textPaint.getFontMetrics();
					String centerText = String
							.valueOf(this.mNextBlockStates[((k + 1 + j
									* this.width) - 1)].getNum());
					float fontTotalHeight = fontMetrics.bottom
							- fontMetrics.top;
					float offY = fontTotalHeight / 2 - fontMetrics.bottom;
					float baseX = l + mSize / 2
							- (textPaint.measureText(centerText) / 2);
					float baseY = t + mSize / 2;
					float newY = baseY + offY;
					canvas.drawText(centerText, baseX, newY, textPaint);
				}
				k++;
			}
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int measuredHeight = measureHeight(heightMeasureSpec);
		int measuredWidth = measureWidth(widthMeasureSpec);
		int mSize = measuredHeight / 2;
		setMeasuredDimension(mSize * this.width, mSize * this.height);
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
		// Log.e("HJJ", "****specModeHeight:" + getModeStr(specMode)
		// + ", specSizeHeight:" + specSize);
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
		// Log.e("HJJ", "****specModeWidth:" + getModeStr(specMode)
		// + ", specSizeWidth:" + specSize);
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

	public void setStates(Block[] mStates) {
		if (mStates.length > mNextBlockStates.length)
			return;
		this.mNextBlockStates = Common.BlocksCopy(mStates, mStates.length);
		invalidate();
	}
}