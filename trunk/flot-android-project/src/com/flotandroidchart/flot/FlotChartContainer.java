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
package com.flotandroidchart.flot;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.flotandroidchart.global.FlotEvent;
import com.flotandroidchart.timer.FPSTimer;


/**
 * Touable View to draw Flot Chart on Android. 
 * <p>
 * <b>Flot</b> is a pure Javascript plotting library for jQuery. 
 * It produces graphical plots of arbitrary datasets on-the-fly client-side.
 * <b>FlotChartContainer</b> is a java charting system based on flot
 * which could work on Android.
 * </p>
 * 
 * <b>Example:</b>
 * <p>
 * 
 * </p>
 *
 */
public class FlotChartContainer extends SurfaceView implements SurfaceHolder.Callback {

	private static final long serialVersionUID = 1L;
	private static final int INVALID_POINTER_ID = -1;
	private FlotDraw _fd;
	private DrawThread drawThread;
	
	private float mLastTouchX;
    private float mLastTouchY;
    private int mActivePointerId = INVALID_POINTER_ID;
    private boolean bMoved = false;
    
	/**
	 * Default FlotChartContainer Constructor to build FlotChartContainer
	 * in Layout xml files, and then setDrawData with FlotDraw.
	 * <b>Example:</b>
	 * 
	 * <p>
	 * FlotChartContainer tv = (FlotChartContainer)this.findViewById(R.id.flotdraw);
	 * if(tv != null) {
	 *      tv.setDrawData(<FlotDraw>);
	 * }
	 * </p>
	 * @param context
	 * @param as
	 */
	public FlotChartContainer(Context context, AttributeSet as) {
		super(context, as);
		init(null);
	}
	
	/**
	 * Customized FlotChartContainer Constructor to build FlotChartContainer
	 * for setContentView, addView to directly creat flot container.
	 * @param context
	 * @param fd
	 */
	public FlotChartContainer(Context context, FlotDraw fd) {
		super(context);
		init(fd);	
	}
	
	/**
	 * Reset FlotDraw Class
	 * @param fd
	 */
	public void setDrawData(FlotDraw fd) {
		init(fd);
	}
	
	/**
	 * Init View by FlotDraw
	 * @param fd
	 */
	
	public void init(FlotDraw fd) {
		this._fd = fd;
		
		if(drawThread == null) {
			SurfaceHolder holder = getHolder();
			holder.addCallback(this);
			
			drawThread = new DrawThread(holder, this.getContext(), this._fd);

			this.setOnTouchListener(new OnTouchListener(){

				@Override
				public boolean onTouch(View arg0, MotionEvent ev) {
					// TODO Auto-generated method stub
					
					final int action = ev.getAction();
					
					switch (action & MotionEvent.ACTION_MASK) {
					case MotionEvent.ACTION_DOWN:
						final float x = ev.getX();
						final float y = ev.getY();
						
						mLastTouchX = x;
			            mLastTouchY = y;
			            mActivePointerId = ev.getPointerId(0);
			            bMoved = false;
						break;
					case MotionEvent.ACTION_MOVE:
						final int pointerIndex = ev.findPointerIndex(mActivePointerId);
						final float x1 = ev.getX(pointerIndex);
						final float y1 = ev.getY(pointerIndex);
						
						mLastTouchX = x1;
						mLastTouchY = y1;
						break;
					case MotionEvent.ACTION_UP:
						mActivePointerId = INVALID_POINTER_ID;

						if(_fd != null && !bMoved) {
							//plot.getData().get(0).label = arg1.getX() + "-" + arg1.getY();
						    _fd.getEventHolder().dispatchEvent(FlotEvent.MOUSE_HOVER, new FlotEvent(ev));
						}
						break;
					case MotionEvent.ACTION_CANCEL:
						mActivePointerId = INVALID_POINTER_ID;
						break;
					case MotionEvent.ACTION_POINTER_UP:
						final int pointerIndex1 = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT; 
						final int pointerId = ev.getPointerId(pointerIndex1);
						
						if (pointerId == mActivePointerId) {
							Log.d("onTouch", "taged;");
							final int newPointerIndex = pointerIndex1 == 0 ? 1 : 0;
						}
						break;
					}
					return true;
				}
				
			});
		}
		else {
			drawThread.setDrawable(this._fd);
		}
		
		setFocusable(true);
		
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		drawThread.setRunning(true);
		drawThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		boolean retry = true;
		drawThread.setRunning(false); 
		while (retry) {
			try {
				drawThread.join();
				retry = false;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				Log.d("SurfaceDestroyed", e.getMessage());
			}
		}
	}
	
	class DrawThread extends Thread {
		
		private SurfaceHolder mSurfaceHolder;
		private boolean mRun = false;
		private FlotDraw _fd;
		FPSTimer timer;
		
		public DrawThread(SurfaceHolder holder, Context context, FlotDraw fd) {
			mSurfaceHolder = holder;
			_fd = fd;
		}
		
		public void setDrawable(FlotDraw fd) {
			_fd = fd;  
			if(_fd != null && _fd.getOptions() != null) {
				timer = new FPSTimer(_fd.getOptions().fps);
			}
			else {
				timer = new FPSTimer(60);
			}
		}
		
		public void setRunning(boolean b) {
			mRun = b;
		}
		
		public void run() {
			int fps = 0;  
			long cur = System.currentTimeMillis();  
			boolean isdraw = true;  
			if(_fd != null && _fd.getOptions() != null) {
				timer = new FPSTimer(_fd.getOptions().fps);
			}
			else {
				timer = new FPSTimer(60);
			}			
			
			while (mRun) {
				Canvas c = null;
				if (isdraw && _fd != null) { 
					try {
						c = mSurfaceHolder.lockCanvas(null);
						synchronized (mSurfaceHolder) {
							Log.d("Draw", "ss");
							doDraw(c);
						}
						fps++;
					} finally {
						if (c != null) {
							mSurfaceHolder.unlockCanvasAndPost(c);
						}
					}
				}
				isdraw = timer.elapsed();
				long now = System.currentTimeMillis(); 
				if (now - cur > 1000) {
					Log.d("KZK", "FPS=" + (fps * 1000 / ((double)now - cur)));
					fps = 0;
					cur = now; 
				}
			}
		}
		
		protected void doDraw(Canvas canvas) {
			Rect mRect = new Rect();
			canvas.getClipBounds(mRect);
			if(_fd != null) {
				canvas.save();
				canvas.translate(mRect.left, mRect.top);
			    _fd.draw(canvas, mRect.width(), mRect.height());
			    //_fd.getEventHolder().dispatchEvent("canvasOverlay", new FlotEvent(canvas));
			    canvas.restore();
			}
		}
	}
}
