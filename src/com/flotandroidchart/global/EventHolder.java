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

import java.util.Enumeration;
import java.util.Vector;

/**
 * A Event Handler to listen and dispatch flot charting event as ActionScript does.
 * <b>Example:</b>
 * <p>
 * EventHolder eventHolder = new EventHolder();
 * -------add event listener section-----------
 * eventHandler.addEventListener(new FlotEventListener() {
 * 
 * 			@Override
 *  		public String Name() {
 *  				// TODO Auto-generated method stub
 *  				return FlotEvent.MOUSE_HOVER;
 *  		}
 *  
 *  		@Override
 *  		public void execute(FlotEvent event) {
 *  			// TODO Auto-generated method stub
 *  			System.out.print("Trig Mouse Hover Event");
 *  		}
 * });
 * 
 * -------dispatch Flot Event---------
 * eventHolder.dispatchEvent(FlotEvent.MOUSE_HOVER, new FlotEvent(v));
 * </p>
 * @author thechatroulettegirls
 *
 */
public class EventHolder {

	private Vector<FlotEventListener> _listener = new Vector<FlotEventListener>();

	/**
	 * Function <b>addEventListener</b> to add listener event for processing event.
	 *
	 * @param flot
	 */
	public void addEventListener(FlotEventListener flot) {
		_listener.add(flot);
	}

	/**
	 * Function <b>removeEventListener</b> to remove event listener.
	 * @param flot
	 */
	public void removeEventListener(FlotEventListener flot) {
		_listener.remove(flot);
	}

	/**
	 * Function <b>dispatchEvent</b> to dispatch flot event. 
	 * @param name
	 * @param _event
	 */
	public void dispatchEvent(String name, FlotEvent _event) {
		Enumeration<FlotEventListener> en = _listener.elements();
		while (en.hasMoreElements()) {
			FlotEventListener fel = en.nextElement();
			if (fel.Name() == name) {
				fel.execute(_event);
			}
		}
	}
}
