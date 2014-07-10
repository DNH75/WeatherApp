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

public class LocationReader extends Composite  {

	private static LocationReaderUiBinder uiBinder = GWT.create(LocationReaderUiBinder.class);
	
	@UiField Button button;
	@UiField ListBox timeCombo;
	@UiField DateBox dateTxt;
	@UiField ListBox locationCombo;
	@UiField Label temperatureLbl;
	@UiField Label humidityLbl;
	
	WeatherServiceAsync service = GWT.create(WeatherService.class);
	
	interface LocationReaderUiBinder extends UiBinder<Widget, LocationReader> {
	}

	public LocationReader() {
		initWidget(uiBinder.createAndBindUi(this));
		DateTimeFormat format = DateTimeFormat.getFormat("HH:00");
		String currTime = format.format(new Date());
		String time;
		for (int i = 0; i < 24; i++){
			if (i < 10){
				time = "0" + i + ":00";
			}else{
				time = i + ":00";
			}
			timeCombo.addItem(time);
			if (time.equals(currTime)){
				timeCombo.setSelectedIndex(i);
			}
		}
		locationCombo.addItem("O'HARE", "O'HARE");
		dateTxt.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("MM/dd/yyyy")));
		dateTxt.setValue(new Date());
		pullTime();
	}

	public LocationReader(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}



	@UiHandler("button")
	void onButtonClick(ClickEvent event) {
		pullTime();
	}
	
	public void pullTime(){
		Date dateTime =  dateTxt.getDatePicker().getValue();
		String timetxt = timeCombo.getValue(timeCombo.getSelectedIndex());
		
		service.getWeatherData(locationCombo.getItemText(locationCombo.getSelectedIndex()), dateTime, timetxt , new AsyncCallback<String[]>() {
			
			@Override
			public void onSuccess(String[] result) {
				if (result.length == 2){
					temperatureLbl.setText(result[0]);
					humidityLbl.setText(result[1]);
				}
				else{
					Window.alert("Error " + result[0]);
					temperatureLbl.setText("N/A");
					humidityLbl.setText("N/A");
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error calculating the weather information :: " + caught.getMessage());
			}
		});	
	}
	
}
