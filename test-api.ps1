# Script de test des endpoints JSON API - Sprint 9
# Usage: .\test-api.ps1

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  Test API JSON - Sprint 9" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

$baseUrl = "http://localhost:8080/spri"

function Test-Endpoint {
    param(
        [string]$name,
        [string]$endpoint
    )
    
    Write-Host "[$name]" -ForegroundColor Yellow -NoNewline
    Write-Host " GET $endpoint" -ForegroundColor Gray
    
    try {
        $response = Invoke-RestMethod -Uri "$baseUrl$endpoint" -Method Get -ContentType "application/json"
        Write-Host "  Status: " -NoNewline -ForegroundColor Gray
        if ($response.status -eq "success") {
            Write-Host $response.status -ForegroundColor Green
        } else {
            Write-Host $response.status -ForegroundColor Red
        }
        Write-Host "  Code: $($response.code)" -ForegroundColor Gray
        Write-Host "  Data: " -NoNewline -ForegroundColor Gray
        Write-Host ($response.data | ConvertTo-Json -Depth 5 -Compress)
        Write-Host ""
    }
    catch {
        Write-Host "  Erreur: $($_.Exception.Message)" -ForegroundColor Red
        Write-Host ""
    }
}

# Tester tous les endpoints
Test-Endpoint "Test 1" "/api/employee"
Test-Endpoint "Test 2" "/api/employees"
Test-Endpoint "Test 3" "/api/employee-detail"
Test-Endpoint "Test 4" "/api/stats"
Test-Endpoint "Test 5" "/api/custom-success"
Test-Endpoint "Test 6" "/api/custom-error"
Test-Endpoint "Test 7" "/api/empty-list"

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  Tests terminés!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Pour tester via navigateur: $baseUrl/pages/test-json-api.html" -ForegroundColor Yellow
