package ru.itmo.webmail.web.page;

import ru.itmo.webmail.model.domain.User;
import ru.itmo.webmail.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class MyArticlesPage extends Page {
    private void action(HttpServletRequest request, Map<String, Object> view) {
        view.put("articles", getArticleService().findAll(((User)view.get("user")).getId()));
    }

    private void update(HttpServletRequest request, Map<String, Object> view) {
        long id = Long.parseLong(request.getParameter("id"));
        long userId = Long.parseLong(request.getParameter("userId"));
        if (userId != ((User)view.get("user")).getId()) {
            return;
        }
        long type = request.getParameter("type").equals("Show") ? 1 : 0;
        getArticleService().update(id, type);
    }

    @Override
    public void before(HttpServletRequest request, Map<String, Object> view) {
        super.before(request, view);
        if (isEmptyUser()) {
            throw new RedirectException("/index");
        }
    }
}
