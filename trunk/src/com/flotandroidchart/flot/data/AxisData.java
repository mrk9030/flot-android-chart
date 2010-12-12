package com.flotandroidchart.flot.data;

import java.util.Vector;

import com.flotandroidchart.flot.format.DoubleFormatter;
import com.flotandroidchart.flot.format.TickFormatter;
import com.flotandroidchart.flot.format.TickGenerator;

public class AxisData {
	public double datamin = Double.NaN;
	public double datamax = Double.NaN;
	public Boolean used = false;
	public double labelHeight = -1;
	public double labelWidth = -1;
	public double max = Double.MAX_VALUE;
	public double min = Double.MIN_VALUE;
	public double scale = 1;
	public int tickDecimals = -1;
	public double tickSize = 0;
	public Vector<TickData> ticks = new Vector<TickData>();
	public TickFormatter tickFormatter = null;
	public TickGenerator tickGenerator = null;

	public DoubleFormatter p2c = null;
	public DoubleFormatter c2p = null;
}
