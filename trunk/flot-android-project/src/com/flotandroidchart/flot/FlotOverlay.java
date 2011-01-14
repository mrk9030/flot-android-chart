package com.flotandroidchart.flot;

import com.flotandroidchart.global.FlotEvent;
import com.flotandroidchart.global.FlotEventListener;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

public class FlotOverlay extends View implements Runnable {
	
	private FlotDraw plot;
	private boolean _repaint = false;

	public FlotOverlay(Context context, FlotDraw fd) {
		super(context);
		
		setFocusable(true);
		setFocusableInTouchMode(true);
		
		if(fd != null) {	
			setDrawData(fd);	
		}
		this.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				if(plot != null) {
					//plot.getData().get(0).label = arg1.getX() + "-" + arg1.getY();
				    plot.getEventHolder().dispatchEvent(FlotEvent.MOUSE_HOVER, new FlotEvent(arg1));
				}
				return true;
			}
			
		});

		new Thread(this).start();
		// TODO Auto-generated constructor stub
	}
	
	public void setDrawData(FlotDraw fd) {
		if(fd != plot) {
			plot = fd;
			if(plot != null) {				
				plot.getEventHolder().addEventListener(new FlotEventListener(){

					@Override
					public String Name() {
						// TODO Auto-generated method stub
						return FlotEvent.CANVAS_REPAINT;
					}

					@Override
					public void execute(FlotEvent event) {
						// TODO Auto-generated method stub

						//repaint();
						_repaint = true;
					}
				});
				_repaint = true;
			}			
		}
	}
	
	protected void onDraw(Canvas paramCanvas) {
		super.onDraw(paramCanvas);
		if(plot != null) {
			plot.getEventHolder().dispatchEvent("canvasOverlay", new FlotEvent(paramCanvas));	
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(!Thread.currentThread().isInterrupted()) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(_repaint) {
				postInvalidate();
				_repaint = false;
			}
		}
	}
}
