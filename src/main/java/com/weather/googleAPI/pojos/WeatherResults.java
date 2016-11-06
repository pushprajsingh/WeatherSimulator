package com.weather.googleAPI.pojos;

import java.io.Serializable;

public class WeatherResults implements Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String city;
    private String position;  
    private String date; 
    private String condition; 
    private double temprature;
    private double pressure;
    private double humidity;   
    
    public WeatherResults()
    {
    	
    }
    
    public WeatherResults(String city,String position,String date,String condition,double temprature,double pressure,double humidity)
    {
    	this.city=city;
    	this.position=position;  
    	this.date=date; 
    	this.condition=condition; 
    	this.temprature=temprature;
    	this.pressure=pressure;
    	this.humidity=humidity;
    	
    }    

    public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public double getTemprature() {
		return temprature;
	}

	public void setTemprature(double temprature) {
		this.temprature = temprature;
	}

	public double getPressure() {
		return pressure;
	}

	public void setPressure(double pressure) {
		this.pressure = pressure;
	}

	public double getHumidity() {
		return humidity;
	}

	public void setHumidity(double humidity) {
		this.humidity = humidity;
	}

	
    
    @Override
    public String toString()
    {
        return "ClassPojo ["
        		+ "position = "+position+","
        		+ "date = "+date+","
        		+ "condition = "+condition+","
        		+ "temprature = "+temprature+","
        		+ "pressure = "+pressure+","
         		+ "humidity = "+humidity+","       		
        		+ "city = "+city+
        		"]";
    }
}