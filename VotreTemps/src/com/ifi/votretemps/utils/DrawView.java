package com.ifi.votretemps.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class DrawView extends View {
	Paint paint = new Paint();
	private float left;
	private float right;
	private float top;
	private float bottom;

	public DrawView(Context context) {
		super(context);
	}

	public DrawView(Context context, float left, float top, float right,
			float bottom) {
		super(context);
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
	}

	@Override
	public void onDraw(Canvas canvas) {
		paint.setStrokeWidth(0);
		paint.setColor(Color.parseColor("#5F021F"));
		canvas.drawRect(this.left, this.top, this.right, this.right, paint);
	}
}
