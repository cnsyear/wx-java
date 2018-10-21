package com.dongliushui.controller;

import com.dongliushui.util.weixin.JsSign;
import com.dongliushui.util.weixin.MyWeixinUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("/weixin")
public class WeixinSignController  {

	@Value("#{weixinProperties['AppId']}")
	private  String appId;

	@Value("#{weixinProperties['AppSecret']}")
	private  String appSecret;

	@Value("#{weixinProperties['AppKey']}")
	private  String appKey;

	/**
	 * 获取accesstoken
	 * https://www.huceo.com/post/414.html
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/sign")
	@ResponseBody
	public Map<String, String> sign(HttpServletRequest request,HttpServletResponse response)
			throws Exception {
		response.setHeader("Access-Control-Allow-Origin", "*");
		HttpSession session = request.getSession();
		String url = request.getParameter("url");
		String accesstoken = (String) session.getAttribute(appId+"accesstoken_session");
		if(accesstoken == null || "".equals(accesstoken)){
			accesstoken = MyWeixinUtil.getAccessToken(appId, appSecret).getToken();
			session.setAttribute(appId+"accesstoken_session",accesstoken);
			session.setMaxInactiveInterval(7200);
		}
		Map<String, String> jsData = JsSign.getJsSignMapResult( appId, appSecret, appKey,accesstoken,url, request);
		return jsData;
	}
}