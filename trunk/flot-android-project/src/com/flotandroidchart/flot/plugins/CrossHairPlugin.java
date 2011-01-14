package com.flotandroidchart.flot.plugins;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.view.MotionEvent;

import com.flotandroidchart.flot.FlotDraw;
import com.flotandroidchart.flot.IPlugin;
import com.flotandroidchart.flot.data.RectOffset;
import com.flotandroidchart.flot.options.Axies;
import com.flotandroidchart.global.FlotEvent;
import com.flotandroidchart.global.FlotEventListener;
import com.flotandroidchart.global.HookEventObject;

public class CrossHairPlugin implements IPlugin {
	
	public String mode = null;
	public int color = 0xCCAA0000;
	public int lineWidth = 1;
	
	public CrossHairPlugin(){
		this(null, 0xCCAA0000, 1);
	}
	
	public CrossHairPlugin(String mode, int rgba, int width){
		this.mode = mode;
		this.color = rgba;
		this.lineWidth = width;
	}

	CrossHair crosshair = null;
	FlotDraw plot = null;
	@Override
	public void init(FlotDraw fd1) {
		// TODO Auto-generated method stub
		crosshair = new CrossHair();
		this.plot = fd1;
		
		plot.getEventHolder().addEventListener(new FlotEventListener(){

			@Override
			public String Name() {
				// TODO Auto-generated method stub
				return FlotEvent.MOUSE_HOVER;
			}

			@Override
			public void execute(FlotEvent event) {
				// TODO Auto-generated method stub
				
				if (crosshair.locked)
					return;
				
				if(event.getSource() instanceof MotionEvent) {
					MotionEvent evt = (MotionEvent)event.getSource();
					if(evt != null){
						RectOffset offset = plot.getPlotOffset();
						crosshair.x = (int) Math.max(0, Math.min(evt.getX() - offset.left, plot.width()));
						crosshair.y = (int) Math.max(0, Math.min(evt.getY() - offset.top, plot.height()));
						plot.redraw();
					}
				}
				
			}
			
		});
		
		plot.addHookListener(new FlotEventListener(){

			@Override
			public String Name() {
				// TODO Auto-generated method stub
				return FlotEvent.HOOK_DRAWOVERLAY;
			}

			@Override
			public void execute(FlotEvent event) {
				// TODO Auto-generated method stub
				if(mode == null) {
					return;
				}
				if(event.getSource() instanceof HookEventObject) {
					HookEventObject heo = (HookEventObject)event.getSource();
					if(heo != null && heo.hookParam.length > 0) {
						Canvas grap = (Canvas) heo.hookParam[0];
						if(grap != null){
							RectOffset rect = plot.getPlotOffset();
							grap.save();
							
							grap.translate(rect.left, rect.top);
							
							if(crosshair.x != -1) {
								Paint p = new Paint();
								p.setColor(color);
								p.setStyle(Style.STROKE);
								p.setStrokeWidth(lineWidth);
								
								if(mode.indexOf('x') != -1) {
									grap.drawLine(crosshair.x, 0, crosshair.x, plot.height(), p);
								}
								
								if(mode.indexOf('y') != -1) {
									grap.drawLine(0, crosshair.y, plot.width(), crosshair.y, p);
								}
							}
							
							grap.restore();
						}
					}
				}
			}
			
		});
	}
	
	public void setCrosshair(Pos pos) {
		if(pos == null) {
			crosshair.x = -1;
		}
		else {
			Axies axes = plot.getAxes();
			
			crosshair.x = (int) Math.max(0, Math.min(pos.x != Integer.MIN_VALUE ? axes.xaxis.p2c.format(pos.x) : axes.x2axis.p2c.format(pos.x2), plot.width()));
			crosshair.y = (int) Math.max(0, Math.min(pos.y != Integer.MIN_VALUE ? axes.yaxis.p2c.format(pos.y) : axes.y2axis.p2c.format(pos.y2), plot.height()));
		}
		
		plot.redraw();
	}
	
	public void clearCrosshair() {
		setCrosshair(null);
	}
	
	public void lockCrosshair(Pos pos) {
		if(pos != null) {
			setCrosshair(pos);
		}
		crosshair.locked = true;
	}
	
	public void unlockCrosshair() {
		crosshair.locked = false;
	}

}

class CrossHair {
	public int x = -1;
	public int y = -1;
	public boolean locked = false;
}

class Pos {
	public int x = Integer.MIN_VALUE;
	public int y = Integer.MIN_VALUE;
	public int x2 = Integer.MIN_VALUE;
	public int y2 = Integer.MIN_VALUE;
}