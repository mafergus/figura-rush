package com.addressunknowngames.shapeninja;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class TimerCounterTextView extends RelativeLayout {

	private TimerCounter timerCounter;
	private long startTimeNs;
	private long timeToCountMs;
	private long timeLeftMs;
	private long totalRunTimeMs = 0;

	private TextView leftText;
	private TextView point;
	private TextView rightText;

	private Callback callback;

	public interface Callback {
		public void onEnded(long totalRunTimeMs);
		public void onTick(long timeElapsed);
	}

	public TimerCounterTextView(Context context) {
		this(context, null);
	}

	public TimerCounterTextView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TimerCounterTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LayoutInflater.from(context).inflate(R.layout.timer_counter_text_view, this);
		this.leftText = (TextView)findViewById(R.id.timerCounterTextViewLeft);
		this.rightText = (TextView)findViewById(R.id.timerCounterTextViewRight);
		this.point = (TextView)findViewById(R.id.timerCounterTextViewPoint);
	}

	private boolean initCalled = false;

	public void init(long intervalMs, long startTimeMs, final Callback callback) {
		assert(!initCalled);
		initCalled = true;
		setOnEndedCallback(callback);
		this.timeToCountMs = startTimeMs;
		timerCounter = new TimerCounter(intervalMs, new TimerCounter.TickCallback() {

			@Override
			public void onTick(long elapsedTimeMs) {
				//				long elapsedTimeMs = elapsedTimeNs / 1000000;
				timeLeftMs -= elapsedTimeMs;
				totalRunTimeMs += elapsedTimeMs;
				setTextColor(getContext().getResources().getColor((timeLeftMs < 4000 ? R.color.Red : R.color.black)));
				if (timeLeftMs < 0) {
					stop();
					setTime(0);
					setTextColor(getContext().getResources().getColor(R.color.black));
					callback.onEnded(totalRunTimeMs);
					return;
				}
				setTime(timeLeftMs);
				if (callback != null) {
					callback.onTick(elapsedTimeMs);
				}
			}
		});
		setTime(timeToCountMs);
	}

	public void setOnEndedCallback(final Callback callback) {
		this.callback = callback;
	}

	public void start() {
		setTime(timeToCountMs);
		timeLeftMs = timeToCountMs;
		timerCounter.start();
	}

	public void stop() {
		timerCounter.stop();
	}

	public void reset() {
		totalRunTimeMs = 0;
		timeLeftMs = timeToCountMs;
		setTime(timeLeftMs);
	}

	public void addTime(long timeMs) {
		timeLeftMs += timeMs;
		setTime(timeLeftMs);
	}

	public void setTime(long timeMs) {
		int hours = (int) (timeMs / (1000 * 60 * 60));
		timeMs -= (hours * 1000 * 60 * 60);
		int minutes = (int) (timeMs / (1000 * 60));
		timeMs -= (minutes * 1000 * 60);
		int seconds = (int) (timeMs / 1000);
		timeMs -= (seconds * 1000);
		int miliseconds = (int) (timeMs);

		String hoursStr = "" + (hours == 0 ? "" : hours);
		String minutesStr = "" + (minutes == 0 ? "" : String.format("%02d", minutes));
		String secondsStr = String.format("%02d", seconds);
		String milisecondsStr = String.format("%2d", miliseconds / 10);

		leftText.setText("" + hoursStr + minutesStr + secondsStr);
		rightText.setText("" + milisecondsStr);
	}
	
	public long getTimeLeftMs() {
		return timeLeftMs;
	}

	public long getTotalRunTimeMs() {
		return totalRunTimeMs;
	}

	public void setTextColor(int color) {
		point.setTextColor(color);
		leftText.setTextColor(color);
		rightText.setTextColor(color);
	}

	public void setTypeface(final Typeface typeface) {
		point.setTypeface(typeface);
		leftText.setTypeface(typeface);
		rightText.setTypeface(typeface);
	}

	public void setTextSize(float sp) {
		point.setTextSize(sp);
		leftText.setTextSize(sp);
		rightText.setTextSize(sp);
	}

}
