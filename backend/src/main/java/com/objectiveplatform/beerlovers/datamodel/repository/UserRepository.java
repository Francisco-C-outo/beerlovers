package com.objectiveplatform.beerlovers.datamodel.repository;

import com.objectiveplatform.beerlovers.datamodel.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsernameEquals(final String username);
}
