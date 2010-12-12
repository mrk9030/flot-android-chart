package com.flotandroidchart.flot.options;

public class SeriesLines {
	private Boolean _show = false;
	public Boolean showSet = false;
	public int lineWidth = 2;
	public Boolean fill = false;
	public int fillColor = 0xff0000;
	public Boolean steps = false;

	public void setShow(Boolean _s) {
		this.showSet = true;
		_show = _s;
	}

	public Boolean getShow() {
		return _show;
	}
}