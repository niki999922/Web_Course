package ru.itmo.webmail.web.page;

import ru.itmo.webmail.model.domain.User;
import ru.itmo.webmail.model.exception.RepositoryException;
import ru.itmo.webmail.model.exception.ValidationException;
import ru.itmo.webmail.model.service.UserService;
import ru.itmo.webmail.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class TalksPage extends Page {
    private void action(HttpServletRequest request, Map<String, Object> view) {
        // No operations.
    }

    private void sendMessage(HttpServletRequest request, Map<String, Object> view) {
        String name = request.getParameter("name");
        String message = request.getParameter("message");
        try {
            if (message == null || message.isEmpty()) throw new ValidationException("Empty message");
            getTalkService().sendMessage((Long) request.getSession(true).getAttribute(USER_ID_SESSION_KEY), name, message);
        } catch (ValidationException | RepositoryException | SQLException e) {
            view.put("name", name);
            view.put("message", message);
            view.put("error", e.getMessage());
            return;
        }
        throw new RedirectException("/talks");
    }

    private void showDialog(HttpServletRequest request, Map<String, Object> view) {
        String name = request.getParameter("name");
        try {
            if (name.isEmpty()) throw new ValidationException("Empty target user");
            UserService.checkLogin(name);
            view.put("messages", getTalkService().findAllMessage(getUserService().find((Long) request.getSession(true).getAttribute(USER_ID_SESSION_KEY)).getLogin(), name));
        } catch (ValidationException | RepositoryException | SQLException e) {
            view.put("name", name);
        }
    }

    private void showAllDialog(HttpServletRequest request, Map<String, Object> view) {
        try {
            List<User> users = getUserService().findAll();
            view.put("messages", getTalkService().findAllMessage(getUserService().find((Long) request.getSession(true).getAttribute(USER_ID_SESSION_KEY)).getLogin(), users));
        } catch (ValidationException | RepositoryException | SQLException e) {}
    }

    @Override
    public void before(HttpServletRequest request, Map<String, Object> view) {
        super.before(request, view);
        if (getUser() == null) {
            throw new RedirectException("/index");
        }
    }
}
