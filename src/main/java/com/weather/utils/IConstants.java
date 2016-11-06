package com.weather.utils;

public interface IConstants {
	
	// Columns #
	static final Integer SNO = 0;
	static final Integer CONDITION = 8;
	static final Integer ELEVATION = 1;
	static final Integer HUMIDITY = 2;
	static final Integer LATITUDE = 3;
	static final Integer LONGITUDE = 4;
	static final Integer PRESSURE = 5;
	static final Integer TEMPRATURE = 6;
	static final Integer TIME = 7;

	// Data Seperation 
	static final Double TRAIN_DATA_PER = 0.9;
	static final Double TEST_DATA_PER = 0.1;
	
	// Linear Regression stats 
	static final Integer SPLIT_DATA_SEED = 20;
	static final Integer NUM_ITERATION = 650;
	static final Double STEP_SIZE = 0.000000000000000001;

	static final Integer RAIN = 0;
	static final Integer SNOW = 1;
	static final Integer SUNNY = 2;

	static final String STR_RAIN = "RAIN";
	static final String STR_SNOW = "SNOW";
	static final String STR_SUNNY = "SUNNY";
	
	static final String LOCALATION_URL="http://maps.googleapis.com/maps/api/geocode/json?sensor=false&address=";
	static final String ELEVATION_URL="https://maps.googleapis.com/maps/api/elevation/json?locations=";
}
