package com.example.registerlogin;

import com.example.registerlogin.entity.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class ResponseController {

    @RequestMapping("/getuser")
    public User getuser(){

        User user = new User(000L,"passwrod","username",1, "sex", "interest","signature","location",
                "portrait_image","background_image",
                "birthday_time", "create_time");
        return user;
    }
}
