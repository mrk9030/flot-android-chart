package com.flotandroidchart.flot.format;

import com.flotandroidchart.flot.data.AxisData;
import com.flotandroidchart.flot.data.TickData;

public interface DrawFormatter {
	public void draw(AxisData axis, TickData tick);
}
