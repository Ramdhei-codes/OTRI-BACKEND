package com.ucaldas.otri.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class OtriController {

    @GetMapping("/hello")
    public String greet(){
        return "Hello, World!";
    }
}
