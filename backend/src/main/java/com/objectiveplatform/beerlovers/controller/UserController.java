package com.objectiveplatform.beerlovers.controller;

import com.objectiveplatform.beerlovers.dto.User;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Base64;

@RestController
@RequestMapping("/v1/")
@CrossOrigin
@NoArgsConstructor
public class UserController {

    @PostMapping("/login")
    public boolean login(@RequestBody @Valid User user) {
        return user.getUserName().equals("user") && user.getPassword().equals("password");
    }

    @GetMapping("/user")
    public Principal user(HttpServletRequest request) {
        String authToken = request.getHeader("Authorization").substring("Basic".length()).trim();
        return () -> new String(Base64.getDecoder().decode(authToken)).split(":")[0];
    }
}
