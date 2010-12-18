package com.flotandroidchart.flot;


import com.flotandroidchart.global.FlotEvent;
import com.flotandroidchart.global.FlotEventListener;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
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
	
	public FlotChartContainer(Context context, AttributeSet as) {
		super(context, as);
		init(null);
	}
	
	
	public void setDrawData(FlotDraw fd) {
		init(fd);
		repaint();
	}
	
	public void init(FlotDraw fd) {
		this._fd = fd;
		if(_fd != null) {
			_fd.getEventHolder().addEventListener(new FlotEventListener(){

				@Override
				public String Name() {
					// TODO Auto-generated method stub
					return FlotEvent.CANVAS_REPAINT;
				}

				@Override
				public void execute(FlotEvent event) {
					// TODO Auto-generated method stub

					repaint();

				}
			});
		}
		
		this.mHandler = new Handler();

	}

	public FlotChartContainer(Context context, FlotDraw fd) {
		super(context);
		init(fd);
		/*
		this.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				if(!_redraw) {
				    _redraw = true;
				    invalidate();
				}
				//_fd.getEventHolder().dispatchEvent(FlotEvent.MOUSE_HOVER, new FlotEvent(arg0));
				return true;
			}
			
		})
		*/;
		
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
	
	public void repaint() {
		this.mHandler.post(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				invalidate();
			}
			
		});
	}
}
