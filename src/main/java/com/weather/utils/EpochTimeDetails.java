package com.weather.utils;

import java.util.Date;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class EpochTimeDetails {


	String timezone;
	int daysAhead;
	long eopchTime;
	String eopchDateTimeStr;
	
	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public int getDaysAhead() {
		return daysAhead;
	}

	public void setDaysAhead(int daysAhead) {
		this.daysAhead = daysAhead;
	}

	public long getEopchTime() {
		return eopchTime;
	}

	public void setEopchTime(long eopchTime) {
		this.eopchTime = eopchTime;
	}

	public String getEopchDateTimeStr() {
		return eopchDateTimeStr;
	}

	public void setEopchDateTimeStr(String eopchDateTimeStr) {
		this.eopchDateTimeStr = eopchDateTimeStr;
	}

	
	public EpochTimeDetails() {
		getEpochDetails("IST",0);
	}
	
	public EpochTimeDetails(String timezone,int daysAhead) {
		getEpochDetails(timezone,daysAhead);
	}
	
	/**
	 * Derive the epoch details for specified time zone by going back.from sepcified number of days from 
	 * current time. 
	 * 
	 * @param timezone
	 * @param daysAhead
	 */
	private void getEpochDetails(String timezone, int daysAhead)
	{
		DateTime dtOrg = new DateTime(DateTime.now());
		DateTime dtUpdated  = null;
		if (daysAhead >= 0) {
			dtUpdated = dtOrg.plusDays(daysAhead);
		} else {
			dtUpdated = dtOrg.minusDays(Math.abs(daysAhead));
		}
		DateTimeZone zone = DateTimeZone.forTimeZone(TimeZone.getTimeZone(timezone));
		DateTimeFormatter FORMATTER = DateTimeFormat.forPattern("yyyy/MM/dd HH:mm:ss").withZone(zone);	
		Date latestDate = new Date(dtUpdated.toString(FORMATTER));
		dtUpdated.toString(FORMATTER);
		
		eopchDateTimeStr=dtUpdated.toString(FORMATTER);
		eopchTime=latestDate.getTime()/1000;;
	}
	
	
	
}
