package com.chemicaltracker.persistence;

import com.chemicaltracker.model.storage.User;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDAO extends DynamoDBDAO<User> implements UserDetailsService {

    private static volatile UserDAO instance;

    public static UserDAO getInstance() {

        if (instance == null) {
            synchronized (UserDAO.class) {
                if (instance == null) {
                    instance = new UserDAO();
                }
            }
        }

        return instance;
    }

    @Override
    public Class getObjectClass() {
        return User.class;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        final User user = find(username);
        if (user == null) {
            throw new UsernameNotFoundException("Could not find username: " + username);
        }
        return user;
    }
}
