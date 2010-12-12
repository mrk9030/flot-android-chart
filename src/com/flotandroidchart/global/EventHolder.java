package com.flotandroidchart.global;

import java.util.Enumeration;
import java.util.Vector;

public class EventHolder {

	Vector<FlotEventListener> _listener = new Vector<FlotEventListener>();

	/**
	 * 
	 * @param flot
	 */
	public void addEventListener(FlotEventListener flot) {
		_listener.add(flot);
	}

	/**
	 * 
	 * @param flot
	 */
	public void removeEventListener(FlotEventListener flot) {
		_listener.remove(flot);
	}

	/**
	 * 
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
