package com.flotandroidchart;

import java.util.Vector;

import com.flotandroidchart.flot.FlotChartContainer;
import com.flotandroidchart.flot.FlotDraw;
import com.flotandroidchart.flot.IPlugin;
import com.flotandroidchart.flot.Options;
import com.flotandroidchart.flot.data.PointData;
import com.flotandroidchart.flot.data.SeriesData;
import com.flotandroidchart.flot.data.TickData;
import com.flotandroidchart.flot.plugins.CrossHairPlugin;

import android.app.Activity;
import android.os.Bundle;

public class Demo3 extends Activity {


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
				
		Options opt = new Options();
		
		Vector<TickData> ticks = new Vector<TickData>();
		ticks.add(new TickData(0.0, ""));
		ticks.add(new TickData(Math.PI/2, "\u03c0/2"));
		ticks.add(new TickData(Math.PI, "\u03c0"));
		ticks.add(new TickData(Math.PI * 3/2, "3\u03c0/2"));
		ticks.add(new TickData(Math.PI * 2, "2\u03c0"));
		opt.xaxis.ticks = ticks;
		opt.grid.backgroundColor = new int[]{0xfff, 0xeee};
		opt.series.lines.setShow(true);
		opt.series.points.show = true;
		
		//opt.grid.hoverable = true;
		
		opt.yaxis.ticks = new Integer(10);
		opt.yaxis.max = 2;
		opt.yaxis.min = -2;
		
		opt.series.points.show = true;
		opt.series.lines.setShow(true);
		opt.canvas.fill = true;
		opt.canvas.fillColor = new int[]{0xff0000, 0xee};

		FlotDraw fd = new FlotDraw(sds, opt, null);


        setContentView(R.layout.main);
        FlotChartContainer tv = (FlotChartContainer)this.findViewById(R.id.flotdraw);
        if(tv != null) {
            tv.setDrawData(fd);
        }
        
    }
}
