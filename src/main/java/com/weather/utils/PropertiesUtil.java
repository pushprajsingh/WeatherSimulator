package com.weather.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {

    private String prop_filePath="";
    Properties mainProperties = null;
    
    
    public PropertiesUtil(String filePath) {
    	prop_filePath=filePath;
    	try {
			loadPropertiesFile(prop_filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
    public Object getPropVal(String key)
    {
	    //retrieve the property we are interested
	    Object obj = mainProperties.getProperty(key);
	    return obj;
    }

	
	public void loadPropertiesFile(String filePath) throws IOException{

         //to load application's properties, we use this class
	    mainProperties = new Properties();

	    //load the file handle for main.properties
	    FileInputStream file = new FileInputStream(prop_filePath);

	    //load all the properties from this file
	    mainProperties.load(file);

	    //we have loaded the properties, so close the file handle
	    file.close();
	}
	
}
