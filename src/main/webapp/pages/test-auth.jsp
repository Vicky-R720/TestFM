<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Test Authentification & Autorisation</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            padding: 20px;
        }
        
        .container {
            max-width: 900px;
            margin: 0 auto;
        }
        
        .header {
            background: white;
            padding: 30px;
            border-radius: 10px;
            margin-bottom: 20px;
            box-shadow: 0 5px 20px rgba(0, 0, 0, 0.1);
        }
        
        h1 {
            color: #333;
            margin-bottom: 10px;
        }
        
        .status {
            padding: 15px;
            border-radius: 5px;
            margin-top: 15px;
            font-size: 16px;
        }
        
        .status.success {
            background: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }
        
        .status.error {
            background: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
        
        .status.info {
            background: #d1ecf1;
            color: #0c5460;
            border: 1px solid #bee5eb;
        }
        
        .user-info {
            background: white;
            padding: 20px;
            border-radius: 10px;
            margin-bottom: 20px;
            box-shadow: 0 5px 20px rgba(0, 0, 0, 0.1);
        }
        
        .user-info h2 {
            color: #333;
            margin-bottom: 10px;
            font-size: 18px;
        }
        
        .user-info p {
            color: #666;
            margin: 5px 0;
        }
        
        .actions {
            background: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 5px 20px rgba(0, 0, 0, 0.1);
        }
        
        .actions h2 {
            color: #333;
            margin-bottom: 20px;
        }
        
        .button-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 15px;
        }
        
        .btn {
            display: block;
            padding: 15px 20px;
            border-radius: 5px;
            text-decoration: none;
            text-align: center;
            font-weight: 600;
            transition: transform 0.2s, box-shadow 0.2s;
            cursor: pointer;
        }
        
        .btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
        }
        
        .btn-primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }
        
        .btn-success {
            background: linear-gradient(135deg, #56ab2f 0%, #a8e063 100%);
            color: white;
        }
        
        .btn-warning {
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
            color: white;
        }
        
        .btn-danger {
            background: linear-gradient(135deg, #ff6b6b 0%, #ee5a6f 100%);
            color: white;
        }
        
        .btn-info {
            background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
            color: white;
        }
        
        .section {
            margin-top: 20px;
            padding-top: 20px;
            border-top: 2px solid #eee;
        }
        
        .section h3 {
            color: #555;
            margin-bottom: 15px;
            font-size: 16px;
        }
        
        .note {
            margin-top: 20px;
            padding: 15px;
            background: #fff3cd;
            border: 1px solid #ffc107;
            border-radius: 5px;
            color: #856404;
            font-size: 14px;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>🔐 Test Authentification & Autorisation</h1>
            <p style="color: #666; margin-top: 5px;">Framework avec annotations @Authorized et @Role</p>
            
            <% if (request.getAttribute("message") != null) { %>
                <div class="status <%= ((String) request.getAttribute("message")).startsWith("✅") ? "success" : 
                    ((String)request.getAttribute("message")).startsWith("❌") ? "error" : "info" %>">
                    <%= request.getAttribute("message") %>
                </div>
            <% } %>
        </div>
        
        <% if (session.getAttribute("username") != null) { %>
            <div class="user-info">
                <h2>👤 Utilisateur connecté</h2>
                <p><strong>Nom :</strong> <%= session.getAttribute("username") %></p>
                <% if (request.getAttribute("roles") != null) { %>
                    <p><strong>Rôles :</strong> <%= request.getAttribute("roles") %></p>
                <% } %>
            </div>
        <% } %>
        
        <div class="actions">
            <h2>🧪 Actions de test</h2>
            
            <% if (session.getAttribute("username") == null) { %>
                <!-- Utilisateur non connecté -->
                <div class="button-grid">
                    <a href="<%= request.getContextPath() %>/auth/login" class="btn btn-primary">
                        🔑 Se connecter
                    </a>
                    <a href="<%= request.getContextPath() %>/auth/home" class="btn btn-info">
                        🏠 Page publique
                    </a>
                </div>
                
                <div class="note">
                    ℹ️ Vous devez vous connecter pour accéder aux pages protégées.
                </div>
            <% } else { %>
                <!-- Utilisateur connecté -->
                <div class="section">
                    <h3>✅ Pages nécessitant uniquement l'authentification</h3>
                    <div class="button-grid">
                        <a href="<%= request.getContextPath() %>/auth/protected" class="btn btn-success">
                            🔒 Page protégée (@Authorized)
                        </a>
                    </div>
                </div>
                
                <div class="section">
                    <h3>👔 Pages nécessitant des rôles spécifiques</h3>
                    <div class="button-grid">
                        <a href="<%= request.getContextPath() %>/auth/admin" class="btn btn-danger">
                            👑 Page Admin (@Role "admin")
                        </a>
                        <a href="<%= request.getContextPath() %>/auth/prof" class="btn btn-warning">
                            👨‍🏫 Page Prof (@Role "prof")
                        </a>
                        <a href="<%= request.getContextPath() %>/auth/staff" class="btn btn-info">
                            📊 Page Staff (@Role "admin" ou "prof")
                        </a>
                    </div>
                </div>
                
                <div class="section">
                    <h3>🚪 Déconnexion</h3>
                    <div class="button-grid">
                        <a href="<%= request.getContextPath() %>/auth/logout" class="btn btn-danger">
                            🚪 Se déconnecter
                        </a>
                    </div>
                </div>
                
                <div class="note">
                    💡 Essayez d'accéder aux pages avec différents comptes pour voir les restrictions de rôles.
                </div>
            <% } %>
        </div>
    </div>
</body>
</html>
