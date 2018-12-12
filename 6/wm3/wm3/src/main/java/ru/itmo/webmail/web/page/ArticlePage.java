package ru.itmo.webmail.web.page;

import ru.itmo.webmail.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class ArticlePage extends Page {
    private void action(HttpServletRequest request, Map<String, Object> view) {
        // No operations.
    }

    public Map<String, Object> addArticle(HttpServletRequest request, Map<String, Object> view) {
        String title = request.getParameter("title");
        String textArticle = request.getParameter("textArticle");
        if (title.isEmpty() || textArticle.isEmpty()) {
            view.put("success", false);
            view.put("error", "Title or text is empty.");
            return view;
        }
        getArticleService().addArticle(getUser(), title, textArticle);
        view.put("success", true);
        view.put("info", "You have added article.");
        return view;
    }

        @Override
    public void before(HttpServletRequest request, Map<String, Object> view) {
        super.before(request, view);
        if (isEmptyUser()) {
            throw new RedirectException("/index");
        }
    }
}
