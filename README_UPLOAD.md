# 🎉 Fonctionnalité Upload Implémentée !

## ✅ Ce qui a été ajouté

### 1. Classe `Upload` (Framework)
Représente un fichier uploadé avec :
- `filename` : Nom du fichier original
- `contentType` : Type MIME (image/png, application/pdf, etc.)
- `size` : Taille en bytes
- `content` : Contenu binaire du fichier
- `savedPath` : Chemin où le fichier a été sauvegardé

**Fichier** : `FrameWorkN/src/main/java/servlet/Upload.java`

### 2. Modification du `FrontServlet`
- Ajout de `@MultipartConfig` pour supporter les requêtes multipart/form-data
- Détection automatique des paramètres de type `Map<String, List<Upload>>`
- Méthode `processFileUploads()` qui :
  - Crée le dossier `upload/` automatiquement
  - Extrait les fichiers uploadés
  - Groupe les fichiers par nom de champ (many-to-one)
  - Sauvegarde les fichiers avec un timestamp unique
  - Retourne un `Map<String, List<Upload>>`

**Fichier** : `FrameWorkN/src/main/java/servlet/FrontServlet.java`

### 3. Classes JSON (Bonus)
- `@Json` : Annotation pour méthodes retournant du JSON
- `JsonResponse` : Classe pour générer des réponses JSON

**Fichiers** : 
- `FrameWorkN/src/main/java/servlet/annotations/Json.java`
- `FrameWorkN/src/main/java/servlet/JsonResponse.java`

### 4. Contrôleur de test
`UploadController` avec deux méthodes :
- `/upload-files` : Retourne du texte simple
- `/api/upload-files` : Retourne du texte formaté

**Fichier** : `TestFM/src/main/java/com/itu/gest_emp/controller/UploadController.java`

### 5. Page de test HTML
Interface pour tester l'upload avec :
- Formulaire HTML classique
- Upload via JavaScript/Fetch API

**Fichier** : `TestFM/src/main/webapp/pages/test-upload.html`

## 🚀 Comment tester

### 1. Démarrer Tomcat
Si Tomcat n'est pas démarré :
```powershell
cd "C:\apache-tomcat-10.1.34\bin"
.\startup.bat
```

### 2. Ouvrir la page de test
```
http://localhost:8080/test-up/pages/test-upload.html
```

### 3. Tester l'upload
1. Cliquer sur "Choisir un fichier" pour les documents
2. Cliquer sur "Choisir un fichier" pour les images
3. Sélectionner plusieurs fichiers (ctrl+clic)
4. Cliquer sur "Envoyer" ou "Envoyer (API)"

### 4. Vérifier les résultats
- La page affichera les détails des fichiers uploadés
- Les fichiers seront sauvegardés dans le dossier `upload/`

## 📋 Utilisation dans vos contrôleurs

### Exemple simple
```java
@Controller
public class MyController {
    
    @PostMapping("/upload")
    public String handleUpload(Map<String, List<Upload>> files) {
        StringBuilder result = new StringBuilder();
        
        // Accéder aux fichiers par nom de champ
        List<Upload> documents = files.get("document");
        List<Upload> images = files.get("image");
        
        // Traiter chaque fichier
        if (documents != null) {
            for (Upload upload : documents) {
                result.append("Document: ").append(upload.getFilename())
                      .append(" (").append(upload.getSize()).append(" bytes)\n");
                result.append("Saved at: ").append(upload.getSavedPath()).append("\n");
            }
        }
        
        return result.toString();
    }
}
```

### Exemple avec traitement d'image
```java
@PostMapping("/save-profile")
public String saveProfile(@RequestParam("name") String name, 
                         Map<String, List<Upload>> files) {
    // Récupérer la photo de profil
    List<Upload> photos = files.get("photo");
    
    if (photos != null && !photos.isEmpty()) {
        Upload photo = photos.get(0);
        
        // Utiliser le contenu du fichier
        byte[] imageData = photo.getContent();
        
        // Ou utiliser le chemin sauvegardé
        String imagePath = photo.getSavedPath();
        
        // Sauvegarder dans la base de données, etc.
    }
    
    return "Profil sauvegardé !";
}
```

## 📁 Structure du dossier upload

Les fichiers sont sauvegardés dans :
```
webapp/upload/
  ├── 1708345678901_photo.jpg
  ├── 1708345678902_document.pdf
  └── 1708345678903_report.xlsx
```

Le nom du fichier est préfixé avec un timestamp (millisecondes) pour éviter les conflits.

## 🔑 Points importants

### 1. Many-to-One
Plusieurs fichiers peuvent avoir le même nom de champ :
```html
<input type="file" name="document" multiple>
```
Résultat : `files.get("document")` retourne une `List<Upload>` avec tous les fichiers.

### 2. Type de paramètre
Le paramètre DOIT être exactement :
```java
Map<String, List<Upload>> files
```

### 3. Formulaire HTML
Le formulaire DOIT avoir :
```html
<form method="post" enctype="multipart/form-data">
```

### 4. Dossier upload
Le dossier est créé automatiquement dans le contexte web de l'application.

## 🎯 Endpoints disponibles

| URL | Méthode | Description |
|-----|---------|-------------|
| `/upload-files` | POST | Upload avec retour texte simple |
| `/api/upload-files` | POST | Upload avec retour formaté |

## 📝 Architecture technique

### Détection du type de paramètre
Le `FrontServlet` utilise la réflexion Java pour détecter :
```java
Map<String, List<Upload>> files
```

Il vérifie :
1. Que le paramètre est un `Map`
2. Que le deuxième argument générique est `List<Upload>`
3. Si oui, il appelle `processFileUploads()`

### Extraction des fichiers
```java
Collection<Part> parts = req.getParts();  // Jakarta Servlet API

for (Part part : parts) {
    String filename = getSubmittedFileName(part);
    byte[] content = part.getInputStream().readAllBytes();
    
    // Créer l'objet Upload
    Upload upload = new Upload(filename, part.getContentType(), 
                               part.getSize(), content);
    
    // Sauvegarder le fichier
    // Grouper par nom de champ
}
```

## 🎉 Test réussi !

Si tout fonctionne, vous devriez voir :
1. ✅ La page de test s'affiche correctement
2. ✅ Les fichiers sont uploadés sans erreur
3. ✅ Les détails des fichiers sont affichés
4. ✅ Les fichiers sont sauvegardés dans `upload/`

## 🐛 Dépannage

### Erreur 404
Vérifier que Tomcat est démarré et que l'URL est correcte.

### Erreur "Method not allowed"
Vérifier que la méthode du contrôleur est annotée avec `@PostMapping`.

### Fichiers non reçus
Vérifier que le formulaire a `enctype="multipart/form-data"`.

### Dossier upload introuvable
Le dossier est créé automatiquement. Vérifier les permissions d'écriture.

## 📚 Documentation complète

Voir `UPLOAD_FEATURE.md` pour la documentation détaillée.

---

**Développé selon votre note** : 
- Map<String, List<Upload>> ✅
- Many-to-one (plot) ✅
- Dossier upload/ ✅
