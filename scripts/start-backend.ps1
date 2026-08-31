<#
.SYNOPSIS
Builds and starts TapeVerify AI locally with the required environment variables.

.DESCRIPTION
Prompts for the database and administrator passwords so they are not stored in
source control. Supply -JwtSecret to keep sessions valid across restarts; when
it is omitted, an ephemeral secure secret is generated for this run.
#>
param(
  [string]$DbUrl = 'jdbc:mysql://localhost:3306/tapeverify?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true',
  [string]$DbUser = 'root',
  [Parameter(Mandatory = $true)][SecureString]$DbPassword,
  [string]$AdminUsername = 'admin',
  [Parameter(Mandatory = $true)][SecureString]$AdminPassword,
  [string]$JwtSecret
)

function ConvertTo-PlainText([SecureString]$Value) {
  $pointer = [Runtime.InteropServices.Marshal]::SecureStringToBSTR($Value)
  try {
    return [Runtime.InteropServices.Marshal]::PtrToStringBSTR($pointer)
  } finally {
    [Runtime.InteropServices.Marshal]::ZeroFreeBSTR($pointer)
  }
}

if ([string]::IsNullOrWhiteSpace($JwtSecret)) {
  $randomBytes = New-Object byte[] 32
  [Security.Cryptography.RandomNumberGenerator]::Fill($randomBytes)
  $JwtSecret = [Convert]::ToBase64String($randomBytes)
  Write-Host 'Generated an ephemeral JWT secret for this local run.' -ForegroundColor Yellow
}

$env:DB_URL = $DbUrl
$env:DB_USER = $DbUser
$env:DB_PASS = ConvertTo-PlainText $DbPassword
$env:JWT_SECRET = $JwtSecret
$env:BOOTSTRAP_ADMIN_USERNAME = $AdminUsername
$env:BOOTSTRAP_ADMIN_PASSWORD = ConvertTo-PlainText $AdminPassword

$backendDirectory = Join-Path $PSScriptRoot '..\tapeverify-backend'
Push-Location (Resolve-Path $backendDirectory)
try {
  & mvn clean package
  if ($LASTEXITCODE -ne 0) { throw 'Backend build failed. Resolve the Maven errors above and run this script again.' }
  & mvn spring-boot:run
  if ($LASTEXITCODE -ne 0) { throw 'Backend did not start. Confirm that MySQL is running and the entered database password is correct.' }
} finally {
  Pop-Location
}
