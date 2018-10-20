package com.dongliushui.service.impl;


import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.dongliushui.service.MenuService;
import com.dongliushui.util.SignUtil;
import com.dongliushui.util.WeixinUtil;

import net.sf.json.JSONObject;
@Service("MenuService")
public class MenuServiceImpl implements MenuService {
	public static Logger log = Logger.getLogger(MenuServiceImpl.class);
	// 菜单创建（POST） 限100（次/天）  
     public static String MENU_CREATE = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN"; 
	
	@Override
	public String CreateMenu(String jsonMenu) {
		String resultStr = "";
		// 调用接口获取token
		String token = SignUtil.ACCESS_TOKEN;
		if (token != null) {
			// 调用接口创建菜单
			int result = createMenu(jsonMenu, token);
			// 判断菜单创建结果
			if (0 == result) {
				resultStr = "菜单创建成功";
				log.info(resultStr);
			} else {
				resultStr = "菜单创建失败，错误码：" + result;
				log.error(resultStr);
			}
		}
		return resultStr; 
	}
	private int createMenu(String jsonMenu, String accessToken) {
		int result = 0;
		// 拼装创建菜单的url
		String url = MENU_CREATE.replace("ACCESS_TOKEN", accessToken);
		// 调用接口创建菜单
		JSONObject jsonObject = WeixinUtil.httpSendPostRequest(url, jsonMenu); 
		if (null != jsonObject) {
			if (0 != jsonObject.getInt("errcode")) {
				result = jsonObject.getInt("errcode");
				log.error(
						"创建菜单失败 errcode:" + jsonObject.getInt("errcode") + "，errmsg:" + jsonObject.getString("errmsg"));
			}
		} 
		return result;

	}
       
}
