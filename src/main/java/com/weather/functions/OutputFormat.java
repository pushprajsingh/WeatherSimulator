package com.weather.functions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.spark.api.java.function.Function;

import com.weather.googleAPI.pojos.WeatherResults;

import scala.Serializable;


public class OutputFormat implements Function<WeatherResults, String>, Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Log LOG = LogFactory.getLog(OutputFormat.class);
	
	public OutputFormat() {

	}

	@Override
	public String call(WeatherResults arg0) throws Exception {
		// TODO Auto-generated method stub
		return getOutputFormat(arg0);
	}
	
	/**
	 * Get the String in the specified format as per the requirements. 
	 * @param wr
	 * @return
	 */
	
	private static String getOutputFormat(WeatherResults wr)
	{	
		String retStr= String.format("%s|%s|%s|%s|%.1f|%.1f|%.0f", wr.getCity(),wr.getPosition(),wr.getDate(),
				wr.getCondition(),(5.0/9.0)*(wr.getTemprature()-32),wr.getPressure(),wr.getHumidity()*100);
		return retStr;		
	}
	

	
	
	
	
	
	
}
