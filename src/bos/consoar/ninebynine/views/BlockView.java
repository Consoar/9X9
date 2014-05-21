package bos.consoar.ninebynine.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;
import bos.consoar.ninebynine.BaseApplication;

public class BlockView extends TextView {
	private Rect mRect;
	private Paint iPaint;
	private Paint oPaint;
	private Paint textPaint;
	public int col;
	public int row;
	private int mState = 0;
	private int mBlockColor[];
	private float textSize = 19f;
	private int centerNum = -1;

	public BlockView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public BlockView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public BlockView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}

	private void init(Context context) {
		float f1 = getResources().getDisplayMetrics().density;
		mBlockColor = BaseApplication.getInstance().getBlockColors();
		setState(0);
		setGravity(Gravity.CENTER);
		this.mRect = new Rect();
		this.oPaint = new Paint();
		this.oPaint.setColor(Color.WHITE);
		this.oPaint.setStrokeWidth(f1 * 2.0F);
		this.textPaint = new Paint(Paint.FAKE_BOLD_TEXT_FLAG
				| Paint.ANTI_ALIAS_FLAG);
		this.textPaint.setColor(Color.WHITE);
		this.textPaint.setStyle(Paint.Style.FILL);
		textSize = textSize * getResources().getDisplayMetrics().density;
		this.textPaint.setTextSize(textSize);
	}

	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// System.out.println("BlockView "+"onDraw");
		if ((iPaint != null) && (isPressed()) && (isSelected()) && mState == 0) {
			int l = getWidth() / 4;
			int t = getHeight() / 4;
			int r = l + getWidth() / 2;
			int b = t + getHeight() / 2;
			// Log.d("BlockView", "l = " + l + " t = " + t + " r = "
			// + r + " b = " + b);
			mRect.set(l, t, r, b);
			canvas.drawRect(mRect, iPaint);
		}
		// if (Game.getLoction(row,col)==0){
		// System.out.println("BlockView pos "+Game.getLoction(row,col));
		// System.out.println("BlockView mState "+mState);
		// System.out.println("BlockView isEnabled "+isEnabled());
		// }
		if ((this.mState == 0) && (!isEnabled())) {
			// draw X
			int w = getWidth();
			int h = getHeight();
			float[] pts = new float[8];
			pts[0] = 0.0F;
			pts[1] = 0.0F;
			pts[2] = w;
			pts[3] = h;
			pts[4] = 0.0F;
			pts[5] = h;
			pts[6] = w;
			pts[7] = 0.0F;
			canvas.drawLines(pts, this.oPaint);
		}
		if (mState != 0 && centerNum != -1) {
			FontMetrics fontMetrics = textPaint.getFontMetrics();
			String centerText = String.valueOf(centerNum);
			float fontTotalHeight = fontMetrics.bottom - fontMetrics.top;
			float offY = fontTotalHeight / 2 - fontMetrics.bottom;
			float baseX = getWidth() / 2
					- (textPaint.measureText(centerText) / 2);
			float baseY = getHeight() / 2;
			float newY = baseY + offY;
			canvas.drawText(centerText, baseX, newY, textPaint);
		}
		// if (mState == 0) {
		// FontMetrics fontMetrics = textPaint.getFontMetrics();
		// String centerText = String.valueOf(Game.getLoction(row, col));
		// float fontTotalHeight = fontMetrics.bottom - fontMetrics.top;
		// float offY = fontTotalHeight / 2 - fontMetrics.bottom;
		// float baseX = getWidth() / 2
		// - (textPaint.measureText(centerText) / 2);
		// float baseY = getHeight() / 2;
		// float newY = baseY + offY;
		// canvas.drawText(centerText, baseX, newY, textPaint);
		// }
	}

	public void setState(int mState) {
		// TODO Auto-generated method stub
		setBackgroundResource(BaseApplication.getInstance()
				.getBlockColorDrawable(mState));
		// setBackgroundColor(BaseApplication.getInstance().getBlockColor(mState));
		this.mState = mState;
	}

	public int getState() {
		return this.mState;
	}

	// void randomBackColor() {
	// this.mState = (int) (Math.random() * mBlockColor.length);
	// setState(this.mState);
	// }

	public void setCenterNum(int n) {
		this.centerNum = n;
		invalidate();
	}

	public int getCenterNum() {
		return centerNum;
	}

	public void setPathState(int mPathState) {
		if (iPaint == null)
			iPaint = new Paint();
		iPaint.setColor(mBlockColor[mPathState]);
		iPaint.setStyle(Paint.Style.FILL);
		invalidate();
	}
}
