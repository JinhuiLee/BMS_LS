package com.itu.controller.pub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.itu.entity.Battery;
import com.itu.entity.BatteryData;
import com.itu.entity.User;
import com.itu.service.BatteryDataServise;
import com.itu.service.BatteryService;
import com.itu.service.UserService;
import com.itu.util.ResponseUtil;
import com.itu.util.can.USBUtil;


import net.sf.json.JSONObject;


/**
 * user controller layer
 * @author xu
 *
 */
@Controller
@RequestMapping("/public/system")
public class SystemController {
	
	@Resource
	private UserService userService;
	
	@Resource
	private BatteryDataServise batteryDataServise;
	
	@Resource
	private BatteryService batteryService;
	

	@RequestMapping("/login")
	public String login(User user, HttpServletRequest request){
		
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(user.getUserName(), user.getPassword());
		try {
			subject.login(token);
			String username = token.getUsername();
			String role = userService.getByUserName(username).getRole();
			if(role.equals("2")){
				return "redirect:/admin/adminmain.jsp";
			}else if(role.equals("1")){
				return "redirect:/user/usermain.jsp";
			}else{
				request.setAttribute("user", user);
				request.setAttribute("errorInfo", "User not authorized!!");
				return "login";
			}
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("user", user);
			request.setAttribute("errorInfo", "Username or password is wrong!!");
			return "login";
		}
	}
	
	/**
	 * refresh system application cache
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/refreshSystem")
	public String refreshSystem(HttpServletRequest request, HttpServletResponse response)throws Exception{
		ServletContext application = RequestContextUtils.getWebApplicationContext(request).getServletContext();
		
		//get latest battery data list and battery information by battery_id 
		int total = batteryService.count();
		System.out.println("total= " + total);
		List<Battery> batteryList= new ArrayList<>();
		List<BatteryData> latestDataList = new ArrayList<>();
		if(total!=0){
			for(int i=1; i<=total; i++){
				Map<String, Object> map = new HashMap<>();
				map.put("id", i);
				map.put("battery_id", i);
				Battery b = batteryService.getById(map);
				batteryList.add(b);
				BatteryData bData = batteryDataServise.getLatestDataByBatteryId(map);
				latestDataList.add(bData);
			}
		}
		application.setAttribute("batteryList", batteryList);
		application.setAttribute("latestBatteryDataList", latestDataList);
		return null;
	}
	
	@RequestMapping("/connect")
	public String connect(HttpServletResponse response) throws Exception{
		//method for connecting  to CAN
		USBUtil usbutil = new USBUtil();
		boolean state = usbutil.connect();
		//boolean state = true;
		
		JSONObject result = new JSONObject();
		if(state){
			result.put("success", true);
		}else{
			result.put("success", false);
		}
		ResponseUtil.write(response, result);
		return null;
	}
	
}
