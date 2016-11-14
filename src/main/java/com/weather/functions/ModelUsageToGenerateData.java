package com.weather.functions;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.tree.model.RandomForestModel;

import com.weather.googleAPI.ElevationFinder;
import com.weather.googleAPI.LocationFinder;
import com.weather.googleAPI.pojos.ElevatorPojo;
import com.weather.googleAPI.pojos.GeoResults;
import com.weather.googleAPI.pojos.Location;
import com.weather.googleAPI.pojos.WeatherResults;
import com.weather.utils.EpochTimeDetails;
import com.weather.utils.IConstants;
import com.weather.utils.WeatherUtils;

import scala.Serializable;


public class ModelUsageToGenerateData implements Function<String, WeatherResults>, Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Log LOG = LogFactory.getLog(ModelUsageToGenerateData.class);
	Map<String,RandomForestModel> models=null;	
	final LocationFinder lf = new LocationFinder();
	final ElevationFinder aa=new ElevationFinder();
	
	public ModelUsageToGenerateData(Map<String,RandomForestModel> models) {

		this.models=models;
	}
	

	@Override
	public WeatherResults call(String arg0) throws Exception {
		String splits[]=arg0.split("#");
		Location loc=lf.getLocation(splits[0]);	
		EpochTimeDetails epochTime=null;
		if (splits[0] == null || splits[0].trim().length() == 0) {
			epochTime = new EpochTimeDetails("Europe/London", 0);
		} else {
			epochTime = new EpochTimeDetails(splits[1], 0);

		}
		long localTime = epochTime.getEopchTime();
		String strEcopchDatetime=epochTime.getEopchDateTimeStr();						
		String geoLoc=loc.getLat()+","+loc.getLng();
		ElevatorPojo elepojo=aa.getElevationObject(geoLoc);							
		
		GeoResults[] results=elepojo.getResults();
		GeoResults res=results[0];
		double elevation = res.getElevation();
		double latitude = res.getLocation().getLat();
		double longitude =res.getLocation().getLng();
		
		double humidity=models.get(IConstants.MODEL_HUMIDITY).predict(Vectors.dense(new double[] { elevation,latitude, longitude,localTime  }));
		double pressure=models.get(IConstants.MODEL_PRESSURE).predict(Vectors.dense(new double[] { elevation, latitude, longitude,localTime }));
		double temperature=models.get(IConstants.MODEL_TEMPRATURE).predict(Vectors.dense(new double[] { elevation, latitude, longitude,localTime }));
		
		Double conditon = models.get(IConstants.MODEL_CONDITION).predict(Vectors.dense(new double[] { elevation, humidity, latitude, longitude, pressure,temperature,localTime }));
		String conditonDerived = WeatherUtils.getCondition(WeatherUtils.getOptimizedValue(conditon));	
				
		String position=String.format("%.2f,%.2f,%.0f",loc.getLat(),loc.getLng(),elevation);
		WeatherResults localRes=new WeatherResults(splits[0],position,strEcopchDatetime,conditonDerived,temperature,pressure,humidity);
		return localRes;
	}

	
	
	
}
