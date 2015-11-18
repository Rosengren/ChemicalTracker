package com.chemicaltracker.persistence;

import com.chemicaltracker.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    public Optional<User> findOneByEmail(String email);

    public Optional<User> findOne(String id);

    public User save(User user);
}
