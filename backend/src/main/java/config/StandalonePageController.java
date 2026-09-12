package com.example.storyworkshop.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@ConditionalOnProperty(name = "app.standalone", havingValue = "true")
public class StandalonePageController {

    @GetMapping({
            "/",
            "/stories",
            "/stories/{id}",
            "/rank",
            "/play/{id}",
            "/progress",
            "/achievements",
            "/my-stories",
            "/editor/{id}",
            "/audit",
            "/report",
            "/admin",
            "/login",
            "/register"
    })
    public String index() {
        return "forward:/index.html";
    }
}
