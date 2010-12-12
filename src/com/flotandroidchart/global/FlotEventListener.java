package com.flotandroidchart.global;

import java.util.EventListener;

public interface FlotEventListener extends EventListener {
	public abstract void execute(FlotEvent event);

	public abstract String Name();
}
