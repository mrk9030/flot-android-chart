package com.flotandroidchart;

import java.util.Vector;

import com.flotandroidchart.flot.FlotChartContainer;
import com.flotandroidchart.flot.FlotDraw;
import com.flotandroidchart.flot.data.PointData;
import com.flotandroidchart.flot.data.SeriesData;

import android.app.Activity;
import android.os.Bundle;

public class Demo1 extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Vector<SeriesData> sds = new Vector<SeriesData>();
		SeriesData sd = new SeriesData();		
		sd.setData(new double[][]{{0, 3}, {4, 8}, {8, 5}, {9, 13}});
		sds.add(sd);
		
		SeriesData sd1 = new SeriesData();
		Vector<PointData> pds = new Vector<PointData>();
		for(double i=0; i < 14; i+=0.5) {
			pds.add(new PointData(i, Math.sin(i)));
		}
		sd1.setData(pds);
		sds.add(sd1);
		
		SeriesData sd2 = new SeriesData();
		sd2.setData(new double[][]{{0, 12}, {7, 12}, null, {7, 2.5}, {12, 2.5}});
		sds.add(sd2);
		
		FlotDraw fd = new FlotDraw(sds, null, null);

        setContentView(R.layout.main);
        FlotChartContainer tv = (FlotChartContainer)this.findViewById(R.id.flotdraw);
        if(tv != null) {
            tv.setDrawData(fd);
        }
        
    }
}
