package erick.bandeco.controller;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class HeightAnimation extends Animation {
	private final int originalHeight;
	private final View view;
	private float heightDiff;

	public HeightAnimation(View view, int fromHeight, int toHeight) {
		this.view = view;
		this.originalHeight = fromHeight;
		this.heightDiff = (toHeight - fromHeight);
	}

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		view.getLayoutParams().height = (int) (originalHeight + heightDiff * interpolatedTime);
		view.requestLayout();
	}

	@Override
	public boolean willChangeBounds() {
		return true;
	}
}
