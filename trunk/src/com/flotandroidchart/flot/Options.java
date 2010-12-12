package com.flotandroidchart.flot;

import java.util.Hashtable;

import com.flotandroidchart.flot.options.*;

public class Options {

	public int[] colors = { 0xedc240, 0xafd8f8, 0xcb4b4b, 0x4da74d, 0x9440ed };

	public Legend legend = new Legend();
	public AxisOption xaxis = new AxisOption();
	public AxisOption yaxis = new AxisOption();
	public AxisOption x2axis = new AxisOption();
	public AxisOption y2axis = new AxisOption();
	public Series series = new Series();
	public Grid grid = new Grid();
	public Hashtable<String, Object> hooks = new Hashtable<String, Object>();

	public Options() {
		yaxis.autoscaleMargin = 0.02;
		y2axis.autoscaleMargin = 0.02;
	}
}
