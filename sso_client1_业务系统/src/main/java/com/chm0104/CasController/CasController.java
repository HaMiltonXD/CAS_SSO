package com.chm0104.CasController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
public class CasController {

    @RequestMapping("/hello")
    public ModelAndView root(HttpSession session){
        ModelAndView mav=new ModelAndView();
        mav.setViewName("index");
        return mav;
    }
    //登出
    @RequestMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:https://192.168.150.128:8443/cas/logout?service=https://192.168.150.128:8443/cas/login";
    }
}
