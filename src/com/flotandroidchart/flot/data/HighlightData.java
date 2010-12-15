package com.flotandroidchart.flot.data;

public class HighlightData {
	public SeriesData series;
	public double[] point;
	public String auto;
	public int dataIndex;
	
	public HighlightData(SeriesData series, double[] point, String auto, int dataIndex) {
		this.series = series;
		this.point = point;
		this.auto = auto;
		this.dataIndex = dataIndex;
	}
}
