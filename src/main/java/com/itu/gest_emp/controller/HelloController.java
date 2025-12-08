package com.itu.gest_emp.controller;

import servlet.annotations.Controller;
import servlet.annotations.Url;
import servlet.ModelView;

@Controller
public class HelloController {

    @Url("/test")
    public ModelView hello() {
        ModelView mv = new ModelView();
        mv.setView("test.jsp");

        // Passer des variables à la JSP
        mv.addObject("message", "Bonjour depuis le controller !");
        mv.addObject("age", 19);
        return mv;
    }

    @Url("/bye")
    public String bye() {
        return "Au revoir !";
    }
}
