package neiu.edu.cs404.summer.server;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import neiu.edu.cs404.summer.client.WeatherService;
import neiu.edu.cs404.summer.shared.SkyCondition;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class WeatherServiceImpl extends RemoteServiceServlet implements
		WeatherService {


	@Override
	public String[] getWeatherData(String location, Date dateToPick, String timeText) {
		try{
			String locationCode = DataReader.getLocationCode(location);
			Date time = new SimpleDateFormat("hh:mm a").parse(timeText);
			Calendar timeCal =  Calendar.getInstance();
			timeCal.setTime(time);
			Calendar cal = Calendar.getInstance();
			cal.setTime(dateToPick);
			cal.set(Calendar.HOUR_OF_DAY, timeCal.get(Calendar.HOUR_OF_DAY));
			cal.set(Calendar.MINUTE, timeCal.get(Calendar.MINUTE));
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND,0);
			cal.add(Calendar.HOUR_OF_DAY, 5);
			dateToPick = cal.getTime();
			return getWeatherInfo(locationCode, dateToPick);
		}catch(Exception ex){
			return new String[]{"No weather information found for the date/time and location"};
		}
	}

	@Override
	public String[] getCurrentTemperature(String location) {
		String locationCode = DataReader.getLocationCode(location);
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND,0);
		cal.add(Calendar.HOUR_OF_DAY, 5);
		System.out.println(cal.getTime());
		String[] data = DataReader.getCurrentTempWindSkyCondFromMetar(locationCode);
		
		double tmpc =  DataReader.getTMPC(locationCode, cal.getTime());
		double dwpc = DataReader.getDWPC(locationCode, cal.getTime());
		double vp = 6.112 * Math.exp((17.67 * dwpc) / (243.50 + dwpc));
		double svp = 6.112 * Math.exp((17.67 * tmpc) / (243.50 + tmpc));
		return new String[]{
				data[0],
				"Humidity: " + new DecimalFormat("##").format(vp * 100/svp) + "%",
				"Wind: " + data[1] + " mph",
				getSkyConditionStyle(data[2])
		};
		//return getWeatherInfo(locationCode, cal.getTime());
	}
	
	public String[] getWeatherInfo(String locationCode, Date dateToPick){
		double tmpc =  DataReader.getTMPC(locationCode, dateToPick);
		double dwpc = DataReader.getDWPC(locationCode, dateToPick);
		double vp = 6.112 * Math.exp((17.67 * dwpc) / (243.50 + dwpc));
		double svp = 6.112 * Math.exp((17.67 * tmpc) / (243.50 + tmpc));
		double windspeed = 1.15077945 * DataReader.getSknt(locationCode, dateToPick);
		return new String[]{new DecimalFormat("###").format( 32 + (tmpc * 9 / 5))  , 
				"Humidity: " + new DecimalFormat("##").format(vp * 100/svp) + "%", 
				"Wind: " + new DecimalFormat("###").format(windspeed) + " mph", 
				getSkyConditionStyle(locationCode)};
	}
	
	public String getSkyConditionStyle(String locationCode){
		String con = DataReader.getSkyCondition(locationCode);
		if (con == SkyCondition.PARTLY_CLOUDY)
			return "weatherIcon-partly-cloudy";
		else if (con == SkyCondition.FOG)
			return "weatherIcon-fog";
		else if (con == SkyCondition.CLOUDY)
			return "weatherIcon-cloudy";
		else if (con == SkyCondition.FOG)
			return "weatherIcon-fog";
		else if (con == SkyCondition.RAIN)
			return "weatherIcon-rain-s-cloudy";
		else if (con == SkyCondition.SUNNY)
			return "weatherIcon-sunny";
		else if (con == SkyCondition.THUNDERSTORM)
			return "weatherIcon-thunderstorm";
		else
			return "weatherIcon-partly-cloudy";
		
	}
//
//	. {
//	    background: url(images/cloudy.png);
//	    height: auto;
//	    width: auto;
//	}
//
//	. {
//	    background: url(images/rain_s_cloudy.png);
//	    height: auto;
//	    width: auto;
//	}
//
//	. {
//	    background: url(images/sunny.png);
//	    height: auto;
//	    width: auto;
//	}
//
//	. {
//	    background: url(images/thunderstorms.png);
//	    height: auto;
//	    width: auto;
//	}
}
