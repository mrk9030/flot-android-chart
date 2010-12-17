package com.flotandroidchart;

import java.util.Vector;

import com.flotandroidchart.flot.FlotChartContainer;
import com.flotandroidchart.flot.FlotDraw;
import com.flotandroidchart.flot.Options;
import com.flotandroidchart.flot.data.PointData;
import com.flotandroidchart.flot.data.SeriesData;
import com.flotandroidchart.flot.data.TickData;

import android.app.Activity;
import android.os.Bundle;

public class vv extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);Vector<SeriesData> sds = new Vector<SeriesData>();
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
		//sds.add(sd3);
		
		
		Options opt = new Options();
		Vector<TickData> ticks = new Vector<TickData>();
		ticks.add(new TickData(0.0, ""));
		ticks.add(new TickData(Math.PI/2, "\u03c0/2"));
		ticks.add(new TickData(Math.PI, "\u03c0"));
		ticks.add(new TickData(Math.PI * 3/2, "3\u03c0/2"));
		ticks.add(new TickData(Math.PI * 2, "2\u03c0"));
		opt.xaxis.ticks = ticks;
		opt.grid.backgroundColor = new int[]{0xffffff, 0xeeeeee};
		opt.grid.clickable = true;
		
		opt.yaxis.ticks = new Integer(10);
		opt.yaxis.max = 2;
		opt.yaxis.min = -2;

		//opt.series.bars.show = true;
		opt.series.lines.setShow(true);

		FlotDraw fd = new FlotDraw(null, sds, opt, null);

        
        FlotChartContainer tv = new FlotChartContainer(this, fd);
        setContentView(tv);
        
    }
}