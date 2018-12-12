package ru.itmo.webmail.model.service;

import ru.itmo.webmail.model.domain.Article;
import ru.itmo.webmail.model.domain.User;
import ru.itmo.webmail.model.repository.ArticleRepository;
import ru.itmo.webmail.model.repository.impl.ArticleRepositoryImpl;

import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class ArticleService {
    private ArticleRepository articleRepository = new ArticleRepositoryImpl();

    public void addArticle(User user, String title, String textArticle) {
        articleRepository.save(user.getId(), title, textArticle);
    }

    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    public List<Article> findAll(long userId) {
        return articleRepository.findAll(userId);
    }

    public void update(long articalId, long type) {
        articleRepository.update(articalId, type);
    }

}
