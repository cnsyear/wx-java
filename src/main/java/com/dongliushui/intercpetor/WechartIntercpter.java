package com.dongliushui.intercpetor;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.dongliushui.util.Configuration;

public class WechartIntercpter  implements HandlerInterceptor{
     Logger log=Logger.getLogger(WechartIntercpter.class);
	
	//在Controller执行前拦截
	@SuppressWarnings("deprecation")
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		 String authorizationUrl=Configuration.getInstance("weixin").getValue("authorization_url");
		 authorizationUrl=authorizationUrl.replace("APPID",Configuration.getInstance("weixin").getValue("AppId"));
		 authorizationUrl=authorizationUrl.replace("REDIRECT_URI",URLEncoder.encode("http://www.cnsyear.cn/weixinCon/userInfo"));
	     log.info("授权链接："+authorizationUrl);
	     response.sendRedirect(authorizationUrl);
		return false;
	}
  
	
	//在Controller执行后执行
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		    
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	 
		
	}

}
