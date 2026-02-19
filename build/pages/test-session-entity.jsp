<%@ page import="com.itu.gest_emp.model.Employee" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Test Session Employee</title>
    <style>
        body { font-family: Arial; padding: 20px; background: #f5f5f5; }
        .box { background: white; padding: 20px; margin: 10px 0; border-radius: 5px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); }
        h1 { color: #333; }
        h2 { color: #666; font-size: 18px; margin-top: 0; }
        input { padding: 8px; width: 200px; margin-right: 10px; }
        button { padding: 8px 15px; cursor: pointer; background: #4CAF50; color: white; border: none; border-radius: 3px; }
        button:hover { background: #45a049; }
        .info { background: #e3f2fd; padding: 10px; margin: 10px 0; border-left: 4px solid #2196F3; }
        .success { background: #e8f5e9; border-left-color: #4CAF50; }
        .warning { background: #fff3e0; border-left-color: #ff9800; }
        hr { margin: 20px 0; border: none; border-top: 1px solid #ddd; }
    </style>
</head>
<body>
    <h1>Test Session avec Employee</h1>
    
    <!-- Message -->
    <% String message = (String) request.getAttribute("message");
       if (message != null) { %>
        <div class="info <%= message.contains("saved") || message.contains("found") ? "success" : "warning" %>">
            <%= message %>
        </div>
    <% } %>
    
    <!-- Employee actuel -->
    <div class="box">
        <h2>Employee en session :</h2>
        <% Employee emp = (Employee) session.getAttribute("employee");
           if (emp != null) { %>
            <p><strong>Nom:</strong> <%= emp.getName() %></p>
            <p><strong>Session ID:</strong> <%= session.getId().substring(0, 8) %>...</p>
        <% } else { %>
            <p><em>Aucun employee en session</em></p>
        <% } %>
    </div>
    
    <hr>
    
    <!-- Formulaire -->
    <div class="box">
        <h2>1. Sauvegarder un Employee</h2>
        <form action="<%= request.getContextPath() %>/session/employee/save" method="POST">
            <input type="text" name="name" placeholder="Nom employee" required>
            <button type="submit">Sauvegarder</button>
        </form>
    </div>
    
    <!-- Actions -->
    <div class="box">
        <h2>2. Actions</h2>
        <form action="<%= request.getContextPath() %>/session/employee/show" method="GET" style="display: inline;">
            <button type="submit">Afficher</button>
        </form>
        <form action="<%= request.getContextPath() %>/session/employee/remove" method="POST" style="display: inline; margin-left: 10px;">
            <button type="submit" style="background: #f44336;">Supprimer</button>
        </form>
    </div>
</body>
</html>
