package com.flotandroidchart.flot.format;

import com.flotandroidchart.flot.data.SeriesData;

public interface TooltipFormatter {
	public String format(SeriesData series, int dataIndex);
}
