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

package com.flotandroidchart.global;

import java.util.EventObject;

import android.view.MotionEvent;

import com.flotandroidchart.flot.data.RectOffset;

/**
 * A FlotEvent often is passed to FlotEventListener to process message input.
 * 
 * @author kxu
 *
 */
public class FlotEvent extends EventObject {
	
	/**
	 * HOOK_PROCESSOPTIONS to hook when processing 'process Flot options'.
	 * Source: 
	 * @see HookEventObject
	 *  
	 *  For hookParam in hooks:
	 *  @see HookEventObject#hookParam
	 *  
	 *  <table>
	 *  <tr>
	 *  <td>
	 *  hookParam[0]
	 *  </td>
	 *  <td>
	 *  @see Options
	 *  Flot Charting options
	 *  </td>
	 *  </tr>
	 *  </table>
	 *    
	 *  Add Listener Sample:
	 *  <code>
	 *  
	 *  FlotDraw Fd = new FlotDraw(....);
	 *  
	 *  Fd.addHookListener(new FlotEventListener(){

			@Override
			public String Name() {
				// TODO Auto-generated method stub
				return FlotEvent.HOOK_PROCESSOPTIONS;
			}

			@Override
			public void execute(FlotEvent event) {
				// TODO Auto-generated method stub
							
				if(event.getSource() instanceof HookEventObject) {
					HookEventObject heo = (HookEventObject)event.getSource();
					if(heo != null && heo.hookParam.length > 0) {
					    Options opt =  (Options)heo.hookParam[0];
					    
					    .......
					    
					}
				}
				
			}
			
		});
	 *  </code>
	 */
	public static final String HOOK_PROCESSOPTIONS = "processOptions";
	/**
	 * HOOK_PROCESSRAWDATA to hook when processing 'Series Raw Data'.
	 * Source: 
	 * @see HookEventObject
	 *  
	 *  For hookParam in hooks:
	 *  @see HookEventObject#hookParam
	 *  
	 *  <table>
	 *  <tr>
	 *  <td>
	 *  hookParam[0]
	 *  </td>
	 *  <td>
	 *  @see SeriesData
	 *  Current Series To Be Processed
	 *  </td>
	 *  </tr>
	 *  <tr>
	 *  <td>
	 *  hookParam[1]
	 *  </td>
	 *  <td>
	 *  @see double[][]
	 *  Current Series Data To Be Processed
	 *  </td>
	 *  </tr>
	 *  <tr>
	 *  <td>
	 *  hookParam[2]
	 *  </td>
	 *  <td>
	 *  @see Datapoints
	 *  Current Series Datapoints To Be Processed
	 *  </td>
	 *  </tr>
	 *  </table>  
	 *  
	 *  Add Listener Sample:
	 *  <code>
	 *  
	 *  FlotDraw Fd = new FlotDraw(....);
	 *  
	 *  Fd.addHookListener(new FlotEventListener(){

			@Override
			public String Name() {
				// TODO Auto-generated method stub
				return FlotEvent.HOOK_PROCESSRAWDATA;
			}

			@Override
			public void execute(FlotEvent event) {
				// TODO Auto-generated method stub
							
				if(event.getSource() instanceof HookEventObject) {
					HookEventObject heo = (HookEventObject)event.getSource();
					if(heo != null && heo.hookParam.length > 0) {
					    SeriesData sd =  (SeriesData)heo.hookParam[0];
					    double[][] data = (double[][])heo.hookParam[1];
					    Datapoints datapoints = (Datapoints)heo.hookParam[2];
					    
					    .......
					    
					}
				}
				
			}
			
		});
	 *  </code>
	 */
	public static final String HOOK_PROCESSRAWDATA = "processRawData";
	/**
	 * HOOK_PROCESSDATAPOINTS to hook when processing data points
	 * Source:
	 *  @see HookEventObject
	 *  
	 *  For hookParam in hooks:
	 *  @see HookEventObject#hookParam
	 *  
	 *  <table>
	 *  <tr>
	 *  <td>
	 *  hookParam[0]
	 *  </td>
	 *  <td>
	 *  @see SeriesData
	 *  Current Series to be processed
	 *  </td>
	 *  </tr>
	 *  <tr>
	 *  <td>
	 *  hookParam[1]
	 *  </td>
	 *  <td>
	 *  @see Datapoints
	 *  Current Series Datapoints to be processed
	 *  </td>
	 *  </tr>
	 *  </table>  
	 *  
	 *  Add Listener Sample:
	 *  <code>
	 *  
	 *  FlotDraw Fd = new FlotDraw(....);
	 *  
	 *  Fd.addHookListener(new FlotEventListener(){

			@Override
			public String Name() {
				// TODO Auto-generated method stub
				return FlotEvent.HOOK_PROCESSDATAPOINTS;
			}

			@Override
			public void execute(FlotEvent event) {
				// TODO Auto-generated method stub
							
				if(event.getSource() instanceof HookEventObject) {
					HookEventObject heo = (HookEventObject)event.getSource();
					if(heo != null && heo.hookParam.length > 0) {
					    SeriesData sd =  (SeriesData)heo.hookParam[0];
					    Datapoints datapoints = (Datapoints)heo.hookParam[1];
					    
					    .......
					    
					}
				}
				
			}
			
		});
	 *  </code>
	 */
	public static final String HOOK_PROCESSDATAPOINTS = "processDatapoints";
	/**
	 * HOOK_PROCESSDATAPOINTS to hook when drawing main Chart
	 * Source:
	 *  @see HookEventObject
	 *  
	 *  For hookParam in hooks:
	 *  @see HookEventObject#hookParam
	 *  
	 *  <table>
	 *  <tr>
	 *  <td>
	 *  hookParam[0]
	 *  </td>
	 *  <td>
	 *  @see Canvas
	 *  Main Canvas to be drawn
	 *  </td>
	 *  </tr>
	 *  </table>  
	 *  
	 *  Add Listener Sample:
	 *  <code>
	 *  
	 *  FlotDraw Fd = new FlotDraw(....);
	 *  
	 *  Fd.addHookListener(new FlotEventListener(){

			@Override
			public String Name() {
				// TODO Auto-generated method stub
				return FlotEvent.HOOK_DRAW;
			}

			@Override
			public void execute(FlotEvent event) {
				// TODO Auto-generated method stub
							
				if(event.getSource() instanceof HookEventObject) {
					HookEventObject heo = (HookEventObject)event.getSource();
					if(heo != null && heo.hookParam.length > 0) {
					    Canvas canvas =  (Canvas)heo.hookParam[0];
					    
					    .......
					    
					}
				}
				
			}
			
		});
	 *  </code>
	 */
	public static final String HOOK_DRAW = "draw";
	public static final String HOOK_BINDEVENTS = "bindEvents";
	/**
	 * HOOK_DRAWOVERLAY to hook when drawing overlay
	 * Source:	
	 *  @see HookEventObject
	 *  
	 *  For hookParam in hooks:
	 *  @see HookEventObject#hookParam
	 *  
	 *  <table>
	 *  <tr>
	 *  <td>
	 *  hookParam[0]
	 *  </td>
	 *  <td>
	 *  @see Canvas
	 *  Chart Canvas Overlay to be drawn
	 *  </td>
	 *  </tr>
	 *  </table>  
	 *  
	 *  Add Listener Sample:
	 *  <code>
	 *  
	 *  FlotDraw Fd = new FlotDraw(....);
	 *  
	 *  Fd.addHookListener(new FlotEventListener(){

			@Override
			public String Name() {
				// TODO Auto-generated method stub
				return FlotEvent.HOOK_DRAWOVERLAY;
			}

			@Override
			public void execute(FlotEvent event) {
				// TODO Auto-generated method stub
							
				if(event.getSource() instanceof HookEventObject) {
					HookEventObject heo = (HookEventObject)event.getSource();
					if(heo != null && heo.hookParam.length > 0) {
					    Canvas canvasOverlay =  (Canvas)heo.hookParam[0];
					    
					    .......
					    
					}
				}
				
			}
			
		});
	 *  </code>
	 */
	public static final String HOOK_DRAWOVERLAY = "drawOverlay";
	/**
	 * MOUSE_HOVER to hook when processing touch events
	 * Source:
	 *  @see MotionEvent
	 *    
	 *  
	 *  Add Listener Sample:
	 *  <code>
	 *  
	 *  FlotDraw Fd = new FlotDraw(....);
	 *  
	 *  Fd.getEventHolder().addEventListener(new FlotEventListener(){

			@Override
			public String Name() {
				// TODO Auto-generated method stub
				return FlotEvent.MOUSE_HOVER;
			}

			@Override
			public void execute(FlotEvent event) {
				// TODO Auto-generated method stub
							
				if(event.getSource() instanceof MotionEvent) {
					MotionEvent evt = (MotionEvent)event.getSource();
					if(evt != null) {
					    
					    .......
					    
					}
				}
				
			}
			
		});
	 *  </code>
	 */
	public static final String MOUSE_HOVER = "hover";
	/**
	 * MOUSE_CLICK to hook when processing click events
	 * Source:
	 *    Not Used In Android Version.
	 */
	public static final String MOUSE_CLICK = "click";
	/**
	 * CANVAS_REPAINT to hook when processing canvas overlay redraw events
	 * Source
	 *  @see FlotDraw
	 *  
	 *  Add Listener Sample:
	 *  <code>
	 *  
	 *  FlotDraw Fd = new FlotDraw(....);
	 *  
	 *  Fd.getEventHolder().addEventListener(new FlotEventListener(){

			@Override
			public String Name() {
				// TODO Auto-generated method stub
				return FlotEvent.CANVAS_REPAINT;
			}

			@Override
			public void execute(FlotEvent event) {
				// TODO Auto-generated method stub
							
				if(event.getSource() instanceof FlotDraw) {
					FlotDraw plot = (FlotDraw)event.getSource();
					if(plot != null) {
					    
					    .......
					    
					}
				}
				
			}
			
		});
	 *  </code> 
	 */
	public static final String CANVAS_REPAINT = "repaint";
	
	public FlotEvent(Object Source) {
		super(Source);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
