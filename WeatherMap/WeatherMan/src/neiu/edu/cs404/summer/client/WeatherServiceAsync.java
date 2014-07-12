package neiu.edu.cs404.summer.client;

import java.util.Date;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>WeatherService</code>.
 */
public interface WeatherServiceAsync {
	void getWeatherData(String location, Date dateToPick, String timeText, AsyncCallback<String[]> callback) throws IllegalArgumentException;

	void getCurrentTemperature(String location, AsyncCallback<String[]> callback) throws IllegalArgumentException;
}
