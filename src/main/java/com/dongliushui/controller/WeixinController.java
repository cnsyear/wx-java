package com.dongliushui.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;
import com.dongliushui.bean.BaseMsg;
import com.dongliushui.bean.LinkMsg;
import com.dongliushui.bean.LocationMsg;
import com.dongliushui.bean.PicMsg;
import com.dongliushui.bean.ShortVideoMsg;
import com.dongliushui.bean.TextMsg;
import com.dongliushui.bean.VideoMsg;
import com.dongliushui.bean.VoiceMsg;
import com.dongliushui.bean.WechartUserInfo;
import com.dongliushui.business.LinkMsgProcesssor;
import com.dongliushui.business.LocationMsgProcesssor;
import com.dongliushui.business.PicMsgProcesssor;
import com.dongliushui.business.TextMsgProcessor;
import com.dongliushui.business.VideoMsgProcesssor;
import com.dongliushui.business.VoiceMsgProcessor;
import com.dongliushui.service.impl.MenuServiceImpl;
import com.dongliushui.util.Configuration;
import com.dongliushui.util.HttpClientUtil;
import com.dongliushui.util.SignUtil;
import com.dongliushui.xmlparser.AllInfoParserHandler;

 

/**
 * @ClassName: WeixinController
 * @Description: 响应Controller
 * @author zhutulang
 * @date 2016年1月4日
 * @version V1.0
 */
@Controller  
@RequestMapping("/weixinCon") 		
public class WeixinController {
	
	 private Logger log = Logger.getLogger(WeixinController.class);
	 
	 @RequestMapping(method = RequestMethod.GET)  
	 public void get(HttpServletRequest request, HttpServletResponse response) {  
		    log.info("请求进来了...");
	        // 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。  
	        String signature = request.getParameter("signature");  
	        // 时间戳  
	        String timestamp = request.getParameter("timestamp");  
	        // 随机数  
	        String nonce = request.getParameter("nonce");  
	        // 随机字符串  
	        String echostr = request.getParameter("echostr");  
	  
	        PrintWriter out = null;  
	        try {  
	            out = response.getWriter();  
	            // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，否则接入失败  
	            if (SignUtil.checkSignature(signature, timestamp, nonce)) {  
	                out.print(echostr);  
	            } 
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        } finally {  
	            out.close();  
	            out = null;  
	        }  
	    }  
	  
	    @RequestMapping(method = RequestMethod.POST)  
	    public void post(HttpServletRequest request, HttpServletResponse response) {    
	    	try {
				request.setCharacterEncoding("UTF-8");
				response.setCharacterEncoding("UTF-8");
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	    	PrintWriter out = null;  
	        try {
	        	out = response.getWriter();  
	        	String responseXml = null;
	        	
	        	BaseMsg theBaseMsg = new AllInfoParserHandler().getMsg(request.getInputStream());
	        	if(theBaseMsg instanceof TextMsg){
	        		TextMsg textMsg = (TextMsg)theBaseMsg;
		            responseXml = TextMsgProcessor.process(textMsg);
	        	}else if(theBaseMsg instanceof PicMsg){
	        		PicMsg picMsg = (PicMsg)theBaseMsg;
	        		responseXml = PicMsgProcesssor.process(picMsg);               
	        	}else if(theBaseMsg instanceof VoiceMsg){
	        		VoiceMsg voiceMsg = (VoiceMsg)theBaseMsg;
	        		responseXml = VoiceMsgProcessor.process(voiceMsg);
	        	}else if(theBaseMsg instanceof VideoMsg || theBaseMsg instanceof ShortVideoMsg){
	        		VideoMsg videoMsg = (VideoMsg)theBaseMsg;
	        		responseXml = VideoMsgProcesssor.process(videoMsg);
	        	}else if(theBaseMsg instanceof LocationMsg){
	        		LocationMsg locationMsg = (LocationMsg)theBaseMsg;
	        		responseXml = LocationMsgProcesssor.process(locationMsg);
	        	}else if(theBaseMsg instanceof LinkMsg){
	        		LinkMsg linkMsg = (LinkMsg)theBaseMsg;
	        		responseXml = LinkMsgProcesssor.process(linkMsg);
	        	}
	        	out.print(responseXml);
	            log.info("response="+responseXml);
	        } catch (Exception e) {
	            e.printStackTrace();  
	        } finally {  
	            out.close();  
	            out = null;  
	        }  
	    }  
	    
	    //菜单
	    public static void main(String[] args) {
	    	    String jsonMenu = "{\"button\":[{\"name\":\"生活助手\",\"sub_button\":[{\"key\":\"11\",\"name\":\"天气预报\",\"type\":\"click\"},{\"key\":\"12\",\"name\":\"公交查询\",\"type\":\"click\"}]},{\"name\":\"音智达\",\"sub_button\":[{\"key\":\"21\",\"name\":\"好东西哦\",\"type\":\"click\"},{\"key\":\"22\",\"name\":\"人脸识别\",\"type\":\"click\"}]},{\"name\":\"更多体验\",\"sub_button\":[{\"key\":\"33\",\"name\":\"幽默笑话\",\"type\":\"click\"},{\"name\":\"View类型的\",\"type\":\"view\",\"url\":\"http://www.cnsyear.cn/wechart/index\"}]}]}";
	    	    MenuServiceImpl impl = new MenuServiceImpl();  
	    	    impl.CreateMenu(jsonMenu);

	    }
	    @RequestMapping("/userInfo")
	    public String getUserInfo(HttpServletRequest request,HttpServletResponse response,Model model){
	    	//1、用户确认授权后首先获得access_token
	    	String code=request.getParameter("code");
	    	log.info("CODE："+code);
	    	String accesstokenUrl=Configuration.getInstance("weixin").getValue("authorization_access_token_url");
	    	//2、拼接获得access_token的url
	    	accesstokenUrl=accesstokenUrl.replace("APPID",Configuration.getInstance("weixin").getValue("AppId"));
	    	accesstokenUrl=accesstokenUrl.replace("SECRET", Configuration.getInstance("weixin").getValue("AppSecret"));
	    	accesstokenUrl=accesstokenUrl.replace("CODE", code);
	       String result=	HttpClientUtil.sendGetRequest(accesstokenUrl, null);
	        JSONObject objAccessToken=JSONObject.parseObject(result); 
	        log.info("ojbAccessToken:"+objAccessToken);
	       String accessToken=objAccessToken.getString("access_token");
	       String openid=objAccessToken.getString("openid");
	      //3、拼接获得userInfo的url
	       String userInfoUrl=Configuration.getInstance("weixin").getValue("wixin_userinfo_url");
	       userInfoUrl=userInfoUrl.replace("ACCESS_TOKEN", accessToken);
	       userInfoUrl=userInfoUrl.replace("OPENID", openid);
	       log.info("userInfoUrl: "+userInfoUrl);
	       String userInfoJson=	HttpClientUtil.sendHttpsGetRequest(userInfoUrl, null);
	       JSONObject objUserInfo=JSONObject.parseObject(userInfoJson);
	        openid=objUserInfo.getString("openid");
	        WechartUserInfo userInfo=new WechartUserInfo();
	        userInfo.setOpenid(openid);
	        userInfo.setNickname(objUserInfo.getString("nickname"));
	        userInfo.setSex(objUserInfo.getString("sex"));
	        userInfo.setProvince(objUserInfo.getString("province"));
	        userInfo.setCity(objUserInfo.getString("city"));
	        userInfo.setCountry(objUserInfo.getString("country"));
	        userInfo.setHeadimgurl(objUserInfo.getString("headimgurl"));
	        userInfo.setPrivilege(objUserInfo.getString("privilege"));
	        userInfo.setUnionid(objUserInfo.getString("unionid")); 
	        log.info("objUserInfo:"+objUserInfo); 
	        model.addAttribute("objUserInfo",objUserInfo);
	      return "/userInfo";
	    }
}
