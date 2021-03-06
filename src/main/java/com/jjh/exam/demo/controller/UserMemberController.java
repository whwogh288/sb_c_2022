package com.jjh.exam.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jjh.exam.demo.service.MemberService;
import com.jjh.exam.demo.util.Ut;
import com.jjh.exam.demo.vo.Member;
import com.jjh.exam.demo.vo.ResultData;
import com.jjh.exam.demo.vo.Rq;

@Controller
public class UserMemberController {
	private MemberService memberService;
	private Rq rq;

	public UserMemberController(MemberService memberService, Rq rq) {
		this.memberService = memberService;
		this.rq = rq;
	}

	@RequestMapping("/usr/member/getLoginIdDup")
	@ResponseBody
	public ResultData getLoginIdDup(String loginId) {
		if (Ut.empty(loginId)) {
			return  ResultData.from("F-A1", "loginId를 입력해주세요.");
		}
		
		Member oldMember = memberService.getMemberByLoginId(loginId);
		
		if ( oldMember != null ) {
			return ResultData.from("F-A", "해당 아이디는 이미 사용중입니다.", "loginId", loginId);
		}
		
		return ResultData.from("S-1", "사용 가능한 로그인아이디입니다.", "loginId", loginId);
	}
	
	@RequestMapping("/usr/member/doJoin")
	@ResponseBody
	public String doJoin(String loginId, String loginPw, String name, String nickname, String cellphoneNo,
			String email, @RequestParam(defaultValue = "/") String afterLoginUri) {

		if (Ut.empty(loginId)) {
			return rq.jshistoryBack("F-1", "loginId(을)를 입력해주세요.");
		}
		if (Ut.empty(loginPw)) {
			return rq.jshistoryBack("F-2", "loginPw(을)를 입력해주세요.");
		}
		if (Ut.empty(name)) {
			return rq.jshistoryBack("F-3", "name(을)를 입력해주세요.");
		}
		if (Ut.empty(nickname)) {
			return rq.jshistoryBack("F-4", "nickname(을)를 입력해주세요.");
		}
		if (Ut.empty(cellphoneNo)) {
			return rq.jshistoryBack("F-5", "cellphoneNo(을)를 입력해주세요.");
		}
		if (Ut.empty(email)) {
			return rq.historyBackJsOnView("F-6", "email(을)를 입력해주세요.");
		}
		
		Member oldMember = memberService.getMemberByLoginId(loginId);
		
		if ( oldMember != null) {
			return rq.jshistoryBack("F-7", String.format("입력하신 로그인아이디(%s) (은)는 이미 사용중입니다.", loginId));
		}

		// S-1
		// 회원가입이 완료되었습니다.
		// Data
		ResultData<Integer> joinRd = memberService.join(loginId, loginPw, name, nickname, cellphoneNo, email);

		if (joinRd.isFail()) {
			return rq.jshistoryBack(joinRd.getResultCode(), joinRd.getMsg());
		}
		
		String afterJoinUri = "../member/login?afterLoginUri=" + Ut.getUriEncoded(afterLoginUri);

		return rq.jsReplace("회원가입이 완료되었습니다. 로그인 후 이용해주세요.", afterJoinUri);
	}
	
	@RequestMapping("/usr/member/doLogout")
	@ResponseBody
	public String doLogout(@RequestParam(defaultValue = "/") String afterLogoutUri) {
		rq.logout();
		
		return rq.jsReplace("로그아웃 되었습니다.", afterLogoutUri);
	}
	
	@RequestMapping("/usr/member/login")
	public String showLogin() {
		return "usr/member/login";
	}
	
	@RequestMapping("/usr/member/join")
	public String showJoin() {
		return "usr/member/join";
	}
	
	@RequestMapping("/usr/member/doLogin")
	@ResponseBody
	public String doLogin(String loginId, String loginPw, @RequestParam(defaultValue = "/") String afterLoginUri) {
		if (Ut.empty(loginId)) {
			return rq.jshistoryBack("loginId(을)를 입력해주세요.");
		}
		if (Ut.empty(loginPw)) {
			return rq.jshistoryBack("loginPw(을)를 입력해주세요.");
		}
		
		Member member = memberService.getMemberByLoginId(loginId);

		if (member == null) {
			return rq.jshistoryBack("존재하지 않는 로그인아이디 입니다.");
		}
		
		if (member.getLoginPw().equals(loginPw) == false) {
			return rq.jshistoryBack("비밀번호가 일치하지 않습니다.");
		}
		
		rq.login(member);
		
		return rq.jsReplace(Ut.f("%s님 환영합니다.", member.getNickname()), afterLoginUri);
	}
	
	@RequestMapping("/usr/member/myPage")
	public String showMyPage() {
		return "usr/member/myPage";
	}
	
	@RequestMapping("/usr/member/checkPassword")
	public String showCheckPassword() {
		return "usr/member/checkPassword";
	}
	
	@RequestMapping("/usr/member/doCheckPassword")
	@ResponseBody
	public String doCheckPassword(String loginPw, String replaceUri) {
		if (Ut.empty(loginPw)) {
			return rq.jshistoryBack("loginPw(을)를 입력해주세요.");
		}
		
		if (rq.getLoginedMember().getLoginPw().equals(loginPw) == false) {
			return rq.jshistoryBack("비밀번호가 일치하지 않습니다.");
		}
		
		if (replaceUri.equals("../member/modify")) {
			String memberModifyAuthKey = memberService.genMemberModifyAuthKey(rq.getLoginedMemberId());
			
			replaceUri += "?memberModifyAuthKey=" + memberModifyAuthKey;
		}
		
		return rq.jsReplace("", replaceUri);
	}
	
	@RequestMapping("/usr/member/modify")
	public String showModify(String memberModifyAuthKey) {
		if ( Ut.empty(memberModifyAuthKey) ) {
			return rq.historyBackJsOnView("memberModifyAuthKey(이)가 필요합니다.");
		}

		ResultData checkMemberModifyAuthKeyRd = memberService.checkMemberModifyAuthKey(rq.getLoginedMemberId(), memberModifyAuthKey);

		if ( checkMemberModifyAuthKeyRd.isFail() ) {
			return rq.historyBackJsOnView(checkMemberModifyAuthKeyRd.getMsg());
		}
		
		return "usr/member/modify";
	}
	
	@RequestMapping("/usr/member/doModify")
	@ResponseBody
	public String doModify(String memberModifyAuthKey, String loginPw, String name, String nickname, String email, String cellphoneNo) {
		if ( Ut.empty(memberModifyAuthKey) ) {
			return rq.jshistoryBack("memberModifyAuthKey(이)가 필요합니다.");
		}

		ResultData checkMemberModifyAuthKeyRd = memberService.checkMemberModifyAuthKey(rq.getLoginedMemberId(), memberModifyAuthKey);

		if ( checkMemberModifyAuthKeyRd.isFail() ) {
			return rq.jshistoryBack(checkMemberModifyAuthKeyRd.getMsg());
		}
		
		if (Ut.empty(loginPw)) {
			loginPw = null;
		}
		
		if (Ut.empty(name)) {
			return rq.jshistoryBack("name(을)를 입력해주세요.");
		}
		
		if (Ut.empty(nickname)) {
			return rq.jshistoryBack("nickname(을)를 입력해주세요.");
		}
		
		if (Ut.empty(email)) {
			return rq.jshistoryBack("email(을)를 입력해주세요.");
		}
		
		if (Ut.empty(cellphoneNo)) {
			return rq.jshistoryBack("cellphoneNo(을)를 입력해주세요.");
		}
		
		ResultData modifyRd = memberService.modify(rq.getLoginedMemberId(), loginPw, name, nickname, email, cellphoneNo);
			return rq.jsReplace(modifyRd.getMsg(), "/");
	}
}
