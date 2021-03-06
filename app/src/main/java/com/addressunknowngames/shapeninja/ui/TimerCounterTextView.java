package com.addressunknowngames.shapeninja.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.addressunknowngames.shapeninja.R;


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
		void onEnded(long totalRunTimeMs);
		void onTick(long timeElapsed);
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
		this.leftText = findViewById(R.id.timerCounterTextViewLeft);
		this.rightText = findViewById(R.id.timerCounterTextViewRight);
		this.point = findViewById(R.id.timerCounterTextViewPoint);
	}

	private boolean initCalled = false;

	public void init(long intervalMs, long startTimeMs) {
		assert(!initCalled);
		initCalled = true;
		this.timeToCountMs = startTimeMs;
		timerCounter = new TimerCounter(intervalMs);
		setTime(timeToCountMs);
	}

	public void start(Callback callback) {
	    this.callback = callback;
		setTime(timeToCountMs);
		timeLeftMs = timeToCountMs;
		timerCounter.start(elapsedTimeMs -> {
			timeLeftMs -= elapsedTimeMs;
			totalRunTimeMs += elapsedTimeMs;
			setTextColor(getContext().getResources().getColor((timeLeftMs < 4000 ? R.color.Red : R.color.black)));
			if (timeLeftMs < 0) {
				stop();
				setTime(0);
				setTextColor(getContext().getResources().getColor(R.color.black));
				this.callback.onEnded(totalRunTimeMs);
				return;
			}
			setTime(timeLeftMs);
			if (this.callback != null) {
				this.callback.onTick(elapsedTimeMs);
			}
		});
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
