package neiu.edu.cs404.summer.client;

import java.util.Date;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface WeatherService extends RemoteService {
	public String[] getWeatherData(String location, Date dateToPick, String timeText);
	public String[] getCurrentTemperature(String location);
}
