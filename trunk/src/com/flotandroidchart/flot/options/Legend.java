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

import com.flotandroidchart.flot.format.StringFormatter;

public class Legend {

	public Boolean show = true;
	public int noColumns = 1;
	public StringFormatter labelFormatter = null;
	public int labelBoxBorderColor = 0xccc;
	public Object container = null;
	public String position = "ne";
	public int margin = 5;
	public String backgroundColor = null;
	public double backgroundOpacity = 0.85;

}
