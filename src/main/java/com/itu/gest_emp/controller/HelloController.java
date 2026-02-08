package com.itu.gest_emp.controller;

import servlet.annotations.*;

import java.util.Map;

import servlet.ModelView;

@Controller
public class HelloController {

    @GetMapping("/test")
    public ModelView testGet() {
        ModelView mv = new ModelView();
        mv.setView("test.jsp");
        mv.addObject("message", "GET request");
        return mv;
    }

    @PostMapping("/test")
    public String testPost(
            @RequestParam("var1") String v1,
            @RequestParam("var2") int v2) {
        return "POST var1=" + v1 + ", var2=" + v2;
    }

    @PostMapping("/save")
    public String save(Map<String, Object> data) {

        ModelView mv = new ModelView();
        mv.setView("test.jsp");

        mv.addObject("nom", data.get("nom"));
        mv.addObject("prenom", data.get("prenom"));
        return data.toString();
    }

}
