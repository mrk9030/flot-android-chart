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

import java.util.EventListener;

/**
 * FlotEventListener is a event listener to process flot message.
 * 
 * @author Administrator
 *
 */
public interface FlotEventListener extends EventListener {
	/**
	 * Main Function to process flot message with specified event.
	 * 
	 * @param event
	 */
	public abstract void execute(FlotEvent event);

	/**
	 * Find Name of Flot Event Listener in @see FlotEvent
	 *  
	 * @return String 
	 * 
	 */
	public abstract String Name();
}
