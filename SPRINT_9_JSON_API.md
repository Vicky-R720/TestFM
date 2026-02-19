# Sprint 9 - API JSON REST ✅

## Fonctionnalité implémentée

Possibilité d'exposer une API REST en utilisant l'annotation `@Json` pour indiquer qu'une méthode retourne du JSON au lieu d'une ModelView.

## 📁 Fichiers créés/modifiés

### Dans le Framework (FrameWorkN)

1. **`servlet/annotations/Json.java`** - Annotation pour marquer les méthodes qui retournent du JSON
2. **`servlet/JsonResponse.java`** - Classe pour structurer les réponses JSON
3. **`servlet/FrontServlet.java`** - Modifié pour détecter `@Json` et retourner du JSON

### Dans le projet de test (TestFM)

1. **`controller/TestController.java`** - Ajout de 7 méthodes de test API
2. **`pages/test-json-api.html`** - Page HTML pour tester les endpoints

## 📋 Format de réponse JSON

Toutes les réponses suivent ce format standard :

```json
{
  "status": "success",  // ou "error"
  "code": 200,         // Code HTTP (200, 404, 500, etc.)
  "data": {            // Données ou [] pour liste vide
    ...
  }
}
```

## 🎯 Endpoints de test disponibles

| Endpoint | Description | Type de retour |
|----------|-------------|----------------|
| `GET /api/employee` | Retourne un employé simple | Objet Employee |
| `GET /api/employees` | Liste de 3 employés | List<Employee> |
| `GET /api/employee-detail` | Employé avec départements | Employee + Departments |
| `GET /api/stats` | Statistiques | Map<String, Object> |
| `GET /api/custom-success` | JsonResponse personnalisé | JsonResponse (success) |
| `GET /api/custom-error` | JsonResponse avec erreur | JsonResponse (error) |
| `GET /api/empty-list` | Liste vide | List<Employee> |

## 🚀 Comment tester

### Option 1 : Via navigateur

1. Démarrer Tomcat (si pas déjà fait)
2. Ouvrir : `http://localhost:8080/spri/pages/test-json-api.html`
3. Cliquer sur "Tester" pour chaque endpoint ou "Tester tous les endpoints"

http://localhost:8080/9/pages/test-json-api.html

### Option 2 : Via curl (PowerShell)

```powershell
# Test 1 : Objet simple
curl http://localhost:8080/spri/api/employee

# Test 2 : Liste d'employés
curl http://localhost:8080/spri/api/employees

# Test 3 : Employé avec départements
curl http://localhost:8080/spri/api/employee-detail

# Test 4 : Map de statistiques
curl http://localhost:8080/spri/api/stats

# Test 5 : Success personnalisé
curl http://localhost:8080/spri/api/custom-success

# Test 6 : Erreur personnalisée
curl http://localhost:8080/spri/api/custom-error

# Test 7 : Liste vide
curl http://localhost:8080/spri/api/empty-list
```

### Option 3 : Via Postman ou Insomnia

Importez les endpoints ci-dessus et testez-les directement.

## 💡 Utilisation dans vos contrôleurs

### Exemple 1 : Retour simple

```java
@Json
@GetMapping("/api/users")
public List<User> getUsers() {
    return userService.findAll();
}
```

### Exemple 2 : Retour avec JsonResponse personnalisé

```java
@Json
@GetMapping("/api/user")
public JsonResponse getUser(@RequestParam("id") int id) {
    User user = userService.findById(id);
    if (user == null) {
        return JsonResponse.error(404, "Utilisateur non trouvé");
    }
    return JsonResponse.success(user);
}
```

### Exemple 3 : Retour d'une Map

```java
@Json
@GetMapping("/api/dashboard")
public Map<String, Object> getDashboard() {
    Map<String, Object> data = new HashMap<>();
    data.put("totalUsers", 150);
    data.put("activeUsers", 120);
    return data;
}
```

## 🔧 Détails techniques

### Comment ça marche ?

1. **Dans le contrôleur** : Annotez la méthode avec `@Json`
2. **Dans FrontServlet** : Détection de l'annotation `@Json`
3. **Conversion** : Le résultat est enveloppé dans un `JsonResponse` (si ce n'est pas déjà fait)
4. **Réponse** : Le JSON est généré et envoyé avec `Content-Type: application/json`

### Conversion JSON

La classe `JsonResponse` utilise la réflexion Java pour convertir :
- Objets simples (Employee, Department, etc.)
- Listes (List<Employee>)
- Maps (Map<String, Object>)
- Types primitifs (String, int, boolean, etc.)
- Objets imbriqués (Employee avec departments[])

## ✅ Avantages

- ✨ Pas besoin de bibliothèque externe (Gson, Jackson)
- 🎯 Format standardisé pour toutes les réponses API
- 🚀 Simple à utiliser (juste `@Json`)
- 📦 Support des objets complexes et listes
- 🛠️ Gestion des erreurs intégrée

## 📝 Notes

- Le Content-Type est automatiquement défini à `application/json`
- Les méthodes sans `@Json` continuent de fonctionner normalement (ModelView, String, etc.)
- La conversion JSON gère les caractères spéciaux (échappement automatique)

## 🎉 C'est prêt !

Votre framework supporte maintenant les API REST ! 🚀
