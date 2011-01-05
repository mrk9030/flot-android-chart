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
package com.flotandroidchart.flot;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;

/**
 * AWT Control to draw Flot Chart on Java GUI or Applet. 
 * <p>
 * <b>Flot</b> is a pure Javascript plotting library for jQuery. 
 * It produces graphical plots of arbitrary datasets on-the-fly client-side.
 * <b>FlotChartContainer</b> is a java charting system based on flot
 * which could work on Java GUI&Applet.
 * </p>
 * 
 * 
 *
 */
public class FlotChartContainer extends Container {

	private static final long serialVersionUID = 1L;
	private FlotDraw _fd;
	FlotOverlay fol;
	public FlotChartContainer(FlotDraw fd) {
		_fd = fd;
		this.setLayout(new GridLayout(1, 1));
		fol = new FlotOverlay(_fd.getEventHolder());
		add(fol);
	}

	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		_fd.draw(g2, this.getWidth(), this.getHeight(), new Font(
				Font.SANS_SERIF, Font.PLAIN, 12));
		fol.paint(g);
	}

}
