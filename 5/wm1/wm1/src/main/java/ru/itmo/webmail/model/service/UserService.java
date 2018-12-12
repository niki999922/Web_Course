package ru.itmo.webmail.model.service;

import com.google.common.hash.Hashing;
import ru.itmo.webmail.model.domain.Message;
import ru.itmo.webmail.model.domain.User;
import ru.itmo.webmail.model.exception.RepositoryException;
import ru.itmo.webmail.model.exception.ValidationException;
import ru.itmo.webmail.model.repository.UserRepository;
import ru.itmo.webmail.model.repository.impl.UserRepositoryImpl;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Math.abs;

@SuppressWarnings("UnstableApiUsage")
public class UserService {
    private static final String USER_PASSWORD_SALT = "dc3475f2b301851b";

    private UserRepository userRepository = new UserRepositoryImpl();

    static public boolean checkLogin(String login) throws ValidationException {
        if (login == null || login.isEmpty()) {
            throw new ValidationException("Login is required");
        }
        if (!login.matches("[a-z]+")) {
            throw new ValidationException("Login can contain only lowercase Latin letters");
        }
        if (login.length() > 8) {
            throw new ValidationException("Login can't be longer than 8");
        }
        return true;
    }

    static public boolean checkPassword(String password) throws ValidationException {
        if (password == null || password.isEmpty()) {
            throw new ValidationException("Password is required");
        }
        if (password.length() < 4) {
            throw new ValidationException("Password can't be shorter than 4");
        }
        if (password.length() > 32) {
            throw new ValidationException("Password can't be longer than 32");
        }
        return true;
    }

    static public boolean checkEmail(String email) throws ValidationException {
        Pattern pattern = Pattern.compile("[a-z0-9]+@[a-z]+[.][a-z]+");
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            throw new ValidationException("An email should look like this: example@inbox.com");
        }
        return true;
    }

    public void validateRegistration(User user, String password) throws ValidationException {
        checkLogin(user.getLogin());
        checkEmail(user.getEmail());
        checkPassword(password);

        if (userRepository.findByLoginOrEmail(user.getLogin(), user.getEmail()) != null) {
            throw new ValidationException("Login or Email is already in use");
        }
    }

    public void register(User user, String password) {
        String passwordSha = getPasswordSha(password);
        userRepository.save(user, passwordSha);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void validateEnter(String login, String password) throws ValidationException {
        checkPassword(password);
        boolean resultCheckLogin = false, resultCheckEmail = false;
        try {
            resultCheckLogin = checkLogin(login);
        } catch (ValidationException ignored) {}
        try {
            resultCheckEmail = checkEmail(login);
        } catch (ValidationException ignored) {}

        if (!(resultCheckLogin || resultCheckEmail)) {
            throw new ValidationException("Incorrect login or email");
        }
    }

    private String getPasswordSha(String password) {
        return Hashing.sha256().hashString(USER_PASSWORD_SALT + password,
                StandardCharsets.UTF_8).toString();
    }

    public User authorize(String login, String password) {
        return userRepository.findByLoginOrEmailAndPasswordSha(login, getPasswordSha(password));
    }

    public User find(long userId) {
        return userRepository.find(userId);
    }
}
