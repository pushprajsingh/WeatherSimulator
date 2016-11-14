package com.weather.functions;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;

import com.weather.model.MLibModel;

import scala.Serializable;


public class ParseDoubleDatatoLabeledPoint implements Function<String, LabeledPoint>, Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final List<Integer> prediction;
	private final List<Integer> label;
	private boolean validAttributes=true;
	private static final Log LOG = LogFactory.getLog(ParseDoubleDatatoLabeledPoint.class);
	
	public ParseDoubleDatatoLabeledPoint(MLibModel mlibModel) {
		this.prediction=mlibModel.getPrediction();
		this.label= mlibModel.getLabel();
		this.validAttributes=checkValidAttributes();
	}	
	
	@Override
	public LabeledPoint call(String arg0) throws Exception {
		//LOG.info("Calling main function of ParseDoubleDatatoLabeledPoint..");
		 if(arg0==null || !validAttributes)
		 {
			 return new LabeledPoint(-1, Vectors.zeros(prediction.size()));
		 }
		 //
		 String[] parts = arg0.split(",");
         double[] v = new double[prediction.size()];
         for(int i=0;i<prediction.size();i++)
         {
        	 Double num;
           	 try
           	 {
           		 num=Double.parseDouble(parts[prediction.get(i)]);
           	 }
           	 catch(NumberFormatException |ArrayIndexOutOfBoundsException e )
           	 {
           		 return new LabeledPoint(-1, Vectors.zeros(prediction.size()));
           	 }
           	 v[i] =num;
        	 //v[i] = Double.parseDouble(parts[prediction.get(i)]) ;
         }
         return new LabeledPoint(Double.parseDouble(parts[label.get(0)]), Vectors.dense(v));     
		
	}
	
	private boolean checkValidAttributes()
	{
		if ( prediction==null || prediction.size()==0 ||
			 label==null || label.size()==0
		   )		
		{
			return false;
		}
		else 
		{
			return true;
		}
	}
	
}
