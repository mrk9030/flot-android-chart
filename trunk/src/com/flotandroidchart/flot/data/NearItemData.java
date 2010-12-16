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

public class NearItemData {
	public double[] datapoint;
	public int dataIndex;
	public SeriesData series;
	public int seriesIndex;
	public int pageX;
	public int pageY;
	
	public NearItemData(double[] datapoint,
			             int dataIndex,
			             SeriesData series,
			             int seriesIndex) {
		this.datapoint = datapoint;
		this.dataIndex = dataIndex;
		this.series = series;
		this.seriesIndex = seriesIndex;
	}
	                    
}
