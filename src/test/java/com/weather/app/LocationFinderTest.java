package com.weather.app;


import static org.junit.Assert.assertTrue;

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

import com.weather.googleAPI.LocationFinder;
import com.weather.googleAPI.pojos.Location;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ URL.class, Location.class,LocationFinder.class,BufferedReader.class,InputStreamReader.class })
public class LocationFinderTest {

	private static final String REQUEST_URL = "http://wwww.google.com";
	
	@Test
	public void getLocationTest() throws Exception {
	        URL url = PowerMockito.mock(URL.class);
	        HttpURLConnection connection = PowerMockito
	                .mock(HttpURLConnection.class);
	        PowerMockito.whenNew(URL.class).withArguments(REQUEST_URL)
	                .thenReturn(url);	       
	        PowerMockito.when(url.openConnection()).thenReturn(connection);
	        InputStream fileinputStream = new FileInputStream("testData/DummyLocationFile.txt");		   
	        PowerMockito.when(connection.getInputStream()).thenReturn(fileinputStream);
	        LocationFinder locationFinder= new LocationFinder();
	        Location location= locationFinder.getLocation(REQUEST_URL);
	        System.out.println(location.getLat() + " , " +location.getLng() );
	        assertTrue(-37.8136276==location.getLat());
	        assertTrue(144.9630576==location.getLng());
	  
	}
	
	@Test
	public void getLocationNoLatitideLongitudeTest() throws Exception {
	        URL url = PowerMockito.mock(URL.class);
	        HttpURLConnection connection = PowerMockito
	                .mock(HttpURLConnection.class);
	        PowerMockito.whenNew(URL.class).withArguments(REQUEST_URL)
	                .thenReturn(url);	       
	        PowerMockito.when(url.openConnection()).thenReturn(connection);
	        InputStream fileinputStream = new FileInputStream("testData/DummyElevationFile.txt");		   
	        PowerMockito.when(connection.getInputStream()).thenReturn(fileinputStream);
	        LocationFinder locationFinder= new LocationFinder();
	        Location location= locationFinder.getLocation(REQUEST_URL);
	        System.out.println(location.getLat() + " , " +location.getLng() );
	        assertTrue(0.0==location.getLat());
	        assertTrue(0.0==location.getLng());
	  
	}
	
	@Test
	public void getLocationExceptionTest() throws Exception {	        
		URL url = PowerMockito.mock(URL.class);
        HttpURLConnection connection = PowerMockito
                .mock(HttpURLConnection.class);
        PowerMockito.whenNew(URL.class).withArguments("tcp:/aaaaa/aaaa")
                .thenReturn(url);	       
        PowerMockito.when(url.openConnection()).thenReturn(connection);
        InputStream fileinputStream = new FileInputStream("testData/DummyLocationFile.txt");		   
        PowerMockito.when(connection.getInputStream()).thenReturn(fileinputStream);
        LocationFinder locationFinder= new LocationFinder();
        Location location= locationFinder.getLocation("tcp:/aaaaa/aaaa");  
        System.out.println(location);
	}
}
