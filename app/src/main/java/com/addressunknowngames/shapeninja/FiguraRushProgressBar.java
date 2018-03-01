package com.addressunknowngames.shapeninja;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class FiguraRushProgressBar extends RelativeLayout {
	final private Context context;
	private ImageView bar;
	private double lastPercentage = 100;

	public FiguraRushProgressBar(Context context) {
		this(context, null);
	}

	public FiguraRushProgressBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public FiguraRushProgressBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;

		LayoutInflater.from(context).inflate(R.layout.figura_rush_progress_bar, this);
		this.bar = (ImageView)findViewById(R.id.imageView);
	}

	public void setProgress(double percentage) {
//		float difference = (float)(percentage - lastPercentage) / (float)lastPercentage;
		float difference = (float)(percentage - lastPercentage);
		lastPercentage = percentage;
		Log.v("MNF", "setProgress " + percentage + " difference " + difference);
		int height = context.getResources().getDisplayMetrics().heightPixels;
		int width = context.getResources().getDisplayMetrics().widthPixels;
		LayoutParams params = new RelativeLayout.LayoutParams(width/5, (int) (height*(percentage/100.0)));
		params.addRule(ALIGN_PARENT_BOTTOM);
//		this.bar.setLayoutParams(params);
		this.bar.animate().scaleY( difference );
		invalidate();
	}

}
