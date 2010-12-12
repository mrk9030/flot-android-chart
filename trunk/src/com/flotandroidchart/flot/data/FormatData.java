package com.flotandroidchart.flot.data;

public class FormatData {
	public String xy;
	public Boolean number;
	public Boolean required;
	public Integer defaultValue;

	public FormatData(String _xy, Boolean _number, Boolean _required,
			Integer _default) {
		this.xy = _xy;
		this.number = _number;
		this.required = _required;
		this.defaultValue = _default;
	}
}
