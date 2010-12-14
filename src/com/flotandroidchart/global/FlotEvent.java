package com.flotandroidchart.global;

import java.util.EventObject;

public class FlotEvent extends EventObject {

	public static final String FLOTEVENT_ADD = "mouse";
	
	
	public static final String HOOK_PROCESSOPTIONS = "processOptions";
	public static final String HOOK_PROCESSRAWDATA = "processRawData";
	public static final String HOOK_PROCESSDATAPOINTS = "processDatapoints";
	public static final String HOOK_DRAW = "draw";
	public static final String HOOK_BINDEVENTS = "bindEvents";
	public static final String HOOK_DRAWOVERLAY = "drawOverlay";

	public FlotEvent(Object source) {
		super(source);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
