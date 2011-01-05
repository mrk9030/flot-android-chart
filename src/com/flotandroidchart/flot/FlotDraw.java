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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.TimeZone;
import java.util.Vector;

import com.flotandroidchart.flot.data.AxisData;
import com.flotandroidchart.flot.data.Datapoints;
import com.flotandroidchart.flot.data.FormatData;
import com.flotandroidchart.flot.data.HighlightData;
import com.flotandroidchart.flot.data.NearItemData;
import com.flotandroidchart.flot.data.RectOffset;
import com.flotandroidchart.flot.data.SeriesData;
import com.flotandroidchart.flot.data.SpecData;
import com.flotandroidchart.flot.data.TickData;
import com.flotandroidchart.flot.format.DoubleFormatter;
import com.flotandroidchart.flot.format.DrawFormatter;
import com.flotandroidchart.flot.format.TickFormatter;
import com.flotandroidchart.flot.format.TickGenerator;
import com.flotandroidchart.flot.options.Axies;
import com.flotandroidchart.flot.options.AxisOption;
import com.flotandroidchart.flot.options.ColorHelper;
import com.flotandroidchart.flot.options.Grid;
import com.flotandroidchart.global.EventHolder;
import com.flotandroidchart.global.FlotEvent;
import com.flotandroidchart.global.FlotEventListener;
import com.flotandroidchart.global.HookEventObject;

public class FlotDraw implements Serializable {

	private static final long serialVersionUID = 1L;

	private Axies axes;

	private int canvasHeight;

	private int canvasWidth;

	Font defaultFont = null;

	private EventHolder eventHolder;

	Graphics2D grap = null;
	Graphics2D grapOverlay = null;

	Vector<HighlightData> highlights = new Vector<HighlightData>();
	private EventHolder hookHolder;
	private Options options;
	private int plotHeight;

	private RectOffset plotOffset = new RectOffset();

	private int plotWidth;

	private boolean redrawing = true;

	private Vector<SeriesData> series;

	Vector<SpecData> spec = new Vector<SpecData>();

	Hashtable<String, Double> timeUnitSize = new Hashtable<String, Double>();

	public FlotDraw(Object _canvas, Vector<SeriesData> _data, Options _options,
			Object _plugins) {
		series = _data;
		if (_options == null) {
			options = new Options();
		} else {
			options = _options;
		}
		axes = new Axies();	
		axes.xaxis = new AxisData();
		axes.yaxis = new AxisData();
		axes.x2axis = new AxisData();
		axes.y2axis = new AxisData();
		hookHolder = new EventHolder();
		eventHolder = new EventHolder();
		eventHolder.addEventListener(new FlotEventListener() {

			@Override
			public void execute(FlotEvent event) {
				// TODO Auto-generated method stub
				if (redrawing) {
					return;
				}
				if (event.getSource() instanceof MouseEvent) {
					MouseEvent evt = (MouseEvent) event.getSource();
					if (evt != null) {
						executeHighlight(Name(), evt, options.grid.hoverable);
					}
				}
			}

			@Override
			public String Name() {
				// TODO Auto-generated method stub
				return FlotEvent.MOUSE_HOVER;
			}

		});
		eventHolder.addEventListener(new FlotEventListener() {

			@Override
			public void execute(FlotEvent event) {
				// TODO Auto-generated method stub
				if (redrawing) {
					return;
				}
				if (event.getSource() instanceof MouseEvent) {
					MouseEvent evt = (MouseEvent) event.getSource();
					if (evt != null) {
						executeHighlight(Name(), evt, options.grid.clickable);
					}
				}
			}

			@Override
			public String Name() {
				// TODO Auto-generated method stub
				return FlotEvent.MOUSE_CLICK;
			}

		});
		
		eventHolder.addEventListener(new FlotEventListener(){

			@Override
			public String Name() {
				// TODO Auto-generated method stub
				return "canvasOverlay";
			}

			@Override
			public void execute(FlotEvent event) {
				// TODO Auto-generated method stub
				Object src = event.getSource();
				if(src instanceof Graphics2D) {
					drawOverlay((Graphics2D)src);
				}
			}
			
		});

		// canvasWidth = 320;
		parseOptions(options);
		setData(series);
		// setupGrid();
	}

	public void addHookListener(FlotEventListener flot) {
		hookHolder.addEventListener(flot);
	}

	private void addLabels(AxisData axis, DrawFormatter df) {
		for (int i = 0; i < axis.ticks.size(); i++) {
			TickData tick = axis.ticks.get(i);
			if (tick.label == null || tick.v < axis.min || tick.v > axis.max) {
				continue;
			}
			df.draw(axis, tick);
		}
	}

	@SuppressWarnings("finally")
	public AxisData axisSpecToRealAxis(Axies ax, String attr) {
		Class<?> c = ax.getClass();
		AxisData av = null;
		try {
			Field field = c.getDeclaredField(attr);
			av = (AxisData) field.get(ax);
			if (av == null) {
				c = axes.getClass();
				field = c.getDeclaredField(attr);
				av = (AxisData) field.get(axes);
				return av;
			}
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			return av;
		}
	}

	private void draw() {
		Grid grid = options.grid;

		if (grid.show && !grid.aboveData) {
			drawGrid();
		}

		for (int i = 0; i < series.size(); i++) {
			drawSeries(series.get(i));
		}

		hookHolder.dispatchEvent(FlotEvent.HOOK_DRAW, new FlotEvent(
				new HookEventObject(this, new Object[] { grap })));

		if (grid.show && grid.aboveData) {
			drawGrid();
		}

		if (options.grid.show) {
			insertLabels();
		}

		insertLegend();
	}

	public void draw(Graphics2D g, int width, int height, Font font) {
		redrawing = true;
		grap = g;
		canvasWidth = width;
		canvasHeight = height;
		defaultFont = font;
		setupGrid();
		if(options.canvas.fill) {
			Paint p = this.getFillStyle(true, options.canvas.fillColor, 0xffffffff, canvasHeight, 0);
			if(p != null) {
				grap.setPaint(p);
				grap.fillRect(0, 0, canvasWidth, canvasHeight);
			}
		}
		draw();
		//drawOverlay();
		grap = null;
		redrawing = false;
	}

	private void drawBar(double x, double y, double b, double barLeft,
			double barRight, double offset, AxisData axisx, AxisData axisy,
			SeriesData currentSeries, Graphics2D overlay) {
		double left, right, bottom, top, tmp;
		boolean drawLeft, drawRight, drawBottom, drawTop;

		if (currentSeries.series.bars.horizontal) {
			drawBottom = drawRight = drawTop = true;
			drawLeft = false;
			left = b;
			right = x;
			top = y + barLeft;
			bottom = y + barRight;

			// account for negative bars
			if (right < left) {
				tmp = right;
				right = left;
				left = tmp;
				drawLeft = true;
				drawRight = false;
			}
		} else {

			drawLeft = drawRight = drawTop = true;
			drawBottom = false;
			left = x + barLeft;
			right = x + barRight;
			bottom = b;
			top = y;

			// account for negative bars
			if (top < bottom) {
				tmp = top;
				top = bottom;
				bottom = tmp;
				drawBottom = true;
				drawTop = false;
			}
		}

		// clip
		if (right < axisx.min || left > axisx.max || top < axisy.min
				|| bottom > axisy.max)
			return;

		if (left < axisx.min) {
			left = axisx.min;
			drawLeft = false;
		}

		if (right > axisx.max) {
			right = axisx.max;
			drawRight = false;
		}

		if (bottom < axisy.min) {
			bottom = axisy.min;
			drawBottom = false;
		}

		if (top > axisy.max) {
			top = axisy.max;
			drawTop = false;
		}

		left = axisx.p2c.format(left);
		bottom = axisy.p2c.format(bottom);
		right = axisx.p2c.format(right);
		top = axisy.p2c.format(top);

		GeneralPath c = new GeneralPath();
		Paint p = this.getFillStyle(currentSeries.series.bars.fill,
				currentSeries.series.bars.fillColor,
				currentSeries.series.color, bottom, top);
		if (currentSeries.series.bars.fill) {
			overlay.setPaint(p);
			c.moveTo(left, bottom);
			c.lineTo(left, top);
			c.lineTo(right, top);
			c.lineTo(right, bottom);
			overlay.fill(c);

			c.reset();
		}

		// draw outline
		if (drawLeft || drawRight || drawTop || drawBottom) {
			BasicStroke bstroke = new BasicStroke(
					currentSeries.series.bars.barWidth, BasicStroke.CAP_BUTT,
					BasicStroke.CAP_ROUND);
			overlay.setStroke(bstroke);
			overlay.setColor(getTranColor(currentSeries.series.color));

			// FIXME: inline moveTo is buggy with excanvas
			c.moveTo(left, bottom + offset);
			if (drawLeft)
				c.lineTo(left, top + offset);
			else
				c.moveTo(left, top + offset);
			if (drawTop)
				c.lineTo(right, top + offset);
			else
				c.moveTo(right, top + offset);
			if (drawRight)
				c.lineTo(right, bottom + offset);
			else
				c.moveTo(right, bottom + offset);
			if (drawBottom)
				c.lineTo(left, bottom + offset);
			else
				c.moveTo(left, bottom + offset);
			c.closePath();
			overlay.draw(c);
		}
	}

	private void drawBarHighlight(SeriesData series, double[] point, Graphics2D overlay) {
		int color = series.series.color;
		series.series.color = (series.series.color | 0x80000000);
		double barLeft = series.series.bars.align.equals("left") ? 0
				: -series.series.bars.barWidth / 2;
		drawBar(point[0], point[1], 0, barLeft, barLeft
				+ series.series.bars.barWidth, 0, series.axes.xaxis,
				series.axes.yaxis, series, overlay);
		series.series.color = color;
	}

	private void drawCenteredString(String str, int top, int left, int width,
			int height, Graphics2D grap) {
		FontMetrics fm = grap.getFontMetrics();
		int x = left + (width - fm.stringWidth(str)) / 2;
		int y = top
				+ (fm.getAscent() + (height - (fm.getAscent() + fm.getDescent())) / 2)
				- 2;
		//Fix Xaxis Label out of canvas
		if(x + fm.stringWidth(str) > canvasWidth) {
			x = canvasWidth - fm.stringWidth(str);
		}
		grap.drawString(str, x, y);
	}

	private void drawGrid() {
		AffineTransform old = grap.getTransform();

		grap.translate(plotOffset.left, plotOffset.top);

		if (options.grid.backgroundColor != null) {
			grap.setPaint(getColorOrGradient(options.grid.backgroundColor,
					plotHeight, 0, 0xffffff));
			grap.fillRect(0, 0, plotWidth, plotHeight);
		}

		grap.setColor(getTranColor(options.grid.tickColor));
		grap.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT,
				BasicStroke.CAP_ROUND));
		GeneralPath shape = new GeneralPath();
		AxisData axis = axes.xaxis;
		if (axis.used) {
			for (int i = 0; i < axis.ticks.size(); i++) {
				double v = axis.ticks.get(i).v;
				if (v < axis.datamin || v > axis.datamax) {
					continue;
				}
				double dx = Math.floor(axis.p2c.format(v)) + .5;
				if(dx >= 0 && dx <= plotWidth) {
				    shape.moveTo(dx, 0);
				    shape.lineTo(dx, plotHeight);
				}
			}
		}

		axis = axes.yaxis;
		if (axis.used) {
			for (int i = 0; i < axis.ticks.size(); i++) {
				double v = axis.ticks.get(i).v;
				if (v < axis.datamin || v > axis.datamax) {
					continue;
				}
				double dy = Math.floor(axis.p2c.format(v)) + .5;
				if(dy >= 0 && dy <= plotHeight) {
				    shape.moveTo(0, dy);
				    shape.lineTo(plotWidth, dy);
				}
			}
		}

		axis = axes.x2axis;
		if (axis.used) {
			for (int i = 0; i < axis.ticks.size(); i++) {
				double v = axis.ticks.get(i).v;
				if (v < axis.datamin || v > axis.datamax) {
					continue;
				}
				shape.moveTo(Math.floor(axis.p2c.format(v)) + .5, -5);
				shape.lineTo(Math.floor(axis.p2c.format(v)) + .5, 5);
			}
		}

		axis = axes.y2axis;
		if (axis.used) {
			for (int i = 0; i < axis.ticks.size(); i++) {
				double v = axis.ticks.get(i).v;
				if (v < axis.datamin || v > axis.datamax) {
					continue;
				}
				shape
						.moveTo(plotWidth - 5,
								Math.floor(axis.p2c.format(v)) + .5);
				shape
						.lineTo(plotWidth + 5,
								Math.floor(axis.p2c.format(v)) + .5);
				// grap.drawLine(plotWidth - 5,
				// (int)(Math.floor(axis.p2c.format(v)) + .5), plotWidth+5,
				// (int)(Math.floor(axis.p2c.format(v)) + .5));
			}
		}

		grap.draw(shape);

		grap.setColor(getTranColor(options.grid.borderColor));
		grap.setStroke(new BasicStroke(options.grid.borderWidth,
				BasicStroke.CAP_BUTT, BasicStroke.CAP_ROUND));
		if (options.grid.borderWidth > 0) {
			int bw = options.grid.borderWidth;
			grap.drawRect(-bw / 2, -bw / 2, plotWidth + bw, plotHeight + bw);
		}

		grap.setTransform(old);
	}

	public void drawOverlay(Graphics2D overlay) {
		
		grapOverlay = overlay;
		
		AffineTransform old = grapOverlay.getTransform();

		grapOverlay.translate(plotOffset.left, plotOffset.top);
		for (int i = 0; i < highlights.size(); i++) {
			HighlightData hi = highlights.get(i);

			SeriesData s = hi.series;
			AxisData axisx = s.axes.xaxis;
			AxisData axisy = s.axes.yaxis;
			double x = axisx.p2c.format(hi.point[0]);
			double y = axisy.p2c.format(hi.point[1]);
			if (hi.series.series.bars.show) {
				// x = x + (hi.series.series.bars.align.equals("left") ?
				// (hi.series.series.bars.barWidth / 2) : 0);
				y = axisy.p2c.format(hi.point[1] / 2);
				drawBarHighlight(hi.series, hi.point, grapOverlay);
			} else {
				drawPointHighlight(hi.series, hi.point, grapOverlay);
			}

			boolean drawLeft = (x > (plotWidth / 2));
			boolean drawTop = (y > (plotHeight / 2));

			String strX = String.format("%.2f", hi.point[0]);
			String strY = String.format("%.2f", hi.point[1]);

			String tooltip = options.grid.tooltipFormatter != null ? options.grid.tooltipFormatter
					.format(s, hi.dataIndex)
					: "(" + strX + "," + strY + ")";
			FontMetrics fm = grapOverlay.getFontMetrics();
			int strWidth = fm.stringWidth(tooltip);
			int strHeight = fm.getHeight();

			GeneralPath gp = new GeneralPath();

			double startX = x + (drawLeft ? -1 : 1) * (strWidth + 30);
			double startY = y + (drawTop ? -1 : 1) * (strHeight + 20);

			gp.moveTo(startX, startY);
			double endX = startX + (drawLeft ? 1 : -1) * (strWidth + 20);
			gp.lineTo(endX, startY);
			double endY = startY + (drawTop ? 1 : -1) * (strHeight + 10);
			gp.lineTo(endX, endY);
			double middleX = endX + (drawLeft ? -1 : 1) * 10;
			gp.lineTo(middleX, endY);
			gp.lineTo(x, y);
			double middleX1 = startX + (drawLeft ? 1 : -1) * 10;
			gp.lineTo(middleX1, endY);
			gp.lineTo(startX, endY);
			gp.closePath();

			Paint p = this.getFillStyle(true, options.grid.tooltipFillColor,
					0xffff67, strHeight + 40, 0);
			if (p != null) {
				grapOverlay.setPaint(p);
				grapOverlay.fill(gp);
			}
			grapOverlay.setStroke(new BasicStroke(1));
			grapOverlay.setColor(new Color(options.grid.tooltipColor));
			grapOverlay.draw(gp);

			grapOverlay.setColor(new Color(0x0));
			startX = startX + (drawLeft ? 0 : -1) * (strWidth + 20);
			startY = startY + (drawTop ? 0 : -1) * (strHeight + 10);
			drawCenteredString(tooltip, (int) startY, (int) startX,
					strWidth + 20, strHeight + 10, grapOverlay);
		}
		grapOverlay.setTransform(old);
	}

	private void drawPointHighlight(SeriesData series, double[] point, Graphics2D overlay) {
		if (series == null || point == null || point.length < 2) {
			return;
		}

		double x = point[0];
		double y = point[1];
		AxisData axisx = series.axes.xaxis;
		AxisData axisy = series.axes.yaxis;

		if (x < axisx.min || x > axisx.max || y < axisy.min || y > axisy.max) {
			return;
		}
		int pointRadius = series.series.points.radius
				+ series.series.points.lineWidth / 2;
		overlay.setStroke(new BasicStroke(pointRadius));
		overlay.setColor(new Color(series.series.color | 0x80000000, true));
		double radius = 1.5 * pointRadius;
		overlay.drawArc((int) (axisx.p2c.format(x) - radius), (int) (axisy.p2c
				.format(y) - radius), (int) (2 * radius), (int) (2 * radius),
				0, 360);

	}

	private void drawRightString(String str, int top, int left, int width,
			int height) {
		FontMetrics fm = grap.getFontMetrics();
		int x = left + (width - fm.stringWidth(str));
		int y = top
				+ (fm.getAscent() + (height - (fm.getAscent() + fm.getDescent())) / 2);
		grap.drawString(str, x, y);
	}

	private void drawSeries(SeriesData currentSeries) {
		if (currentSeries.series.lines.getShow()) {
			drawSeriesLines(currentSeries);
		}

		if (currentSeries.series.bars.show) {
			drawSeriesBars(currentSeries);
		}

		if (currentSeries.series.points.show) {
			drawSeriesPoints(currentSeries);
		}
	}

	private void drawSeriesBars(SeriesData currentSeries) {
		AffineTransform old = grap.getTransform();

		grap.translate(plotOffset.left, plotOffset.top);

		double barLeft = currentSeries.series.bars.align.equals("left") ? 0
				: -currentSeries.series.bars.barWidth / 2;
		plotBars(currentSeries, currentSeries.datapoints, barLeft, barLeft
				+ currentSeries.series.bars.barWidth, 0,
				currentSeries.axes.xaxis, currentSeries.axes.yaxis);
		grap.setTransform(old);
	}

	private void drawSeriesLines(SeriesData currentSeries) {
		AffineTransform old = grap.getTransform();

		grap.translate(plotOffset.left, plotOffset.top);
		BasicStroke bstroke = new BasicStroke(
				currentSeries.series.lines.lineWidth, BasicStroke.CAP_BUTT,
				BasicStroke.CAP_ROUND);
		int lw = currentSeries.series.lines.lineWidth;
		int sw = currentSeries.series.shadowSize;
		if (lw > 0 && sw > 0) {
			grap.setColor(new Color(0x19000000, true));
			grap.setStroke(bstroke);
			double angle = Math.PI / 18;
			plotLine(currentSeries.datapoints, Math.sin(angle)
					* (lw / 2 + sw / 2), Math.cos(angle) * (lw / 2 + sw / 2),
					currentSeries.axes.xaxis, currentSeries.axes.yaxis);

			new BasicStroke(currentSeries.series.lines.lineWidth / 2,
					BasicStroke.CAP_BUTT, BasicStroke.CAP_ROUND);
			plotLine(currentSeries.datapoints, Math.sin(angle)
					* (lw / 2 + sw / 4), Math.cos(angle) * (lw / 2 + sw / 4),
					currentSeries.axes.xaxis, currentSeries.axes.yaxis);

		}

		grap.setStroke(bstroke);
		Paint p = this.getFillStyle(options.series.lines.fill,
				options.series.lines.fillColor, currentSeries.series.color, 0,
				plotHeight);
		if (options.series.lines.fill && p != null) {
			grap.setPaint(p);
			plotLineArea(currentSeries.datapoints, currentSeries.axes.xaxis,
					currentSeries.axes.yaxis);
		}

		if (lw > 0) {
			grap.setColor(getTranColor(currentSeries.series.color));
			plotLine(currentSeries.datapoints, 0, 0, currentSeries.axes.xaxis,
					currentSeries.axes.yaxis);
		}
		grap.setTransform(old);
	}

	private void drawSeriesPoints(SeriesData currentSeries) {
		AffineTransform old = grap.getTransform();

		grap.translate(plotOffset.left, plotOffset.top);

		int lw = currentSeries.series.lines.lineWidth;
		int sw = currentSeries.series.shadowSize;
		int radius = currentSeries.series.points.radius;

		if (lw > 0 && sw > 0) {
			int w = sw / 2;
			grap.setStroke(new BasicStroke(w, BasicStroke.CAP_BUTT,
					BasicStroke.CAP_ROUND));
			grap.setColor(new Color(0x19000000, true));
			plotPoints(false, currentSeries, currentSeries.datapoints, radius,
					w + w / 2.0, 180, currentSeries.axes.xaxis,
					currentSeries.axes.yaxis);

			grap.setColor(new Color(0x32000000, true));
			plotPoints(false, currentSeries, currentSeries.datapoints, radius,
					w / 2.0, 180, currentSeries.axes.xaxis,
					currentSeries.axes.yaxis);
		}

		grap.setStroke(new BasicStroke(lw, BasicStroke.CAP_BUTT,
				BasicStroke.CAP_ROUND));
		grap.setColor(getTranColor(currentSeries.series.color));
		plotPoints(true, currentSeries, currentSeries.datapoints, radius, 0,
				360, currentSeries.axes.xaxis, currentSeries.axes.yaxis);

		grap.setTransform(old);
	}

	private boolean equals(double p0, double p1) {
		return p0 == p1 ? true : Math.abs(p0 - p1) < 0.00001;
	}

	private void executeHighlight(String name, MouseEvent evt, boolean hover) {

		double canvasX = evt.getX() - plotOffset.left;
		double canvasY = evt.getY() - plotOffset.top;

		NearItemData item = findNearbyItem(canvasX, canvasY, hover);

		if (options.grid.autoHighlight) {
			boolean _redraw = false;
			for (int i = 0; i < highlights.size(); i++) {
				HighlightData h = highlights.get(i);
				if (h.auto.equals(name)
						&& !(item != null && h.series == item.series && h.point == item.datapoint)) {
					unhighlight(h.series, h.point);
					_redraw = true;
				}
			}

			if (item != null) {
				highlight(item.series, item.datapoint, name, item.dataIndex);
				_redraw = true;
			}

			if (_redraw) {
				// finished = true;
				redraw();
			}
		}

	}

	public void fillInSeriesOptions() {
		for (int k = 0; k < series.size(); k++) {
			SeriesData s = series.get(k);
			if (!s.series.lines.getShow() && options.series.lines.getShow()) {
				s.series.lines.setShow(options.series.lines.getShow());
			}
			if (!s.series.points.show && options.series.points.show) {
				s.series.points.show = options.series.points.show;
			}
			if (!s.series.bars.show && options.series.bars.show) {
				s.series.bars.show = options.series.bars.show;
			}
		}

		int neededColors = series.size();

		Vector<Integer> colors = new Vector<Integer>();
		int i = 0;
		int variation = 0;

		while (colors.size() < neededColors) {
			ColorHelper c;
			if (options.colors.length == i) {
				c = new ColorHelper(100, 100, 100);
			} else {
				c = new ColorHelper(options.colors[i]);
			}
			int sign = (variation % 2 == 1 ? -1 : 1);
			c.scale("rgb", 1 + sign * Math.ceil(variation / 2) * 0.2);

			colors.add(c.rgb());
			++i;
			if (i >= options.colors.length) {
				i = 0;
				++variation;
			}
		}

		int colori = 0;
		SeriesData s;
		for (i = 0; i < series.size(); ++i) {

			s = series.get(i);
			s.series.color = colors.get(colori).intValue();
			++colori;

			s.series.defaultLinesShow();

			s.axes.xaxis = this.axisSpecToRealAxis(s.axes, "xaxis");
			s.axes.yaxis = this.axisSpecToRealAxis(s.axes, "yaxis");
		}

	}

	public NearItemData findNearbyItem(double mouseX, double mouseY,
			boolean hover) {
		int maxDistance = options.grid.mouseActiveRadius;
		double smallestDistance = maxDistance * maxDistance + 1;
		int[] item = null;
		int i, j;

		for (i = 0; i < series.size(); i++) {
			SeriesData s = series.get(i);

			if (!hover) {
				continue;
			}
			AxisData axisx = s.axes.xaxis;
			AxisData axisy = s.axes.yaxis;
			Vector<Double> points = s.datapoints.points;
			int ps = s.datapoints.pointsize;
			double mx = axisx.c2p.format(mouseX);
			double my = axisy.c2p.format(mouseY);
			double maxx = maxDistance / axisx.scale;
			double maxy = maxDistance / axisy.scale;

			if (s.series.lines.getShow() || s.series.points.show) {
				for (j = 0; j < points.size(); j += ps) {
					double x = points.get(j).doubleValue();
					double y = points.get(j + 1).doubleValue();

					if (x - mx > maxx || x - mx < -maxx || y - my > maxy
							|| y - my < -maxy) {
						continue;
					}

					double dx = Math.abs(axisx.p2c.format(x) - mouseX);
					double dy = Math.abs(axisy.p2c.format(y) - mouseY);
					double dist = dx * dx + dy * dy;

					if (dist <= smallestDistance) {
						smallestDistance = dist;
						item = new int[2];
						item[0] = i;
						item[1] = j / ps;
					}
				}
			}

			if (s.series.bars.show && item == null) {
				double barLeft = s.series.bars.align.equals("left") ? 0
						: -s.series.bars.barWidth / 2;
				double barRight = barLeft + s.series.bars.barWidth;

				for (j = 0; j < points.size(); j += ps) {
					double x = points.get(j);
					double y = points.get(j + 1);
					double b = points.get(j + 2);

					if (s.series.bars.horizontal ? (mx <= Math.max(b, x)
							&& mx >= Math.min(b, x) && my >= y + barLeft && my <= y
							+ barRight)
							: (mx >= x + barLeft && mx <= x + barRight
									&& my >= Math.min(b, y) && my <= Math.max(
									b, y))) {
						item = new int[2];
						item[0] = i;
						item[1] = j / ps;
					}

				}
			}
		}

		if (item != null && item.length == 2) {
			i = item[0];
			j = item[1];

			return new NearItemData(series.get(i).getData()[j], j, series
					.get(i), i);
		}
		return null;
	}

	public Axies getAxes() {
		return axes;
	}

	public Graphics2D getCanvas() {
		return grap;
	}
	
	public Graphics2D getCanvasOverlay(){
		return grapOverlay;
	}

	private Paint getColorOrGradient(Object spec, double bottom, double top,
			int defaultColor) {
		if (spec instanceof Integer) {
			return getTranColor(((Integer) spec).intValue());
		} else if (spec instanceof int[]) {
			int[] specColors = (int[]) spec;
			if (specColors != null) {
				float[] dist = new float[specColors.length];
				Color[] colors = new Color[specColors.length];

				for (int i = 0; i < specColors.length; i++) {
					dist[i] = i / (specColors.length - 1.0f);
					colors[i] = getTranColor(specColors[i]);
				}

				return new LinearGradientPaint(0.0f, (float) top, (float) 0,
						(float) bottom, dist, colors);
			}
		}
		return getTranColor(defaultColor);
	}

	public Vector<SeriesData> getData() {
		return series;
	}

	public EventHolder getEventHolder() {
		return eventHolder;
	}

	private Paint getFillStyle(boolean filloptions, Object fillColor,
			int seriesColor, double bottom, double top) {
		if (!filloptions) {
			return null;
		}
		if (fillColor != null) {
			return getColorOrGradient(fillColor, bottom, top, seriesColor);
		}
		return new Color(seriesColor | 0x66000000, true);
	}

	public Object getOptions() {
		return options;
	}

	public Object getPlaceholder() {
		return new Object();
	}

	public RectOffset getPlotOffset() {
		return plotOffset;
	}

	private Color getTranColor(int rgba) {
		return new Color(rgba, ((rgba >> 24) != 0));
	}

	public int height() {
		return plotHeight;
	}

	public void highlight(SeriesData s, double[] point, String auto,
			int dataIndex) {
		int i = indexOfHighlight(s, point);
		if (i == -1) {
			highlights.add(new HighlightData(s, point, auto, dataIndex));
		} else if (auto == null || auto.length() == 0) {
			highlights.get(i).auto = "false";
		}
	}

	public int indexOfHighlight(SeriesData s, double[] point) {
		for (int i = 0; i < highlights.size(); i++) {
			HighlightData h = highlights.get(i);
			if (h.series == s && h.point.length > 1 && point.length > 1) {
				if (equals(h.point[0], point[0])
						&& equals(h.point[1], point[1])) {
					return i;
				}
			}
		}
		return -1;
	}

	private void insertLabels() {
		// TODO Auto-generated method stub
		final int margin = options.grid.labelMargin + options.grid.borderWidth;

		grap.setColor(Color.black);
		grap.setFont(defaultFont);
		if (axes.xaxis.used) {
			addLabels(axes.xaxis, new DrawFormatter() {

				@Override
				public void draw(AxisData axis, TickData tick) {
					// TODO Auto-generated method stub
					drawCenteredString(tick.label, plotOffset.top + plotHeight
							+ margin, (int) Math.round(plotOffset.left
							+ axis.p2c.format(tick.v) - axis.labelWidth / 2),
							(int) axis.labelWidth, (int) axis.labelHeight, grap);
				}

			});
		}

		if (axes.yaxis.used) {
			addLabels(axes.yaxis, new DrawFormatter() {

				@Override
				public void draw(AxisData axis, TickData tick) {
					// TODO Auto-generated method stub
					drawRightString(tick.label, (int) Math.round(plotOffset.top
							+ axis.p2c.format(tick.v) - axis.labelHeight / 2),
							0, (int) axis.labelWidth, (int) axis.labelHeight);
				}

			});
		}
	}

	private void insertLegend() {
		if (!options.legend.show) {
			return;
		}
		FontMetrics fm = grap.getFontMetrics();
		int max = 0;
		int cnt = 0;
		for (int i = 0; i < series.size(); i++) {
			SeriesData s = series.get(i);
			String label = s.label;
			if (options.legend.labelFormatter != null) {
				label = options.legend.labelFormatter.format(label, s);
			}
			if (label == null || label.length() == 0) {
				continue;
			}
			int labelWdt = fm.stringWidth(label);
			if (labelWdt > max) {
				max = labelWdt;
			}
			cnt++;
		}
		if (cnt == 0)
			return;
		int width = max + 20;
		int height = (fm.getHeight() > 16 ? fm.getHeight() : 16) + 4;
		int nWidth = Math.min(cnt, options.legend.noColumns);
		int nHeight = cnt / options.legend.noColumns
				+ (cnt % options.legend.noColumns == 0 ? 0 : 1);

		AffineTransform old = grap.getTransform();

		grap.translate(plotOffset.left + plotWidth - nWidth * width - 10,
				plotOffset.top + 10);
		
		Paint p = this.getFillStyle(true, options.legend.backgroundColor, 0xffffffff, height * nHeight, 0);
		if(p != null) {
		    grap.setPaint(p);
		    grap.fillRect(0, 0, nWidth * width, height * nHeight);
		}

		for (int i = 0; i < series.size(); i++) {
			SeriesData s = series.get(i);
			String label = s.label;
			if (options.legend.labelFormatter != null) {
				label = options.legend.labelFormatter.format(label, s);
			}
			if (label == null || label.length() == 0) {
				continue;
			}
			grap.setColor(getTranColor(options.legend.labelBoxBorderColor));
			grap.setStroke(new BasicStroke(1));
			grap.drawRect(width * (i % nWidth), (height - 10) / 2 + height
					* (i / nWidth), 14, 10);

			grap.setColor(getTranColor(s.series.color));
			grap.fillRect(2 + width * (i % nWidth), (height - 10) / 2 + height
					* (i / nWidth) + 2, 11, 7);

			grap.setColor(getTranColor(options.legend.labelColor));
			grap.drawString(label, 18 + width * (i % nWidth),
					(fm.getAscent() + (height - (fm.getAscent() + fm
							.getDescent())) / 2)
							+ height * (i / nWidth));
		}

		grap.setTransform(old);
	}

	private void measureLabels(AxisData axis, AxisOption axisOption) {

		axis.labelWidth = axisOption.labelWidth;
		axis.labelHeight = axisOption.labelHeight;

		FontMetrics fm = grap.getFontMetrics(defaultFont);

		if (axis == axes.xaxis || axis == axes.x2axis) {
			if (axis.labelWidth == -1) {
				axis.labelWidth = (canvasWidth / (axis.ticks.size() > 0 ? axis.ticks
						.size()
						: 1));
			}
			if (axis.labelHeight == -1) {

				axis.labelHeight = fm.getHeight();
			}
		} else if (axis.labelWidth == -1 || axis.labelHeight == -1) {
			int max = 0;
			for (int i = 0; i < axis.ticks.size(); i++) {
				int c_max = fm.stringWidth(axis.ticks.get(i).label);
				if (c_max > max) {
					max = c_max;
				}
			}
			if (axis.labelWidth == -1) {
				axis.labelWidth = max;
			}
			if (axis.labelHeight == -1) {
				axis.labelHeight = (canvasHeight / (axis.ticks.size() > 0 ? axis.ticks
						.size()
						: 1));
			}
		}

		/*
		 * System.out.println("Width:" + axis.labelWidth + "Height:" +
		 * axis.labelHeight);
		 */
	}

	public Object offset() {
		return plotOffset;
	}

	public void parseOptions(Options opt) {

		hookHolder.dispatchEvent(FlotEvent.HOOK_PROCESSOPTIONS, new FlotEvent(
				new HookEventObject(this, new Object[] { opt })));
	}

	private void plotBars(SeriesData currentSeries, Datapoints datapoints,
			double barLeft, double barRight, double offset, AxisData axisx,
			AxisData axisy) {
		Vector<Double> points = datapoints.points;
		int ps = datapoints.pointsize;

		for (int i = 0; i < points.size(); i += ps) {
			if (Double.isNaN(points.get(i))) {
				continue;
			}
			drawBar(points.get(i), points.get(i + 1), points.get(i + 2),
					barLeft, barRight, offset, axisx, axisy, currentSeries, grap);
		}
	}

	private void plotLine(Datapoints datapoints, double xoffset,
			double yoffset, AxisData axisx, AxisData axisy) {
		GeneralPath shape = new GeneralPath();
		Vector<Double> points = datapoints.points;
		int ps = datapoints.pointsize;
		double prevx = Double.MIN_VALUE;
		double prevy = Double.MIN_VALUE;

		for (int i = ps; i < points.size(); i += ps) {
			double x1 = points.get(i - ps);
			double y1 = points.get(i - ps + 1);
			double x2 = points.get(i);
			double y2 = points.get(i + 1);

			if (Double.isNaN(x1) || Double.isNaN(x2)) {
				continue;
			}

			if (y1 <= y2 && y1 < axisy.min) {
				if (y2 < axisy.min) {
					continue;
				}
				x1 = (axisy.min - y1) / (y2 - y1) * (x2 - x1) + x1;
				y1 = axisy.min;
			} else if (y2 <= y1 && y2 < axisy.min) {
				if (y1 < axisy.min)
					continue;
				x2 = (axisy.min - y1) / (y2 - y1) * (x2 - x1) + x1;
				y2 = axisy.min;
			}

			// clip with ymax
			if (y1 >= y2 && y1 > axisy.max) {
				if (y2 > axisy.max)
					continue;
				x1 = (axisy.max - y1) / (y2 - y1) * (x2 - x1) + x1;
				y1 = axisy.max;
			} else if (y2 >= y1 && y2 > axisy.max) {
				if (y1 > axisy.max)
					continue;
				x2 = (axisy.max - y1) / (y2 - y1) * (x2 - x1) + x1;
				y2 = axisy.max;
			}

			// clip with xmin
			if (x1 <= x2 && x1 < axisx.min) {
				if (x2 < axisx.min)
					continue;
				y1 = (axisx.min - x1) / (x2 - x1) * (y2 - y1) + y1;
				x1 = axisx.min;
			} else if (x2 <= x1 && x2 < axisx.min) {
				if (x1 < axisx.min)
					continue;
				y2 = (axisx.min - x1) / (x2 - x1) * (y2 - y1) + y1;
				x2 = axisx.min;
			}

			// clip with xmax
			if (x1 >= x2 && x1 > axisx.max) {
				if (x2 > axisx.max)
					continue;
				y1 = (axisx.max - x1) / (x2 - x1) * (y2 - y1) + y1;
				x1 = axisx.max;
			} else if (x2 >= x1 && x2 > axisx.max) {
				if (x1 > axisx.max)
					continue;
				y2 = (axisx.max - x1) / (x2 - x1) * (y2 - y1) + y1;
				x2 = axisx.max;
			}

			if (x1 != prevx || y1 != prevy)
				shape.moveTo(axisx.p2c.format(x1) + xoffset, axisy.p2c
						.format(y1)
						+ yoffset);

			prevx = x2;
			prevy = y2;
			shape.lineTo(axisx.p2c.format(x2) + xoffset, axisy.p2c.format(y2)
					+ yoffset);
		}

		grap.draw(shape);
	}

	private void plotLineArea(Datapoints datapoints, AxisData axisx,
			AxisData axisy) {
		Vector<Double> points = datapoints.points;
		int ps = datapoints.pointsize;
		double bottom = Math.min(Math.max(0, axisy.min), axisy.max);
		double top;
		double lastX = 0;
		boolean areaOpen = false;
		GeneralPath ctx = new GeneralPath();

		for (int i = ps; i < points.size(); i += ps) {
			double x1 = points.get(i - ps);
			double y1 = points.get(i - ps + 1);
			double x2 = points.get(i);
			double y2 = points.get(i + 1);

			if (areaOpen && !Double.isNaN(x1) && Double.isNaN(x2)) {
				// close area
				ctx.lineTo(axisx.p2c.format(lastX), axisy.p2c.format(bottom));
				ctx.closePath();
				grap.fill(ctx);
				ctx.reset();
				areaOpen = false;
				continue;
			}

			if (Double.isNaN(x1) || Double.isNaN(x2))
				continue;

			// clip x values

			// clip with xmin
			if (x1 <= x2 && x1 < axisx.min) {
				if (x2 < axisx.min)
					continue;
				y1 = (axisx.min - x1) / (x2 - x1) * (y2 - y1) + y1;
				x1 = axisx.min;
			} else if (x2 <= x1 && x2 < axisx.min) {
				if (x1 < axisx.min)
					continue;
				y2 = (axisx.min - x1) / (x2 - x1) * (y2 - y1) + y1;
				x2 = axisx.min;
			}

			// clip with xmax
			if (x1 >= x2 && x1 > axisx.max) {
				if (x2 > axisx.max)
					continue;
				y1 = (axisx.max - x1) / (x2 - x1) * (y2 - y1) + y1;
				x1 = axisx.max;
			} else if (x2 >= x1 && x2 > axisx.max) {
				if (x1 > axisx.max)
					continue;
				y2 = (axisx.max - x1) / (x2 - x1) * (y2 - y1) + y1;
				x2 = axisx.max;
			}

			if (!areaOpen) {
				// open area
				// ctx.beginPath();
				ctx.moveTo(axisx.p2c.format(x1), axisy.p2c.format(bottom));
				areaOpen = true;
			}

			// now first check the case where both is outside
			if (y1 >= axisy.max && y2 >= axisy.max) {
				ctx.lineTo(axisx.p2c.format(x1), axisy.p2c.format(axisy.max));
				ctx.lineTo(axisx.p2c.format(x2), axisy.p2c.format(axisy.max));
				lastX = x2;
				continue;
			} else if (y1 <= axisy.min && y2 <= axisy.min) {
				ctx.lineTo(axisx.p2c.format(x1), axisy.p2c.format(axisy.min));
				ctx.lineTo(axisx.p2c.format(x2), axisy.p2c.format(axisy.min));
				lastX = x2;
				continue;
			}

			// else it's a bit more complicated, there might
			// be two rectangles and two triangles we need to fill
			// in; to find these keep track of the current x values
			double x1old = x1, x2old = x2;

			// and clip the y values, without shortcutting

			// clip with ymin
			if (y1 <= y2 && y1 < axisy.min && y2 >= axisy.min) {
				x1 = (axisy.min - y1) / (y2 - y1) * (x2 - x1) + x1;
				y1 = axisy.min;
			} else if (y2 <= y1 && y2 < axisy.min && y1 >= axisy.min) {
				x2 = (axisy.min - y1) / (y2 - y1) * (x2 - x1) + x1;
				y2 = axisy.min;
			}

			// clip with ymax
			if (y1 >= y2 && y1 > axisy.max && y2 <= axisy.max) {
				x1 = (axisy.max - y1) / (y2 - y1) * (x2 - x1) + x1;
				y1 = axisy.max;
			} else if (y2 >= y1 && y2 > axisy.max && y1 <= axisy.max) {
				x2 = (axisy.max - y1) / (y2 - y1) * (x2 - x1) + x1;
				y2 = axisy.max;
			}

			// if the x value was changed we got a rectangle
			// to fill
			if (x1 != x1old) {
				if (y1 <= axisy.min)
					top = axisy.min;
				else
					top = axisy.max;

				ctx.lineTo(axisx.p2c.format(x1old), axisy.p2c.format(top));
				ctx.lineTo(axisx.p2c.format(x1), axisy.p2c.format(top));
			}

			// fill the triangles
			ctx.lineTo(axisx.p2c.format(x1), axisy.p2c.format(y1));
			ctx.lineTo(axisx.p2c.format(x2), axisy.p2c.format(y2));

			// fill the other rectangle if it's there
			if (x2 != x2old) {
				if (y2 <= axisy.min)
					top = axisy.min;
				else
					top = axisy.max;

				ctx.lineTo(axisx.p2c.format(x2), axisy.p2c.format(top));
				ctx.lineTo(axisx.p2c.format(x2old), axisy.p2c.format(top));
			}

			lastX = Math.max(x2, x2old);
		}

		if (areaOpen) {
			ctx.lineTo(axisx.p2c.format(lastX), axisy.p2c.format(bottom));
			grap.fill(ctx);
		}
	}

	private void plotPoints(boolean fill, SeriesData currentSeries,
			Datapoints datapoints, double radius, double offset,
			int circumference, AxisData axisx, AxisData axisy) {
		Vector<Double> points = datapoints.points;
		int ps = datapoints.pointsize;

		for (int i = 0; i < points.size(); i += ps) {
			double x = points.get(i);
			double y = points.get(i + 1);
			if (Double.isNaN(x) || x < axisx.min || x > axisx.max
					|| y < axisy.min || y > axisy.max) {
				continue;
			}

			Paint p = getFillStyle(currentSeries.series.points.fill,
					currentSeries.series.points.fillColor,
					currentSeries.series.color, 0, 0);

			if (fill && currentSeries.series.points.fill && p != null) {
				grap.setPaint(p);
				grap.fillArc((int) (axisx.p2c.format(x) - radius),
						(int) (axisy.p2c.format(y) + offset - radius),
						(int) (2 * radius), (int) (2 * radius), 180,
						circumference);
				grap.setColor(getTranColor(currentSeries.series.color));
				grap.setStroke(new BasicStroke(
						currentSeries.series.lines.lineWidth,
						BasicStroke.CAP_BUTT, BasicStroke.CAP_ROUND));
				grap.drawArc((int) (axisx.p2c.format(x) - radius),
						(int) (axisy.p2c.format(y) + offset - radius),
						(int) (2 * radius), (int) (2 * radius), 180,
						circumference);
				continue;
			}
			grap.drawArc((int) (axisx.p2c.format(x) - radius), (int) (axisy.p2c
					.format(y)
					+ offset - radius), (int) (2 * radius), (int) (2 * radius),
					180, circumference);
		}
	}

	public void pointOffset() {

	}

	private void prepareTickGeneration(AxisData axis, final AxisOption axisOption) {
		double noTicks;
		double axisOptionTicks = -1.0;
		double size = 1;
		TickGenerator generator = null;
		TickFormatter formatter = null;
		if (axisOption.ticks instanceof Integer) {
			axisOptionTicks = ((Integer) axisOption.ticks).intValue();
		}
		if (axisOptionTicks > 0) {
			noTicks = axisOptionTicks;
		} else if (axis == axes.xaxis || axis == axes.yaxis) {
			noTicks = 0.3 * Math.sqrt(canvasWidth);
		} else {
			noTicks = 0.3 * Math.sqrt(canvasHeight);
		}

		double delta = (axis.max - axis.min) / noTicks;

		if (axisOption.mode != null && axisOption.mode.equals("time")) {

			double minSize = 0;
			if (axisOption.minTickSize != null) {
				if(axisOption.tickSize != null && !Double.isNaN(axisOption.tickSize.number)) {
					minSize = axisOption.tickSize.number;
				}
				else if(axisOption.minTickSize != null && axisOption.minTickSize.timeNumber != null){
				    minSize = axisOption.minTickSize.timeNumber.i0 * timeUnitSize.get(axisOption.minTickSize.timeNumber.i1);
				}
			}
			int i = 0;
			for (; i < spec.size() - 1; i++) {
				if (delta < (spec.get(i).i0
						* timeUnitSize.get(spec.get(i).i1).doubleValue() + spec
						.get(i + 1).i0
						* timeUnitSize.get(spec.get(i + 1).i1).doubleValue()) / 2
						&& spec.get(i).i0 * timeUnitSize.get(spec.get(i).i1) >= minSize) {
					break;
				}
			}

			size = spec.get(i).i0;
			String unit = spec.get(i).i1;

			if (unit.equals("year")) {
				double magn = Math.pow(10, Math.floor(Math.log(delta
						/ timeUnitSize.get("year")) / 2.302585));
				double norm = (delta / timeUnitSize.get("year")) / magn;
				if (norm < 1.5)
					size = 1;
				else if (norm < 3)
					size = 2;
				else if (norm < 7.5)
					size = 5;
				else
					size = 10;

				size *= magn;
			}

			if (axisOption.tickSize != null && axisOption.tickSize.timeNumber != null) {
				size = axisOption.tickSize.timeNumber.i0;
				unit = axisOption.tickSize.timeNumber.i1;
			}

			axis.tickSize.timeNumber = new SpecData(size, unit);

			generator = new TickGenerator() {
				public Vector<TickData> generator(AxisData axis) {
					Vector<TickData> ticks = new Vector<TickData>();
					
					SpecData specSize = axis.tickSize.timeNumber;

					double tickSize = specSize.i0;
					String unit = specSize.i1;
					
					Calendar d = Calendar.getInstance(TimeZone.getTimeZone("UTC")); 
					d.setTimeInMillis((long)axis.min);
					
					double step = tickSize * timeUnitSize.get(unit);

					if (unit.equals("second")) {
						d.set(Calendar.SECOND, (int) floorInBase(d.get(Calendar.SECOND), tickSize));
					} else if (unit.equals("minute")) {
						d.set(Calendar.MINUTE, (int) floorInBase(d.get(Calendar.MINUTE), tickSize));
					} else if (unit.equals("hour")) {
						d.set(Calendar.HOUR , (int) floorInBase(d.get(Calendar.HOUR), tickSize));
					} else if (unit.equals("month")) {
						d.set(Calendar.MONTH , (int) floorInBase(d.get(Calendar.MONTH), tickSize));
					} else if (unit.equals("year")) {
						d.set(Calendar.YEAR , (int) floorInBase(d.get(Calendar.YEAR), tickSize));
					}
					
					d.set(Calendar.MILLISECOND, 0);
					if(step >= timeUnitSize.get("minute")) {
						d.set(Calendar.SECOND, 0);
					}
					else if(step >= timeUnitSize.get("hour")) {
						d.set(Calendar.MINUTE, 0);
					}
					else if(step >= timeUnitSize.get("day")) {
						d.set(Calendar.HOUR, 0);
					}
					else if(step >= timeUnitSize.get("day") * 4) {
						d.set(Calendar.DAY_OF_MONTH, 1);
					}
					else if(step >= timeUnitSize.get("year")) {
						d.set(Calendar.MONTH, 0);
					}
					
					long carry = 0;
					long v = -1;
					long prev;
					

                    do {
                        prev = v;
                        v = d.getTimeInMillis();
                        ticks.add(new TickData(v, axis.tickFormatter.formatNumber(v, axis)));
                        if (unit.equals("month")) {
                            if (tickSize < 1) {
                                // a bit complicated - we'll divide the month
                                // up but we need to take care of fractions
                                // so we don't end up in the middle of a day
                                d.set(Calendar.DAY_OF_MONTH, 1);
                                long start = d.getTimeInMillis();
                                d.set(Calendar.MONTH, d.get(Calendar.MONTH) + 1);
                                long end = d.getTimeInMillis();
                                d.setTimeInMillis((long) (v + carry * timeUnitSize.get("hour") + (end - start) * tickSize));
                                carry = d.get(Calendar.HOUR);
                                d.set(Calendar.HOUR, 0);
                            }
                            else
                                d.set(Calendar.MONTH, (int) (d.get(Calendar.MONTH) + tickSize));
                        }
                        else if (unit == "year") {
                            d.set(Calendar.YEAR, (int) (d.get(Calendar.YEAR) + tickSize));
                        }
                        else
                            d.setTimeInMillis((long) (v + step));
                    } while (v < axis.max && v != prev);
                    

					return ticks;
				}
			};
			
			formatter = new TickFormatter(){
				public String formatNumber(double val, AxisData axis) {
					Calendar d = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
					
					if(axis.tickSize.timeNumber != null) {
						d.setTimeInMillis((long) val);
						if(axisOption.timeformat != null) {
							return formatDate(d, axisOption.timeformat, axisOption.monthNames);
						}
						
						double t = axis.tickSize.timeNumber.i0 * timeUnitSize.get(axis.tickSize.timeNumber.i1);
						double span = axis.max - axis.min;
						String suffix = (axisOption.twelveHourClock) ? "%p" : "";
						
						String fmt = "";
	                    if (t < timeUnitSize.get("minute"))
	                        fmt = "%h:%M:%S" + suffix;
	                    else if (t < timeUnitSize.get("day")) {
	                        if (span < 2 * timeUnitSize.get("day"))
	                            fmt = "%h:%M" + suffix;
	                        else
	                            fmt = "%b %d %h:%M" + suffix;
	                    }
	                    else if (t < timeUnitSize.get("month"))
	                        fmt = "%b %d";
	                    else if (t < timeUnitSize.get("year")) {
	                        if (span < timeUnitSize.get("year"))
	                            fmt = "%b";
	                        else
	                            fmt = "%b %y";
	                    }
	                    else
	                        fmt = "%y";
						return formatDate(d, fmt, axisOption.monthNames);
					}
					return "";
				}
				
				public String formatDate(Calendar d, String fmt, String[] monthNames) {
					String r = "";
					boolean escape = false;
					int hours = d.get(Calendar.HOUR);
					boolean isAM = hours < 12;
					
					if(monthNames == null) {
						monthNames = new String[] {
								"Jan", "Feb", "Mar", 
								"Apr", "May", "Jun", 
								"Jul", "Aug", "Sep", 
								"Oct", "Nov", "Dec"
						};
					}
					
					if(fmt.matches(".*(%p|%P).*")) {
						if(hours > 12) {
							hours = hours - 12;
						}
						else if(hours == 0) {
							hours = 12;
						}
					}
					for(int i=0;i<fmt.length();i++) {
						char c = fmt.charAt(i);
						
						String tmp = "";
						if(escape) {
							switch(c) {
			                case 'h': tmp = "" + hours; break;
			                case 'H': tmp = leftPad(hours); break;
			                case 'M': tmp = leftPad(d.get(Calendar.MINUTE)); break;
			                case 'S': tmp = leftPad(d.get(Calendar.SECOND)); break;
			                case 'd': tmp = "" + d.get(Calendar.DAY_OF_MONTH); break;
			                case 'm': tmp = "" + (d.get(Calendar.MONTH) + 1); break;
			                case 'y': tmp = "" + d.get(Calendar.YEAR); break;
			                case 'b': tmp = "" + monthNames[d.get(Calendar.MONTH)]; break;
			                case 'p': tmp = (isAM) ? ("" + "am") : ("" + "pm"); break;
			                case 'P': tmp = (isAM) ? ("" + "AM") : ("" + "PM"); break;
							}
							r += tmp;
							escape = false;
						}
						else {
							if(c == '%') {
								escape = true;
							}
							else {
								r += c;
							}
						}
					}
					return r;
				}
				
				public String leftPad(int n) {
					String t = "" + n;
					return t.length() == 1 ? "0" + t : t;
				}
			};

		} else {
			int maxDec = axisOption.tickDecimals;
			int dec = (int) -Math.floor(Math.log(delta) / 2.302585);

			if (maxDec != -1 && dec > maxDec) {
				dec = maxDec;
			}

			double magn = Math.pow(10, -dec);
			double norm = delta / magn;

			if (norm < 1.5) {
				size = 1;
			} else if (norm < 3) {
				size = 2;

				if (norm > 2.25 && (maxDec == -1 || dec + 1 <= maxDec)) {
					size = 2.5;
					++dec;
				}
			} else if (norm < 7.5) {
				size = 5;
			} else {
				size = 10;
			}

			size *= magn;
			if (axisOption.minTickSize != null
					&& !Double.isNaN(axisOption.minTickSize.number) &&size < axisOption.minTickSize.number) {
				size = axisOption.minTickSize.number;
			}

			if (axisOption.tickSize != null && !Double.isNaN(axisOption.tickSize.number)) {
				size = axisOption.tickSize.number;
			}

			axis.tickDecimals = Math.max(0, (maxDec != -1 ? maxDec : dec));
			generator = new TickGenerator();
			formatter = new TickFormatter();
			axis.tickSize.number = size;
		}

		axis.tickGenerator = generator;

		if (axisOption.tickFormatter != null) {
			axis.tickFormatter = axisOption.tickFormatter;
		} else {
			axis.tickFormatter = formatter;
		}

	}

	public void processData() {

		for (int i = 0; i < series.size(); i++) {
			SeriesData sd = series.get(i);
			hookHolder.dispatchEvent(FlotEvent.HOOK_PROCESSRAWDATA,
					new FlotEvent(new HookEventObject(this, new Object[] { sd,
							sd.getData(), sd.datapoints })));
		}

		for (int i = 0; i < series.size(); i++) {
			SeriesData sd = series.get(i);
			double[][] data = sd.getData();

			if (sd.datapoints.format.size() == 0) {
				sd.datapoints.format.add(new FormatData("x", true, true, null));
				sd.datapoints.format.add(new FormatData("y", true, true, null));

				if (sd.series.bars.show) {
					sd.datapoints.format.add(new FormatData("y", true, true,
							new Integer(0)));
				}
			}

			if (sd.datapoints.pointsize != 0) {
				continue;
			}

			if (sd.datapoints.pointsize == 0) {
				sd.datapoints.pointsize = sd.datapoints.format.size();
			}

			int ps = sd.datapoints.pointsize;
			Vector<Double> points = sd.datapoints.points;

			sd.axes.xaxis.used = sd.axes.yaxis.used = true;

			int k = 0;
			for (int j = 0; j < data.length; j++, k += ps) {
				double[] p = data[j];

				Boolean nullify = ((p == null) || (p.length == 0));
				if (!nullify) {
					for (int m = 0; m < ps; m++) {
						if (m < p.length) {
							double val = p[m];
							FormatData f = sd.datapoints.format.get(m);

							if (f != null) {
							}
							points.add(new Double(val));
						} else {
							points.add(new Double(0));
						}
					}
				}
			}
		}

		for (int i = 0; i < series.size(); i++) {
			SeriesData sd = series.get(i);
			hookHolder.dispatchEvent(FlotEvent.HOOK_PROCESSDATAPOINTS,
					new FlotEvent(new HookEventObject(this, new Object[] { sd,
							sd.datapoints })));
		}

		double xmin = Double.MAX_VALUE;
		double xmax = Double.MIN_VALUE;
		double ymin = Double.MAX_VALUE;
		double ymax = Double.MIN_VALUE;

		for (int i = 0; i < series.size(); i++) {
			SeriesData s = series.get(i);
			Vector<Double> points = s.datapoints.points;
			int ps = s.datapoints.pointsize;
			for (int j = 0; j < points.size(); j += ps) {
				if (points.get(j) == null) {
					continue;
				}

				for (int m = 0; m < ps; m++) {
					double val = points.get(j + m).doubleValue();
					FormatData f = s.datapoints.format.get(m);
					if (f != null) {
						if (f.xy.equals("x")) {
							if (val < xmin) {
								xmin = val;
							}
							if (val > xmax) {
								xmax = val;
							}
						}

						if (f.xy.equals("y")) {
							if (val < ymin) {
								ymin = val;
							}
							if (val > ymax) {
								ymax = val;
							}
						}
					}
				}

			}

			if (s.series.bars.show) {
				double delta = (s.series.bars.align.equals("left") ? 0
						: -s.series.bars.barWidth / 2);

				if (s.series.bars.horizontal) {
					ymin += delta;
					ymax += delta + s.series.bars.barWidth;
				} else {
					xmin += delta;
					xmax += delta + s.series.bars.barWidth;
				}
			}

			updateAxis(s.axes.xaxis, xmin, xmax);
			updateAxis(s.axes.yaxis, ymin, ymax);
		}
	}

	private void redraw() {
		eventHolder
				.dispatchEvent(FlotEvent.CANVAS_REPAINT, new FlotEvent(this));
	}

	public void removeHookListener(FlotEventListener flot) {
		hookHolder.removeEventListener(flot);
	}

	public void setData(Vector<SeriesData> _series) {
		series = _series;
		fillInSeriesOptions();
		processData();
		spec.add(new SpecData(1, "second"));
		spec.add(new SpecData(2, "second"));
		spec.add(new SpecData(5, "second"));
		spec.add(new SpecData(10, "second"));
		spec.add(new SpecData(30, "second"));

		spec.add(new SpecData(1, "minute"));
		spec.add(new SpecData(2, "minute"));
		spec.add(new SpecData(5, "minute"));
		spec.add(new SpecData(10, "minute"));
		spec.add(new SpecData(30, "minute"));

		spec.add(new SpecData(1, "hour"));
		spec.add(new SpecData(2, "hour"));
		spec.add(new SpecData(4, "hour"));
		spec.add(new SpecData(8, "hour"));
		spec.add(new SpecData(12, "hour"));

		spec.add(new SpecData(1, "day"));
		spec.add(new SpecData(2, "day"));
		spec.add(new SpecData(3, "day"));

		spec.add(new SpecData(0.25, "month"));
		spec.add(new SpecData(0.5, "month"));
		spec.add(new SpecData(1, "month"));
		spec.add(new SpecData(2, "month"));
		spec.add(new SpecData(3, "month"));
		spec.add(new SpecData(6, "month"));

		spec.add(new SpecData(1, "year"));

		timeUnitSize.put("second", new Double(1000.0));
		timeUnitSize.put("minute", new Double(60 * 1000.0));
		timeUnitSize.put("hour", new Double(60 * 60 * 1000.0));
		timeUnitSize.put("day", new Double(24 * 60 * 60 * 1000.0));
		timeUnitSize.put("month", new Double(30 * 24 * 60 * 60 * 1000.0));
		timeUnitSize.put("year", new Double(365.2425 * 24 * 60 * 60 * 1000));
	}

	public void setEventHolder(EventHolder eventHolder) {
		this.eventHolder = eventHolder;
	}

	private void setGridSpacing() {
		int maxOutset = options.grid.borderWidth;
		for (int i = 0; i < series.size(); i++) {
			maxOutset = Math
					.max(maxOutset,
							2 * (series.get(i).series.points.radius + series
									.get(i).series.points.lineWidth / 2));
		}

		plotOffset.reset(maxOutset, maxOutset, maxOutset, maxOutset);
		int margin = options.grid.labelMargin + options.grid.borderWidth;

		if (axes.xaxis.labelWidth > 0) {
			plotOffset.bottom = (int) Math.max(maxOutset,
					axes.xaxis.labelHeight + margin);
		}
		if (axes.yaxis.labelWidth > 0)
			plotOffset.left = (int) Math.max(maxOutset, axes.yaxis.labelWidth
					+ margin);
		if (axes.x2axis.labelHeight > 0)
			plotOffset.top = (int) Math.max(maxOutset, axes.x2axis.labelHeight
					+ margin);
		if (axes.y2axis.labelWidth > 0)
			plotOffset.right = (int) Math.max(maxOutset, axes.y2axis.labelWidth
					+ margin);

		plotWidth = canvasWidth - plotOffset.left - plotOffset.right;
		plotHeight = canvasHeight - plotOffset.bottom - plotOffset.top;
	}

	private void setRange(AxisData axis, AxisOption axisOption) {
		double axisOptionMax = Double.NaN;
		double axisOptionMin = Double.NaN;
		double axisMargin = Double.NaN;

		axisOptionMax = axisOption.max;
		axisOptionMin = axisOption.min;
		axisMargin = axisOption.autoscaleMargin;

		double max = (!Double.isNaN(axisOptionMax) ? axisOptionMax
				: axis.datamax);
		double min = (!Double.isNaN(axisOptionMin) ? axisOptionMin
				: axis.datamin);

		double delta = max - min;

		if (delta == 0.0) {
			double widen = (max == 0 ? 1 : 0.01);
			if (Double.isNaN(axisOptionMin)) {
				min -= widen;
			}

			if (Double.isNaN(axisOptionMax) || !Double.isNaN(axisOptionMin)) {
				max += widen;
			}
		} else {
			if (!Double.isNaN(axisMargin)) {
				if (Double.isNaN(axisOptionMin)) {
					min -= delta * axisMargin;

					if (min < 0 && axis.datamin != Double.MIN_VALUE
							&& axis.datamin >= 0) {
						min = 0;
					}
				}

				if (Double.isNaN(axisOptionMax)) {
					max += delta * axisMargin;

					if (max > 0 && axis.datamax != Double.MAX_VALUE
							&& axis.datamax <= 0) {
						max = 0;
					}
				}
			}
		}

		axis.max = max;
		axis.min = min;
	}

	@SuppressWarnings("unchecked")
	private void setTicks(AxisData axis, AxisOption axisOption) {
		if (!axis.used) {
			return;
		}
		if (axisOption.ticks == null) {
			axis.ticks = axis.tickGenerator.generator(axis);
		} else if (axisOption.ticks instanceof Integer) {
			int tickCnt = ((Integer) axisOption.ticks).intValue();
			if (tickCnt > 0) {
				axis.ticks = axis.tickGenerator.generator(axis);
			}
		} else if (axisOption.ticks instanceof Vector<?>) {
			axis.ticks = new Vector<TickData>();
			Vector<TickData> ticks = (Vector<TickData>) (axisOption.ticks);
			if (ticks != null) {
				for (int i = 0; i < ticks.size(); i++) {
					String label = "";
					double v = 0;
					TickData td = ticks.get(i);
					if (td != null) {
						v = td.v;
						label = td.label;
						if (label == null || label.length() == 0)
							label = axis.tickFormatter.formatNumber(v, axis);
						axis.ticks.add(new TickData(v, label));
					}
				}
			}
		}

		if (!Double.isNaN(axisOption.autoscaleMargin) && axis.ticks.size() > 0) {
			if (Double.isNaN(axisOption.min)) {
				axis.min = Math.min(axis.min, axis.ticks.get(0).v);
			}
			if (Double.isNaN(axisOption.max)) {
				axis.max = Math.max(axis.max, axis.ticks
						.get(axis.ticks.size() - 1).v);
			}
		}
	}

	private void setTransformationHelpers(AxisData axis, AxisOption axisOption) {
		DoubleFormatter identify = new DoubleFormatter() {

			@Override
			public double format(double arg0) {
				// TODO Auto-generated method stub
				return arg0;
			}

		};

		final DoubleFormatter t = (axisOption.transform != null ? axisOption.transform
				: identify);
		final DoubleFormatter it = axisOption.inverseTransform;
		final double s;
		final double m;

		if (axis == axes.xaxis || axis == axes.x2axis) {
			s = axis.scale = plotWidth
					/ (t.format(axis.max) - t.format(axis.min));
			m = t.format(axis.min);

			if (t == identify) {
				axis.p2c = new DoubleFormatter() {

					@Override
					public double format(double p) {
						// TODO Auto-generated method stub
						return (p - m) * s;
					}

				};
			} else {
				axis.p2c = new DoubleFormatter() {

					@Override
					public double format(double p) {
						// TODO Auto-generated method stub
						return (t.format(p) - m) * s;
					}

				};
			}

			if (it == null) {
				axis.c2p = new DoubleFormatter() {

					@Override
					public double format(double c) {
						// TODO Auto-generated method stub
						return m + c / s;
					}

				};
			} else {
				axis.c2p = new DoubleFormatter() {

					@Override
					public double format(double c) {
						// TODO Auto-generated method stub
						return it.format(m + c / s);
					}

				};
			}
		} else {
			s = axis.scale = plotHeight
					/ (t.format(axis.max) - t.format(axis.min));
			m = t.format(axis.max);

			if (t == identify) {
				axis.p2c = new DoubleFormatter() {

					@Override
					public double format(double p) {
						// TODO Auto-generated method stub
						return (m - p) * s;
					}

				};
			} else {
				axis.p2c = new DoubleFormatter() {

					@Override
					public double format(double p) {
						// TODO Auto-generated method stub
						return (m - t.format(p)) * s;
					}

				};
			}

			if (it == null) {
				axis.c2p = new DoubleFormatter() {

					@Override
					public double format(double c) {
						// TODO Auto-generated method stub
						return m - c / s;
					}

				};
			} else {
				axis.c2p = new DoubleFormatter() {

					@Override
					public double format(double c) {
						// TODO Auto-generated method stub
						return it.format(m - c / s);
					}

				};
			}
		}
	}

	public void setupGrid() {
		Hashtable<AxisData, AxisOption> ht = new Hashtable<AxisData, AxisOption>();

		ht.put(axes.xaxis, options.xaxis);
		ht.put(axes.yaxis, options.yaxis);
		ht.put(axes.x2axis, options.x2axis);
		ht.put(axes.y2axis, options.y2axis);

		Enumeration<AxisData> e = ht.keys();

		while (e.hasMoreElements()) {
			AxisData axis = e.nextElement();
			this.setRange(axis, ht.get(axis));
		}
 
		if (grap != null && options.grid.show) {
			e = ht.keys();

			while (e.hasMoreElements()) {
				AxisData axis = e.nextElement();

				prepareTickGeneration(axis, ht.get(axis));
				setTicks(axis, ht.get(axis));
				measureLabels(axis, ht.get(axis));
			}
 
			setGridSpacing();
		} else {
			plotOffset.left = plotOffset.right = plotOffset.top = plotOffset.bottom = 0;
			plotWidth = canvasWidth;
			plotHeight = canvasHeight;
		}

		e = ht.keys();

		while (e.hasMoreElements()) {
			AxisData axis = e.nextElement();
			this.setTransformationHelpers(axis, ht.get(axis));
		}
	}

	public void triggerRedrawOverlay() {

	}

	public void unhighlight(SeriesData s, double[] point) {
		if (s == null && point == null) {
			highlights.clear();
			// ###Redraw
			return;
		}

		int i = indexOfHighlight(s, point);
		if (i != -1) {
			highlights.remove(i);
			// ###Redraw
		}
	}

	private void updateAxis(AxisData axis, double min, double max) {
		if (Double.isNaN(axis.datamin) || min < axis.datamin)
			axis.datamin = min;
		if (Double.isNaN(axis.datamax) || max > axis.datamax)
			axis.datamax = max;
	}

	public int width() {
		return plotWidth;
	}
}
