package com.flotandroidchart.flot.options;

public class Series {
	public SeriesPoints points = new SeriesPoints();
	public SeriesLines lines = new SeriesLines();
	public SeriesBars bars = new SeriesBars();
	public int shadowSize = 3;
	public int color = -1;

	public void defaultLinesShow() {
		if (!lines.showSet) {
			if (!points.show && !bars.show) {
				lines.setShow(true);
			}
		}
	}
}
