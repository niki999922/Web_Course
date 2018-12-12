package ru.itmo.webmail.web.page;

import ru.itmo.webmail.model.domain.User;
import ru.itmo.webmail.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class AdminPage extends Page{
    @Override
    public void before(HttpServletRequest request, Map<String, Object> view) {
        super.before(request, view);
        if (isEmptyUser()) {
            throw new RedirectException("/index");
        }
    }

    public String update(HttpServletRequest request, Map<String, Object> view) {
        long id = Long.parseLong(request.getParameter("id"));
        if (!getUser().isAdmin()) return "You aren't admin!";
        long type = request.getParameter("type").equals("Enable") ? 1 : 0;
        getUserService().update(id, type);
        return "Confirm";
    }

    public String getUserIsAdmin(HttpServletRequest request, Map<String, Object> view) {
        return getUser().getAdmin();
    }


    private void action(HttpServletRequest request, Map<String, Object> view) {
        view.put("users", getUserService().findAll());
    }
}
