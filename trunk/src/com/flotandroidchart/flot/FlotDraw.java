package com.flotandroidchart.flot;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.flotandroidchart.flot.data.AxisData;
import com.flotandroidchart.flot.data.Datapoints;
import com.flotandroidchart.flot.data.FormatData;
import com.flotandroidchart.flot.data.RectOffset;
import com.flotandroidchart.flot.data.SeriesData;
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

public class FlotDraw implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Vector<SeriesData> series;

	private Options options;

	private Object canvas;

	private Object overlay;

	private EventHolder eventHolder;

	private Axies axes;

	private RectOffset plotOffset = new RectOffset();

	private int canvasWidth;
	private int canvasHeight;
	private int plotWidth;
	private int plotHeight;

	private Object hooks;

	public FlotDraw(Object _canvas, Vector<SeriesData> _data, Options _options,
			Object _plugins) {
		series = _data;
		if(_options == null) {
		    options = new Options();
		}
		else {
			options = _options;
		}
		axes = new Axies();
		axes.xaxis = new AxisData();
		axes.yaxis = new AxisData();
		axes.x2axis = new AxisData();
		axes.y2axis = new AxisData();
		// canvasWidth = 320;
		setData(series);
		// setupGrid();
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

	public void setData(Vector<SeriesData> _series) {
		series = _series;
		fillInSeriesOptions();
		processData();
	}

	private void updateAxis(AxisData axis, double min, double max) {
		if (Double.isNaN(axis.datamin) || min < axis.datamin)
			axis.datamin = min;
		if (Double.isNaN(axis.datamax) || max > axis.datamax)
			axis.datamax = max;
	}

	public void processData() {
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

			Boolean insertSteps = sd.series.lines.getShow()
					&& sd.series.lines.steps;
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

	public void fillInSeriesOptions() {
		for(int k =0;k<series.size();k++) {
			SeriesData s = series.get(k);
			if(!s.series.lines.getShow() && options.series.lines.getShow()) {
				s.series.lines.setShow(options.series.lines.getShow());
			}
			if(!s.series.points.show && options.series.points.show) {
				s.series.points.show = options.series.points.show;
			}
			if(!s.series.bars.show && options.series.bars.show) {
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

		if (options.grid.show) {
			insertLabels();
		}
	}

	private void drawGrid() {
		AffineTransform old = grap.getTransform();

		grap.translate(plotOffset.left, plotOffset.top);
		grap.setColor(new Color(options.grid.tickColor, true));
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
				shape.moveTo(Math.floor(axis.p2c.format(v)) + .5, 0);
				shape.lineTo(Math.floor(axis.p2c.format(v)) + .5, plotHeight);
				// grap.drawLine((int)(Math.floor(axis.p2c.format(v)) + .5), 0,
				// (int)(Math.floor(axis.p2c.format(v)) + .5), plotHeight);
			}
		}

		axis = axes.yaxis;
		if (axis.used) {
			for (int i = 0; i < axis.ticks.size(); i++) {
				double v = axis.ticks.get(i).v;
				if (v < axis.datamin || v > axis.datamax) {
					continue;
				}
				shape.moveTo(0, Math.floor(axis.p2c.format(v)) + .5);
				shape.lineTo(plotWidth, Math.floor(axis.p2c.format(v)) + .5);
				// grap.drawLine(0, (int)(Math.floor(axis.p2c.format(v)) + .5),
				// plotWidth, (int)(Math.floor(axis.p2c.format(v)) + .5));
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
				// grap.drawLine((int)(Math.floor(axis.p2c.format(v)) + .5), -5,
				// (int)(Math.floor(axis.p2c.format(v)) + .5), 5);
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

		grap.setColor(new Color(options.grid.borderColor));
		grap.setStroke(new BasicStroke(options.grid.borderWidth,
				BasicStroke.CAP_BUTT, BasicStroke.CAP_ROUND));
		if (options.grid.borderWidth > 0) {
			int bw = options.grid.borderWidth;
			grap.drawRect(-bw / 2, -bw / 2, plotWidth + bw, plotHeight + bw);
		}

		grap.setTransform(old);
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
							(int) axis.labelWidth, (int) axis.labelHeight);
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

	private void draw() {
		Grid grid = options.grid;

		if (grid.show && !grid.aboveData) {
			drawGrid();
		}

		for (int i = 0; i < series.size(); i++) {
			drawSeries(series.get(i));
		}

		if (grid.show && grid.aboveData) {
			drawGrid();
		}
		
		insertLegend();
	}
	
	private void insertLegend() {
		if(!options.legend.show) {
			return;
		}
		FontMetrics fm = grap.getFontMetrics();
		int max = 0;
		int cnt = 0;
		for(int i=0;i<series.size();i++) {
			SeriesData s = series.get(i);
			String label = s.label;
			if(options.legend.labelFormatter != null){
				label = options.legend.labelFormatter.format(label, s);
			}
			if(label == null || label.length() == 0) {
				continue;
			}
			int labelWdt = fm.stringWidth(label);
			if(labelWdt > max) {
				max = labelWdt;
			}
			cnt++;
		}
		if(cnt == 0)
			return;
		int width = max + 20;
		int height = (fm.getHeight() > 16 ? fm.getHeight() : 16)+4;
		int nWidth = Math.min(cnt, options.legend.noColumns);
		int nHeight = cnt / options.legend.noColumns + (cnt % options.legend.noColumns == 0 ? 0 : 1);
		
		AffineTransform old = grap.getTransform();

		grap.translate(plotOffset.left + plotWidth - nWidth * width - 10, plotOffset.top + 10);
		grap.setColor(new Color(0xddffffff, true));
		grap.fillRect(0, 0, nWidth * width, height * nHeight);
		
		for(int i=0;i<series.size();i++) {
			SeriesData s = series.get(i);
			String label = s.label;
			if(options.legend.labelFormatter != null){
				label = options.legend.labelFormatter.format(label, s);
			}
			if(label == null || label.length() == 0) {
				continue;
			}
			grap.setColor(new Color(0xcccccc));
			grap.setStroke(new BasicStroke(1));
			grap.drawRect(width * (i%nWidth), (height - 10)/2 + height * (i / nWidth), 14, 10);
			
			grap.setColor(new Color(s.series.color));
			grap.fillRect(2 + width * (i%nWidth), (height - 10)/2 + height * (i/nWidth) + 2, 11, 7);
			
			grap.setColor(new Color(0x545454));
			grap.drawString(label, 18 + width * (i%nWidth), (fm.getAscent() + (height - (fm.getAscent() + fm.getDescent())) / 2) + height * (i/nWidth));
		}
		
		grap.setTransform(old);
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
			plotPoints(false, currentSeries, currentSeries.datapoints, radius, w + w / 2.0,
					180, currentSeries.axes.xaxis, currentSeries.axes.yaxis);

			grap.setColor(new Color(0x32000000, true));
			plotPoints(false, currentSeries, currentSeries.datapoints, radius, w / 2.0, 180,
					currentSeries.axes.xaxis, currentSeries.axes.yaxis);
		}

		grap.setStroke(new BasicStroke(lw, BasicStroke.CAP_BUTT,
				BasicStroke.CAP_ROUND));
		grap.setColor(new Color(currentSeries.series.color));
		plotPoints(true, currentSeries, currentSeries.datapoints, radius, 0, 360,
				currentSeries.axes.xaxis, currentSeries.axes.yaxis);

		grap.setTransform(old);
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

			if (fill && currentSeries.series.points.fill) {
				grap.setPaint(new Color(currentSeries.series.points.fillColor));
				grap.fillArc((int) (axisx.p2c.format(x) - radius),
						(int) (axisy.p2c.format(y) + offset - radius),
						(int) (2 * radius), (int) (2 * radius), 180,
						circumference);
				grap.setColor(new Color(currentSeries.series.color));
				grap.setStroke(new BasicStroke(currentSeries.series.lines.lineWidth,
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

	private void drawSeriesBars(SeriesData currentSeries) {
		AffineTransform old = grap.getTransform();

		grap.translate(plotOffset.left, plotOffset.top);

		double barLeft = currentSeries.series.bars.align.equals("left") ? 0
				: -currentSeries.series.bars.barWidth / 2;
		plotBars(currentSeries, currentSeries.datapoints, barLeft, barLeft
				+ currentSeries.series.bars.barWidth, 0, currentSeries.axes.xaxis,
				currentSeries.axes.yaxis);
		grap.setTransform(old);
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
					barLeft, barRight, offset, axisx, axisy, currentSeries);
		}
	}

	private void drawBar(double x, double y, double b, double barLeft,
			double barRight, double offset, AxisData axisx, AxisData axisy,
			SeriesData currentSeries) {
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
		if (currentSeries.series.bars.fill) {
			grap.setPaint(new Color(currentSeries.series.color | 0x64000000, true));
			c.moveTo(left, bottom);
			c.lineTo(left, top);
			c.lineTo(right, top);
			c.lineTo(right, bottom);
			grap.fill(c);

			c.reset();
		}

		// draw outline
		if (drawLeft || drawRight || drawTop || drawBottom) {
			BasicStroke bstroke = new BasicStroke(currentSeries.series.bars.barWidth,
					BasicStroke.CAP_BUTT, BasicStroke.CAP_ROUND);
			grap.setStroke(bstroke);
			grap.setColor(new Color(currentSeries.series.color));

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
			grap.draw(c);
		}
	}

	private void drawSeriesLines(SeriesData currentSeries) {
		AffineTransform old = grap.getTransform();

		grap.translate(plotOffset.left, plotOffset.top);
		BasicStroke bstroke = new BasicStroke(currentSeries.series.lines.lineWidth,
				BasicStroke.CAP_BUTT, BasicStroke.CAP_ROUND);
		int lw = currentSeries.series.lines.lineWidth;
		int sw = currentSeries.series.shadowSize;
		if (lw > 0 && sw > 0) {
			grap.setColor(new Color(0x19000000, true));
			grap.setStroke(bstroke);
			double angle = Math.PI / 18;
			plotLine(currentSeries.datapoints, Math.sin(angle) * (lw / 2 + sw / 2),
					Math.cos(angle) * (lw / 2 + sw / 2), currentSeries.axes.xaxis,
					currentSeries.axes.yaxis);

			BasicStroke bstroke1 = new BasicStroke(
					currentSeries.series.lines.lineWidth / 2, BasicStroke.CAP_BUTT,
					BasicStroke.CAP_ROUND);
			plotLine(currentSeries.datapoints, Math.sin(angle) * (lw / 2 + sw / 4),
					Math.cos(angle) * (lw / 2 + sw / 4), currentSeries.axes.xaxis,
					currentSeries.axes.yaxis);

		}

		grap.setStroke(bstroke);
		if (options.series.lines.fill) {
			grap.setColor(new Color(options.series.lines.fillColor));
			plotLineArea(currentSeries.datapoints, currentSeries.axes.xaxis,
					currentSeries.axes.yaxis);
		}

		if (lw > 0) {
			grap.setColor(new Color(currentSeries.series.color));
			plotLine(currentSeries.datapoints, 0, 0, currentSeries.axes.xaxis,
					currentSeries.axes.yaxis);
		}
		grap.setTransform(old);
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

	private void addLabels(AxisData axis, DrawFormatter df) {
		for (int i = 0; i < axis.ticks.size(); i++) {
			TickData tick = axis.ticks.get(i);
			if (tick.label == null || tick.v < axis.min || tick.v > axis.max) {
				continue;
			}
			df.draw(axis, tick);
		}
	}

	private void drawCenteredString(String str, int top, int left, int width,
			int height) {
		FontMetrics fm = grap.getFontMetrics();
		int x = left + (width - fm.stringWidth(str)) / 2;
		int y = top
				+ (fm.getAscent() + (height - (fm.getAscent() + fm.getDescent())) / 2)
				- 2;
		grap.drawString(str, x, y);
	}

	private void drawRightString(String str, int top, int left, int width,
			int height) {
		FontMetrics fm = grap.getFontMetrics();
		int x = left + (width - fm.stringWidth(str));
		int y = top
				+ (fm.getAscent() + (height - (fm.getAscent() + fm.getDescent())) / 2);
		grap.drawString(str, x, y);
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

		System.out.println("Width:" + axis.labelWidth + "Height:"
				+ axis.labelHeight);
	}

	private void setTicks(AxisData axis, AxisOption axisOption) {
		if (!axis.used) {
			return;
		}
		if (axisOption.ticks == null) {
			axis.ticks = axis.tickGenerator.generator(axis);
		}
		else if(axisOption.ticks instanceof Integer) {
			int tickCnt = ((Integer)axisOption.ticks).intValue();
			if(tickCnt > 0) {
				axis.ticks = axis.tickGenerator.generator(axis);
			}
		}
		else if(axisOption.ticks instanceof Vector) {
			axis.ticks = new Vector<TickData>();
			Vector<TickData> ticks = (Vector<TickData>)axisOption.ticks;
			if(ticks != null) {
				for(int i=0;i<ticks.size();i++) {
					String label = "";
					double v = 0;
					TickData td = ticks.get(i);
					if(td != null) {
						v = td.v;
						label = td.label;
						if(label == null || label.length() == 0)
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

	private void prepareTickGeneration(AxisData axis, AxisOption axisOption) {
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
			if (axisOption.minTickSize != Double.MIN_VALUE
					&& size < axisOption.minTickSize) {
				size = axisOption.minTickSize;
			}

			if (axisOption.tickSize != Double.MIN_VALUE) {
				size = axisOption.tickSize;
			}

			axis.tickDecimals = Math.max(0, (maxDec != -1 ? maxDec : dec));
			generator = new TickGenerator();
			formatter = new TickFormatter();
		}
		
		axis.tickSize = size;
		axis.tickGenerator = generator;
		
		if(axisOption.tickFormatter != null) {
			axis.tickFormatter = axisOption.tickFormatter;
		}
		else {
		    axis.tickFormatter = formatter;
		}

	}

	Graphics2D grap = null;
	Font defaultFont = null;

	public void draw(Graphics2D g, int width, int height, Font font) {
		grap = g;
		canvasWidth = width;
		canvasHeight = height;
		defaultFont = font;
		setupGrid();
		draw();
		grap = null;
	}

	public Object getPlaceholder() {
		return new Object();
	}

	public Object getCanvas() {
		return canvas;
	}

	public RectOffset getPlotOffset() {
		return plotOffset;
	}

	public int width() {
		return plotWidth;
	}

	public int height() {
		return plotHeight;
	}

	public Object offset() {
		return plotOffset;
	}

	public Vector<SeriesData> getData() {
		return series;
	}

	public Axies getAxes() {
		return axes;
	}

	public Object getOptions() {
		return options;
	}

	public void highlight() {

	}

	public void unhighlight() {

	}

	public void triggerRedrawOverlay() {

	}

	public void pointOffset() {

	}
}
