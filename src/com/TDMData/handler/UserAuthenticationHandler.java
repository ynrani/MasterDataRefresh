package com.TDMData.handler;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.TDMData.constant.AppConstant;
import com.TDMData.constant.MessageConstant;

public class UserAuthenticationHandler implements AuthenticationSuccessHandler {

	private Logger logger = Logger.getLogger(UserAuthenticationHandler.class);
	
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	public RedirectStrategy getRedirectStrategy() {
		return redirectStrategy;
	}

	public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
		this.redirectStrategy = redirectStrategy;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication) throws IOException,
			ServletException {
		logger.info(com.TDMData.constant.MessageConstant.USER_AUTH_HNDLR 
				+ com.TDMData.constant.MessageConstant.USER_AUTH_HNDLR_BEGIN);
	//	Collection<? extends GrantedAuthority> auth = authentication.getAuthorities();
		
		clearSessions(request,authentication);
		handle(request, response, authentication);
	}
	
	public void clearSessions(HttpServletRequest request,Authentication authentication) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			return;
		}
		session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		User user = (User) authentication.getPrincipal();
		
		request.getSession().setAttribute(AppConstant.SESSION_UID, user.getUsername());
	}
	
	public void handle(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication) throws IOException {
		String redirectedUrl = determineTargetUrl(authentication);
		request.getSession().setAttribute(AppConstant.SESSION_PROJ,
				request.getParameter(AppConstant.SESSION_PROJ));		
		if(response.isCommitted()) {
			return;
		}
		
		redirectStrategy.sendRedirect(request, response, redirectedUrl);
	}
	
	protected String determineTargetUrl(Authentication authentication) {		
		Collection<? extends GrantedAuthority> collect = authentication.getAuthorities();
		boolean adminFlag = false;
		boolean userFlag = false;
		
		for(GrantedAuthority auth : collect) {
			if(auth.getAuthority().equals(AppConstant.ROLE_USER)) {
				userFlag = true;
			}
			else if(auth.getAuthority().equals(AppConstant.ROLE_ADMIN)) {
				adminFlag = true;
			}
		}		
		if(adminFlag) {
			return "/"+AppConstant.TABLE_EXTR_DASH_BOARD;
		}
		else if(userFlag) {
			return "/"+AppConstant.TABLE_EXTR_DASH_BOARD;
		}
		else {
			logger.info(com.TDMData.constant.MessageConstant.USER_AUTH_HNDLR  
					+ com.TDMData.constant.MessageConstant.USER_AUTH_HNDLR_FAIL
					+ com.TDMData.constant.MessageConstant.LOG_ERROR_EXCEPTION);
			throw new IllegalStateException();
		}
	}	
}
