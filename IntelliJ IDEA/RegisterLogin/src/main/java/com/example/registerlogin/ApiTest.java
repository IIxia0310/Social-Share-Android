package com.example.registerlogin;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiTest {

    @GetMapping("/gettest")
    public String gettest(@RequestParam("username") String username){
        return "test"+username;
    }
}
