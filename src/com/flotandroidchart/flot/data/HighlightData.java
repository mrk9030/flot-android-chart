package com.flotandroidchart.flot.data;

public class HighlightData {
	public SeriesData series;
	public double[] point;
	public String auto;
	
	public HighlightData(SeriesData series, double[] point, String auto) {
		this.series = series;
		this.point = point;
		this.auto = auto;
	}
}
