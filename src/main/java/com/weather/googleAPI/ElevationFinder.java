package com.weather.googleAPI;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import com.weather.googleAPI.pojos.ElevatorPojo;
import com.weather.utils.IConstants;

public class ElevationFinder implements Serializable {

	/**
	 * Get the location coordinator (elevation) from google API for a specified location latitude and 
	 * Longitude provided as parameter.
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ElevatorPojo getElevationObject(String elevationList) {

		URL url;
		ElevatorPojo ep = null;
		try {
			String urlString = IConstants.ELEVATION_URL + URLEncoder.encode(elevationList, "UTF-8");
			url = new URL(urlString);
			HttpURLConnection conn;
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			String line, outputString = "";
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			while ((line = reader.readLine()) != null) {
				outputString += line;
			}
			ep = (ElevatorPojo) JsonGenerator.generateTOfromJson(outputString, ElevatorPojo.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ep;
	}
}
