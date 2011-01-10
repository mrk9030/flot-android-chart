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

import java.util.Vector;

import javax.swing.JFrame;

import com.flotandroidchart.flot.FlotChartContainer;
import com.flotandroidchart.flot.FlotDraw;
import com.flotandroidchart.flot.IPlugin;
import com.flotandroidchart.flot.Options;
import com.flotandroidchart.flot.data.AxisData;
import com.flotandroidchart.flot.data.PointData;
import com.flotandroidchart.flot.data.SeriesData;
import com.flotandroidchart.flot.data.TickData;
import com.flotandroidchart.flot.options.Axies;
import com.flotandroidchart.flot.options.ColorHelper;
import com.flotandroidchart.flot.plugins.CrossHairPlugin;
import com.flotandroidchart.global.*;

public class vv {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ColorHelper c = new ColorHelper(0xffffdd);
		System.out.print(c._r + "" + c._b);

		Vector<SeriesData> sds = new Vector<SeriesData>();
		SeriesData sd = new SeriesData();
		Vector<PointData> pds = new Vector<PointData>();
		for(double i=0; i < Math.PI * 2; i+=0.25) {
			pds.add(new PointData(i, Math.sin(i)));
		}
		sd.setData(pds);
		sd.label = "sin(x)";
		sds.add(sd);		 
		

		SeriesData sd1 = new SeriesData();
		Vector<PointData> pds1 = new Vector<PointData>();
		for(double i=0; i < Math.PI * 2; i+=0.25) {
			pds1.add(new PointData(i, Math.cos(i)));
		}
		sd1.setData(pds1);
		sd1.label = "cos(x)";
		sds.add(sd1);
		
		SeriesData sd2 = new SeriesData();
		Vector<PointData> pds2 = new Vector<PointData>();
		for(double i=0; i < Math.PI * 2; i+=0.1) {
			pds2.add(new PointData(i, Math.tan(i)));
		}
		sd2.setData(pds2);
		sd2.label = "tan(x)";
		sds.add(sd2);		
		
		
		SeriesData sd3 = new SeriesData();
		sd3.setData(new double[][]{{1, 1}, {3, 2.5}});
		sd3.label = "Bars";
		sd3.series.bars.show = true;
		sds.add(sd3);
		
		
		Options opt = new Options();
		Vector<TickData> ticks = new Vector<TickData>();
		ticks.add(new TickData(0.0, ""));
		ticks.add(new TickData(Math.PI/2, "\u03c0/2"));
		ticks.add(new TickData(Math.PI, "\u03c0"));
		ticks.add(new TickData(Math.PI * 3/2, "3\u03c0/2"));
		ticks.add(new TickData(Math.PI * 2, "2\u03c0"));
		opt.xaxis.ticks = ticks;
		opt.grid.backgroundColor = new int[]{0xffffff, 0xeeeeee};
		opt.grid.hoverable = true;
		
		opt.yaxis.ticks = new Integer(10);
		opt.yaxis.max = 2;
		opt.yaxis.min = -2;

		opt.series.points.show = true;
		opt.series.lines.setShow(true);
		IPlugin[] plugins = new IPlugin[1];
		plugins[0] = new CrossHairPlugin("xy", 0xCCAAA0A0, 2);

		FlotDraw fd = new FlotDraw(null, sds, opt, plugins);
		
		FlotChartContainer f = new FlotChartContainer(fd);
		f.setBounds(0, 0, 320, 480);
		JFrame frame = new JFrame();
		frame.add(f);
		frame.setBounds(0, 0, 320, 500);
		frame.setVisible(true);
		Object dd = new Double(33.0);

		System.out.println((dd instanceof Double) + "Max:"
				+ fd.getAxes().xaxis.datamax);

		AxisData ad = fd.axisSpecToRealAxis(new Axies(), "xaxis");

		System.out.println(ad.datamax);

		// TODO Auto-generated method stub
		EventHolder v = new EventHolder();
		FlotEventListener _l = new FlotEventListener() {

			@Override
			public String Name() {
				// TODO Auto-generated method stub
				return FlotEvent.MOUSE_HOVER;
			}

			@Override
			public void execute(FlotEvent event) {
				// TODO Auto-generated method stub
				System.out.print("Trig");
			}

		};
		v.addEventListener(_l);
		System.out.print("add");
		v.dispatchEvent(FlotEvent.MOUSE_HOVER, new FlotEvent(v));
		v.dispatchEvent("vv", new FlotEvent(v));
		v.dispatchEvent(FlotEvent.MOUSE_HOVER, new FlotEvent(v));
		v.removeEventListener(_l);
		v.dispatchEvent(FlotEvent.MOUSE_HOVER, new FlotEvent(v));
		v.dispatchEvent(FlotEvent.MOUSE_HOVER, new FlotEvent(v));
	}

}
