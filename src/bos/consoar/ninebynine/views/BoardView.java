package bos.consoar.ninebynine.views;

import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import bos.consoar.ninebynine.BaseApplication;
import bos.consoar.ninebynine.R;
import bos.consoar.ninebynine.support.BlockAnimatorListener;
import bos.consoar.ninebynine.support.Common;
import bos.consoar.ninebynine.support.DisplayTool;
import bos.consoar.ninebynine.support.GBTools;
import bos.consoar.ninebynine.support.entity.Block;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

public class BoardView extends ViewGroup {
	private int width;
	private int height;
	private int mGravity;
	private Rect mBackgroundRect;
	public BlockView[] mBlockViews;
	public BlockView[] mBeforeBlockViews;
	private View frontView;
	private boolean mEnabled = true;
	private Rect mRect;
	private Paint iPaint;
	private int mBackgroundColor;
	private int mGridColor;
	private boolean isTablet;

	public BoardView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init(context, attrs, defStyle);
	}

	public BoardView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context, attrs, R.attr.boardViewStyle);
	}

	public BoardView(Context context) {
		super(context);
		init(context, null, R.attr.boardViewStyle);
		// TODO Auto-generated constructor stub
	}

	private void init(Context context, AttributeSet attrs, int defStyle) {
		// TODO Auto-generated method stub
		if (isInEditMode()) {
			return;
		}
		int mGridColor = context
				.getTheme()
				.obtainStyledAttributes(attrs, new int[] { R.attr.gridColor },
						defStyle, 0).getColor(0, 0);
		setWillNotDraw(false);
		this.mBackgroundRect = new Rect();
		this.width = Common.width;
		this.height = Common.height;
		this.mRect = new Rect();
		this.iPaint = new Paint();
		this.mGridColor = mGridColor;
		this.mBlockViews = new BlockView[this.width * this.height];
		int k = 0;
		while (k < this.width * this.height) {
			BlockView bv = new BlockView(context);
			bv.col = (k % this.width);
			bv.row = (k / this.width);
			addView(bv);
			this.mBlockViews[k] = bv;
			k++;
		}
		frontView = new View(context);
		frontView.setVisibility(View.INVISIBLE);
		// ¼æÈÝ2.3
		GBTools.GBsetAlpha(frontView, 0.5f);
		// frontView.setAlpha(0.5F);
		addView(frontView);
		isTablet = DisplayTool.isTablet2(context);
	}

	public void frontViewGone() {
		GBTools.GBsetAlpha(frontView, 0.0F);
	}

	public AnimatorSet getAnimeStateSet(int pos, Block mState) {
		if (mState.getState() != 0) {
				AnimatorSet mAnimatorSet1 = new AnimatorSet();
				mAnimatorSet1.setInterpolator(new LinearInterpolator());
				mAnimatorSet1.addListener(new BlockAnimatorListener(this,mBlockViews[pos], mState));
				Animator[] mAnimator = new Animator[3];
				mAnimator[0] = ObjectAnimator.ofFloat(this.mBlockViews[pos],
						"scaleX", new float[] { 2.0F, 1.0F });
				mAnimator[1] = ObjectAnimator.ofFloat(this.mBlockViews[pos],
						"scaleY", new float[] { 2.0F, 1.0F });
				mAnimator[2] = ObjectAnimator.ofFloat(this.mBlockViews[pos],
						"alpha", new float[] { 0.5F, 1F });
				mAnimatorSet1.playTogether(mAnimator);
				mAnimatorSet1.setDuration(200L);
				return mAnimatorSet1;
		} else {
				AnimatorSet mAnimatorSet3 = new AnimatorSet();
				AnimatorSet mAnimatorSet1 = new AnimatorSet();
				mAnimatorSet1.setInterpolator(new LinearInterpolator());
				mAnimatorSet1.addListener(new BlockAnimatorListener(this,
						mBlockViews[pos], mState));
				Animator[] mAnimator = new Animator[3];
				mAnimator[0] = ObjectAnimator.ofFloat(this.mBlockViews[pos],
						"scaleX", new float[] { 1.0F, 2.0F });
				mAnimator[1] = ObjectAnimator.ofFloat(this.mBlockViews[pos],
						"scaleY", new float[] { 1.0F, 2.0F });
				mAnimator[2] = ObjectAnimator.ofFloat(this.mBlockViews[pos],
						"alpha", new float[] { 1.0F, 0.0F });

				AnimatorSet mAnimatorSet2 = new AnimatorSet();
				Animator[] mAnimator2 = new Animator[3];
				mAnimator2[0] = ObjectAnimator.ofFloat(
						this.mBlockViews[pos], "scaleX", new float[] { 1.0F,
								1.0F });
				mAnimator2[1] = ObjectAnimator.ofFloat(
						this.mBlockViews[pos], "scaleY", new float[] { 1.0F,
								1.0F });
				mAnimator2[2] = ObjectAnimator.ofFloat(
						this.mBlockViews[pos], "alpha", new float[] { 1.0F,
								1.0F });
				mAnimatorSet1.playTogether(mAnimator);
				mAnimatorSet1.setDuration(200L);
				mAnimatorSet2.playTogether(mAnimator2);
				mAnimatorSet2.setDuration(0L);
				mAnimatorSet3.play(mAnimatorSet2).after(mAnimatorSet1);
				return mAnimatorSet3;
		}
	}

	public void setAnimeState(int pos, Block mState) {
		AnimatorSet mAnimatorSet = new AnimatorSet();
		AnimatorSet mAnimatorSet1 = new AnimatorSet();
		mAnimatorSet1.setInterpolator(new LinearInterpolator());
		mAnimatorSet1.addListener(new BlockAnimatorListener(this,
				mBlockViews[pos], mState));
		Animator[] mAnimator = new Animator[3];
		mAnimator[0] = ObjectAnimator.ofFloat(this.mBlockViews[pos], "scaleX",
				new float[] { 1.0F, 2.0F });
		mAnimator[1] = ObjectAnimator.ofFloat(this.mBlockViews[pos], "scaleY",
				new float[] { 1.0F, 2.0F });
		mAnimator[2] = ObjectAnimator.ofFloat(this.mBlockViews[pos], "alpha",
				new float[] { 1.0F, 0.0F });

		AnimatorSet mAnimatorSet2 = new AnimatorSet();
		Animator[] mAnimator2 = new Animator[3];
		mAnimator2[0] = ObjectAnimator.ofFloat(this.mBlockViews[pos], "scaleX",
				new float[] { 1.0F, 1.0F });
		mAnimator2[1] = ObjectAnimator.ofFloat(this.mBlockViews[pos], "scaleY",
				new float[] { 1.0F, 1.0F });
		mAnimator2[2] = ObjectAnimator.ofFloat(this.mBlockViews[pos], "alpha",
				new float[] { 1.0F, 1.0F });
		mAnimatorSet1.playTogether(mAnimator);
		mAnimatorSet1.setDuration(2000L);
		mAnimatorSet2.playTogether(mAnimator2);
		mAnimatorSet2.setDuration(0L);
		mAnimatorSet.play(mAnimatorSet2).after(mAnimatorSet1);
		mAnimatorSet.start();
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

	public int getLocation(MotionEvent ev) {
		int k = -1;
		Rect localRect = new Rect();
		int cWidth = getChildWidth();
		int cHeight = getChildHeight();
		// ´Ó±³¾°ÍÆËã³öÆåÅÌµÄ±ßÔµ
		int l = mBackgroundRect.left
				+ (mBackgroundRect.width() - cWidth * this.width) / 2;
		int t = mBackgroundRect.top
				+ (mBackgroundRect.height() - cHeight * this.height) / 2;
		localRect.set(l, t, l + cWidth * this.width, t + cHeight * this.height);
		float x = ev.getX();
		float y = ev.getY();
		if ((x >= localRect.left) && (x < localRect.right)
				&& (y >= localRect.top) && (y < localRect.bottom))
			k = (int) ((x - l) / cWidth) + (int) ((y - t) / cHeight)
					* this.width;
		return k;
	}

	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// System.out.println("BoardView " + "onDraw");
		float f1 = getResources().getDisplayMetrics().density;
		int cWidth = getChildWidth();
		int cHeight = getChildHeight();
		int l1 = mBackgroundRect.left
				+ (mBackgroundRect.width() - cWidth * this.width) / 2;
		int t1 = mBackgroundRect.top
				+ (mBackgroundRect.height() - cHeight * this.height) / 2;
		int mSpace = (int) (f1 * 1.0F);
		this.iPaint.setColor(mBackgroundColor);
		canvas.drawRect(mBackgroundRect, this.iPaint);
		for (int k = 0; k < this.width * this.height; k++) {
			int col = k % this.width;
			int row = k / this.width;
			int l = l1 + mSpace + col * cWidth;
			int t = t1 + mSpace + row * cHeight;
			int r = l + cWidth - mSpace * 2;
			int b = t + cHeight - mSpace * 2;
			this.mRect.left = l;
			this.mRect.top = t;
			this.mRect.right = r;
			this.mRect.bottom = b;
			this.iPaint.setColor(mGridColor);
			canvas.drawRect(this.mRect, this.iPaint);
		}
	}

	// protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	// // System.out.println("BoardView " + "onMeasure");
	// int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
	// int measureHeigth = MeasureSpec.getSize(heightMeasureSpec);
	// setMeasuredDimension(measureWidth, measureHeigth);
	// // TODO Auto-generated method stub
	// for (int i = 0; i < getChildCount(); i++) {
	// View v = getChildAt(i);
	// // Log.v("BoardView", "measureWidth is " + v.getMeasuredWidth()
	// // + "measureHeight is " + v.getMeasuredHeight());
	// int widthSpec = 0;
	// int heightSpec = 0;
	// LayoutParams params = v.getLayoutParams();
	// if (params.width > 0) {
	// widthSpec = MeasureSpec.makeMeasureSpec(params.width,
	// MeasureSpec.EXACTLY);
	// } else if (params.width == -1) {
	// widthSpec = MeasureSpec.makeMeasureSpec(measureWidth,
	// MeasureSpec.EXACTLY);
	// } else if (params.width == -2) {
	// widthSpec = MeasureSpec.makeMeasureSpec(measureWidth,
	// MeasureSpec.AT_MOST);
	// }
	// if (params.height > 0) {
	// heightSpec = MeasureSpec.makeMeasureSpec(params.height,
	// MeasureSpec.EXACTLY);
	// } else if (params.height == -1) {
	// heightSpec = MeasureSpec.makeMeasureSpec(measureHeigth,
	// MeasureSpec.EXACTLY);
	// } else if (params.height == -2) {
	// heightSpec = MeasureSpec.makeMeasureSpec(measureWidth,
	// MeasureSpec.AT_MOST);
	// }
	// v.measure(widthSpec, heightSpec);
	// }
	// }
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// System.out.println("BoardView " + "onLayout");
		if (isInEditMode()) {
			return;
		}
		// TODO Auto-generated method stub
		Log.d("BoardView", "changed = " + changed + " left = " + l + " top = "
				+ t + " right = " + r + " botom = " + b);
		int num = getChildCount();
		float f1 = getResources().getDisplayMetrics().density;
		int cWidth = getChildWidth();
		int cHeight = getChildHeight();
		int cSize = getSize();
		int tl, tt, tr, tb, tspace;
		BlockView bv;
		mBackgroundRect.left = 0;
		int k = 0;
		while (k < num) {
			mBackgroundRect.right = (cSize + mBackgroundRect.left);
			if (isTablet)
				mBackgroundRect.top = 0;
			else
				mBackgroundRect.top = (b - cSize);
			mBackgroundRect.bottom = (cSize + mBackgroundRect.top);
			tl = mBackgroundRect.left
					+ (mBackgroundRect.width() - cWidth * this.width) / 2;
			tt = mBackgroundRect.top
					+ (mBackgroundRect.height() - cHeight * this.height) / 2;
			// System.out.println("onLayout cSize " + cSize);
			// System.out.println("onLayout tt " + (tt - mBackgroundRect.top));
			// System.out.println("onLayout mBackgroundRect.top "
			// + mBackgroundRect.top);
			tspace = (int) (f1 * 1.0F);
			View localView = getChildAt(k);
			if ((localView instanceof BlockView)) {
				bv = (BlockView) localView;
				// if (bv.getState() != 0)
				// bv.bringToFront();
				int bvr = tl + tspace + cWidth * bv.col;
				int bvb = tt + tspace + cHeight * bv.row;
				bv.layout(bvr, bvb, bvr + cWidth - tspace * 2, bvb + cHeight
						- tspace * 2);
			}
			k++;
		}
		this.frontView.layout(0, 0, cWidth * 2, cHeight * 2);
	}

	public void clearPath() {
		System.out.println("clearPath");
		setPath(0, -1, -1, new Integer[] {});
		frontView.setVisibility(View.GONE);
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH)
			GBTools.GBsetAlpha(frontView, 0.0F);
		this.invalidate();
	}

	public void setPath(int mState, int src, int dst, Integer[] aPath) {
		for (int i = 0; i < aPath.length; ++i) {
			mBlockViews[aPath[i]].setPathState(mState);
			mBlockViews[aPath[i]].setPressed(true);
			mBlockViews[aPath[i]].setSelected(true);
		}
		if (dst != -1) {
			mBlockViews[dst].setPathState(mState);
			BlockView bv = mBlockViews[dst];
			frontView.setVisibility(View.VISIBLE);
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH)
				GBTools.GBsetAlpha(frontView, 0.5F);
			frontView.bringToFront();
			frontView.setBackgroundColor(BaseApplication.getInstance()
					.getBlockColor(mState));
			// Log.d("BoardView", "frontView.getLeft() = " + frontView.getLeft()
			// + " frontView.getTop() = " + frontView.getTop());
			int fvx = bv.getLeft() + bv.getWidth() / 2 - frontView.getWidth()
					/ 2;
			int fvy = bv.getTop() + bv.getHeight() / 2 - frontView.getHeight()
					/ 2;
			GBTools.GBsetTranslation(frontView, fvx, fvy);
			// frontView.setX(bv.getX() + bv.getWidth() / 2 -
			// frontView.getWidth() / 2);
			// frontView.setY(bv.getY() + bv.getHeight() / 2 -
			// frontView.getHeight() / 2);
		}
		invalidate();
	}

	public void setValidBlock(List<Integer> live_box) {
		setAllBlockViewsEnabled(true);
		for (int i = 0; i < width * height; ++i) {
			if (mBlockViews[i].getState() == 0 && !live_box.contains(i)) {
				// System.out.println("!ValidBlock " + i);
				mBlockViews[i].setEnabled(false);
			} else {
				mBlockViews[i].setEnabled(true);
			}
		}
	}

	public void setBackgroundColor(int mBackgroundColor) {
		this.mBackgroundColor = mBackgroundColor;
	}

	public void setEnabled(boolean mEnabled) {
		this.mEnabled = mEnabled;
	}

	public void setBlockViewPressed(int k, boolean mPressed) {
		this.mBlockViews[k].setPressed(mPressed);
	}

	public void setAllBlockViewsEnabled(boolean mEnabled) {
		for (int i = 0; i < mBlockViews.length; ++i)
			mBlockViews[i].setEnabled(mEnabled);
	}

	public void setAllBlockViewsSelected(boolean mSelected) {
		for (int i = 0; i < mBlockViews.length; ++i)
			mBlockViews[i].setSelected(mSelected);
	}

	public void setAllBlockViewsPressed(boolean mPressed) {
		for (int i = 0; i < mBlockViews.length; ++i)
			mBlockViews[i].setPressed(mPressed);
	}

	public int getBlockViewState(int k) {
		return this.mBlockViews[k].getState();
	}

	public int getBlockViewNum(int k) {
		return this.mBlockViews[k].getCenterNum();
	}

	public void setBlockViewNum(int k, int num) {
		this.mBlockViews[k].setCenterNum(num);
	}

	public Block[] getBoard() {
		int m = this.mBlockViews.length;
		Block[] mBlocks = new Block[m];
		for (int n = 0; n < m; n++) {
			mBlocks[n] = new Block(mBlockViews[n].getState(),
					mBlockViews[n].getCenterNum());
			// System.out.println(mBlocks[n].toString());
		}
		return mBlocks;
	}

	public void setBoard(Block mBlocks[]) {
		for (int k = 0; k < mBlocks.length; ++k) {
			this.mBlockViews[k].setState(mBlocks[k].getState());
			this.mBlockViews[k].setCenterNum(mBlocks[k].getNum());
			this.mBlockViews[k].setSelected(false);
			this.mBlockViews[k].setPressed(false);
			if (mBlocks[k].getState() == 0) {
				this.mBlockViews[k].setEnabled(true);
			}
		}
	}

	public void setBlockViewState(int k, int mState) {
//		System.out.println("setBlockViewState " + k + " " + mState);
		this.mBlockViews[k].setState(mState);
	}

	public void setBlockViewSelected(int k, boolean mSelected) {
		this.mBlockViews[k].setSelected(mSelected);
	}

	public BlockView[] getmBlockViews() {
		return mBlockViews;
	}

	public void setmBlockViews(BlockView[] mBlockViews) {
		this.mBlockViews = mBlockViews;
	}

	public BlockView[] getmBeforeBlockViews() {
		return mBeforeBlockViews;
	}

	public void setmBeforeBlockViews(BlockView[] mBeforeBlockViews) {
		this.mBeforeBlockViews = mBeforeBlockViews;
	}
	public boolean isBlockViewSelected(int k) {
		return this.mBlockViews[k].isSelected();
	}

	public boolean isBlockViewEnabled(int k) {
		return this.mBlockViews[k].isEnabled();
	}

	public void setGravity(int mGravity) {
		if (mGravity != this.mGravity) {
			this.mGravity = mGravity;
			invalidate();
		}
	}
}
