package com.TDMData.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.TDMData.constant.MessageConstant;

@Controller
public class UserAuthenticationController {

	private static Logger logger = Logger.getLogger(UserAuthenticationController.class);
	
	
	@RequestMapping(value="sessionExp",method=RequestMethod.GET)
	public String getLoginSessionExpire() {
		
		logger.info(MessageConstant.TDM_LOGIN_CNTRL+MessageConstant.SESSION_EXPIRE);
	
		return "login";
	}
	
	@RequestMapping(value="authFail",method=RequestMethod.GET)
	public String getAuthFail() {
		logger.info(MessageConstant.TDM_LOGIN_CNTRL+MessageConstant.AUTH_FAIL);
		
		return "login";
	}
	
	@RequestMapping(value="login",method=RequestMethod.GET)
	public String getlogin() {
		
		return "login";
	}
	@RequestMapping(value="spring_security_login",method=RequestMethod.GET)
	public String getSpringLogin() {
		
		return "login";
	}
}
