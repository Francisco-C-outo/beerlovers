package com.objectiveplatform.beerlovers.service;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    /**
     * Since we did not intend to fully implement a complete flow of registration, login, logout, for simplicity
     * purposes we only used the username to verify the user information we possess is valid. To implement this,
     * username was made unique in our backend.
     */
    public String retrieveAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return authentication.getName();
        }
        throw new IllegalStateException("There is currently no logged in user!");
    }
}
