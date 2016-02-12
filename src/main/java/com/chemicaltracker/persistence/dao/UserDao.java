package com.chemicaltracker.persistence.dao;

import com.chemicaltracker.persistence.model.User;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDao extends DynamoDBDao<User> implements UserDetailsService {

    private static volatile UserDao instance;

    public static UserDao getInstance() {

        if (instance == null) {
            synchronized (UserDao.class) {
                if (instance == null) {
                    instance = new UserDao();
                }
            }
        }

        return instance;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        logger.info("Looking up username: " + username);
        final User user = find(username);
        if (user == null) {
            logger.warn("Did not find username " + username + " in DB");
            throw new UsernameNotFoundException("Could not find username: " + username);
        } else {
            logger.info("Successfully found user in DB!");
        }
        return user;
    }
}
