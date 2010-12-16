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

package com.flotandroidchart.flot.data;

import java.util.Vector;

import com.flotandroidchart.flot.options.Axies;
import com.flotandroidchart.flot.options.Series;

public class SeriesData {
	private double[][] data;
	public String label = null;

	public double[][] getData() {
		return this.data;
	}

	public void setData(double[][] data) {
		if (data != null && data.length > 0 && data[0].length == 2) {
			this.data = data;
		} else {
			this.data = null;
		}
	}
	
	public void setData(Vector<PointData> data) {
		if(data != null && data.size() > 0) {
			this.data = new double[data.size()][2];
			for(int i=0;i<data.size();i++) {
				this.data[i][0] = data.get(i).x;
				this.data[i][1] = data.get(i).y;
			}
		}
		else {
			this.data = null;
		}
	}

	public Series series = new Series();
	public Axies axes = new Axies();

	public Datapoints datapoints = new Datapoints();
}
