package ru.itmo.webmail.web.page;

import ru.itmo.webmail.model.domain.User;
import ru.itmo.webmail.model.repository.UserRepository;
import ru.itmo.webmail.model.repository.impl.UserRepositoryImpl;
import ru.itmo.webmail.model.service.NewsService;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Map;

public class IndexPage extends Page{

    private NewsService newsService = new NewsService();
    private UserRepository userRepository = new UserRepositoryImpl();

    private void action(Map<String, Object> view) {
        view.put("news", newsService.findAll().toArray());
    }

    private void registrationDone(HttpServletRequest request, Map<String, Object> view) {
        view.put("message", "You have been registered");
        view.put("userCount", userRepository.findCount());
    }

    private void loginDone(HttpServletRequest request, Map<String, Object> view) {
        view.put("message", "Welcome back, " + ((User)request.getSession().getAttribute("user")).getLogin());
        view.put("userCount", userRepository.findCount());
    }

    private void turnOffCheats(HttpServletRequest request, Map<String, Object> view) {
        view.put("userCount", userRepository.findCount());
        view.put("message", "Cheats off!!!");
    }


    private void logoutDone(HttpServletRequest request, Map<String, Object> view) {
        view.put("userCount", userRepository.findCount());
        view.put("message", "You have logged out");
    }

    private void addDone(HttpServletRequest request, Map<String, Object> view) {
        view.put("message", "You have successfully added new article");
        view.put("userCount", userRepository.findCount());
    }

}
