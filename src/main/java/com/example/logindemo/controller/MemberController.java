package com.example.logindemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MemberController {

    @ResponseBody
    @RequestMapping(value = "/getStringTest",method = RequestMethod.GET)
    public String getStringTest(){
        return "Hello,world!";
    }
}
