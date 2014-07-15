package neiu.edu.cs404.summer.server;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.TreeMap;

import neiu.edu.cs404.summer.shared.SkyCondition;

public class DataReader {
	
	static Hashtable<String, TreeMap<Date, WeatherInfo>> cityData = new Hashtable<String, TreeMap<Date, WeatherInfo>>();  
	//static Hashtable<String, String> locations = new Hashtable<String, String>();
	
	static Hashtable<String, CityInfo> cities = new Hashtable<String, CityInfo>();
	
	
	static{
		CityInfo info = new CityInfo();
		info.setCode("KORD");
		info.setName("Chicago, IL");
		info.setFileName("KORD.DAT");
		info.setURL("http://www.meteor.iastate.edu/~ckarsten/bufkit/data/nam/nam_kord.buf");
		info.setMetarURL("http://w1.weather.gov/data/METAR/KORD.1.txt");
		cities.put(info.getCode(), info);
		loadFromURLAndDumpIntoFileAsObjects(info.getCode());
	}
	
//	public synchronized static void readFromURLAndDumpIntoFile(){
//		try {
//			URL oracle = new URL(
//					"http://www.meteor.iastate.edu/~ckarsten/bufkit/data/nam/nam_kord.buf");
//			BufferedReader in = new BufferedReader(new InputStreamReader(
//					oracle.openStream()));
//			String inputLine;
//			boolean beforeDataLine = false, dataLine1Read = false;
//			FileOutputStream out = new FileOutputStream(new File("Weather.Dat"), true);
//			while ((inputLine = in.readLine()) != null) {
//				if (inputLine.startsWith("STID")){
//					out.write((inputLine + System.getProperty("line.separator")).getBytes());
//					beforeDataLine = false;
//					dataLine1Read = false;
//				}
//				else if (inputLine.startsWith("CFRL HGHT"))
//				{
//					out.write((inputLine + System.getProperty("line.separator")).getBytes());
//					beforeDataLine = true;
//					dataLine1Read = false;
//				}
//				else if (beforeDataLine){
//					out.write((inputLine + System.getProperty("line.separator")).getBytes());
//					dataLine1Read = true;
//					beforeDataLine = false;
//				}else if (dataLine1Read){
//					out.write((inputLine + System.getProperty("line.separator")).getBytes());
//					dataLine1Read = false;
//				}
//				out.flush();
//			}
//			out.close();
//			in.close();
//			
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//	}
	
//	public synchronized static void loadDataFromFile() {
//		try {
//			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream("Weather.Dat")));
////			URL oracle = new URL(
////					"http://www.meteor.iastate.edu/~ckarsten/bufkit/data/nam/nam_kord.buf");
////			BufferedReader in = new BufferedReader(new InputStreamReader(oracle.openStream()));
//			
//			String inputLine;
//			String[] lineValues;
//			SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd/HHmm");
//			String city= null;
//			Date dateTime = null;
//			String dataLine1 = null, dataLine2 = null;
//			boolean beforeDataLine = false, dataLine1Read = false;
////			FileOutputStream out = new FileOutputStream(new File("Weather.Dat"), true);
//			WeatherInfo infoObject ;
//			while ((inputLine = in.readLine()) != null) {
//				if (inputLine.startsWith("STID")){
//					inputLine = inputLine.replaceAll(" = ", "=");
////					out.write((inputLine + System.getProperty("line.separator")).getBytes());
//					lineValues = inputLine.split(" ");
//					city = lineValues[0].split("=")[1];
//					dateTime = dateFormat.parse(lineValues[2].split("=")[1]);
//					beforeDataLine = false;
//					dataLine1Read = false;
//					if (!cityData.containsKey(city))
//						cityData.put(city, new TreeMap<Date, WeatherInfo>());
//				}
//				else if (inputLine.startsWith("CFRL HGHT"))
//				{
////					out.write((inputLine + System.getProperty("line.separator")).getBytes());
//					beforeDataLine = true;
//					dataLine1Read = false;
//					dataLine1 = null;
//					dataLine2 = null;
//				}
//				else if (beforeDataLine){
////					out.write((inputLine + System.getProperty("line.separator")).getBytes());
//					dataLine1 = inputLine;
//					dataLine1Read = true;
//					beforeDataLine = false;
//				}else if (dataLine1Read){
////					out.write((inputLine + System.getProperty("line.separator")).getBytes());
//					dataLine2 = inputLine;
//					lineValues = dataLine1.split(" ");
//					infoObject = new WeatherInfo();
//					infoObject.setPres(Double.parseDouble(lineValues[0]));
//					infoObject.setTmpc(Double.parseDouble(lineValues[1]));
//					infoObject.setTmwc(Double.parseDouble(lineValues[2]));
//					infoObject.setDwpc(Double.parseDouble(lineValues[3]));
//					infoObject.setThte(Double.parseDouble(lineValues[4]));
//					infoObject.setDrct(Double.parseDouble(lineValues[5]));
//					infoObject.setSknt(Double.parseDouble(lineValues[6]));
//					infoObject.setOmeg(Double.parseDouble(lineValues[7]));
//					lineValues = dataLine2.split(" ");
//					infoObject.setCfrl(Double.parseDouble(lineValues[0]));
//					infoObject.setHght(Double.parseDouble(lineValues[1]));
//					infoObject.setTime(dateTime);
//					dataLine1Read = false;
//					TreeMap<Date, WeatherInfo> timeData = cityData.get(city);
//					if (timeData == null){
//						timeData = new TreeMap<Date, WeatherInfo>();
//					}
//					timeData.put(dateTime, infoObject);
//				}
//			}
////			out.flush();
//			in.close();
//			
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//	}
	
	public static SkyCondition getSkyCondition(String cityCode){
		CityInfo info = cities.get(cityCode);
		if (info != null){
			try{
				URL url = new URL(info.getMetarURL());
				BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
				String inputLine;
				while ((inputLine = in.readLine()) != null) {
					if (inputLine.startsWith("METAR " + cityCode)){
						if (inputLine.contains("RA")){
							return SkyCondition.RAIN;
						}
						else if (inputLine.contains("TS"))
						{
							return SkyCondition.THUNDERSTORM;
						}
						else if (inputLine.contains("COR")){
							return SkyCondition.SUNNY;
						}
						else if (inputLine.contains("OVC") || inputLine.contains("BKN")){
							return SkyCondition.CLOUDY;
						}
						else if (inputLine.contains("FEW")){
							return SkyCondition.PARTLY_CLOUDY;
						}
						else if (inputLine.contains("FG")){
							return SkyCondition.FOG;
						}
						else
						{
							return SkyCondition.PARTLY_CLOUDY;
						}
					}
				}
			}catch(Exception ex){
				
			}
		}
		return SkyCondition.PARTLY_CLOUDY;
	}

	public static double getDWPC(String cityCode, Date dateTime) {
		TreeMap<Date, WeatherInfo> timeWeatherInfo = cityData.get(cityCode);
		if (timeWeatherInfo != null){
			WeatherInfo weatherInfo = timeWeatherInfo.get(dateTime);
			if (weatherInfo != null){
				return weatherInfo.getDwpc();
			}
		}
		throw new RuntimeException("No weather data found for the city and the time");		
	}

	public static double getTMPC(String cityCode, Date dateTime) {
		TreeMap<Date, WeatherInfo> timeWeatherInfo = cityData.get(cityCode);
		if (timeWeatherInfo != null){
			WeatherInfo weatherInfo = timeWeatherInfo.get(dateTime);
			if (weatherInfo != null){
				return weatherInfo.getTmpc();
			}
		}
		throw new RuntimeException("No weather data found for the city and the time");
	}
	
	public static double getSknt(String cityCode, Date dateTime)
	{
		TreeMap<Date, WeatherInfo> timeWeatherInfo = cityData.get(cityCode);
		if (timeWeatherInfo != null){
			WeatherInfo weatherInfo = timeWeatherInfo.get(dateTime);
			if (weatherInfo != null){
				return weatherInfo.getSknt();
			}
		}
		throw new RuntimeException("No weather data found for the city and the time");
	}
	
	public synchronized static void loadFromURLAndDumpIntoFileAsObjects(String cityCode) {
		try {
			CityInfo cityInfo = cities.get(cityCode);
			//Load the data from an existing file if any
			loadDataFromFileToMap(cityInfo);
			
			URL url = new URL(cityInfo.getURL());
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			String inputLine;
			String[] lineValues;
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd/HHmm");
			Date dateTime = null;
			String dataLine1 = null, dataLine2 = null;
			boolean beforeDataLine = false, dataLine1Read = false;
			WeatherInfo infoObject ;
			while ((inputLine = in.readLine()) != null) {
				if (inputLine.startsWith("STID")){
					inputLine = inputLine.replaceAll(" = ", "=");
					lineValues = inputLine.split(" ");
					cityCode = lineValues[0].split("=")[1];
					dateTime = dateFormat.parse(lineValues[2].split("=")[1]);
					beforeDataLine = false;
					dataLine1Read = false;
					if (!cityData.containsKey(cityCode))
						cityData.put(cityCode, new TreeMap<Date, WeatherInfo>());
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
					infoObject.setTime(dateTime);
					dataLine1Read = false;
					addWeatherInfo(cityCode, infoObject);
				}
			}
			in.close();
			
			ObjectOutputStream outstream = new ObjectOutputStream(new FileOutputStream(cityInfo.getFileName()));
			TreeMap<Date, WeatherInfo> timeData = cityData.get(cityCode);
			Collection<WeatherInfo> list = timeData.values();
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				WeatherInfo type = (WeatherInfo) iterator.next();
				outstream.writeObject(type);
			}
			outstream.close();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}		
	}
	


	private static synchronized void loadDataFromFileToMap(CityInfo cityInfo) {
        FileInputStream fs = null;
        ObjectInputStream os = null;
        
        try {
        	fs = new FileInputStream(cityInfo.getFileName());
            os = new ObjectInputStream(fs);
            WeatherInfo info;
            while(true){
            	try{
            		info = (WeatherInfo) os.readObject();
            	}catch(EOFException ex){
            		info = null;
            	}
            	if (info == null)
            		break;
            	else
            	{
            		addWeatherInfo(cityInfo.getCode(), info);
            	}
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
		
	}
	
	public synchronized static void addWeatherInfo(String cityCode, WeatherInfo info){
		TreeMap<Date, WeatherInfo> timeData = cityData.get(cityCode);
		if (timeData == null){
			timeData = new TreeMap<Date, WeatherInfo>();
			cityData.put(cityCode, timeData);
		}
		timeData.put(info.getTime(), info);
	}

	public static void main(String[] args) {
		CityInfo info = new CityInfo();
		info.setCode("KORD");
		info.setName("Chicago, IL");
		info.setFileName("KORD.DAT");
		info.setURL("http://www.meteor.iastate.edu/~ckarsten/bufkit/data/nam/nam_kord.buf");
		
		cities.put(info.getName(), info);
		
		loadFromURLAndDumpIntoFileAsObjects(info.getName());
	}

	public static String getLocationCode(String cityName) {
		Collection<CityInfo> col = cities.values();
		for (Iterator iterator = col.iterator(); iterator.hasNext();) {
			CityInfo cityInfo = (CityInfo) iterator.next();
			if (cityName.equals(cityInfo.getName()))
				return cityInfo.getCode();
		}
		return null;
	}
	
}
