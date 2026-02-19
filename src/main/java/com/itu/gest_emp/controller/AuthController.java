package com.itu.gest_emp.controller;

import jakarta.servlet.http.HttpSession;
import servlet.ModelView;
import servlet.UserSession;
import servlet.annotations.*;

import java.util.HashMap;
import java.util.Map;

public class AuthController {

    /**
     * Page d'accueil publique - accessible sans authentification
     */
    @GetMapping("/auth/home")
    public ModelView home() {
        ModelView mv = new ModelView();
        mv.setView("test-auth.jsp");
        mv.addObject("message", "Page publique - accessible à tous");
        return mv;
    }

    /**
     * Formulaire de login
     */
    @GetMapping("/auth/login")
    public ModelView loginForm() {
        ModelView mv = new ModelView();
        mv.setView("test-login.jsp");
        return mv;
    }

    /**
     * Traitement du login
     */
    @PostMapping("/auth/login")
    public ModelView login(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @Session HttpSession session) {
        
        ModelView mv = new ModelView();
        mv.setView("test-auth.jsp");
        
        // Simulation d'authentification simple
        // Dans un vrai système, on vérifierait en base de données
        if ("admin".equals(username) && "admin".equals(password)) {
            // Créer un UserSession simple
            SimpleUserSession userSession = new SimpleUserSession("admin", new String[]{"admin", "user"});
            
            session.setAttribute("auth", true);
            session.setAttribute("role", userSession);
            session.setAttribute("username", username);
            
            mv.addObject("message", "✅ Connecté en tant qu'admin");
            mv.addObject("username", username);
            mv.addObject("roles", "admin, user");
        } 
        else if ("prof".equals(username) && "prof".equals(password)) {
            SimpleUserSession userSession = new SimpleUserSession("prof", new String[]{"prof", "user"});
            
            session.setAttribute("auth", true);
            session.setAttribute("role", userSession);
            session.setAttribute("username", username);
            
            mv.addObject("message", "✅ Connecté en tant que prof");
            mv.addObject("username", username);
            mv.addObject("roles", "prof, user");
        }
        else if ("user".equals(username) && "user".equals(password)) {
            SimpleUserSession userSession = new SimpleUserSession("user", new String[]{"user"});
            
            session.setAttribute("auth", true);
            session.setAttribute("role", userSession);
            session.setAttribute("username", username);
            
            mv.addObject("message", "✅ Connecté en tant qu'utilisateur simple");
            mv.addObject("username", username);
            mv.addObject("roles", "user");
        }
        else {
            mv.addObject("message", "❌ Login ou mot de passe incorrect");
        }
        
        return mv;
    }

    /**
     * Déconnexion
     */
    @GetMapping("/auth/logout")
    public ModelView logout(@Session HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
        
        ModelView mv = new ModelView();
        mv.setView("test-auth.jsp");
        mv.addObject("message", "✅ Déconnecté avec succès");
        return mv;
    }

    /**
     * Page protégée - nécessite uniquement l'authentification
     */
    @Authorized
    @GetMapping("/auth/protected")
    public ModelView protectedPage(@Session HttpSession session) {
        ModelView mv = new ModelView();
        mv.setView("test-auth.jsp");
        String username = (String) session.getAttribute("username");
        mv.addObject("message", "✅ Page protégée - Bonjour " + username + " !");
        mv.addObject("username", username);
        return mv;
    }

    /**
     * Page admin - nécessite le rôle "admin"
     */
    @Role({"admin"})
    @GetMapping("/auth/admin")
    public ModelView adminPage(@Session HttpSession session) {
        ModelView mv = new ModelView();
        mv.setView("test-auth.jsp");
        String username = (String) session.getAttribute("username");
        mv.addObject("message", "✅ Page Admin - Bienvenue administrateur " + username);
        mv.addObject("username", username);
        return mv;
    }

    /**
     * Page professeur - nécessite le rôle "prof"
     */
    @Role({"prof"})
    @GetMapping("/auth/prof")
    public ModelView profPage(@Session HttpSession session) {
        ModelView mv = new ModelView();
        mv.setView("test-auth.jsp");
        String username = (String) session.getAttribute("username");
        mv.addObject("message", "✅ Page Professeur - Bienvenue professeur " + username);
        mv.addObject("username", username);
        return mv;
    }

    /**
     * Page accessible aux admins OU aux profs
     */
    @Role({"admin", "prof"})
    @GetMapping("/auth/staff")
    public ModelView staffPage(@Session HttpSession session) {
        ModelView mv = new ModelView();
        mv.setView("test-auth.jsp");
        String username = (String) session.getAttribute("username");
        mv.addObject("message", "✅ Page Staff - Accessible aux admins et profs");
        mv.addObject("username", username);
        return mv;
    }

    /**
     * Implémentation simple de UserSession
     */
    private static class SimpleUserSession implements UserSession {
        private String username;
        private String[] roles;

        public SimpleUserSession(String username, String[] roles) {
            this.username = username;
            this.roles = roles;
        }

        @Override
        public String[] getRoles() {
            return roles;
        }

        @Override
        public boolean hasRole(String role) {
            if (roles == null) return false;
            for (String r : roles) {
                if (r.equals(role)) {
                    return true;
                }
            }
            return false;
        }

        public String getUsername() {
            return username;
        }
    }
}
