package com.weather.app;

import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import com.weather.utils.EpochTimeDetails;

public class EpochTimeDetailsTest {
	@Test
	public void getMLibModelTest() {
		EpochTimeDetails ep = new EpochTimeDetails("IST",0);
		
		SimpleDateFormat simpledatafo = new SimpleDateFormat("yyyy/MM/dd");
		Date newDate = new Date();
		String expectedDate= simpledatafo.format(newDate);				
		String timeString = ep.getEopchDateTimeStr() ;
		assertTrue(ep.getEopchTime() > 1400000000);
		assertTrue(timeString.contains(expectedDate));	
	}	
}
