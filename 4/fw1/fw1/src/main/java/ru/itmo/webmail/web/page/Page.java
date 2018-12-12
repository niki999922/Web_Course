package ru.itmo.webmail.web.page;

import ru.itmo.webmail.model.domain.User;
import ru.itmo.webmail.model.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

class Page {

    private UserService userService = new UserService();

    private static final  String[] nameLogout = {"<div class=\"enter-or-register-box\">\n" +
            "        <a href=\"/me\">",
            "        </a>\n" +
            "        |\n" +
            "        <a href=\"/logout\">Logout</a>\n" +
            "    </div>"};

    private static final String enterRegister = "<div class=\"enter-or-register-box\">\n" +
            "        <a href=\"/enter\">Enter</a>\n" +
            "        |\n" +
            "        <a href=\"/register\">Register</a>\n" +
            "    </div>";

    void before(HttpServletRequest request, Map<String, Object> view) {
        view.put("userCount", userService.findCount());
        view.put("userService", userService);
    }

    void after(HttpServletRequest request, Map<String, Object> view) {
        view.put("userCount", userService.findCount());
        Object user = request.getSession().getAttribute("user");
        if (user != null) {
            view.put("userBox", nameLogout[0] + ((User)user).getLogin() + nameLogout[1]);
            view.put("addNews", "<li><a href=\"/addNews\">Add News</a></li>");
        } else {
            view.put("userBox", enterRegister);
        }
    }

}
