package com.fourchat.infrastructure.controllers;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping(path = "/greets")
    public String greets() {

        OAuth2User user = ((OAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return "Hello" + user.getAttribute("name");
    }

    @GetMapping(path = "/unauthenticated")
    public String unauthenticatedRequests() {
        return "Hello, this is an unauthenticated request";
    }

}
