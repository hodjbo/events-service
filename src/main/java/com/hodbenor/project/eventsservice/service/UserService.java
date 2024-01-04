package com.hodbenor.project.eventsservice.service;

import com.hodbenor.project.eventsservice.dao.UserDao;
import com.hodbenor.project.eventsservice.dao.beans.User;
import com.hodbenor.project.eventsservice.security.TokenService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private static final Logger LOGGER = LogManager.getLogger(UserService.class);

    private final UserDao userDao;
    private final TokenService tokenService;

    public UserService(UserDao userDao, TokenService tokenService) {
        this.userDao = userDao;
        this.tokenService = tokenService;
    }

    public Optional<User> signUp(String username, String password) {
        if (findUser(username).isPresent()) {
            LOGGER.error("Failed to insert new user: {} -> user already exist", username);

            return Optional.empty();
        }
        try {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setAuthToken(tokenService.generateToken());

            if (userDao.insertUser(user) > 0) {
                return Optional.of(user);
            }
        } catch (Exception e) {
            LOGGER.error("Failed to insert new user: {}", username, e);
        }

        return Optional.empty();
    }

    public String login(String username, String password) {
        return userDao.findUser(username, password)
                .map(user -> {
                    String authToken = tokenService.generateToken();
                    user.setAuthToken(authToken);
                    try {
                        userDao.updateToken(user);
                    return authToken;
                    } catch (Exception e) {
                        LOGGER.error("Failed to update token for user {}", user, e);

                        return null;
                    }

                }).orElse(null);
    }

    public Optional<User> findUser(String username) {
        try {

            return userDao.findUser(username);
        } catch (Exception e) {
            LOGGER.error("Failed to findUser user: {}", username, e);

            return Optional.empty();
        }
    }

    public Optional<User> findUserById(long userId) {
        try {

            return userDao.findUserById(userId);
        } catch (Exception e) {
            LOGGER.error("Failed to findUserById user: {}", userId, e);

            return Optional.empty();
        }
    }

    public Optional<User> findUserByToken(String loginToken) {
        try {

            return userDao.findUserByToken(loginToken);
        } catch (Exception e) {
            LOGGER.error("Failed to findUserByToken loginToken: {}", loginToken, e);

            return Optional.empty();
        }
    }
}