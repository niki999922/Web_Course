package ru.itmo.wm4.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.itmo.wm4.service.UserService;

import javax.servlet.http.HttpServletRequest;

@Controller
public class UsersPage extends Page {
    private final UserService userService;

    public UsersPage(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/users")
    public String main(Model model) {
        model.addAttribute("users", userService.findAll());
        return "UsersPage";
    }

    @PostMapping(path = "/switchDisable")
    public String switchDisable(HttpServletRequest request) {
        if (request.getSession().getAttribute(USER_ID_SESSION_KEY) != null) {
            long id = Long.parseLong(request.getParameter("id"));
            String status = request.getParameter("status");
            int disable = status.equalsIgnoreCase("enable") ? 0 : 1;
            userService.switchDisable(id, disable);
        }
        return "redirect:/users";
    }
}
