package com.dongliushui.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/wechart")
public class WechartController {
	private Logger log = Logger.getLogger(WechartController.class);
	 
	 @RequestMapping("/index")
	 @ResponseBody
	 public String getTestIndex(){
		 log.info("进入了index");
		 return null;
	 }
}
