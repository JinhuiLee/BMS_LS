package com.itu.controller.pub;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.itu.entity.Battery;
import com.itu.entity.BatteryData;
import com.itu.service.BatteryDataServise;
import com.itu.service.BatteryService;
import com.itu.util.ResponseUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * public battery controller layer
 * @author xu
 *
 */

@Controller
@RequestMapping("/public/battery")
public class BatteryController {
	
	@Resource
	private BatteryService batteryService;
	
	@Resource
	private BatteryDataServise batteryDataSerivce;
	
	@RequestMapping("/list")
	public ModelAndView list(HttpServletResponse response) throws Exception{
		List<Battery> list = batteryService.list();
		if(list.size()!=0){
			for(Battery b:list){
				Map<String, Object> map = new HashMap<>();
				System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"+b.getId());
				map.put("battery_id", b.getId());
				BatteryData latestBd = batteryDataSerivce.getLatestDataByBatteryId(map);
				System.out.println("*********************************************");
				System.out.println(latestBd);
				b.setLatestBatteryData(latestBd);
			}
		}
		JSONObject result = new JSONObject();
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss"));
		JSONArray jsonArray = JSONArray.fromObject(list,jsonConfig);
		result.put("rows", jsonArray);
		result.put("total", list.size());
		ResponseUtil.write(response, result);
		return null;
	}
}
