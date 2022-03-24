package com.jjh.exam.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserHomeController {
	private int count;
	public UserHomeController() {
		count = 0;
	}
	
    @RequestMapping("usr/home/getCount")
    @ResponseBody
    public int getCount() {
    	return count++;
    }
    
    @RequestMapping("usr/home/doSetCount")
    @ResponseBody
    public String doSetCount(int count) {
    	this.count = count;
    	return "count의 값이"+ this.count + "초과되었습니다.";
    }
}
