package com.jjh.exam.demo.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jjh.exam.demo.service.MemberService;
import com.jjh.exam.demo.util.Ut;
import com.jjh.exam.demo.vo.Member;
import com.jjh.exam.demo.vo.ResultData;

@Controller
public class UserMemberController {
	private MemberService memberService;

	public UserMemberController(MemberService memberService) {
		this.memberService = memberService;
	}

	@RequestMapping("/usr/member/dojoin")
	@ResponseBody
	public ResultData<Member> dojoin(String loginId, String loginPw, String name, String nickname, String cellphoneNo,
			String email) {

		if (Ut.empty(loginId)) {
			return ResultData.from("F-1", "loginId(을)를 입력해주세요.");
		}
		if (Ut.empty(loginPw)) {
			return ResultData.from("F-2", "loginPw(을)를 입력해주세요.");
		}
		if (Ut.empty(name)) {
			return ResultData.from("F-3", "name(을)를 입력해주세요.");
		}
		if (Ut.empty(nickname)) {
			return ResultData.from("F-4", "nickname(을)를 입력해주세요.");
		}
		if (Ut.empty(cellphoneNo)) {
			return ResultData.from("F-5", "cellphoneNo(을)를 입력해주세요.");
		}
		if (Ut.empty(email)) {
			return ResultData.from("F-6", "email(을)를 입력해주세요.");
		}

		// S-1
		// 회원가입이 완료되었습니다.
		// Data
		ResultData<Integer> joinRd = memberService.join(loginId, loginPw, name, nickname, cellphoneNo, email);

		if (joinRd.isFail()) {
			return (ResultData) joinRd;
		}
		
		Member member = memberService.getMemberById((int)joinRd.getData1());
		return ResultData.newData(joinRd, member);
	}
	
	@RequestMapping("/usr/member/dologin")
	@ResponseBody
	public ResultData doLogin(HttpSession httpSession, String loginId, String loginPw) {
		boolean isLogined = false;
		
		if (httpSession.getAttribute("loginedMemberId") == null) {
			isLogined = true;
		}
		if (isLogined) {
			return ResultData.from("F-5", "이미 로그인되었습니다.");
		}
		if (Ut.empty(loginId)) {
			return ResultData.from("F-1", "loginId(을)를 입력해주세요.");
		}
		if (Ut.empty(loginPw)) {
			return ResultData.from("F-2", "loginPw(을)를 입력해주세요.");
		}
		
		Member member = memberService.getMemberByLoginId(loginId);

		if (member == null) {
			return ResultData.from("F-3", "존재하지 않는 로그인아이디 입니다.");
		}
		
		if (member.getLoginPw().equals(loginPw) == false) {
			return ResultData.from("F-4", "비밀번호가 일치하지 않습니다.");
		}
		
		httpSession.setAttribute("loginedMemberId", member.getId());
		
		return ResultData.from("S-1", Ut.f("%s님 환영합니다.", member.getNickname()));
	}
}