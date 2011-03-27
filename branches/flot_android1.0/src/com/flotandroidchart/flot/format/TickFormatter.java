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

import com.flotandroidchart.flot.data.AxisData;

public class TickFormatter {
	public double format(double value, AxisData axis) {
		return roundDouble(value, axis.tickDecimals);
	}

	public double roundDouble(double d, int c) {
		int temp = (int) ((d * Math.pow(10, c)));
		return (((double) temp) / Math.pow(10, c));
	}

	public String formatNumber(double val, AxisData axis) {
		if (axis.tickDecimals == 0) {
			return String.valueOf((int) val);
		} else {
			return String.format("%." + axis.tickDecimals + "f", val);
		}
		// return String.valueOf(roundDouble(val, axis.tickDecimals));
	}
}
