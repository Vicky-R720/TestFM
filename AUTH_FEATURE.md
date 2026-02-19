# Fonctionnalité d'Authentification et Autorisation

## Vue d'ensemble

Cette fonctionnalité ajoute la sécurité au niveau des méthodes dans votre framework web avec deux annotations:
- `@Authorized` - Requiert que l'utilisateur soit authentifié
- `@Role` - Requiert que l'utilisateur ait un rôle spécifique

## Configuration

### 1. Configuration dans web.xml

Ajoutez ces paramètres d'initialisation dans votre servlet `FrontServlet`:

```xml
<servlet>
    <servlet-name>Front</servlet-name>
    <servlet-class>servlet.FrontServlet</servlet-class>
    
    <!-- Configuration pour l'authentification et les rôles -->
    <init-param>
        <param-name>authSessionKey</param-name>
        <param-value>auth</param-value>
    </init-param>
    <init-param>
        <param-name>roleSessionKey</param-name>
        <param-value>role</param-value>
    </init-param>
</servlet>
```

- `authSessionKey` : Nom de la variable de session qui contient l'état d'authentification (par défaut: "auth")
- `roleSessionKey` : Nom de la variable de session qui contient les rôles de l'utilisateur (par défaut: "role")

## Annotations

### @Authorized

Marque une méthode qui nécessite l'authentification. L'utilisateur doit être connecté pour y accéder.

```java
@Authorized
@GetMapping("/protected")
public ModelView protectedPage() {
    // Cette page nécessite que l'utilisateur soit authentifié
    return mv;
}
```

### @Role

Marque une méthode qui nécessite un ou plusieurs rôles spécifiques. L'utilisateur doit être authentifié ET avoir au moins un des rôles spécifiés.

```java
@Role({"admin"})
@GetMapping("/admin")
public ModelView adminPage() {
    // Seulement les admins peuvent accéder
    return mv;
}

@Role({"admin", "prof"})
@GetMapping("/staff")
public ModelView staffPage() {
    // Les admins OU les profs peuvent accéder
    return mv;
}
```

## Interface UserSession

L'interface `UserSession` permet de gérer les rôles de l'utilisateur:

```java
public interface UserSession {
    String[] getRoles();
    boolean hasRole(String role);
}
```

### Implémentation exemple

```java
public class SimpleUserSession implements UserSession {
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
            if (r.equals(role)) return true;
        }
        return false;
    }
}
```

## Utilisation dans un contrôleur

### Login
```java
@PostMapping("/auth/login")
public ModelView login(
        @RequestParam("username") String username,
        @RequestParam("password") String password,
        @Session HttpSession session) {
    
    // Vérifier les identifiants (base de données, etc.)
    if (validate(username, password)) {
        // Créer l'objet UserSession avec les rôles de l'utilisateur
        UserSession userSession = new SimpleUserSession(username, new String[]{"admin", "user"});
        
        // Stocker dans la session
        session.setAttribute("auth", true);
        session.setAttribute("role", userSession);
        session.setAttribute("username", username);
        
        // Rediriger vers la page d'accueil
        return redirectToHome();
    }
    
    return showLoginError();
}
```

### Logout
```java
@GetMapping("/auth/logout")
public ModelView logout(@Session HttpSession session) {
    if (session != null) {
        session.invalidate();
    }
    return redirectToLogin();
}
```

### Méthodes protégées
```java
// Accessible uniquement aux utilisateurs authentifiés
@Authorized
@GetMapping("/profile")
public ModelView profile(@Session HttpSession session) {
    String username = (String) session.getAttribute("username");
    // ...
}

// Accessible uniquement aux admins
@Role({"admin"})
@GetMapping("/admin/users")
public ModelView manageUsers() {
    // ...
}

// Accessible aux admins ET aux profs
@Role({"admin", "prof"})
@GetMapping("/dashboard")
public ModelView dashboard() {
    // ...
}
```

## Types de données supportés pour les rôles

La variable de session `role` peut être:

1. **UserSession** (recommandé)
   ```java
   session.setAttribute("role", new SimpleUserSession("user", new String[]{"admin", "user"}));
   ```

2. **String** (un seul rôle)
   ```java
   session.setAttribute("role", "admin");
   ```

3. **String[]** (tableau de rôles)
   ```java
   session.setAttribute("role", new String[]{"admin", "user"});
   ```

## Codes de réponse

- **403 Forbidden** - Si l'utilisateur n'est pas authentifié ou n'a pas le rôle requis
  - Message: "❌ 403 Forbidden - Authentication required"
  - Message: "❌ 403 Forbidden - Required role: admin or prof"

## Test de la fonctionnalité

### Comptes de test disponibles

Dans `AuthController`, trois comptes de test sont définis:

| Username | Password | Rôles |
|----------|----------|-------|
| admin    | admin    | admin, user |
| prof     | prof     | prof, user |
| user     | user     | user |

### Pages de test

1. **Page de login**: `/auth/login`
2. **Page d'accueil**: `/auth/home` (publique)
3. **Page protégée**: `/auth/protected` (nécessite authentification)
4. **Page admin**: `/auth/admin` (nécessite rôle "admin")
5. **Page prof**: `/auth/prof` (nécessite rôle "prof")
6. **Page staff**: `/auth/staff` (nécessite rôle "admin" OU "prof")

### Test manuel

1. Démarrez l'application
2. Accédez à `http://localhost:8080/auth/home`
3. Cliquez sur "Se connecter"
4. Testez avec différents comptes:
   - Connectez-vous en tant que "user" -> ne peut pas accéder aux pages admin/prof
   - Connectez-vous en tant que "prof" -> peut accéder à la page prof et staff
   - Connectez-vous en tant que "admin" -> peut accéder à toutes les pages

## Fichiers créés

### Framework (FrameWorkN)
- `src/main/java/servlet/annotations/Authorized.java` - Annotation @Authorized
- `src/main/java/servlet/annotations/Role.java` - Annotation @Role
- `src/main/java/servlet/UserSession.java` - Interface UserSession
- `src/main/java/servlet/FrontServlet.java` - Modifié pour ajouter la vérification de sécurité

### Application de test (TestFM)
- `src/main/java/com/itu/gest_emp/controller/AuthController.java` - Contrôleur de test
- `src/main/webapp/pages/test-login.jsp` - Page de login
- `src/main/webapp/pages/test-auth.jsp` - Page de test principale
- `src/main/webapp/WEB-INF/web.xml` - Modifié avec configuration de sécurité

## Bonnes pratiques

1. **Toujours valider les identifiants en base de données** dans un environnement de production
2. **Hasher les mots de passe** (ex: bcrypt)
3. **Utiliser HTTPS** en production pour protéger les sessions
4. **Définir un timeout de session approprié**
5. **Logger les tentatives de connexion** pour la sécurité
6. **Implémenter un système de "remember me"** si nécessaire
7. **Gérer les erreurs de manière sécurisée** (ne pas révéler si un username existe)

## Évolutions possibles

- [ ] Support pour les permissions granulaires
- [ ] Annotations au niveau de la classe
- [ ] Integration avec JWT pour les APIs
- [ ] Support pour plusieurs providers d'authentification
- [ ] Cache des permissions pour la performance
- [ ] Audit trail des accès
