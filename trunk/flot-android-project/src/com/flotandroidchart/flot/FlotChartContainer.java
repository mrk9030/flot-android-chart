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
import android.util.AttributeSet;
import android.widget.FrameLayout;


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
public class FlotChartContainer extends FrameLayout {

	private static final long serialVersionUID = 1L;
	private FlotDraw _fd;
	private FlotPlot mainCanvas;
	private FlotOverlay overlayCanvas;
	//private Handler mHandler;
	
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
		if(mainCanvas == null) {
			mainCanvas = new FlotPlot(getContext(), _fd);
			addView(mainCanvas, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		}
		if(overlayCanvas == null){
			overlayCanvas = new FlotOverlay(getContext(), _fd);
			addView(overlayCanvas, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		}
		if(_fd != null) {
			mainCanvas.setDrawData(_fd);
			overlayCanvas.setDrawData(_fd);
		}
		
		//this.mHandler = new Handler();
		
	}
}
