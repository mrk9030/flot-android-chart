package com.flotandroidchart;

import java.util.Vector;

import com.flotandroidchart.flot.FlotChartContainer;
import com.flotandroidchart.flot.FlotDraw;
import com.flotandroidchart.flot.data.PointData;
import com.flotandroidchart.flot.data.SeriesData;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class DemoTabHost extends TabActivity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.demo_tabhost);
		TabHost tabs = this.getTabHost();
		tabs.addTab(tabs.newTabSpec("Demo1").setIndicator("Line").setContent(
				new Intent(this, Demo1.class)));
		tabs.addTab(tabs.newTabSpec("Demo2").setIndicator("bar").setContent(
				new Intent(this, Demo.class)));
		tabs.addTab(tabs.newTabSpec("Demo3").setIndicator("point").setContent(
				new Intent(this, Demo_List.class)));
		tabs.setCurrentTab(0);
	}
}
