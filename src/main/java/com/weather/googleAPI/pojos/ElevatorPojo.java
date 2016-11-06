package com.weather.googleAPI.pojos;

public class ElevatorPojo {
	 private GeoResults[] results;

	    private String status;

	    public GeoResults[] getResults ()
	    {
	        return results;
	    }

	    public void setResults (GeoResults[] results)
	    {
	        this.results = results;
	    }

	    public String getStatus ()
	    {
	        return status;
	    }

	    public void setStatus (String status)
	    {
	        this.status = status;
	    }

	    @Override
	    public String toString()
	    {
	        return "ClassPojo [results = "+results+", status = "+status+"]";
	    }
}
