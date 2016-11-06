package com.weather.model;

import java.util.ArrayList;
import java.util.List;

public class MLibModel {
	
	private List<Integer> prediction;
	private List<Integer> label;
	
	public List<Integer> getPrediction() {
		if(prediction==null)
		{
			prediction=new ArrayList<Integer>();
		}
		return prediction;
	}
	public void setPrediction(List<Integer> prediction) {
		this.prediction = prediction;
	}
	public List<Integer> getLabel() {
		if(label==null)
		{
			label=new ArrayList<Integer>();
		}
		return label;
	}
	public void setLabel(List<Integer> label) {
		this.label = label;
	}
	
}
