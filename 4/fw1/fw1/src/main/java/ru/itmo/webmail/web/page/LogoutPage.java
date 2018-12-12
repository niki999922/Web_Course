package ru.itmo.webmail.web.page;

import ru.itmo.webmail.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;

public class LogoutPage extends Page{

    private void action(HttpServletRequest request) {
        request.getSession().setAttribute("user", null);
        throw new RedirectException("/index", "logoutDone");
    }

}
