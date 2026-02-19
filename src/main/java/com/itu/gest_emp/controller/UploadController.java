package com.itu.gest_emp.controller;

import servlet.annotations.*;
import servlet.Upload;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UploadController {
    
    // Test upload de fichiers avec Map<String, List<Upload>>
    @PostMapping("/upload-files")
    public String uploadFiles(Map<String, List<Upload>> files) {
        StringBuilder result = new StringBuilder();
        result.append("Fichiers reçus :\n\n");
        
        for (Map.Entry<String, List<Upload>> entry : files.entrySet()) {
            String fieldName = entry.getKey();
            List<Upload> uploads = entry.getValue();
            
            result.append("Champ : ").append(fieldName).append("\n");
            
            for (Upload upload : uploads) {
                result.append("  - Nom : ").append(upload.getFilename()).append("\n");
                result.append("    Type : ").append(upload.getContentType()).append("\n");
                result.append("    Taille : ").append(upload.getSize()).append(" bytes\n");
                result.append("    Sauvegardé à : ").append(upload.getSavedPath()).append("\n");
            }
            result.append("\n");
        }
        
        return result.toString();
    }
    
    // Test upload de fichiers avec retour texte simple
    @PostMapping("/api/upload-files")
    public String uploadFilesApi(Map<String, List<Upload>> files) {
        StringBuilder result = new StringBuilder();
        result.append("=== API Upload Result ===\n\n");
        
        int totalFiles = 0;
        long totalSize = 0;
        
        for (Map.Entry<String, List<Upload>> entry : files.entrySet()) {
            String fieldName = entry.getKey();
            List<Upload> uploads = entry.getValue();
            
            result.append("Field: ").append(fieldName).append("\n");
            
            for (Upload upload : uploads) {
                result.append("  - File: ").append(upload.getFilename()).append("\n");
                result.append("    Type: ").append(upload.getContentType()).append("\n");
                result.append("    Size: ").append(upload.getSize()).append(" bytes\n");
                result.append("    Path: ").append(upload.getSavedPath()).append("\n");
                
                totalFiles++;
                totalSize += upload.getSize();
            }
            result.append("\n");
        }
        
        result.append("Total files: ").append(totalFiles).append("\n");
        result.append("Total size: ").append(totalSize).append(" bytes\n");
        
        return result.toString();
    }
}
