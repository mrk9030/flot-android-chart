package com.flotandroidchart.flot.data;

public class NearItemData {
	public double[] datapoint;
	public int dataIndex;
	public SeriesData series;
	public int seriesIndex;
	public int pageX;
	public int pageY;
	
	public NearItemData(double[] datapoint,
			             int dataIndex,
			             SeriesData series,
			             int seriesIndex) {
		this.datapoint = datapoint;
		this.dataIndex = dataIndex;
		this.series = series;
		this.seriesIndex = seriesIndex;
	}
	                    
}
