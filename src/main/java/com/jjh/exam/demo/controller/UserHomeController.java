package com.jjh.exam.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserHomeController {
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
}
