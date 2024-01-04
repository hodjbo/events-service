package com.hodbenor.project.eventsservice.service;

import com.hodbenor.project.eventsservice.dao.UserDao;
import com.hodbenor.project.eventsservice.dao.beans.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private static final Logger LOGGER = LogManager.getLogger(UserService.class);

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public long signUp(String username, String password) {
        try {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            return userDao.insertUser(user);
        } catch (Exception e) {
            LOGGER.error("Failed to insert new user: {}", username, e);

            return -1;
        }
    }

    public Optional<User> findUser(String username, String password) {
        try {

            return userDao.findUser(username, password);
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
}