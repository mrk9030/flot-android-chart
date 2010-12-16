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

import com.flotandroidchart.flot.format.DoubleFormatter;
import com.flotandroidchart.flot.format.TickFormatter;
import com.flotandroidchart.flot.format.TickGenerator;

public class AxisData {
	public double datamin = Double.NaN;
	public double datamax = Double.NaN;
	public Boolean used = false;
	public double labelHeight = -1;
	public double labelWidth = -1;
	public double max = Double.MAX_VALUE;
	public double min = Double.MIN_VALUE;
	public double scale = 1;
	public int tickDecimals = -1;
	public double tickSize = 0;
	public Vector<TickData> ticks = new Vector<TickData>();
	public TickFormatter tickFormatter = null;
	public TickGenerator tickGenerator = null;

	public DoubleFormatter p2c = null;
	public DoubleFormatter c2p = null;
}
