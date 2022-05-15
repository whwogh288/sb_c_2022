package com.jjh.exam.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jjh.exam.demo.service.MemberService;
import com.jjh.exam.demo.vo.Member;

@Controller
public class UserMemberController {
	private MemberService memberService;
	
public UserMemberController(MemberService memberService) {
	this.memberService = memberService;
}
	
	@RequestMapping("/usr/member/dojoin")
	@ResponseBody
	public Object dojoin(String loginId, String loginPw, String name, String nickname, String cellphoneNo, String email) {
		 int id = memberService.join(loginId, loginPw, name, nickname, cellphoneNo, email);
		 
		 if ( id == -1 ) {
			 return "해당 로그인아이디는 이미 사용중입니다.";
		 }		
		 Member member = memberService.getMemberById(id);
		 
		 return member;
	}
}

