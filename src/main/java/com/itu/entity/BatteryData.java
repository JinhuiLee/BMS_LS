package com.itu.entity;

import java.util.Date;

/**
 * BatteryData entity
 * @author xu
 *
 */
public class BatteryData {
	
	private Integer id;
	private Integer battery_id;
	private Integer ch_cur;
	private Integer dis_cur;
	private Double temperature;
	private Integer battery_status;
	private Integer charger_status;
	private Date timestp;
	private Integer stateofcharge;
	private Integer proc = 0;
	
	private String timestpDateStr; //string of releaseDate, year, month and day only	
	
	
	@Override
	public String toString() {
		return "BatteryData [id=" + id + ", battery_id=" + battery_id + ", ch_cur=" + ch_cur + ", dis_cur=" + dis_cur
				+ ", temperature=" + temperature + ", battery_status=" + battery_status + ", charger_status="
				+ charger_status + ", timestp=" + timestp + ", stateofcharge=" + stateofcharge + ", proc=" + proc
				+ ", timestpDateStr=" + timestpDateStr + "]";
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getBattery_id() {
		return battery_id;
	}

	public void setBattery_id(Integer battery_id) {
		this.battery_id = battery_id;
	}

	public Integer getCh_cur() {
		return ch_cur;
	}

	public void setCh_cur(Integer ch_cur) {
		this.ch_cur = ch_cur;
	}

	public Integer getDis_cur() {
		return dis_cur;
	}

	public void setDis_cur(Integer dis_cur) {
		this.dis_cur = dis_cur;
	}

	public Double getTemperature() {
		return temperature;
	}

	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}

	public Integer getBattery_status() {
		return battery_status;
	}

	public void setBattery_status(Integer battery_status) {
		this.battery_status = battery_status;
	}

	public Integer getCharger_status() {
		return charger_status;
	}

	public void setCharger_status(Integer charger_status) {
		this.charger_status = charger_status;
	}

	public Date getTimestp() {
		return timestp;
	}

	public void setTimestp(Date timestp) {
		this.timestp = timestp;
	}

	public Integer getStateofcharge() {
		return stateofcharge;
	}

	public void setStateofcharge(Integer stateofcharge) {
		this.stateofcharge = stateofcharge;
	}

	public String getTimestpDateStr() {
		return timestpDateStr;
	}

	public void setTimestpDateStr(String timestpDateStr) {
		this.timestpDateStr = timestpDateStr;
	}

	public Integer getProc() {
		return proc;
	}

	public void setProc(Integer proc) {
		this.proc = proc;
	}
	
	
	
}
