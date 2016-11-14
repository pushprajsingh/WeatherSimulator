package com.weather.app;

import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.util.Arrays;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.weather.functions.ParseDoubleDatatoLabeledPoint;
import com.weather.functions.ParseTextDatatoLabeledPoint;
import com.weather.model.MLibModel;
import com.weather.utils.IConstants;
import com.weather.utils.WeatherUtils;

import scala.Tuple2;

public class SparkFunctionsTest implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static JavaRDD<String> data=null;
	private static SparkConf conf = null;
	private static JavaSparkContext sc = null;
	static final String path = "data/training_data.csv";
	
	@BeforeClass
	public static void loadTestDataFromFile()
	{

		conf = new SparkConf().setAppName("SparkWeatherPrediction").setMaster("local[*]");
		sc = new JavaSparkContext(conf);
		
	
		JavaRDD<String> rawdata = sc.textFile(path);
		data = rawdata.mapToPair(new PairFunction<String, Double, String>() {
	        @Override
	        public Tuple2<Double, String> call(String s) throws Exception {
	        	String splits[]=s.split(",");
	        	Double val=0.0;
	        	try
	        	{
	        		val=Double.parseDouble(splits[IConstants.TIME]);
	        	}
	        	catch(Exception e)
	        	{
	        		val=0.0;
	        	}
	            return new Tuple2<Double,String>(val,s);
	        }
	    }).sortByKey(true).map(f -> f._2());
		
		//sc.stop();
	}
	
	@AfterClass
	public static void performCleanUp()
	{
		sc.stop();
	}
	
			
			
	@Test
	public void ParseDoubleDatatoLabeledPointTest() {	     
		MLibModel testModel = WeatherUtils.getMLibModel(Arrays.asList(IConstants.ELEVATION, IConstants.LATITUDE, IConstants.LONGITUDE, IConstants.TIME), Arrays.asList(IConstants.HUMIDITY));
		JavaRDD<LabeledPoint> records  =  data.map(new ParseDoubleDatatoLabeledPoint(testModel));
		assertTrue(records.count() > 0);

	}
	
	@Test
	public void ParseDoubleDatatoLabeledPointNullStringTest() {	     
		MLibModel testModel = WeatherUtils.getMLibModel(null,null);
		JavaRDD<LabeledPoint> records  =  data.map(new ParseDoubleDatatoLabeledPoint(testModel));
		double returnArr[]=records.first().features().toArray();		
		assertTrue(returnArr.length == 0 );
	}
	
	@Test
	public void ParseDoubleDatatoLabeledPointInvalidIndexTest() {	     
		MLibModel testModel = WeatherUtils.getMLibModel(Arrays.asList(-20, IConstants.LATITUDE, IConstants.LONGITUDE, IConstants.TIME), Arrays.asList(IConstants.HUMIDITY));
		JavaRDD<LabeledPoint> records  =  data.map(new ParseDoubleDatatoLabeledPoint(testModel));
		double returnArr[]=records.first().features().toArray();		
		assertTrue(returnArr[0]==0);
	}
	
	@Test
	public void ParseTextDatatoLabeledPointNullStringTest() {
		MLibModel testModel = WeatherUtils.getMLibModel(null,null);
		JavaRDD<LabeledPoint> records  =  data.map(new ParseTextDatatoLabeledPoint(testModel));
		double returnArr[]=records.first().features().toArray();		
		assertTrue(returnArr.length == 0 );

	}
	
	@Test
	public void ParseTextDatatoLabeledPointTest() {
		MLibModel testModel = WeatherUtils.getMLibModel(Arrays.asList(IConstants.ELEVATION, IConstants.HUMIDITY, IConstants.LATITUDE, IConstants.LONGITUDE,IConstants.TEMPRATURE), Arrays.asList(IConstants.CONDITION));
		JavaRDD<LabeledPoint> records  =  data.map(new ParseTextDatatoLabeledPoint(testModel));
		assertTrue(records.count() > 0);

	}

	
	@Test
	public void ParseTextDatatoLabeledPointInvalidIndexTest() {
		MLibModel testModel = WeatherUtils.getMLibModel(Arrays.asList(300, IConstants.HUMIDITY, IConstants.LATITUDE, IConstants.LONGITUDE,IConstants.TEMPRATURE), Arrays.asList(IConstants.CONDITION));
		JavaRDD<LabeledPoint> records  =  data.map(new ParseTextDatatoLabeledPoint(testModel));
		double returnArr[]=records.first().features().toArray();		
		assertTrue(returnArr[0]==0);

	}
	
	
}
