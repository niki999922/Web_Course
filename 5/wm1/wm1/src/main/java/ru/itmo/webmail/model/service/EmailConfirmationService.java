package ru.itmo.webmail.model.service;

import ru.itmo.webmail.model.domain.User;
import ru.itmo.webmail.model.exception.ValidationException;
import ru.itmo.webmail.model.repository.EmailConfirmationRepository;
import ru.itmo.webmail.model.repository.impl.EmailConfirmationRepositoryImpl;

import java.sql.SQLException;
import java.util.Random;

import static java.lang.Math.abs;

@SuppressWarnings("UnstableApiUsage")
public class EmailConfirmationService {
    private EmailConfirmationRepository emailConfirmationRepository = new EmailConfirmationRepositoryImpl();
    private static final long SECRET_LENGTH = 66;

    public String addConfirm(User user) {
        String secret = generateSecret();
        emailConfirmationRepository.addConfirm(user, secret);
        return secret;
    }

    public void validateSecret(String secret) throws ValidationException {
        if (secret.length() != SECRET_LENGTH ) {
            throw new ValidationException("Secret is not correct");
        }
    }

    public void confirm(String secret) throws SQLException {
        emailConfirmationRepository.confirm(secret);
    }

    private String generateSecret() {
        final Random random = new Random();
        char[] text = new char[(int) SECRET_LENGTH];
        for (int i = 0; i < SECRET_LENGTH; i++)
        {
            text[i] = (char)(((abs(random.nextInt() % 2) == 1)? abs(random.nextInt())% 26 + 97 : abs(random.nextInt())% 10 + 48));
        }
        return new String(text);
    }

}
