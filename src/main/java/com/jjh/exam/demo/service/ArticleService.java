package com.jjh.exam.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jjh.exam.demo.repository.ArticleRepository;
import com.jjh.exam.demo.vo.Article;

@Service
public class ArticleService {
	private ArticleRepository articleRepository;
	
	private ArticleService(ArticleRepository articleRepository) {
		this.articleRepository = articleRepository;
		articleRepository.makeTestData();
	}

	public Article writeArticle(String title, String body) {
		return articleRepository.writeArticle(title, body);
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