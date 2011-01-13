package com.flotandroidchart.flot;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

public class FlotPlot extends View {

	private FlotDraw _fd;
	private Rect mRect = new Rect();
	
	public FlotPlot(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public FlotPlot(Context context, FlotDraw fd) {
		super(context);
		this._fd = fd;
	}
	
	public void setDrawData(FlotDraw fd) {
		this._fd = fd;
	}
	
	protected void onDraw(Canvas paramCanvas) {
		super.onDraw(paramCanvas);
		paramCanvas.getClipBounds(mRect);
		if(_fd != null) {
			paramCanvas.save();
			paramCanvas.translate(mRect.left, mRect.top);
		    _fd.draw(paramCanvas, mRect.width(), mRect.height());
		    paramCanvas.restore();
		}
	}
}
