package com.chemicaltracker.service;

import com.chemicaltracker.persistence.dao.UserDao;
import com.chemicaltracker.persistence.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private UserDao userDB = UserDao.getInstance();

    @Override
    public void addUser(User user) {
        userDB.create(user);
    }
}
