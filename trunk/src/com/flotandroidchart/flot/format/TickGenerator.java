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

package com.flotandroidchart.flot.format;

import java.util.Vector;

import com.flotandroidchart.flot.data.AxisData;
import com.flotandroidchart.flot.data.TickData;

;

public class TickGenerator {
	public Vector<TickData> generator(AxisData axis) {
		Vector<TickData> ticks = new Vector<TickData>();

		double start = floorInBase(axis.min, axis.tickSize.number);
		int i = 0;
		double v = Double.NaN;
		double prev;

		do {
			prev = v;
			v = start + i * axis.tickSize.number;
			ticks.add(new TickData(v, (axis.tickFormatter == null ? String
					.valueOf(v) : axis.tickFormatter.formatNumber(v, axis))));
			++i;
		} while (v < axis.max && v != prev);
		return ticks;
	}

	protected double floorInBase(double n, double base) {
		return base * Math.floor(n / base);
	}
}
