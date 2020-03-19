package com.example.logindemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class JumpController {
    @GetMapping("/user/main")
    public String jumpMain(){
        return "index";
    }

    @GetMapping("/user/login")
    public String jumpLogin(){return "login";}

    @GetMapping("/user/register")
    public String jumpRegister(){return "register";}

    @GetMapping("/user/login2")
    public String jumpLogin2(){return "main";}

    @GetMapping("/user/commodity")
    public String jumpCommonDity(){return "commodity";}
}
