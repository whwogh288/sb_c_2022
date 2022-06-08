package com.jjh.exam.demo.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jjh.exam.demo.service.ArticleService;
import com.jjh.exam.demo.service.BoardService;
import com.jjh.exam.demo.util.Ut;
import com.jjh.exam.demo.vo.Article;
import com.jjh.exam.demo.vo.Board;
import com.jjh.exam.demo.vo.ResultData;
import com.jjh.exam.demo.vo.Rq;

@Controller
public class UserArticleController {
	private ArticleService articleService;
	private BoardService boardService;
	
	public UserArticleController(ArticleService articleService, BoardService boardService) {
		this.articleService = articleService;
		this.boardService = boardService;
	}
	
	// 액션 메서드 시작
	@RequestMapping("/usr/article/doWrite")
	@ResponseBody
	public String doWrite(HttpServletRequest req, String title, String body, String replaceUri) {
		Rq rq = (Rq)req.getAttribute("rq");
		
		if (Ut.empty(title)) {
			return rq.jshistoryBack("title(을)를 입력해주세요.");
		}
		if (Ut.empty(body)) {
			return rq.jshistoryBack("body(을)를 입력해주세요.");
		}
		
		ResultData<Integer> writeArticleRd = articleService.writeArticle(rq.getLoginedMemberId(), title, body);
		int id = (int) writeArticleRd.getData1();
		
		if (Ut.empty(replaceUri)) {
			replaceUri = Ut.f("../article/detail?id=%d", id);
		}

		return rq.jsReplace(Ut.f("%d번 글이 생성되었습니다.", id), replaceUri);
	}

	@RequestMapping("/usr/article/list")
	public String showList(HttpServletRequest req, Model model, int boardId) {
		Board board = boardService.getBoardById(boardId);
		
		Rq rq = (Rq)req.getAttribute("rq");
		
		if ( board == null) {
			return rq.historyBackJsOnView(Ut.f("%d번 게시판은 존재하지 않습니다.", boardId));
		}
		
		List<Article> articles = articleService.getForPrintArticles(rq.getLoginedMemberId());
		
		model.addAttribute("board", board);
		model.addAttribute("articles", articles);
		
		return "usr/article/list";
	}
	
	@RequestMapping("/usr/article/detail")
	public String showDetail(HttpServletRequest req, Model model, int id) {
		Rq rq = (Rq)req.getAttribute("rq");
		
		Article article = articleService.getForPrintArticle(rq.getLoginedMemberId(), id);
		
		model.addAttribute("article", article);
		
		return "usr/article/detail";
	}
	
	@RequestMapping("/usr/article/getArticle")
	@ResponseBody
	public ResultData<Article> getArticle(HttpServletRequest req, int id) {
		Rq rq = (Rq)req.getAttribute("rq");
		
		Article article = articleService.getForPrintArticle(rq.getLoginedMemberId(), id);
		
		if ( article == null ) {
			return ResultData.from("F-1", Ut.f("%d번 게시물이 존재하지 않습니다.", id)); 
		}
		return ResultData.from("S-1", Ut.f("%d번 게시물입니다.", id), "article", article);
	}

	@RequestMapping("/usr/article/doDelete")
	@ResponseBody
	public String doDelete(HttpServletRequest req, int id) {
		Rq rq = (Rq)req.getAttribute("rq");
		
		Article article = articleService.getForPrintArticle(rq.getLoginedMemberId(), id);
		
		if (article == null) {
			rq.jshistoryBack(Ut.f("%d번 게시물이 존재하지 않습니다.", id));
		}
		
		if (article.getMemberId() != rq.getLoginedMemberId()) {
			return rq.jshistoryBack("권한이 없습니다.");
		}

		articleService.deleteArticle(id);

		return rq.jsReplace(Ut.f("%d번 게시물을 삭제하였습니다.", id), "../article/list");
	}
	
	@RequestMapping("/usr/article/modify")
	public String showModify(HttpServletRequest req, Model model, int id) {
		Rq rq = (Rq)req.getAttribute("rq");
		
		Article article = articleService.getForPrintArticle(rq.getLoginedMemberId(), id);
		
		if (article == null) {
			return rq.historyBackJsOnView(Ut.f("%d번 게시물이 존재하지 않습니다.", id));
		}
		
		ResultData actorCanModifyRd = articleService.actorCanModify(rq.getLoginedMemberId(), article);
		
		if (actorCanModifyRd.isFail()) {
			return rq.historyBackJsOnView(actorCanModifyRd.getMsg());
		}
		
		model.addAttribute("article", article);
		
		return "usr/article/modify";
	}
	
	@RequestMapping("/usr/article/doModify")
	@ResponseBody
	public String doModify(HttpServletRequest req, int id, String title, String body) {
		Rq rq = (Rq)req.getAttribute("rq");
		
		Article article = articleService.getForPrintArticle(rq.getLoginedMemberId(), id);

		if (article == null) {
			return rq.jshistoryBack(Ut.f("%d번 게시물이 존재하지 않습니다.", id));
		}
		
		ResultData actorCanModifyRd = articleService.actorCanModify(rq.getLoginedMemberId(), article);
		
		if (actorCanModifyRd.isFail()) {
			return rq.jshistoryBack(actorCanModifyRd.getMsg());
		}

	    articleService.modifyArticle(id, title, body);
	    
	    return rq.jsReplace(Ut.f("%d번 글이 수정되었습니다.", id), Ut.f("../article/detail?id=%d", id));
		}
	
	@RequestMapping("/usr/article/write")
	public String showWrite(HttpServletRequest req, Model model) {
		Rq rq = (Rq)req.getAttribute("rq");
		
		return "usr/article/write";
	}
	
	// 액션 메서드 끝
}