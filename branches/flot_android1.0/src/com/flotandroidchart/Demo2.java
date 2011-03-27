package com.flotandroidchart;

import java.util.Vector;

import com.flotandroidchart.flot.FlotChartContainer;
import com.flotandroidchart.flot.FlotDraw;
import com.flotandroidchart.flot.data.PointData;
import com.flotandroidchart.flot.data.SeriesData;

import android.app.Activity;
import android.os.Bundle;

public class Demo2 extends Activity {

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
		sd1.series.lines.setShow(true);
		sd1.series.lines.fill = true;
		sds.add(sd1);
		
		SeriesData sd2 = new SeriesData();		
		sd2.setData(new double[][]{{0, 3}, {4, 8}, {8, 5}, {9, 13}});
		sd2.series.bars.show = true;
		sds.add(sd2);
		
		SeriesData sd3 = new SeriesData();
		Vector<PointData> pds1 = new Vector<PointData>();
		for(double i=0; i < 14; i+=0.5) {
			pds1.add(new PointData(i, Math.cos(i)));
		}
		sd3.setData(pds1);
		sd3.series.points.show = true;
		sds.add(sd3);
		
		SeriesData sd4 = new SeriesData();
		Vector<PointData> pds2 = new Vector<PointData>();
		for(double i=0; i < 14; i+=0.1) {
			pds2.add(new PointData(i, Math.sqrt(i * 10)));
		}
		sd4.setData(pds2);
		sd4.series.lines.setShow(true);
		sds.add(sd4);
		
		SeriesData sd5 = new SeriesData();
		Vector<PointData> pds3 = new Vector<PointData>();
		for(double i=0; i < 14; i+=0.5) {
			pds3.add(new PointData(i, Math.sqrt(i)));
		}
		sd5.setData(pds3);
		sd5.series.lines.setShow(true);
		sd5.series.points.show = true;
		sds.add(sd5);
		
		SeriesData sd6 = new SeriesData();
		Vector<PointData> pds4 = new Vector<PointData>();
		for(double i=0; i < 14; i+=0.5 + Math.random()) {
			pds4.add(new PointData(i, Math.sqrt(2*i + Math.sin(i) + 5)));
		}
		sd6.setData(pds4);
		sd6.series.lines.setShow(true);
		sd6.series.lines.steps = true;
		sds.add(sd6);
		
		FlotDraw fd = new FlotDraw(sds, null, null);

        setContentView(R.layout.main);
        FlotChartContainer tv = (FlotChartContainer)this.findViewById(R.id.flotdraw);
        if(tv != null) {
            tv.setDrawData(fd);
        }
    }
}
