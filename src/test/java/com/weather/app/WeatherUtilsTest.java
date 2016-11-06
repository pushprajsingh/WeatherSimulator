package com.weather.app;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import com.weather.model.MLibModel;
import com.weather.utils.WeatherUtils;

public class WeatherUtilsTest {

	@Test
	public void getMLibModelTest() {
		MLibModel model = WeatherUtils.getMLibModel(Arrays.asList(1, 2, 3, 4), Arrays.asList(6));
		assertTrue(model.getPrediction().size() == 4 && model.getLabel().size() == 1);

	}
	
	@Test
	public void getConditionTest() {		
	  assertTrue("SNOW".equalsIgnoreCase(WeatherUtils.getCondition(1)));
	}

	@Test
	public void getConditionTest2() {		
	  assertTrue("SUNNY".equalsIgnoreCase(WeatherUtils.getCondition(1000)));
	}
	
	@Test
	public void getConditionTest3() {		
	  assertTrue("SUNNY".equalsIgnoreCase(WeatherUtils.getCondition(-23)));
	}
	
	@Test
	public void getOptimizedValueTest() {		
	  assertTrue(0==WeatherUtils.getOptimizedValue(0.22));
	}
	
	
	@Test
	public void getOptimizedValueTest2() {		
	  assertTrue(2==WeatherUtils.getOptimizedValue(1.52));
	}
	
	@Test
	public void getOptimizedValueTest3(){		
	  assertTrue(2==WeatherUtils.getOptimizedValue(2.22));
	}
	

	
}
