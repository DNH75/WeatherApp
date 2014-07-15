package neiu.edu.cs404.summer.client;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.client.ui.TextBox;

public class LocationReader extends Composite  {

	private static LocationReaderUiBinder uiBinder = GWT.create(LocationReaderUiBinder.class);
	
	@UiField Button btnSearch;
	@UiField ListBox cmbTime;
	@UiField DateBox txtDate;
	@UiField ListBox cmbLocation;
	@UiField Label lblTemperature;
	@UiField Label lblHumidity;
	@UiField Label lblCurrentHumidity;
	@UiField Label lblCurrentTemp;
	@UiField Label lblCurrentWind;
	@UiField Label lblDegree;
	@UiField Label lblCurrentLocation;
	@UiField Label lblWeatherIcon;
	@UiField Label lblWind;
	String[] times = new String[]{"12:00 AM", "01:00 AM", "02:00 AM", "03:00 AM", "04:00 AM", "05:00 AM", "06:00 AM", 
			"07:00 AM", "08:00 AM", "09:00 AM", "10:00 AM", "11:00 AM", 
			"12:00 PM", "01:00 PM", "02:00 PM", "03:00 PM", "04:00 PM", "05:00 PM", "06:00 PM", 
			"07:00 PM", "08:00 PM", "09:00 PM", "10:00 PM", "11:00 PM" };
	WeatherServiceAsync service = GWT.create(WeatherService.class);
	
	interface LocationReaderUiBinder extends UiBinder<Widget, LocationReader> {
	}

	public LocationReader() {
		initWidget(uiBinder.createAndBindUi(this));
		DateTimeFormat format = DateTimeFormat.getFormat("hh:00 a");
		String currTime = format.format(new Date());
		for (int i = 0; i < times.length; i++){
			cmbTime.addItem(times[i]);
			if (times[i].equals(currTime)){
				cmbTime.setSelectedIndex(i);
			}
		}
		cmbLocation.addItem("Chicago, IL", "Chicago, IL");
		txtDate.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("MM/dd/yyyy")));
		txtDate.setValue(new Date());
		getCurrentTemperature();
	}

	private void getCurrentTemperature() {
		String location = lblCurrentLocation.getText();
		
		service.getCurrentTemperature(location, new AsyncCallback<String[]>() {
			
			@Override
			public void onSuccess(String[] result) {
				lblCurrentTemp.setText(result[0]);
				lblCurrentHumidity.setText(result[1]);
				lblCurrentWind.setText(result[2]);
				lblDegree.setText( "\u00b0F");
				lblWeatherIcon.addStyleName(result[3]);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error calculating the current weather.");
			}
		});	
		
	}

	public LocationReader(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}



	@UiHandler("btnSearch")
	void onBtnSearchClick(ClickEvent event) {
		pullTime();
	}
	
	public void pullTime(){
		Date dateTime =  txtDate.getDatePicker().getValue();
		String timetxt = cmbTime.getValue(cmbTime.getSelectedIndex());
		
		service.getWeatherData(cmbLocation.getItemText(cmbLocation.getSelectedIndex()), dateTime, timetxt , new AsyncCallback<String[]>() {
			
			@Override
			public void onSuccess(String[] result) {
				if (result.length >= 3){
					lblTemperature.setText("Temperature: " + result[0] +  "\u00b0F" );
					lblHumidity.setText(result[1]);
					lblWind.setText(result[2]);
				}
				else{
					Window.alert("Error " + result[0]);
					lblTemperature.setText("N/A");
					lblHumidity.setText("N/A");
					lblWind.setText("N/A");
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error calculating the weather information :: " + caught.getMessage());
			}
		});	
	}
	
}
