package com.dongliushui.util;


import net.sf.json.JSONObject;

public class WeixinUtil {

	public static JSONObject httpSendPostRequest(String url, String jsonMenu){
        String result=HttpClientUtil.sendPostRequest(url, jsonMenu, null,null); 
		return JSONObject.fromObject(result);
	}

}
