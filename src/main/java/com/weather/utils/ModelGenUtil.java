package com.weather.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.regression.LinearRegressionModel;
import org.apache.spark.mllib.regression.LinearRegressionWithSGD;
import org.apache.spark.mllib.tree.RandomForest;
import org.apache.spark.mllib.tree.model.RandomForestModel;

public class ModelGenUtil {

	public static LinearRegressionModel getTrainLinearRegressionModel(JavaRDD<LabeledPoint> tainRDD, int numIterations,
			double stepSize) {
		return LinearRegressionWithSGD.train(JavaRDD.toRDD(tainRDD), numIterations, stepSize);
	}

	/**
	 * Generate  RandomForest Regressor for text search
	 * @param tainRDD
	 * @return
	 */
	public static RandomForestModel getTrainRandomForestModel(JavaRDD<LabeledPoint> tainRDD) {
		Map<Integer, Integer> categoricalFeaturesInfo = new HashMap<>();
		Integer numTrees = 10; 
		String featureSubsetStrategy = "all"; 
		String impurity = "variance";
		Integer maxDepth = 24;
		Integer maxBins = 32;
		Integer seed = 12345;
		return RandomForest.trainRegressor(tainRDD, categoricalFeaturesInfo, numTrees, featureSubsetStrategy, impurity,
				maxDepth, maxBins, seed);
	}
	
	/**
	 * Generate  RandomForest Regressor for Numeric search
	 * @param tainRDD
	 * @return
	 */
	
	/*public static RandomForestModel getTrainRandomForestModel_Num(JavaRDD<LabeledPoint> tainRDD) {
		Map<Integer, Integer> categoricalFeaturesInfo = new HashMap<>();
		Integer numTrees = 10; 
		String featureSubsetStrategy = "all"; 
		String impurity = "variance";
		Integer maxDepth = 24;
		Integer maxBins = 32;
		Integer seed = 12345;
		return RandomForest.trainRegressor(tainRDD, categoricalFeaturesInfo, numTrees, featureSubsetStrategy, impurity,
				maxDepth, maxBins, seed);
		
		
	}*/
}
