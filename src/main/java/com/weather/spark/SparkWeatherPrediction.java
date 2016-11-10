package com.weather.spark;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.tree.RandomForest;
import org.apache.spark.mllib.tree.model.RandomForestModel;

import com.weather.functions.ParseDoubleDatatoLabeledPoint;
import com.weather.functions.ParseTextDatatoLabeledPoint;
import com.weather.googleAPI.ElevationFinder;
import com.weather.googleAPI.LocationFinder;
import com.weather.googleAPI.pojos.ElevatorPojo;
import com.weather.googleAPI.pojos.GeoResults;
import com.weather.googleAPI.pojos.Location;
import com.weather.googleAPI.pojos.WeatherResults;
import com.weather.model.MLibModel;
import com.weather.utils.EpochTimeDetails;
import com.weather.utils.IConstants;
import com.weather.utils.ModelGenUtil;
import com.weather.utils.WeatherUtils;

import jodd.io.FileUtil;

/**
 * This is the main porcess spark process that will be called to fetch input file
 * Generate models for each criteria (passing prediction and labels for each model,along with training data.
 * Once the models are available then apply these models on test data fetching from another file.
 * Process each field in format and save in specified directory. 
 * 
 * This process has lot of scope for improvement with respect to exception handling, optimizing algorithms. 
 */
public class SparkWeatherPrediction {
	private static final Log LOG = LogFactory.getLog(SparkWeatherPrediction.class);
	public static void main(String[] args) {
		
		SparkConf conf = new SparkConf().setAppName("SparkWeatherPrediction").setMaster("local[*]");
		JavaSparkContext sc = new JavaSparkContext(conf);
		
		// file paths to get training data and test data 
		final String path = "data/training_data.csv";
		final String testLocations = "data/test_loc.txt";	
		final String outPath="data/final_result/";
		checkAndDeleteOutPutFolder(outPath);
		
		JavaRDD<String> rawdata = sc.textFile(path);   
		
		// sort data 
		JavaRDD<String> data = rawdata.mapToPair(new PairFunction<String, Double, String>() {
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
		
		
		// If Linear Regression is to be used instead of Random Forest Algorithm 
		/*
		LinearRegressionModel humidityModel = getRegressionModelOnTrainData(data,Arrays.asList(IConstants.ELEVATION, IConstants.LATITUDE, IConstants.LONGITUDE, IConstants.TIME), Arrays.asList(IConstants.HUMIDITY));
		LinearRegressionModel pressureModel = getRegressionModelOnTrainData(data,Arrays.asList(IConstants.ELEVATION, IConstants.LATITUDE, IConstants.LONGITUDE, IConstants.TIME), Arrays.asList(IConstants.PRESSURE));
		LinearRegressionModel tempModel = getRegressionModelOnTrainData(data,Arrays.asList(IConstants.ELEVATION, IConstants.LATITUDE, IConstants.LONGITUDE, IConstants.TIME), Arrays.asList(IConstants.TEMPRATURE));
		*/	
		
			// Get Sprcific Model running again the traiing data loaded for each criteria
			// Note we are consideringn all the fields for weather condition derivation.
			RandomForestModel humidityModel = getRandomForestModelOnTrainData_Num(data,Arrays.asList(IConstants.ELEVATION, IConstants.LATITUDE, IConstants.LONGITUDE, IConstants.TIME), Arrays.asList(IConstants.HUMIDITY));
			RandomForestModel pressureModel = getRandomForestModelOnTrainData_Num(data,Arrays.asList(IConstants.ELEVATION, IConstants.LATITUDE, IConstants.LONGITUDE, IConstants.TIME), Arrays.asList(IConstants.PRESSURE));
			RandomForestModel tempModel = getRandomForestModelOnTrainData_Num(data,Arrays.asList(IConstants.ELEVATION, IConstants.LATITUDE, IConstants.LONGITUDE, IConstants.TIME), Arrays.asList(IConstants.TEMPRATURE));
			RandomForestModel condModel = getRandomForestModelOnTrainData(data,Arrays.asList(IConstants.ELEVATION, IConstants.HUMIDITY, IConstants.LATITUDE, IConstants.LONGITUDE, IConstants.PRESSURE, IConstants.TEMPRATURE,IConstants.TIME), Arrays.asList(IConstants.CONDITION));
				
			// Load test location data and get the coordinates calling google API for elivation , latitude , longitude. 
			JavaRDD<String> testLocationRDD = sc.textFile(testLocations);
			final LocationFinder lf = new LocationFinder();
			final ElevationFinder aa=new ElevationFinder();
			
			
			// process the models for each test data to derive the dependent fields. 
			JavaRDD<WeatherResults> perCity = testLocationRDD.map(new Function<String, WeatherResults>(){
				private static final long serialVersionUID = 1L;
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
						
						double humidity=humidityModel.predict(Vectors.dense(new double[] { elevation,latitude, longitude,localTime  }));
						double pressure=pressureModel.predict(Vectors.dense(new double[] { elevation, latitude, longitude,localTime }));
						double temperature=tempModel.predict(Vectors.dense(new double[] { elevation, latitude, longitude,localTime }));
						
						Double conditon = condModel.predict(Vectors.dense(new double[] { elevation, humidity, latitude, longitude, pressure,temperature,localTime }));
						String conditonDerived = WeatherUtils.getCondition(WeatherUtils.getOptimizedValue(conditon));	
								
						String position=String.format("%.2f,%.2f,%.0f",loc.getLat(),loc.getLng(),elevation);
						WeatherResults localRes=new WeatherResults(splits[0],position,strEcopchDatetime,conditonDerived,temperature,pressure,humidity);
						return localRes;
				}			
			}); 			
	
		// convert to specified output format 
		JavaRDD<String> perCityFormattedData = perCity.map(new Function<WeatherResults, String>() {
			@Override
			public String call(WeatherResults arg0) throws Exception {
				return getOutputFormat(arg0);
			}
		});	

		// Save the data to file and check if the file is available before storing latest record. 		
		
		perCityFormattedData.saveAsTextFile(outPath);
			
		sc.stop();
	}
	
	/**
	 * Method to check if output directory existis. If existix the delete it as we are usng spark.
	 * It will fail if there is a direcotry aleady available. 
	 * @param path
	 */
	private static void checkAndDeleteOutPutFolder(String path)
	{
		try {
			File folderExisting = new File(path);		  
			if (folderExisting.exists()){
				FileUtil.deleteDir(path);
			}		
		} catch (IOException e) {		
			e.printStackTrace();
		}
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
	
	// If Linear regression Algo has to be used 
	/*private static LinearRegressionModel getRegressionModelOnTrainData(JavaRDD<String> data,List<Integer> pred,List<Integer> label)
	{		
		LOG.info("Getting LinearRegressionModel model");
		MLibModel mlibModel = WeatherUtils.getMLibModel(pred, label);
		JavaRDD<LabeledPoint>[] rddsplits = data.map(new ParseDoubleDatatoLabeledPoint(mlibModel))
				.randomSplit(new double[] { IConstants.TRAIN_DATA_PER, IConstants.TEST_DATA_PER }, IConstants.SPLIT_DATA_SEED);
		JavaRDD<LabeledPoint> trainData = rddsplits[0];
		return ModelGenUtil.getTrainLinearRegressionModel(trainData, IConstants.NUM_ITERATION, IConstants.STEP_SIZE);		
	}*/
		
	/**
	 * Create and get RandomForest Regressor model for training data for text criteria. 
	 * @param data
	 * @param pred
	 * @param label
	 * @return
	 */
	private static RandomForestModel getRandomForestModelOnTrainData(JavaRDD<String> data,List<Integer> pred,List<Integer> label)
	{
		LOG.info("Getting RandomForestModel model");
		MLibModel condMlibModel = WeatherUtils.getMLibModel(pred, label);
		JavaRDD<LabeledPoint>[] rddsplits_cond = data.map(new ParseTextDatatoLabeledPoint(condMlibModel))
				.randomSplit(new double[] { IConstants.TRAIN_DATA_PER, IConstants.TEST_DATA_PER }, IConstants.SPLIT_DATA_SEED);
		JavaRDD<LabeledPoint> trainData_cond = rddsplits_cond[0];
		return ModelGenUtil.getTrainRandomForestModel(trainData_cond);
	}	
	
	/**
	 * Create and get RandomForest Regressor model for training data for number(double) criteria. 
	 * @param data
	 * @param pred
	 * @param label
	 * @return
	 */
	
	private static RandomForestModel getRandomForestModelOnTrainData_Num(JavaRDD<String> data,List<Integer> pred,List<Integer> label)
	{
		LOG.info("Getting RandomForestModel model for numeric");
		MLibModel condMlibModel = WeatherUtils.getMLibModel(pred, label);
		JavaRDD<LabeledPoint>[] rddsplits_cond = data.map(new ParseDoubleDatatoLabeledPoint(condMlibModel))
				.randomSplit(new double[] { IConstants.TRAIN_DATA_PER, IConstants.TEST_DATA_PER }, IConstants.SPLIT_DATA_SEED);
		JavaRDD<LabeledPoint> trainData_cond = rddsplits_cond[0];
		return ModelGenUtil.getTrainRandomForestModel(trainData_cond);
	}	
	
}
