package com.weather.googleAPI;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import com.weather.googleAPI.pojos.Location;

public class LocationFinder implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Get the location coordinator (latitude , longitude) from google API for a specified location
	 * provided as parameter. 
	 * @param locationName
	 * @return
	 */
	public Location getLocation(String locationURL) {
		Location loc = new Location();
		URL url;
		
		try {
			//String urlString=IConstants.LOCALATION_URL+URLEncoder.encode(locationName, "UTF-8");
			url = new URL(locationURL);

			HttpURLConnection conn;
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			String line, outputString = "";
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			while ((line = reader.readLine()) != null) {
				outputString += line;
			}
			//System.out.println(outputString);
			
			JSONObject jsonObject = new JSONObject(outputString);
			Double strLatitude = jsonObject.getJSONArray("results").getJSONObject(0).getJSONObject("geometry")
					.getJSONObject("location").getDouble("lat");
			loc.setLat(strLatitude);
			
			Double strLongitude = jsonObject.getJSONArray("results").getJSONObject(0).getJSONObject("geometry")
					.getJSONObject("location").getDouble("lng");
			loc.setLng(strLongitude);

		} catch (Exception e) {

			e.printStackTrace();
		}

		return loc;
	}
}
