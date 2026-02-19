# Test Auth Feature
# Ce script teste la fonctionnalité d'authentification et d'autorisation

$baseUrl = "http://localhost:8080"

Write-Host "🧪 Test de la fonctionnalité d'authentification et d'autorisation" -ForegroundColor Cyan
Write-Host ""

# Test 1: Accès à une page publique
Write-Host "1️⃣ Test: Page publique (sans authentification)" -ForegroundColor Yellow
$response = Invoke-WebRequest -Uri "$baseUrl/auth/home" -Method GET -UseBasicParsing
if ($response.StatusCode -eq 200) {
    Write-Host "   ✅ OK - Page publique accessible" -ForegroundColor Green
} else {
    Write-Host "   ❌ ERREUR - Status: $($response.StatusCode)" -ForegroundColor Red
}
Write-Host ""

# Test 2: Accès à une page protégée sans authentification (doit échouer)
Write-Host "2️⃣ Test: Page protégée sans authentification (doit échouer)" -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "$baseUrl/auth/protected" -Method GET -UseBasicParsing -ErrorAction Stop
    Write-Host "   ❌ ERREUR - La page devrait être bloquée (Status: $($response.StatusCode))" -ForegroundColor Red
} catch {
    if ($_.Exception.Response.StatusCode -eq 403) {
        Write-Host "   ✅ OK - Accès refusé (403 Forbidden)" -ForegroundColor Green
    } else {
        Write-Host "   ⚠️  Autre erreur: $($_.Exception.Response.StatusCode)" -ForegroundColor Yellow
    }
}
Write-Host ""

# Test 3: Accès à une page admin sans authentification (doit échouer)
Write-Host "3️⃣ Test: Page admin sans authentification (doit échouer)" -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "$baseUrl/auth/admin" -Method GET -UseBasicParsing -ErrorAction Stop
    Write-Host "   ❌ ERREUR - La page devrait être bloquée (Status: $($response.StatusCode))" -ForegroundColor Red
} catch {
    if ($_.Exception.Response.StatusCode -eq 403) {
        Write-Host "   ✅ OK - Accès refusé (403 Forbidden)" -ForegroundColor Green
    } else {
        Write-Host "   ⚠️  Autre erreur: $($_.Exception.Response.StatusCode)" -ForegroundColor Yellow
    }
}
Write-Host ""

# Test 4: Login en tant qu'admin
Write-Host "4️⃣ Test: Login en tant qu'admin" -ForegroundColor Yellow
$session = New-Object Microsoft.PowerShell.Commands.WebRequestSession
$loginData = @{
    username = "admin"
    password = "admin"
}
try {
    $response = Invoke-WebRequest -Uri "$baseUrl/auth/login" -Method POST -Body $loginData -WebSession $session -UseBasicParsing
    if ($response.StatusCode -eq 200) {
        Write-Host "   ✅ OK - Login admin réussi" -ForegroundColor Green
    }
} catch {
    Write-Host "   ❌ ERREUR - Login échoué: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

# Test 5: Accès à la page protégée avec session admin
Write-Host "5️⃣ Test: Page protégée avec session admin" -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "$baseUrl/auth/protected" -Method GET -WebSession $session -UseBasicParsing
    if ($response.StatusCode -eq 200) {
        Write-Host "   ✅ OK - Accès autorisé" -ForegroundColor Green
    }
} catch {
    Write-Host "   ❌ ERREUR - Status: $($_.Exception.Response.StatusCode)" -ForegroundColor Red
}
Write-Host ""

# Test 6: Accès à la page admin avec session admin
Write-Host "6️⃣ Test: Page admin avec session admin" -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "$baseUrl/auth/admin" -Method GET -WebSession $session -UseBasicParsing
    if ($response.StatusCode -eq 200) {
        Write-Host "   ✅ OK - Accès admin autorisé" -ForegroundColor Green
    }
} catch {
    Write-Host "   ❌ ERREUR - Status: $($_.Exception.Response.StatusCode)" -ForegroundColor Red
}
Write-Host ""

# Test 7: Login en tant que user simple
Write-Host "7️⃣ Test: Login en tant que user simple" -ForegroundColor Yellow
$sessionUser = New-Object Microsoft.PowerShell.Commands.WebRequestSession
$loginData = @{
    username = "user"
    password = "user"
}
try {
    $response = Invoke-WebRequest -Uri "$baseUrl/auth/login" -Method POST -Body $loginData -WebSession $sessionUser -UseBasicParsing
    if ($response.StatusCode -eq 200) {
        Write-Host "   ✅ OK - Login user réussi" -ForegroundColor Green
    }
} catch {
    Write-Host "   ❌ ERREUR - Login échoué: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

# Test 8: Tentative d'accès admin avec session user (doit échouer)
Write-Host "8️⃣ Test: Page admin avec session user (doit échouer)" -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "$baseUrl/auth/admin" -Method GET -WebSession $sessionUser -UseBasicParsing -ErrorAction Stop
    Write-Host "   ❌ ERREUR - L'accès devrait être refusé (Status: $($response.StatusCode))" -ForegroundColor Red
} catch {
    if ($_.Exception.Response.StatusCode -eq 403) {
        Write-Host "   ✅ OK - Accès refusé (403 Forbidden)" -ForegroundColor Green
    } else {
        Write-Host "   ⚠️  Autre erreur: $($_.Exception.Response.StatusCode)" -ForegroundColor Yellow
    }
}
Write-Host ""

Write-Host "✨ Tests terminés!" -ForegroundColor Cyan
Write-Host ""
Write-Host "📝 Pour tester manuellement:" -ForegroundColor White
Write-Host "   1. Ouvrez votre navigateur" -ForegroundColor Gray
Write-Host "   2. Accédez à: $baseUrl/auth/home" -ForegroundColor Gray
Write-Host "   3. Testez avec les comptes:" -ForegroundColor Gray
Write-Host "      - admin/admin (tous les accès)" -ForegroundColor Gray
Write-Host "      - prof/prof (accès prof et staff)" -ForegroundColor Gray
Write-Host "      - user/user (accès protégé uniquement)" -ForegroundColor Gray
