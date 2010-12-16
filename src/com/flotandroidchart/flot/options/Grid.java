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

import com.flotandroidchart.flot.format.TooltipFormatter;

public class Grid {
	public Boolean show = true;
	public Boolean aboveData = false;
	public int color = 0x545454;
	public Object backgroundColor = null;
	public int tickColor = 0x26000000;
	public int labelMargin = 5;
	public int borderWidth = 2;
	public int borderColor = 0x545454;
	public Object markings = null;
	public String markingsColor = "#f4f4f4";
	public int markingsLineWidth = 2;
	public Boolean clickable = false;
	public Boolean hoverable = false;
	public Boolean autoHighlight = true;
	public int mouseActiveRadius = 10;
	public int tooltipColor = 0x545454;
	public Object tooltipFillColor = new Integer(0xffff67);
	public TooltipFormatter tooltipFormatter = null;
}
