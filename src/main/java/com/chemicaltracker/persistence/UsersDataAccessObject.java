package com.chemicaltracker.persistence;

import com.chemicaltracker.model.Login;

public interface UsersDataAccessObject {

    public boolean validateLogin(Login login);
}
