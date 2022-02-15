package com.objectiveplatform.beerlovers.event;

import com.objectiveplatform.beerlovers.datamodel.entity.User;
import com.objectiveplatform.beerlovers.datamodel.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * On startup let's register our user to our db - obviously would not be needed in fully operational application
 * once again opting for this strategy for simplicity reasons
 */
@Component
@RequiredArgsConstructor
public class DummyDataOnStartup {

    @Autowired
    private final UserRepository userRepository;

    @EventListener
    public void appReady(ApplicationReadyEvent event) {
        final String username = "user";
        final String password = "password";
        User defaultUser = new User();
        defaultUser.setUsername(username);
        defaultUser.setPassword(password);
        userRepository.save(defaultUser);
    }
}
