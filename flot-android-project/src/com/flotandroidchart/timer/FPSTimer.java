package com.flotandroidchart.timer;

class FPSTimer {
	private int mFPS;
	private double mSecPerFrame;
	private double mSecTiming;
	private long mCur;
	public FPSTimer(int fps) {
		mFPS = fps;
		reset();
	}
	public void reset() {
		mSecPerFrame = 1.0 / mFPS;
		mCur = System.currentTimeMillis();
		mSecTiming = 0.0;
	}
	public boolean elapsed() {
		long next = System.currentTimeMillis();
		long passage_time = next - mCur;
		mCur = next;
		mSecTiming += (passage_time/1000.0);
		mSecTiming -= mSecPerFrame;
		if (mSecTiming > 0) {
			if (mSecTiming > mSecPerFrame) {
				reset();
				return true; // force redraw
			}
			return false;
		}
		try {
			Thread.sleep((long)(-mSecTiming * 1000.0));
		} catch (InterruptedException e) {
		}
		return true;
	}
}
