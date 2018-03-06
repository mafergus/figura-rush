package com.addressunknowngames.shapeninja.ui;

import android.os.Handler;
import android.os.Looper;

public class TimerCounter {
	private float lastUpdateTime;
	
	private long intervalMs;
	private TickCallback tickCallback;
	
	private TimerRunnable runnable = new TimerRunnable();
	private Handler h = new Handler(Looper.getMainLooper());
	
	private boolean isRunning = false;
	
	public interface TickCallback {
		void onTick(long elapsedTimeMs);
	}

	private class TimerRunnable implements Runnable {

		@Override
		public void run() {
			float time = System.nanoTime();
			float timeElapsed = time - lastUpdateTime;
			lastUpdateTime = time;
			long timeElapsedMs = (long) (timeElapsed / 1000000);
			if (tickCallback != null && isRunning) {
				tickCallback.onTick(timeElapsedMs);
			}

			h.postDelayed(this, intervalMs);
		}
	}
	
	public TimerCounter(long intervalMs) {
		this.intervalMs = intervalMs;
	}
	
	public void start(TickCallback callback) {
		this.tickCallback = callback;
		isRunning = true;
		lastUpdateTime = System.nanoTime();
		h.postDelayed(runnable, intervalMs);
	}
	
	public void stop() {
		isRunning = false;
		h.removeCallbacks(runnable);
	}
	
	public boolean isRunning() { 
		return isRunning;
	}
	
}
