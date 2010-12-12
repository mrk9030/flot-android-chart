package com.flotandroidchart.flot.format;

import com.flotandroidchart.flot.data.AxisData;

public class TickFormatter {
	public double format(double value, AxisData axis) {
		return roundDouble(value, axis.tickDecimals);
	}

	public double roundDouble(double d, int c) {
		int temp = (int) ((d * Math.pow(10, c)));
		return (((double) temp) / Math.pow(10, c));
	}

	public String formatNumber(double val, AxisData axis) {
		if (axis.tickDecimals == 0) {
			return String.valueOf((int) val);
		} else {
			return String.format("%." + axis.tickDecimals + "f", val);
		}
		// return String.valueOf(roundDouble(val, axis.tickDecimals));
	}
}
