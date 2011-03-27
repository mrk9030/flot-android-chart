package com.flotandroidchart.flot.data;

public class RangeData {
	
	public double from = Double.NaN;
	public double to = Double.NaN;
	public AxisData axis = null;
	
	public RangeData() {
		
	}
	
	public RangeData(double from, double to) {
		this.from = from;
		this.to = to;
	}

}
