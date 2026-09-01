# =====================================================
# SESSION START HOOK
# =====================================================

Write-Host ""
Write-Host "======================================="
Write-Host "   SESSION START HOOK EXECUTED"
Write-Host "======================================="
Write-Host ""

# Repository root
$repoRoot = Resolve-Path (Join-Path $PSScriptRoot "..\..")

# Proof file
$statusFile = Join-Path $PSScriptRoot "hook-status.txt"

@"
SESSION START HOOK EXECUTED
Timestamp: $(Get-Date -Format "yyyy-MM-dd HH:mm:ss")
Repository: $repoRoot
"@ | Out-File $statusFile -Force

Write-Host "Checking Jira MCP configuration..."

# MCP configuration file
$mcpFile = Join-Path $repoRoot ".vscode\mcp.json"

if (-not (Test-Path $mcpFile)) {

    @"
SESSION START HOOK FAILED
Reason: MCP configuration file missing
File: $mcpFile
Timestamp: $(Get-Date -Format "yyyy-MM-dd HH:mm:ss")
"@ | Out-File $statusFile -Force

    Write-Error "MCP configuration file not found: $mcpFile"
    exit 2
}

$content = Get-Content $mcpFile -Raw

if ($content -notmatch "mcp\.atlassian\.com/v1/mcp/authv2") {

    @"
SESSION START HOOK FAILED
Reason: Atlassian Rovo MCP endpoint not configured
Timestamp: $(Get-Date -Format "yyyy-MM-dd HH:mm:ss")
"@ | Out-File $statusFile -Force

    Write-Error "Atlassian Rovo MCP endpoint is not configured."
    exit 2
}

@"
SESSION START HOOK PASSED
Result: Jira MCP configuration validated successfully
Timestamp: $(Get-Date -Format "yyyy-MM-dd HH:mm:ss")
"@ | Out-File $statusFile -Force

Write-Host "Atlassian Rovo MCP configuration detected."
Write-Host "Jira MCP pre-check passed."

exit 0