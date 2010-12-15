package com.flotandroidchart.flot.options;

import com.flotandroidchart.flot.format.TooltipFormatter;

public class Grid {
	public Boolean show = true;
	public Boolean aboveData = false;
	public int color = 0x545454;
	public Object backgroundColor = null;
	public int tickColor = 0x26000000;
	public int labelMargin = 5;
	public int borderWidth = 2;
	public int borderColor = 0x545454;
	public Object markings = null;
	public String markingsColor = "#f4f4f4";
	public int markingsLineWidth = 2;
	public Boolean clickable = false;
	public Boolean hoverable = false;
	public Boolean autoHighlight = true;
	public int mouseActiveRadius = 10;
	public int tooltipColor = 0x545454;
	public Object tooltipFillColor = new Integer(0xffff67);
	public TooltipFormatter tooltipFormatter = null;
}
