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
	
    @RequestMapping("usr/home/main")
    @ResponseBody
    public String showMain() {
    	return "안녕하세요";
    }
    
    @RequestMapping("usr/home/main2")
    @ResponseBody
    public String ddd() {
    	return "반갑습니다.";
    } 
    
    @RequestMapping("usr/home/main3")
    @ResponseBody
    public String aaa() {
    	return "또만나요.";
    }
    
    @RequestMapping("usr/home/main4")
    @ResponseBody
    public int showMain4() {
    	return count++;
    }
    
    @RequestMapping("usr/home/main5")
    @ResponseBody
    public String showMain5() {
    	count = 0;
    	return "count의 값이 0으로 초과되었습니다.";
    }
}
