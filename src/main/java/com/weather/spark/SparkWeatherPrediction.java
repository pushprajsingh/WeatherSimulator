package com.weather.spark;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.tree.model.RandomForestModel;

import com.weather.functions.ModelUsageToGenerateData;
import com.weather.functions.OutputFormat;
import com.weather.functions.ParseDoubleDatatoLabeledPoint;
import com.weather.functions.ParseTextDatatoLabeledPoint;
import com.weather.functions.SplitDataToSort;
import com.weather.googleAPI.pojos.WeatherResults;
import com.weather.model.MLibModel;
import com.weather.utils.IConstants;
import com.weather.utils.ModelGenUtil;
import com.weather.utils.PropertiesUtil;
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
		
		PropertiesUtil propertiesUtil = new PropertiesUtil("conf/config.properties");		
		SparkConf conf = new SparkConf().setAppName((String)propertiesUtil.getPropVal("app.name")).setMaster((String)propertiesUtil.getPropVal("app.master"));
		JavaSparkContext sc = new JavaSparkContext(conf);
		
		// file paths to get training data and test data 
		final String path = (String)propertiesUtil.getPropVal("Input.training.filePath");
		final String testLocations = (String)propertiesUtil.getPropVal("Input.TestLoc.filePath");
		final String outPath=(String)propertiesUtil.getPropVal("Output.filePath");
		checkAndDeleteOutPutFolder(outPath);
		
		JavaRDD<String> rawdata = sc.textFile(path);	
		// sorted RDD 
		JavaRDD<String> data = rawdata.mapToPair(new SplitDataToSort()).sortByKey(true).map(f -> f._2());
		
		// Get Sprcific Model running again the traiing data loaded for each criteria
		// Note: We are consideringn all the fields for weather condition derivation.
		Map<String,RandomForestModel> models=getModels(data);
		
		// Load test location data and get the coordinates calling google API for elivation , latitude , longitude. 
		JavaRDD<String> testLocationRDD = sc.textFile(testLocations);
    
		// process the models for each test data to derive the dependent fields. 
		JavaRDD<WeatherResults> perCity = testLocationRDD.map(new ModelUsageToGenerateData(models));

		// Convert the records to output format 
		JavaRDD<String> perCityFormattedData = perCity.map(new OutputFormat());	
    	
		// Save the data to file and check if the file is available before storing latest record. 		
    	perCityFormattedData.saveAsTextFile(outPath);
			
		sc.stop();
	}
	
	
	private static Map<String,RandomForestModel> getModels(JavaRDD<String> data)
	{
		
		Map<String,RandomForestModel> models =new HashMap<String,RandomForestModel>();
		// Get specific Model running again the training data loaded for each criteria
		// Note we are considering all the fields for weather condition derivation.				    
		RandomForestModel humidityModel = getRandomForestModelOnTrainData_Num(data,Arrays.asList(IConstants.ELEVATION, IConstants.LATITUDE, IConstants.LONGITUDE, IConstants.TIME), Arrays.asList(IConstants.HUMIDITY));
		RandomForestModel pressureModel = getRandomForestModelOnTrainData_Num(data,Arrays.asList(IConstants.ELEVATION, IConstants.LATITUDE, IConstants.LONGITUDE, IConstants.TIME), Arrays.asList(IConstants.PRESSURE));
		RandomForestModel tempModel = getRandomForestModelOnTrainData_Num(data,Arrays.asList(IConstants.ELEVATION, IConstants.LATITUDE, IConstants.LONGITUDE, IConstants.TIME), Arrays.asList(IConstants.TEMPRATURE));
		//RandomForestModel condModel = getRandomForestModelOnTrainData(data,Arrays.asList(IConstants.ELEVATION, IConstants.HUMIDITY, IConstants.LATITUDE, IConstants.LONGITUDE, IConstants.PRESSURE, IConstants.TEMPRATURE,IConstants.TIME), Arrays.asList(IConstants.CONDITION));
		RandomForestModel condModel = getRandomForestModelOnTrainData(data,Arrays.asList(IConstants.ELEVATION, IConstants.HUMIDITY, IConstants.LATITUDE, IConstants.LONGITUDE,IConstants.TEMPRATURE), Arrays.asList(IConstants.CONDITION));
			
		models.put(IConstants.MODEL_HUMIDITY, humidityModel);
		models.put(IConstants.MODEL_PRESSURE, pressureModel);
		models.put(IConstants.MODEL_TEMPRATURE, tempModel);
		models.put(IConstants.MODEL_CONDITION, condModel);		
		
		return models;
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
