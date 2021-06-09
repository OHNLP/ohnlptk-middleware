package org.ohnlp.ohnlptk.controllers;

import org.ohnlp.ohnlptk.auth.AuthAndAccessComponent;
import org.ohnlp.ohnlptk.entities.User;
import org.ohnlp.ohnlptk.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

/**
 * Contains User Information management functions
 */
@Controller
@RequestMapping("/_user")
public class UserInfoController {
    private final AuthAndAccessComponent authAndAccessComponent;
    private final UserRepository userRepository;

    @Autowired
    public UserInfoController(AuthAndAccessComponent authAndAccessComponent, UserRepository userRepository) {
        this.authAndAccessComponent = authAndAccessComponent;
        this.userRepository = userRepository;
    }

    @RequestMapping("/details")
    public @ResponseBody User userDetails(@ApiIgnore Authentication auth) {
        return this.authAndAccessComponent.getUserForSpringSecurityContextAuth(auth);
    }
}
