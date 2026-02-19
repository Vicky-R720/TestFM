# Fonctionnalité Session - Guide d'utilisation

## 📋 Vue d'ensemble

La fonctionnalité Session permet d'injecter automatiquement les sessions HTTP dans les méthodes des contrôleurs via l'annotation `@Session`.

## 🎯 Fonctionnalités implémentées

### 1. Annotation `@Session`
- **Fichier**: `FrameWorkN/src/main/java/servlet/annotations/Session.java`
- Peut être utilisée sur les paramètres de méthode
- Supporte deux types d'injection:
  - `HttpSession` - Session Jakarta complète
  - `Map<String, Object>` - Map contenant tous les attributs de session (lecture seule)

### 2. Injection automatique dans FrontServlet
- **Fichier**: `FrameWorkN/src/main/java/servlet/FrontServlet.java`
- Détection automatique de l'annotation `@Session`
- Injection de la session avant l'appel de la méthode du contrôleur

### 3. Contrôleur de test complet
- **Fichier**: `TestFM/src/main/java/com/itu/gest_emp/controller/SessionController.java`
- Tests CRUD complets pour la session
- Exemples pratiques (panier, login, compteur)

### 4. Interface de test HTML
- **Fichier**: `TestFM/src/main/webapp/pages/test-session.html`
- Interface utilisateur complète pour tester toutes les fonctionnalités
- Design moderne et responsive

## 🚀 Utilisation

### Exemple 1: Injection de HttpSession (lecture/écriture)
```java
@PostMapping("/session/set")
public String setSession(@RequestParam("key") String key, 
                        @RequestParam("value") String value,
                        @Session jakarta.servlet.http.HttpSession session) {
    session.setAttribute(key, value);
    return "Session updated";
}
```

### Exemple 2: Injection de Map (lecture seule)
```java
@GetMapping("/session/all")
public String getAllSession(@Session Map<String, Object> session) {
    StringBuilder result = new StringBuilder();
    for (Map.Entry<String, Object> entry : session.entrySet()) {
        result.append(entry.getKey()).append(" = ")
              .append(entry.getValue()).append("\n");
    }
    return result.toString();
}
```

## 📝 Opérations disponibles

### CRUD de base

| Endpoint | Méthode | Paramètres | Description |
|----------|---------|------------|-------------|
| `/session/set` | POST | key, value | Ajouter/modifier une valeur |
| `/session/get` | GET | key | Lire une valeur |
| `/session/all` | GET | - | Lire toutes les valeurs |
| `/session/remove` | POST | key | Supprimer une valeur |
| `/session/clear` | POST | - | Vider la session |

### Exemples pratiques

| Endpoint | Description |
|----------|-------------|
| `/session/visit-count` | Compteur de visites |
| `/session/cart/add` | Ajouter au panier |
| `/session/cart/view` | Voir le panier |
| `/session/cart/clear` | Vider le panier |
| `/session/login` | Connexion utilisateur |
| `/session/whoami` | Vérifier l'utilisateur connecté |
| `/session/logout` | Déconnexion |
| `/api/session/info` | Informations session (JSON) |

## 🧪 Comment tester

### Méthode 1: Via l'interface HTML

1. **Compiler le framework**:
   ```powershell
   cd FrameWorkN
   .\launch.bat
   ```

2. **Compiler et lancer l'application de test**:
   ```powershell
   cd ..\TestFM
   .\launch.bat
   ```

3. **Ouvrir dans le navigateur**:
   ```
   http://localhost:8080/pages/test-session.html
   ```

4. **Tester les fonctionnalités**:
   - Ajouter des valeurs en session
   - Les récupérer
   - Tester le panier d'achat
   - Tester le système de login
   - Voir les informations de session

### Méthode 2: Via PowerShell/Curl

```powershell
# Ajouter une valeur
Invoke-WebRequest -Uri "http://localhost:8080/session/set" `
  -Method POST -Body "key=username&value=John" `
  -SessionVariable session

# Récupérer la valeur
Invoke-WebRequest -Uri "http://localhost:8080/session/get?key=username" `
  -WebSession $session

# Voir toute la session
Invoke-WebRequest -Uri "http://localhost:8080/session/all" `
  -WebSession $session

# Compteur de visites
Invoke-WebRequest -Uri "http://localhost:8080/session/visit-count" `
  -WebSession $session

# Infos JSON
Invoke-WebRequest -Uri "http://localhost:8080/api/session/info" `
  -WebSession $session
```

### Méthode 3: Script PowerShell automatisé

Créez un fichier `test-session.ps1`:
```powershell
# Test complet de la session
$baseUrl = "http://localhost:8080"

Write-Host "=== Test Session ===" -ForegroundColor Cyan

# Test 1: Set
Write-Host "`n1. SET - Ajouter une valeur" -ForegroundColor Yellow
$response = Invoke-WebRequest -Uri "$baseUrl/session/set" `
  -Method POST -Body "key=username&value=Alice" -SessionVariable session
Write-Host $response.Content

# Test 2: Get
Write-Host "`n2. GET - Récupérer la valeur" -ForegroundColor Yellow
$response = Invoke-WebRequest -Uri "$baseUrl/session/get?key=username" `
  -WebSession $session
Write-Host $response.Content

# Test 3: Compteur
Write-Host "`n3. Compteur de visites" -ForegroundColor Yellow
1..3 | ForEach-Object {
    $response = Invoke-WebRequest -Uri "$baseUrl/session/visit-count" `
      -WebSession $session
    Write-Host $response.Content
}

# Test 4: Panier
Write-Host "`n4. Panier d'achat" -ForegroundColor Yellow
"Laptop", "Mouse", "Keyboard" | ForEach-Object {
    $response = Invoke-WebRequest -Uri "$baseUrl/session/cart/add" `
      -Method POST -Body "item=$_" -WebSession $session
    Write-Host $response.Content
}

$response = Invoke-WebRequest -Uri "$baseUrl/session/cart/view" `
  -WebSession $session
Write-Host $response.Content

# Test 5: Session complète
Write-Host "`n5. Tout le contenu de la session" -ForegroundColor Yellow
$response = Invoke-WebRequest -Uri "$baseUrl/session/all" `
  -WebSession $session
Write-Host $response.Content

# Test 6: Info JSON
Write-Host "`n6. Informations session (JSON)" -ForegroundColor Yellow
$response = Invoke-WebRequest -Uri "$baseUrl/api/session/info" `
  -WebSession $session
$json = $response.Content | ConvertFrom-Json | ConvertTo-Json -Depth 10
Write-Host $json
```

Exécuter:
```powershell
.\test-session.ps1
```

## ⚠️ Points importants

1. **Map vs HttpSession**:
   - `Map<String, Object>` est une **copie** des attributs (lecture seule)
   - `HttpSession` permet de **modifier** la session

2. **Gestion des cookies**:
   - La session utilise un cookie `JSESSIONID`
   - Important d'utiliser `-SessionVariable` avec PowerShell
   - Ou `credentials: 'same-origin'` avec fetch en JavaScript

3. **Invalidation**:
   - `session.invalidate()` détruit complètement la session
   - Une nouvelle session sera créée à la prochaine requête

## 🎨 Personnalisation

Pour ajouter vos propres fonctionnalités de session:

1. Créer une méthode dans un contrôleur
2. Ajouter `@Session` sur un paramètre
3. Utiliser `HttpSession` pour modifier ou `Map` pour lire

Exemple:
```java
@GetMapping("/mon-endpoint")
public String maMethode(@Session HttpSession session,
                       @RequestParam("data") String data) {
    session.setAttribute("myData", data);
    return "Données sauvegardées";
}
```

## 📊 Architecture

```
Requête HTTP
    ↓
FrontServlet.service()
    ↓
Détection de @Session sur paramètres
    ↓
Injection de HttpSession ou Map<String, Object>
    ↓
Appel de la méthode du contrôleur
    ↓
Retour de la réponse
```

## ✅ Tests recommandés

1. ✅ Ajouter une valeur simple
2. ✅ Récupérer une valeur
3. ✅ Supprimer une valeur
4. ✅ Vider la session
5. ✅ Tester avec des objets complexes (List, Map)
6. ✅ Tester la persistance entre requêtes
7. ✅ Tester l'invalidation
8. ✅ Tester l'API JSON
