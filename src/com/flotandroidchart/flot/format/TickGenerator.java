package com.flotandroidchart.flot.format;

import java.util.Vector;

import com.flotandroidchart.flot.data.AxisData;
import com.flotandroidchart.flot.data.TickData;

;

public class TickGenerator {
	public Vector<TickData> generator(AxisData axis) {
		Vector<TickData> ticks = new Vector<TickData>();

		double start = floorInBase(axis.min, axis.tickSize);
		int i = 0;
		double v = Double.NaN;
		double prev;

		do {
			prev = v;
			v = start + i * axis.tickSize;
			ticks.add(new TickData(v, (axis.tickFormatter == null ? String
					.valueOf(v) : axis.tickFormatter.formatNumber(v, axis))));
			++i;
		} while (v < axis.max && v != prev);
		return ticks;
	}

	protected double floorInBase(double n, double base) {
		return base * Math.floor(n / base);
	}
}
