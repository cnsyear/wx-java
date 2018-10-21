package com.dongliushui.controller;

import com.dongliushui.util.Const;
import com.dongliushui.util.JS_Sign;
import com.dongliushui.util.MyWeixinUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("/wxSign")
public class WechatController  {

	/**
	 * 获取分享
	 * https://www.huceo.com/post/414.html
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("")
	@ResponseBody
	public Map<String, String> sign(HttpServletRequest request,HttpServletResponse response)
			throws Exception {
		response.setHeader("Access-Control-Allow-Origin", "*");
		HttpSession session = request.getSession();
		String url = request.getParameter("url");
		String accesstoken = (String) session.getAttribute(Const.APPID+"accesstoken_session");
		if(accesstoken == null || "".equals(accesstoken)){
			accesstoken = MyWeixinUtil.getAccessToken(Const.APPID, Const.AppSecret).getToken();
			session.setAttribute(Const.APPID+"accesstoken_session",accesstoken);
			session.setMaxInactiveInterval(7200);
		}
		Map<String, String> js_data = JS_Sign.getJSSignMapResult( Const.APPID, Const.AppSecret, Const.API_KEY,accesstoken,url, request);
		return js_data;
	}
}