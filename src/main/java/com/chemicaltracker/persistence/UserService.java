package com.chemicaltracker.persistence;

import java.util.Optional;
import java.util.Collection;

import com.chemicaltracker.model.User;
import com.chemicaltracker.model.UserCreateForm;

public interface UserService {

    public Optional<User> getUserById(long id);

    public Optional<User> getUserByEmail(String email);

    public User create(UserCreateForm form);
}
