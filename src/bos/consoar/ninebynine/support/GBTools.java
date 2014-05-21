/**
 * 用动画间接实现一些API 11+中的View API
 */
package bos.consoar.ninebynine.support;

import android.view.View;

import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

public class GBTools {
	public static void GBsetTranslation(View view, int x, int y) {
		ObjectAnimator anim = ObjectAnimator.ofFloat(view, "translationX", x
				- view.getLeft());
		anim.setDuration(0);
		ObjectAnimator anim2 = ObjectAnimator.ofFloat(view, "translationY", y
				- view.getTop());
		anim2.setDuration(0);
		AnimatorSet set = new AnimatorSet();
		set.play(anim).with(anim2);
		set.start();
	}

	public static void GBsetAlpha(View view, float alpha) {
		ObjectAnimator anim = ObjectAnimator.ofFloat(view, "alpha", alpha);
		anim.setDuration(0);
		AnimatorSet set = new AnimatorSet();
		set.play(anim);
		set.start();
	}
}
