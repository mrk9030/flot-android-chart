package com.flotandroidchart;

import java.util.Vector;

import com.flotandroidchart.flot.FlotChartContainer;
import com.flotandroidchart.flot.FlotDraw;
import com.flotandroidchart.flot.Options;
import com.flotandroidchart.flot.data.MarkingData;
import com.flotandroidchart.flot.data.PointData;
import com.flotandroidchart.flot.data.RangeData;
import com.flotandroidchart.flot.data.SeriesData;
import com.flotandroidchart.flot.data.TickData;

import android.app.Activity;
import android.os.Bundle;

public class Demo6 extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Vector<SeriesData> sds = new Vector<SeriesData>();
		
		
		SeriesData sd1 = new SeriesData();
		Vector<PointData> pds = new Vector<PointData>();
		for(double i=0; i < 20; i++) {
			pds.add(new PointData(i, Math.sin(i)));
		}
		sd1.setData(pds);
		sd1.label="Pressure";
		sd1.series.color = 0xee000333;
		sd1.series.bars.show = true;
		sd1.series.bars.barWidth = 0.5f;
		sds.add(sd1);
		
		Options opt = new Options();
		opt.xaxis.autoscaleMargin = 0.02;
		opt.xaxis.ticks = new Vector<TickData>();
		opt.yaxis.max = 2;
		opt.yaxis.min = -2;
		Vector<MarkingData> markings = new Vector<MarkingData>();
		markings.add(new MarkingData(0xf6f6f6, 0, null, new RangeData(.5, Double.NaN)));
		markings.add(new MarkingData(0xf6f6f6, 0, null, new RangeData(Double.NaN, -.5)));
		markings.add(new MarkingData(0xff000000, 1, new RangeData(2, 2), null));
		markings.add(new MarkingData(0xff000000, 1, new RangeData(8, 8), null));
		opt.grid.markings = markings;
		
		FlotDraw fd = new FlotDraw(sds, opt, null);

        setContentView(R.layout.main);
        FlotChartContainer tv = (FlotChartContainer)this.findViewById(R.id.flotdraw);
        if(tv != null) {
            tv.setDrawData(fd);
        }
        
    }
}
