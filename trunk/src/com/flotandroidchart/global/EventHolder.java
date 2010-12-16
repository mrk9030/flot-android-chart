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
