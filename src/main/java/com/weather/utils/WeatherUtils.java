package com.weather.utils;

import java.util.List;

import com.weather.model.MLibModel;

public class WeatherUtils {
	public static Integer getOptimizedValue(Double element) {
		int cond_idx = -1;
		if (((element % 1 <= 0.75) && (element / 1 == 0)) || ((element % 1 <= 0.25) && (element / 1 > 0))) // FLOOR
		{
			cond_idx = new Double(Math.floor(element)).intValue();
		} else // CEIL
		{
			cond_idx = new Double(Math.ceil(element)).intValue();
		}
		return cond_idx;
	}

	public static String getCondition(int cond_idx) {
		String retString = "";
		switch (cond_idx) {
		case 0: // rain
		{
			retString = IConstants.STR_RAIN;
			break;
		}
		case 1: // snow
		{
			retString = IConstants.STR_SNOW;
			break;
		}
		default: // sunny
			retString = IConstants.STR_SUNNY;
			break;
		}
		return retString;
	}
	
	public static MLibModel getMLibModel(List<Integer> prediction, List<Integer> label) {
		MLibModel mlib = new MLibModel();
		mlib.setPrediction(prediction);
		mlib.setLabel(label);
		return mlib;
	}
	
}
