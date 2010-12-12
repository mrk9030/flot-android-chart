package com.flotandroidchart.flot.options;

public class ColorHelper {
	public int _r;
	public int _g;
	public int _b;
	public int _a;

	public ColorHelper(int r, int g, int b, int a) {
		this._r = r;
		this._g = g;
		this._b = b;
		this._a = a;
		normalize();
	}

	public ColorHelper(int r, int g, int b) {
		this._r = r;
		this._g = g;
		this._b = b;
		this._a = 255;
		normalize();
	}

	public ColorHelper(int rgb) {
		this._r = (rgb >> 16) & 0xff;
		this._g = (rgb >> 8) & 0xff;
		this._b = rgb & 0xff;
		this._a = 255;
	}

	public ColorHelper add(String rgb, int var) {

		if (rgb.indexOf('r') != -1) {
			_r += var;
		}

		if (rgb.indexOf('g') != -1) {
			_g += var;
		}

		if (rgb.indexOf('b') != -1) {
			_b += var;
		}

		return normalize();
	}

	public ColorHelper scale(String rgb, double var) {

		if (rgb.indexOf('r') != -1) {
			_r *= var;
		}

		if (rgb.indexOf('g') != -1) {
			_g *= var;
		}

		if (rgb.indexOf('b') != -1) {
			_b *= var;
		}

		return normalize();
	}

	public ColorHelper normalize() {
		_r = (int) clamp(0, this._r, 255);
		_g = (int) clamp(0, this._g, 255);
		_b = (int) clamp(0, this._b, 255);
		_a = (int) clamp(0, this._a, 255);
		return this;
	}

	public int rgb() {
		return (this._r << 16) | (this._g << 8) | this._b;
	}

	public double clamp(double min, double value, double max) {
		return value < min ? min : (value > max ? max : value);
	}
}
