package com.hodbenor.project.eventsservice.dao;

import com.hodbenor.project.eventsservice.dao.beans.User;

import java.util.Optional;

public interface UserDao {
    long insertUser(User user);
    Optional<User> findUserById(long userId);
    Optional<User> findUserByToken(String loginToken);
    Optional<User> findUser(String username, String password);
}
