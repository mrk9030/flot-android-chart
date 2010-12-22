package com.flotandroidchart.flot.format;

import com.flotandroidchart.flot.data.SeriesData;

public interface StringFormatter {
	public String format(String str, SeriesData axis);

	public String formatNumber(double val, SeriesData axis);
}
