package neiu.edu.cs404.summer.server;

import java.io.Serializable;
import java.util.Date;

public class WeatherInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6641054158153192543L;
	double pres;
	double tmpc;
	double tmwc;
	double dwpc;
	double thte;
	double drct;
	double sknt;
	double omeg;
	double cfrl;
	double hght;
	Date time;
	public double getPres() {
		return pres;
	}

	public void setPres(double pres) {
		this.pres = pres;
	}

	public double getTmpc() {
		return tmpc;
	}

	public void setTmpc(double tmpc) {
		this.tmpc = tmpc;
	}

	public double getTmwc() {
		return tmwc;
	}

	public void setTmwc(double tmwc) {
		this.tmwc = tmwc;
	}

	public double getDwpc() {
		return dwpc;
	}

	public void setDwpc(double dwpc) {
		this.dwpc = dwpc;
	}

	public double getThte() {
		return thte;
	}

	public void setThte(double thte) {
		this.thte = thte;
	}

	public double getDrct() {
		return drct;
	}

	public void setDrct(double drct) {
		this.drct = drct;
	}

	public double getSknt() {
		return sknt;
	}

	public void setSknt(double sknt) {
		this.sknt = sknt;
	}

	public double getOmeg() {
		return omeg;
	}

	public void setOmeg(double omeg) {
		this.omeg = omeg;
	}

	public double getCfrl() {
		return cfrl;
	}

	public void setCfrl(double cfrl) {
		this.cfrl = cfrl;
	}

	public double getHght() {
		return hght;
	}

	public void setHght(double hght) {
		this.hght = hght;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}
	

}
