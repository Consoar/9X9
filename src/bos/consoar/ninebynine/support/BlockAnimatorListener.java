package bos.consoar.ninebynine.support;

import android.content.Intent;
import bos.consoar.ninebynine.BaseApplication;
import bos.consoar.ninebynine.support.entity.Block;
import bos.consoar.ninebynine.views.BlockView;
import bos.consoar.ninebynine.views.BoardView;

import com.nineoldandroids.animation.Animator;

public class BlockAnimatorListener implements Animator.AnimatorListener {
	private BlockView bv;
	private BoardView mBoardView;
	private Block mState;

	public BlockAnimatorListener(BoardView mBoardView, BlockView bv,
			Block mState) {
		this.bv = bv;
		this.mState = mState;
		this.mBoardView = mBoardView;
	}

	@Override
	public void onAnimationCancel(Animator arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationEnd(Animator arg0) {
		// TODO Auto-generated method stub
		if (mState.getState() == 0) {
			bv.setState(mState.getState());
			bv.setSelected(false);
			bv.setPressed(false);
			bv.setEnabled(true);
		}
		Intent intent = new Intent("bos.consoar.ninebynine.GameIsOver");
		BaseApplication.getInstance().sendBroadcast(intent);
	}

	@Override
	public void onAnimationRepeat(Animator arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationStart(Animator arg0) {
		// TODO Auto-generated method stub
		// System.out.println("BlockAnimatorListener-onAnimationStart");
		if (mState.getState() != 0) {
			GBTools.GBsetAlpha(bv, 0.0F);
			bv.setState(mState.getState());
			bv.setPressed(false);
		}
		bv.bringToFront();
		mBoardView.invalidate();
	}

}
