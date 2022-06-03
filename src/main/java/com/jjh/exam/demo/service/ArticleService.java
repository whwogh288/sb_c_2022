package com.jjh.exam.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jjh.exam.demo.repository.ArticleRepository;
import com.jjh.exam.demo.util.Ut;
import com.jjh.exam.demo.vo.Article;
import com.jjh.exam.demo.vo.ResultData;

@Service
public class ArticleService {
	private ArticleRepository articleRepository;
	
	private ArticleService(ArticleRepository articleRepository) {
		this.articleRepository = articleRepository;
	}

	public ResultData writeArticle(int memberId, String title, String body) {
		articleRepository.writeArticle(memberId, title, body);
		int id = articleRepository.getlastInsertId();
		
		return ResultData.from("S-1", Ut.f("%d번 게시물이 생성되었습니다.", id), "id", id);
	}

	public List<Article> getForPrintArticles() {
		return articleRepository.getArticles();
	}

	public Article getForPrintArticle(int id) {
		return articleRepository.getForPrintArticle(id);
	}

	public void deleteArticle(int id) {
		articleRepository.deleteArticle(id);
		
	}

	public ResultData<Article> modifyArticle(int id, String title, String body) {
		articleRepository.modifyArticle(id, title, body);
		
		Article article = getForPrintArticle(id);
		
		return ResultData.from("S-1", Ut.f("%d번 게시물을 수정하였습니다.", id), "article", article);
		
	}

	public ResultData actorCanModify(int actorId, Article article) {
		if(article == null) {
			return ResultData.from("f-1", "게시물이 존재하지 않습니다.");
		}
		if(article .getMemberId() != actorId) {
			return ResultData.from("F-2", "권한이 없습니다.");
		}
		return ResultData.from("S-1", "게시물 수정이 가능합니다.");
	}
}