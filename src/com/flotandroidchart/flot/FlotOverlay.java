package com.flotandroidchart.flot;
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

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import com.flotandroidchart.global.EventHolder;
import com.flotandroidchart.global.FlotEvent;
import com.flotandroidchart.global.FlotEventListener;


public class FlotOverlay extends Component implements Runnable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public EventHolder eventHolder;
	private boolean _repaint = false;
	public FlotOverlay(EventHolder evt) {
		eventHolder = evt;
		this.setVisible(true);
		eventHolder.addEventListener(new FlotEventListener(){

			@Override
			public String Name() {
				// TODO Auto-generated method stub
				return FlotEvent.CANVAS_REPAINT;
			}

			@Override
			public void execute(FlotEvent event) {
				// TODO Auto-generated method stub
				_repaint = true;
				//repaint();
			}
			
		});

		this.addMouseMotionListener(new MouseMotionListener(){

			@Override
			public void mouseDragged(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseMoved(MouseEvent arg0) {
				// TODO Auto-generated method stub
				eventHolder.dispatchEvent(FlotEvent.MOUSE_HOVER, new FlotEvent(arg0));
			}
			
		});
		
		this.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				eventHolder.dispatchEvent(FlotEvent.MOUSE_CLICK, new FlotEvent(arg0));
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		new Thread(this).start();
	}
	
	public void paint(Graphics g) {
		if(eventHolder != null) {
			Graphics2D grap = (Graphics2D)g;
			eventHolder.dispatchEvent("canvasOverlay", new FlotEvent(grap));
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(!Thread.currentThread().isInterrupted()) {
			try {
				Thread.sleep(800);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(_repaint) {
				repaint();
				_repaint = false;
			}
		}
	}
}
