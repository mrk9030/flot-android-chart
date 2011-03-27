package com.flotandroidchart.flot.data;

public class MarkingData {
	
	public int color = 0;
	public int lineWidth = 0;
	public RangeData xaxis = new RangeData();
	public RangeData yaxis = new RangeData();
	
	public MarkingData(int color, int lineWidth, RangeData xaxis, RangeData yaxis) {
		this.color = color;
		this.lineWidth = lineWidth;
		if(xaxis != null) {
			this.xaxis = xaxis;
		}
		if(yaxis != null) {
			this.yaxis = yaxis;
		}
	}

}
