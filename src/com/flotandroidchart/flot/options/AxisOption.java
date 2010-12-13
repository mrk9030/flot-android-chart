package com.flotandroidchart.flot.options;

import com.flotandroidchart.flot.format.DoubleFormatter;
import com.flotandroidchart.flot.format.TickFormatter;

public class AxisOption {
	public String mode = null;
	public DoubleFormatter transform = null;
	public DoubleFormatter inverseTransform = null;
	public double min = Double.NaN;
	public double max = Double.NaN;
	public double autoscaleMargin = Double.NaN;

	public Object ticks = null;
	public TickFormatter tickFormatter = null;
	public int labelWidth = -1;
	public int labelHeight = -1;

	public int tickDecimals = -1;
	public double tickSize = Double.MIN_VALUE;
	public double minTickSize = Double.MIN_VALUE;
	public Object monthNames = null;
	public Object timeformat = null;
	public Boolean twelveHourClock = false;
}
