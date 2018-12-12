package ru.itmo.webmail.web.page;

import ru.itmo.webmail.model.exception.RepositoryException;
import ru.itmo.webmail.model.exception.ValidationException;
import ru.itmo.webmail.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.Map;

public class ConfirmPage extends Page {
    private void action(HttpServletRequest request, Map<String, Object> view) {
        // No operations.
    }

    private void confirm(HttpServletRequest request, Map<String, Object> view) {
        String secret = request.getParameter("secret");
        try {
            getEmailConfirmationService().validateSecret(secret);
            getEmailConfirmationService().confirm(secret);
        } catch (RepositoryException | ValidationException | SQLException e) {
            view.put("secret", secret);
            view.put("error", e.getMessage());
            return;
        }
        request.getSession().removeAttribute("secret");
        throw new RedirectException("/index", "confirmDone");
    }

    @Override
    public void before(HttpServletRequest request, Map<String, Object> view) {
        super.before(request, view);

        if (getUser() != null) {
            throw new RedirectException("/index");
        }
    }
}
