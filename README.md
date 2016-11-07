# WeatherSimulator
This is a sample project with Spark Mlib to generate weather simulator.

Details of the project.

1. Weather Simulator is making use of Spark Mlib algorithm (RandomForest) [ It can also use LinearRegressionModel for numeric fields ]
2. Generate training data set calling google api's for specified locations, using python script extract_training_data.py
    URL in use are as below from Google
    LOCALATION_URL=http://maps.googleapis.com/maps/api/geocode/json?sensor=false&address=<<latitide,longitude>>
	  ELEVATION_URL=https://maps.googleapis.com/maps/api/elevation/json?locations=<<place>>"
    
    DarkSky.org is used to generatae the public key to get training data for a year with respect to humidity, presssure, 
    temperature and condition, longitude , latitude , altitude and time.  This data is stored in file under "/data/training_data.csv"
    
3. Once we have trainign data we will process records in spark usng RandomForest algorithms, creating 
   training models for PRESSURE,HUMIDITY,TEMPERATURE and CONDITION.
   CONDITION Modle is a probability on three conditions only "RAIN , SNOW , SUNNY"
   
4. Processed results are stored in /data/final_result folder.


Usage: 

1. Download the project
2. Execute the python script "extract_training_data.py" (specify the input file and output file location as per your requirement 
   and update the key generarted from darksky in the code - example here it is 72b0e9e922014d57d775ecc2c55d732f)
2. execute "mvn clean install" 
