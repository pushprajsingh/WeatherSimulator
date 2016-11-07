package com.weather.functions;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;

import com.weather.model.MLibModel;
import com.weather.utils.IConstants;

import scala.Serializable;

public class ParseTextDatatoLabeledPoint implements Function<String, LabeledPoint>, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final List<Integer> prediction;
	private final List<Integer> label;


	private static final Log LOG = LogFactory.getLog(ParseTextDatatoLabeledPoint.class);

	public ParseTextDatatoLabeledPoint(MLibModel mlibModel) {
		this.prediction = mlibModel.getPrediction();
		this.label = mlibModel.getLabel();
	}

	@Override
	public LabeledPoint call(String arg0) throws Exception {
		// LOG.info("Calling main function of ParseTextDatatoLabeledPoint..");
		if (arg0 == null) {
			return new LabeledPoint(-1, Vectors.zeros(prediction.size()));
		}

		String[] parts = arg0.split(",");
		double[] v = new double[prediction.size()];
		for (int i = 0; i < prediction.size(); i++) {
			Double num;
	       	 try
	       	 {
	       		 num=Double.parseDouble(parts[prediction.get(i)]);
	       	 }
	       	 catch(NumberFormatException e)
	       	 {
	       		 return new LabeledPoint(-1, Vectors.zeros(prediction.size()));
	       	 }
	       	 v[i] =num;
			//v[i] = Double.parseDouble(parts[prediction.get(i)]);
		}

		double condVal = -1;

		if (parts[label.get(0)].toLowerCase().contains("rain")) {
			condVal = IConstants.RAIN;
		} else if (parts[label.get(0)].toLowerCase().contains("snow")) {
			condVal = IConstants.SNOW;
		} else {
			condVal = IConstants.SUNNY;
		}
		return new LabeledPoint(condVal, Vectors.dense(v));

	}

}
