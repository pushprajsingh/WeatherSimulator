package com.weather.googleAPI.pojos;

public class GeoResults
{
    private Location location;

    private double elevation;

    private String resolution;

    public Location getLocation ()
    {
        return location;
    }

    public void setLocation (Location location)
    {
        this.location = location;
    }

    public double getElevation ()
    {
        return elevation;
    }

    public void setElevation (double elevation)
    {
        this.elevation = elevation;
    }

    public String getResolution ()
    {
        return resolution;
    }

    public void setResolution (String resolution)
    {
        this.resolution = resolution;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [location = "+location+", elevation = "+elevation+", resolution = "+resolution+"]";
    }
}