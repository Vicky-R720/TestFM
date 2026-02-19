package com.itu.gest_emp.controller;

import servlet.annotations.*;
import servlet.JsonResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SessionController {

    // ============== TESTS SESSION (CRUD) ==============
    
    // Test 1 : Ajouter une valeur en session (CREATE)
    @PostMapping("/session/add")
    public String addToSession(@RequestParam("key") String key, 
                               @RequestParam("value") String value,
                               @Session Map<String, Object> session) {
        // Ajouter dans la session via HttpSession (car Map est une copie)
        // Pour modifier la session, on doit utiliser HttpSession directement
        return "Pour modifier la session, utilisez HttpSession directement";
    }
    
    // Test 1b : Ajouter une valeur en session avec HttpSession
    @PostMapping("/session/set")
    public String setSession(@RequestParam("key") String key, 
                            @RequestParam("value") String value,
                            @Session jakarta.servlet.http.HttpSession session) {
        session.setAttribute(key, value);
        return "Session updated: " + key + " = " + value;
    }
    
    // Test 2 : Lire une valeur de la session (READ)
    @GetMapping("/session/get")
    public String getFromSession(@RequestParam("key") String key,
                                 @Session jakarta.servlet.http.HttpSession session) {
        Object value = session.getAttribute(key);
        if (value == null) {
            return "Key '" + key + "' not found in session";
        }
        return "Session[" + key + "] = " + value;
    }
    
    // Test 3 : Lire toutes les valeurs de la session (READ ALL)
    @GetMapping("/session/all")
    public String getAllSession(@Session Map<String, Object> session) {
        if (session.isEmpty()) {
            return "Session is empty";
        }
        
        StringBuilder result = new StringBuilder("Session contents:\n");
        for (Map.Entry<String, Object> entry : session.entrySet()) {
            result.append("  ").append(entry.getKey())
                  .append(" = ").append(entry.getValue()).append("\n");
        }
        return result.toString();
    }
    
    // Test 4 : Supprimer une valeur de la session (DELETE)
    @PostMapping("/session/remove")
    public String removeFromSession(@RequestParam("key") String key,
                                   @Session jakarta.servlet.http.HttpSession session) {
        Object oldValue = session.getAttribute(key);
        session.removeAttribute(key);
        return "Removed from session: " + key + " (was: " + oldValue + ")";
    }
    
    // Test 5 : Vider toute la session
    @PostMapping("/session/clear")
    public String clearSession(@Session jakarta.servlet.http.HttpSession session) {
        session.invalidate();
        return "Session cleared (invalidated)";
    }
    
    // Test 6 : Incrémenter un compteur de visites
    @GetMapping("/session/visit-count")
    public String visitCount(@Session jakarta.servlet.http.HttpSession session) {
        Integer count = (Integer) session.getAttribute("visitCount");
        if (count == null) {
            count = 0;
        }
        count++;
        session.setAttribute("visitCount", count);
        return "Vous avez visité cette page " + count + " fois";
    }
    
    // Test 7 : Simuler un panier d'achat (session complexe)
    @PostMapping("/session/cart/add")
    public String addToCart(@RequestParam("item") String item,
                           @Session jakarta.servlet.http.HttpSession session) {
        @SuppressWarnings("unchecked")
        List<String> cart = (List<String>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute("cart", cart);
        }
        cart.add(item);
        return "Added '" + item + "' to cart. Total items: " + cart.size();
    }
    
    @GetMapping("/session/cart/view")
    public String viewCart(@Session jakarta.servlet.http.HttpSession session) {
        @SuppressWarnings("unchecked")
        List<String> cart = (List<String>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            return "Cart is empty";
        }
        
        StringBuilder result = new StringBuilder("Shopping Cart:\n");
        for (int i = 0; i < cart.size(); i++) {
            result.append((i + 1)).append(". ").append(cart.get(i)).append("\n");
        }
        return result.toString();
    }
    
    @PostMapping("/session/cart/clear")
    public String clearCart(@Session jakarta.servlet.http.HttpSession session) {
        session.removeAttribute("cart");
        return "Cart cleared";
    }
    
    // Test 8 : API JSON avec session
    @Json
    @GetMapping("/api/session/info")
    public Map<String, Object> getSessionInfo(@Session jakarta.servlet.http.HttpSession session) {
        Map<String, Object> info = new HashMap<>();
        info.put("sessionId", session.getId());
        info.put("creationTime", session.getCreationTime());
        info.put("lastAccessedTime", session.getLastAccessedTime());
        info.put("maxInactiveInterval", session.getMaxInactiveInterval());
        info.put("isNew", session.isNew());
        
        // Ajouter tous les attributs
        Map<String, Object> attributes = new HashMap<>();
        java.util.Enumeration<String> names = session.getAttributeNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            attributes.put(name, session.getAttribute(name));
        }
        info.put("attributes", attributes);
        
        return info;
    }
    
    // Test 9 : Exemple pratique - Système de connexion simple
    @PostMapping("/session/login")
    public String login(@RequestParam("username") String username,
                       @Session jakarta.servlet.http.HttpSession session) {
        session.setAttribute("user", username);
        session.setAttribute("loginTime", System.currentTimeMillis());
        return "Welcome " + username + "! You are now logged in.";
    }
    
    @GetMapping("/session/whoami")
    public String whoami(@Session jakarta.servlet.http.HttpSession session) {
        String user = (String) session.getAttribute("user");
        if (user == null) {
            return "You are not logged in";
        }
        Long loginTime = (Long) session.getAttribute("loginTime");
        long duration = (System.currentTimeMillis() - loginTime) / 1000;
        return "You are logged in as: " + user + " (since " + duration + " seconds)";
    }
    
    @PostMapping("/session/logout")
    public String logout(@Session jakarta.servlet.http.HttpSession session) {
        String user = (String) session.getAttribute("user");
        session.invalidate();
        return "Goodbye " + (user != null ? user : "Guest") + "! You have been logged out.";
    }
    
    // ============== TESTS AVEC ENTITES ==============
    
    // Sauvegarder un Employee en session
    @PostMapping("/session/employee/save")
    public servlet.ModelView saveEmployee(@RequestParam("name") String name,
                                         @Session jakarta.servlet.http.HttpSession session) {
        com.itu.gest_emp.model.Employee emp = new com.itu.gest_emp.model.Employee();
        emp.setName(name);
        session.setAttribute("employee", emp);
        
        servlet.ModelView mv = new servlet.ModelView();
        mv.setView("test-session-entity.jsp");
        mv.addObject("message", "Employee '" + name + "' saved in session!");
        mv.addObject("employee", emp);
        return mv;
    }
    
    // Afficher l'Employee depuis la session
    @GetMapping("/session/employee/show")
    public servlet.ModelView showEmployee(@Session jakarta.servlet.http.HttpSession session) {
        com.itu.gest_emp.model.Employee emp = (com.itu.gest_emp.model.Employee) session.getAttribute("employee");
        
        servlet.ModelView mv = new servlet.ModelView();
        mv.setView("test-session-entity.jsp");
        if (emp != null) {
            mv.addObject("message", "Employee found in session!");
            mv.addObject("employee", emp);
        } else {
            mv.addObject("message", "No employee in session.");
        }
        return mv;
    }
    
    // Supprimer l'Employee de la session
    @PostMapping("/session/employee/remove")
    public servlet.ModelView removeEmployee(@Session jakarta.servlet.http.HttpSession session) {
        com.itu.gest_emp.model.Employee emp = (com.itu.gest_emp.model.Employee) session.getAttribute("employee");
        session.removeAttribute("employee");
        
        servlet.ModelView mv = new servlet.ModelView();
        mv.setView("test-session-entity.jsp");
        if (emp != null) {
            mv.addObject("message", "Employee '" + emp.getName() + "' removed from session!");
        } else {
            mv.addObject("message", "No employee was in session.");
        }
        return mv;
    }
}
