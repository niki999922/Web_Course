package ru.itmo.webmail.model.repository;

import ru.itmo.webmail.model.domain.Article;

import java.util.List;

public interface ArticleRepository {

    void save(long userId, String title, String textArticle);
    List<Article> findAll();
    List<Article> findAll(long userId);
    void update(long articalId, long type);
}
