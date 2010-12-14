package com.flotandroidchart.global;

import com.flotandroidchart.flot.FlotDraw;

public class HookEventObject {
	public FlotDraw fd;
	public Object[] hookParam;
	
	public HookEventObject(FlotDraw fd, Object[] hookParam) {
		this.fd = fd;
		this.hookParam = hookParam;
	}
}
