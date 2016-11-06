package com.weather.app;


import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.weather.utils.EpochTimeDetails;

public class TestElevation {
public static void main(String[] args) {
	
/*	outputString{   "results" : [      {         "address_components" : [            {               "long_name" : "Melbourne",               "short_name" : "Melbourne",               "types" : [ "colloquial_area", "locality", "political" ]            },            {               "long_name" : "Victoria",               "short_name" : "VIC",               "types" : [ "administrative_area_level_1", "political" ]            },            {               "long_name" : "Australia",               "short_name" : "AU",               "types" : [ "country", "political" ]            }         ],         "formatted_address" : "Melbourne VIC, Australia",         "geometry" : {            "bounds" : {               "northeast" : {                  "lat" : -37.5112737,                  "lng" : 145.5125287               },               "southwest" : {                  "lat" : -38.4338593,                  "lng" : 144.5937418               }            },            "location" : {               "lat" : -37.8136276,               "lng" : 144.9630576            },            "location_type" : "APPROXIMATE",            "viewport" : {               "northeast" : {                  "lat" : -37.5112737,                  "lng" : 145.5125287               },               "southwest" : {                  "lat" : -38.4338593,                  "lng" : 144.5937418               }            }         },         "partial_match" : true,         "place_id" : "ChIJ90260rVG1moRkM2MIXVWBAQ",         "types" : [ "colloquial_area", "locality", "political" ]      },      {         "address_components" : [            {               "long_name" : "Melbourne",               "short_name" : "Melbourne",               "types" : [ "locality", "political" ]            },            {               "long_name" : "Victoria",               "short_name" : "VIC",               "types" : [ "administrative_area_level_1", "political" ]            },            {               "long_name" : "Australia",               "short_name" : "AU",               "types" : [ "country", "political" ]            },            {               "long_name" : "3004",               "short_name" : "3004",               "types" : [ "postal_code" ]            }         ],         "formatted_address" : "Melbourne VIC 3004, Australia",         "geometry" : {            "bounds" : {               "northeast" : {                  "lat" : -37.79944589999999,                  "lng" : 144.989097               },               "southwest" : {                  "lat" : -37.8555268,                  "lng" : 144.951435               }            },            "location" : {               "lat" : -37.81361100000001,               "lng" : 144.963056            },            "location_type" : "APPROXIMATE",            "viewport" : {               "northeast" : {                  "lat" : -37.79944589999999,                  "lng" : 144.989097               },               "southwest" : {                  "lat" : -37.8555268,                  "lng" : 144.951435               }            }         },         "partial_match" : true,         "place_id" : "ChIJgf0RD69C1moR4OeMIXVWBAU",         "types" : [ "locality", "political" ]      }   ],   "status" : "OK"}
	-37.8136276
	144.9630576
	1608.637939453125
	39.7391536    -104.9847034
	-50.78903961181641
	36.455556    -116.866667*/
/*	
	
	LocationFinder lf = new LocationFinder();
	Location loc=lf.getLocation("Melbourn,Australia");
	
	ElevationFinder aa=new ElevationFinder();
	String geoLoc=loc.getLat()+","+loc.getLng();
	ElevatorPojo elepojo=aa.getElevationObject(geoLoc);
	
	for(GeoResults res:elepojo.getResults())
	{
		System.out.println(res.getElevation());
		System.out.println(res.getLocation().getLat() +"    "+ res.getLocation().getLng());
	}*/

	//long now = Instant.now().getEpochSecond();
	//1478434206876
	//1451700000
	//1478434272
	//System.out.println(now);
	
	DateTime dtOrg = new DateTime(DateTime.now());
	DateTime dtPlusOne = dtOrg.plusDays(0);
	TimeZone timeZone = TimeZone.getTimeZone("EET");
	DateTimeZone zone = DateTimeZone.forTimeZone(timeZone);
	DateTimeFormatter FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss Z").withZone(zone);	
	System.out.println(dtPlusOne.toString(FORMATTER));	
	System.out.println(dtPlusOne.toDate().getTime()/1000);	
	EpochTimeDetails aa=new EpochTimeDetails("IST",0);	
	aa.getEopchDateTimeStr();
}

  

}
