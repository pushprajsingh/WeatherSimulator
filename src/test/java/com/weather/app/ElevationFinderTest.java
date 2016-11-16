package com.weather.app;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.weather.googleAPI.ElevationFinder;
import com.weather.googleAPI.pojos.ElevatorPojo;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ URL.class, ElevatorPojo.class,ElevationFinder.class,BufferedReader.class,InputStreamReader.class })
public class ElevationFinderTest {
	private static final String REQUEST_URL = "http://wwww.google.com";
	
	@Test
	public void getElevationObjectTest() throws Exception {
	        URL url = PowerMockito.mock(URL.class);
	        HttpURLConnection connection = PowerMockito
	                .mock(HttpURLConnection.class);
	        PowerMockito.whenNew(URL.class).withArguments(REQUEST_URL)
	                .thenReturn(url);	       
	        PowerMockito.when(url.openConnection()).thenReturn(connection);
	        InputStream fileinputStream = new FileInputStream("testData/DummyElevationFile.txt");		   
	        PowerMockito.when(connection.getInputStream()).thenReturn(fileinputStream);
	        ElevationFinder elevationFinder= new ElevationFinder();
	        //elevationFinder.getElevationObject(IConstants.ELEVATION_URL + URLEncoder.encode("Melbourne,Australia", "UTF-8"));
	        ElevatorPojo elevatorPojo= elevationFinder.getElevationObject(REQUEST_URL);
	        System.out.println(elevatorPojo.getResults().length);
	        //Assert.assertEquals(1==elevatorPojo.getResults().length);
	        assertEquals(1, elevatorPojo.getResults().length);	       	    
	}
	
	@Test(expected=NullPointerException.class)
	public void getElevationObjectExceptionTest() throws Exception {	        
	        URL url = PowerMockito.mock(URL.class);
	        HttpURLConnection connection = PowerMockito
	                .mock(HttpURLConnection.class);
	        PowerMockito.whenNew(URL.class).withArguments("tcp://abcd/pqrs").thenReturn(url);	       
	        PowerMockito.when(url.openConnection()).thenReturn(connection);
	        InputStream fileinputStream = new FileInputStream("testData/DummyElevationFile.txt");		   
	        PowerMockito.when(connection.getInputStream()).thenReturn(fileinputStream);
	        ElevationFinder elevationFinder= new ElevationFinder();
	        //elevationFinder.getElevationObject(IConstants.ELEVATION_URL + URLEncoder.encode("Melbourne,Australia", "UTF-8"));
	        ElevatorPojo elevatorPojo= elevationFinder.getElevationObject(REQUEST_URL);
	        int count=elevatorPojo.getResults().length;
	        /*//Assert.assertEquals(1==elevatorPojo.getResults().length);
	        assertEquals(1, elevatorPojo.getResults().length);	*/       	    
	}
}
