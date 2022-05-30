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
		
		return ResultData.from("S-1", Ut.f("%d번 게시물이 생성되었습니다.", id), id);
	}

	public List<Article> getArticles() {
		return articleRepository.getArticles();
	}

	public Article getArticle(int id) {
		return articleRepository.getArticle(id);
	}

	public void deleteArticle(int id) {
		articleRepository.deleteArticle(id);
		
	}

	public void modifyArticle(int id, String title, String body) {
		articleRepository.modifyArticle(id, title, body);
		
	}
}