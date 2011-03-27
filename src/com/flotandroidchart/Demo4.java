package com.flotandroidchart;

import java.util.Vector;

import android.app.Activity;
import android.os.Bundle;

import com.flotandroidchart.flot.FlotChartContainer;
import com.flotandroidchart.flot.FlotDraw;
import com.flotandroidchart.flot.Options;
import com.flotandroidchart.flot.data.PointData;
import com.flotandroidchart.flot.data.SeriesData;
import com.flotandroidchart.flot.data.TickData;

public class Demo4 extends Activity {


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Vector<SeriesData> sds = new Vector<SeriesData>();
		
		SeriesData sd1 = new SeriesData();
		Vector<PointData> pds = new Vector<PointData>();
		for(double i=0; i < 14; i+=0.5) {
			pds.add(new PointData(i, Math.sin(i)));
		}
		sd1.setData(pds);
		sd1.label = "sin(x)";
		sds.add(sd1);
		

		
		SeriesData sd2 = new SeriesData();
		Vector<PointData> pds1 = new Vector<PointData>();
		for(double i=0; i < 14; i+=0.5) {
			pds1.add(new PointData(i, Math.cos(i)));
		}
		sd2.setData(pds1);
		sd2.label = "cos(x)";
		sds.add(sd2);
		
        Options opt = new Options();
		
		opt.grid.hoverable = true;
		
		opt.series.lines.setShow(true);
		opt.series.points.show = true;
		
		opt.yaxis.max = 1.2;
		opt.yaxis.min = -1.2;

		
		FlotDraw fd = new FlotDraw(sds, opt, null);

        setContentView(R.layout.main);
        FlotChartContainer tv = (FlotChartContainer)this.findViewById(R.id.flotdraw);
        if(tv != null) {
            tv.setDrawData(fd);
        }
        
    }
}
