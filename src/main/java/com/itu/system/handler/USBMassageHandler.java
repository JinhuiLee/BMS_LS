package com.itu.system.handler;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;


import com.itu.entity.BatteryData;
import com.itu.entity.BatteryParameter;
import com.itu.service.BatteryDataServise;
import com.itu.service.BatteryParameterService;
@Component
public class USBMassageHandler {
	
	
	
	//message type const
	private final Integer BATTERY_RESPONSE = 3;	
	private final Integer BATTERY_REPORT = 4;
	
	//message action const
	private final Integer BATTERY_TIME =1;
	private final Integer BATTERY_DATA =2;
	
	//
    private double CHARGE_SENSE_RESISTANCE = 0.1;
    private double DISCHARGE_SENSE_RESISTANCE = 11.3;
    private double CHARGE_OPAMP_GAIN = 20.0;
    private int ADC_FULL_SCALE_VALUE = 256;
    private int ADC_REF = 3;
    private double TEMPERATURE_FACTOR = 1000.0;
    

	
	@Resource
	private BatteryDataServise batteryDataServise;
	
	@Resource
	private BatteryParameterService batteryParameterService;
	
	public int handleMessage(byte[] data){
		if(data==null || data.length==0){
			System.out.println("************Communication error! data list is null!!*************");
			return -1;//error status
		}
			
		List<Integer> dataList = new ArrayList<>();
		for(byte n : data){
			
			System.out.println((int)(n & 0xFF));
			dataList.add((int)(n & 0xFF));
		}
		Integer type = dataList.get(0);
		Integer action = dataList.get(1);
		if(type == BATTERY_REPORT){
			if(action == BATTERY_DATA){
				
				//get a battery data instance
				BatteryData batteryData = new BatteryData();
				int i = 2;
				batteryData.setBattery_id(dataList.get(i++));
				
				//get battery parameter by batteryId
				BatteryParameter batteryParameter = new BatteryParameter();
				batteryParameter.setBatteryId(batteryData.getBattery_id());
				batteryParameterService.getByBatteryId(batteryParameter);
				
				//map adc result to real voltage value
				batteryData.setCh_cur((int)(1000 * dataList.get(i++) / ADC_FULL_SCALE_VALUE * ADC_REF));// / batteryParameter.getSensingGain() / batteryParameter.getrChargeValue()));
				batteryData.setDis_cur((int)(1000 * dataList.get(i++) / ADC_FULL_SCALE_VALUE * ADC_REF));// / batteryParameter.getrDischargeValue()));
				batteryData.setTemperature((double)(int)dataList.get(i++));
				batteryData.setStateofcharge(dataList.get(i++));
				batteryData.setCharger_status((int)dataList.get(i) & 1);
				batteryData.setBattery_status((int)dataList.get(i) >> 1);

				batteryDataServise.add(batteryData);
			}
			return 0;
		}
		return 0;
		
	}
	
}
