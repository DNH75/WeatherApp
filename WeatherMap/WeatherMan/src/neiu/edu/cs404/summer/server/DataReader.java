package neiu.edu.cs404.summer.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

public class DataReader {
	
	static Hashtable<String, Hashtable<Date, WeatherInfo>> cityData = new Hashtable<String, Hashtable<Date, WeatherInfo>>();  
	static Hashtable<String, String> locations = new Hashtable<String, String>();
	public static void main(String[] args) {
		loadData();
	}
	public static String getLocationCode(String location){
		return locations.get(location);
	}
	
	public synchronized static void loadData() {
		try {
			URL oracle = new URL(
					"http://www.meteor.iastate.edu/~ckarsten/bufkit/data/nam/nam_kord.buf");
			BufferedReader in = new BufferedReader(new InputStreamReader(
					oracle.openStream()));
			String inputLine;
			String[] lineValues;
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd/HHmm");
			String city= null;
			Date dateTime = null;
			String dataLine1 = null, dataLine2 = null;
			boolean beforeDataLine = false, dataLine1Read = false;
			WeatherInfo infoObject ;
			while ((inputLine = in.readLine()) != null) {
				if (inputLine.startsWith("STID")){
					inputLine = inputLine.replaceAll(" = ", "=");
					lineValues = inputLine.split(" ");
					city = lineValues[0].split("=")[1];
					dateTime = dateFormat.parse(lineValues[2].split("=")[1]);
					beforeDataLine = false;
					dataLine1Read = false;
					if (!cityData.containsKey(city))
						cityData.put(city, new Hashtable<Date, WeatherInfo>());
				}
				else if (inputLine.startsWith("CFRL HGHT"))
				{
					beforeDataLine = true;
					dataLine1Read = false;
					dataLine1 = null;
					dataLine2 = null;
				}
				else if (beforeDataLine){
					dataLine1 = inputLine;
					dataLine1Read = true;
					beforeDataLine = false;
				}else if (dataLine1Read){
					dataLine2 = inputLine;
					lineValues = dataLine1.split(" ");
					infoObject = new WeatherInfo();
					infoObject.setPres(Double.parseDouble(lineValues[0]));
					infoObject.setTmpc(Double.parseDouble(lineValues[1]));
					infoObject.setTmwc(Double.parseDouble(lineValues[2]));
					infoObject.setDwpc(Double.parseDouble(lineValues[3]));
					infoObject.setThte(Double.parseDouble(lineValues[4]));
					infoObject.setDrct(Double.parseDouble(lineValues[5]));
					infoObject.setSknt(Double.parseDouble(lineValues[6]));
					infoObject.setOmeg(Double.parseDouble(lineValues[7]));
					lineValues = dataLine2.split(" ");
					infoObject.setCfrl(Double.parseDouble(lineValues[0]));
					infoObject.setHght(Double.parseDouble(lineValues[1]));
					dataLine1Read = false;
					Hashtable<Date, WeatherInfo> timeData = cityData.get(city);
					if (timeData == null){
						timeData = new Hashtable<Date, WeatherInfo>();
					}
					timeData.put(dateTime, infoObject);
				}
			}
			in.close();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		locations.put("Chicago, IL", "KORD");
	}

	public static double getDWPC(String location, Date dateTime) {
		Hashtable<Date, WeatherInfo> timeWeatherInfo = cityData.get(location);
		if (timeWeatherInfo != null){
			WeatherInfo weatherInfo = timeWeatherInfo.get(dateTime);
			if (weatherInfo != null){
				return weatherInfo.getDwpc();
			}
		}
		throw new RuntimeException("No weather data found for the city and the time");		
	}

	public static double getTMPC(String location, Date dateTime) {
		Hashtable<Date, WeatherInfo> timeWeatherInfo = cityData.get(location);
		if (timeWeatherInfo != null){
			WeatherInfo weatherInfo = timeWeatherInfo.get(dateTime);
			if (weatherInfo != null){
				return weatherInfo.getTmpc();
			}
		}
		throw new RuntimeException("No weather data found for the city and the time");
	}
	
	public static double getSknt(String location, Date dateTime)
	{
		Hashtable<Date, WeatherInfo> timeWeatherInfo = cityData.get(location);
		if (timeWeatherInfo != null){
			WeatherInfo weatherInfo = timeWeatherInfo.get(dateTime);
			if (weatherInfo != null){
				return weatherInfo.getSknt();
			}
		}
		throw new RuntimeException("No weather data found for the city and the time");
	}
	
}
