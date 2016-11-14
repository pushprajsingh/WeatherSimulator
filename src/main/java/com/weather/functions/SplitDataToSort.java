package com.weather.functions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.spark.api.java.function.PairFunction;

import com.weather.utils.IConstants;

import scala.Serializable;
import scala.Tuple2;


public class SplitDataToSort implements PairFunction<String, Double, String>, Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Log LOG = LogFactory.getLog(SplitDataToSort.class);
	
	public SplitDataToSort() {

	}

	@Override
	public Tuple2<Double, String> call(String arg0) throws Exception {
		// TODO Auto-generated method stub

    	String splits[]=arg0.split(",");
    	Double val=0.0;
    	try
    	{
    		val=Double.parseDouble(splits[IConstants.TIME]);
    	}
    	catch(Exception e)
    	{
    		val=0.0;
    	}
        return new Tuple2<Double,String>(val,arg0);
    
	}	
	
	
	
	
	
}
