package com.jjh.exam.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jjh.exam.demo.service.ReactionPointService;
import com.jjh.exam.demo.vo.ResultData;
import com.jjh.exam.demo.vo.Rq;

@Controller
public class UserReactionPointController {
	private ReactionPointService reactionPointService;
	private Rq rq;

	public UserReactionPointController(ReactionPointService reactionPointService, Rq rq) {
		this.reactionPointService = reactionPointService;
		this.rq = rq;
	}

	@RequestMapping("/usr/reactionPoint/doGoodReaction")
	@ResponseBody
	String doGoodReaction(String relTypeCode, int relId, String replaceUri) {
		ResultData actorCanMakeReactionPointRd = reactionPointService.actorCanMakeReactionPoint(rq.getLoginedMemberId(),
				relTypeCode, relId);

		if (actorCanMakeReactionPointRd.isFail()) {
			return rq.jshistoryBack(actorCanMakeReactionPointRd.getMsg());
		}

		ResultData addGoodReactionPoint = reactionPointService.addGoodReactionPoint(rq.getLoginedMemberId(), relTypeCode, relId);

		return rq.jsReplace(addGoodReactionPoint.getMsg(), replaceUri);
	}
	
	@RequestMapping("/usr/reactionPoint/doBadReaction")
	@ResponseBody
	String doBadReaction(String relTypeCode, int relId, String replaceUri) {
		ResultData actorCanMakeReactionPointRd = reactionPointService.actorCanMakeReactionPoint(rq.getLoginedMemberId(),
				relTypeCode, relId);

		if (actorCanMakeReactionPointRd.isFail()) {
			return rq.jshistoryBack(actorCanMakeReactionPointRd.getMsg());
		}

		ResultData addBadReactionPointRd = reactionPointService.addBadReactionPoint(rq.getLoginedMemberId(), relTypeCode, relId);

		return rq.jsReplace(addBadReactionPointRd.getMsg(), replaceUri);
	}

	@RequestMapping("/usr/reactionPoint/doCancelGoodReaction")
	@ResponseBody
	String doCancelGoodReaction(String relTypeCode, int relId, String replaceUri) {
		ResultData actorCanMakeReactionPointRd = reactionPointService.actorCanMakeReactionPoint(rq.getLoginedMemberId(),
				relTypeCode, relId);

		if (actorCanMakeReactionPointRd.isSuccess()) {
			return rq.jshistoryBack("이미 취소되었습니다.");
		}

		ResultData deleteGoodReactionPointRd = reactionPointService.deleteGoodReactionPoint(rq.getLoginedMemberId(), relTypeCode, relId);

		return rq.jsReplace(deleteGoodReactionPointRd.getMsg(), replaceUri);
	}

	@RequestMapping("/usr/reactionPoint/doCancelBadReaction")
	@ResponseBody
	String doCancelBadReaction(String relTypeCode, int relId, String replaceUri) {
		ResultData actorCanMakeReactionPointRd = reactionPointService.actorCanMakeReactionPoint(rq.getLoginedMemberId(),
				relTypeCode, relId);
		
		if (actorCanMakeReactionPointRd.isSuccess()) {
			return rq.jshistoryBack("이미 취소되었습니다.");
		}
		
		ResultData deleteBadReactionPointRd = reactionPointService.deleteBadReactionPoint(rq.getLoginedMemberId(), relTypeCode, relId);
		
		return rq.jsReplace(deleteBadReactionPointRd.getMsg(), replaceUri);
	}
}