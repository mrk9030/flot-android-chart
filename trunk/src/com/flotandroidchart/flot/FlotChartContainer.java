package com.flotandroidchart.flot;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.view.View;

/*
Copyright 2010 Kxu
Copyright 2010 TheChatrouletteGirls.Com.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/


/**
 * AWT Control to draw Flot Chart on Java GUI or Applet. 
 * <p>
 * <b>Flot</b> is a pure Javascript plotting library for jQuery. 
 * It produces graphical plots of arbitrary datasets on-the-fly client-side.
 * <b>FlotChartContainer</b> is a java charting system based on flot
 * which could work on Java GUI&Applet.
 * </p>
 * 
 * 
 *
 */
public class FlotChartContainer extends View {

	private static final long serialVersionUID = 1L;
	private FlotDraw _fd;
	private Rect mRect = new Rect();
	private Handler mHandler;

	public FlotChartContainer(Context context, FlotDraw fd) {
		super(context);
		_fd = fd;
		//FlotOverlay fol = new FlotOverlay(_fd.getEventHolder());
		//add(fol);
		this.mHandler = new Handler();
	}
	
	protected void onDraw(Canvas paramCanvas) {
		super.onDraw(paramCanvas);
		paramCanvas.getClipBounds(mRect);
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		paramCanvas.drawRect(mRect, paint);
		_fd.draw(paramCanvas, mRect.width(), mRect.height());
	}
}
