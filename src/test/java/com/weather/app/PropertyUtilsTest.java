package com.weather.app;

import static org.junit.Assert.*;

import javax.validation.constraints.AssertTrue;

import org.junit.Test;

import com.weather.utils.PropertiesUtil;


/*
 * 
 * app.version=1.0
app.name=SparkWeatherPrediction
app.master="local[*]"
Input.training.filePath = "data/training_data.csv";
Input.TestLoc.filePath ="data/test_loc.txt";	
Output.filePath="data/final_result/";
 * 
 */
public class PropertyUtilsTest {

	@Test
	public void getPropertesTest() {
		PropertiesUtil propertiesUtil=new PropertiesUtil("conf/config.properties");
		assertTrue(1.0==Float.parseFloat((String)propertiesUtil.getPropVal("app.version")));
		assertEquals("SparkWeatherPrediction", (String)propertiesUtil.getPropVal("app.name"));
		assertEquals("local[*]", (String)propertiesUtil.getPropVal("app.master"));
		assertEquals("data/training_data.csv", (String)propertiesUtil.getPropVal("Input.training.filePath"));
		assertEquals("data/test_loc.txt", (String)propertiesUtil.getPropVal("Input.TestLoc.filePath"));
		assertEquals("data/final_result/", propertiesUtil.getPropVal("Output.filePath"));
		

	}
	
	@Test
	public void getPropertesInvalidPropTest() {
		PropertiesUtil propertiesUtil=new PropertiesUtil("conf/config.properties");
		assertNull(propertiesUtil.getPropVal("INVALID PROPERRY"));
		

	}
	
	
	
}
