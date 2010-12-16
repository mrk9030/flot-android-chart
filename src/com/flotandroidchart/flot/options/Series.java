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

package com.flotandroidchart.flot.options;

public class Series {
	public SeriesPoints points = new SeriesPoints();
	public SeriesLines lines = new SeriesLines();
	public SeriesBars bars = new SeriesBars();
	public int shadowSize = 3;
	public int color = -1;

	public void defaultLinesShow() {
		if (!lines.showSet) {
			if (!points.show && !bars.show) {
				lines.setShow(true);
			}
		}
	}
}
