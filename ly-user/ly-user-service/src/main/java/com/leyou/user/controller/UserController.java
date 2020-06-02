package com.leyou.user.controller;

import com.leyou.user.pojo.User;
import com.leyou.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/check/{data}/{type}")
    public ResponseEntity<Boolean> checkUser(@PathVariable("data") String data , @PathVariable("type") Integer type){
        Boolean bool = userService.checkUser(data, type);

        if(bool!=null){
            return ResponseEntity.ok(bool);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PostMapping("/code")
    public ResponseEntity<Void> sendVerifyCode(@RequestParam("phone")String phone){
        Boolean boo = userService.sendVerifyCode(phone);
        if(boo==null||!boo){
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("register")
    public ResponseEntity<Void> register(User user,@RequestParam String code){

        Boolean boo = userService.register(user, code);

        if(boo){
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("query")
    public ResponseEntity<User> login(@RequestParam("username") String username,@RequestParam("password")String password){

        User user = userService.login(username, password);
        if(user==null){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(user);
    }
}
