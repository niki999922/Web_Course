package ru.itmo.wp.servlet;

import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class MessagesServlet extends HttpServlet {

    List<Messages> list = new ArrayList<>();

    private class Messages {
     final private String user;
     final private String text;

        Messages(String user, String text) {
            this.user = user;
            this.text = text;
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        String uri = request.getRequestURI();
        HttpSession session = request.getSession(true);
        if (uri.endsWith("auth")) {
            String userName = request.getParameter("user");
            if (userName == null ) userName = "";
            session.setAttribute("user", userName);
            String json = new Gson().toJson(userName);
            response.getWriter().print(json);
            response.getWriter().flush();
        }

        if (uri.endsWith("findAll")) {
            String json = new Gson().toJson(list);
            response.getWriter().print(json);
            response.getWriter().flush();
        }

        if (uri.endsWith("add")) {
            String text = request.getParameter("text");
            String user = (String) session.getAttribute("user");
            if (user == null) user = "";
            list.add(new Messages(user, text));
            String json = new Gson().toJson(text);
            response.getWriter().print(json);
            response.getWriter().flush();
        }
    }
}
