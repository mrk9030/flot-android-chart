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

import java.util.Hashtable;

import com.flotandroidchart.flot.options.*;

public class Options {

	public int[] colors = { 0xedc240, 0xafd8f8, 0xcb4b4b, 0x4da74d, 0x9440ed };

	public Grid grid = new Grid();
	public Hashtable<String, Object> hooks = new Hashtable<String, Object>();
	public Legend legend = new Legend();
	public Series series = new Series();
	public AxisOption x2axis = new AxisOption();
	public AxisOption xaxis = new AxisOption();
	public AxisOption y2axis = new AxisOption();
	public AxisOption yaxis = new AxisOption();
	public BackgroundCanvas canvas = new BackgroundCanvas();
	public int fps = 100;

	public Options() {
		yaxis.autoscaleMargin = 0.02;
		y2axis.autoscaleMargin = 0.02;
	}
}
