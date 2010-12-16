/*
Copyright 2010 Kxu
Copyright 2010 TheChatrouletteGirls.Com.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.flotandroidchart.global;

import java.util.EventObject;

public class FlotEvent extends EventObject {
	
	public static final String HOOK_PROCESSOPTIONS = "processOptions";
	public static final String HOOK_PROCESSRAWDATA = "processRawData";
	public static final String HOOK_PROCESSDATAPOINTS = "processDatapoints";
	public static final String HOOK_DRAW = "draw";
	public static final String HOOK_BINDEVENTS = "bindEvents";
	public static final String HOOK_DRAWOVERLAY = "drawOverlay";

	public static final String MOUSE_HOVER = "hover";
	public static final String MOUSE_CLICK = "click";
	
	public static final String CANVAS_REPAINT = "repaint";
	
	public FlotEvent(Object source) {
		super(source);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
