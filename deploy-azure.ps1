param(
    [string]$AppName = "jdemo",

    [string]$ResourceGroup = "jdemo_group",

    [string]$PlanName = "jdemo-plan",

    [string]$Location = "Southeast Asia",

    [string]$Sku = "B1",

    [string]$LinuxFxVersion = "JAVA:25-java25"
)

$ErrorActionPreference = "Stop"

function Require-Command {
    param([string]$Name)

    if (-not (Get-Command $Name -ErrorAction SilentlyContinue)) {
        throw "Required command not found: $Name"
    }
}

function Invoke-External {
    param(
        [Parameter(Mandatory = $true)]
        [scriptblock]$Command,

        [Parameter(Mandatory = $true)]
        [string]$FailureMessage
    )

    & $Command
    if ($LASTEXITCODE -ne 0) {
        throw $FailureMessage
    }
}

function Validate-AzureName {
    param(
        [Parameter(Mandatory = $true)]
        [string]$Value,

        [Parameter(Mandatory = $true)]
        [string]$Label
    )

    if ($Value -match "[\s()]") {
        throw "$Label '$Value' contains spaces or parentheses. Use a simple Azure resource name."
    }
}

Require-Command "az"

Validate-AzureName -Value $AppName -Label "App name"
Validate-AzureName -Value $PlanName -Label "Plan name"
Validate-AzureName -Value $ResourceGroup -Label "Resource group"

if (-not (Test-Path ".\gradlew.bat")) {
    throw "Run this script from the project root."
}

Write-Host "Checking Azure login..."
Invoke-External -FailureMessage "Azure CLI is not logged in. Run 'az login' first." -Command {
    az account show | Out-Null
}

Write-Host "Using Azure linuxFxVersion $LinuxFxVersion"

Write-Host "Building Spring Boot jar..."
.\gradlew.bat clean bootJar

$jar = Get-ChildItem ".\build\libs\*.jar" |
    Sort-Object LastWriteTime -Descending |
    Select-Object -First 1

if (-not $jar) {
    throw "No jar found under build\libs."
}

Write-Host "Creating resource group $ResourceGroup in $Location..."
Invoke-External -FailureMessage "Failed to create resource group '$ResourceGroup'." -Command {
    az group create `
        --name $ResourceGroup `
        --location $Location | Out-Null
}

Write-Host "Creating App Service plan $PlanName..."
Invoke-External -FailureMessage "Failed to create App Service plan '$PlanName'. Check the plan name, SKU, and region." -Command {
    az appservice plan create `
        --name $PlanName `
        --resource-group $ResourceGroup `
        --sku $Sku `
        --is-linux | Out-Null
}

Write-Host "Creating web app $AppName..."
Invoke-External -FailureMessage "Failed to create web app '$AppName'." -Command {
    az webapp create `
        --name $AppName `
        --resource-group $ResourceGroup `
        --plan $PlanName | Out-Null
}

Write-Host "Configuring web app stack to $LinuxFxVersion..."
Invoke-External -FailureMessage "Failed to configure the web app stack for '$AppName'." -Command {
    az webapp config set `
        --resource-group $ResourceGroup `
        --name $AppName `
        --linux-fx-version $LinuxFxVersion | Out-Null
}

$actualLinuxFxVersion = az webapp config show `
    --resource-group $ResourceGroup `
    --name $AppName `
    --query linuxFxVersion `
    --output tsv

if ($LASTEXITCODE -ne 0) {
    throw "Failed to read back the web app stack for '$AppName'."
}

if ($actualLinuxFxVersion -notmatch '^JAVA[|:]') {
    throw "Web app stack is '$actualLinuxFxVersion' instead of Java. Deployment stopped."
}

Write-Host "Deploying $($jar.Name)..."
Invoke-External -FailureMessage "Failed to deploy '$($jar.Name)' to '$AppName'." -Command {
    az webapp deploy `
        --resource-group $ResourceGroup `
        --name $AppName `
        --src-path $jar.FullName `
        --type jar | Out-Null
}

$url = "https://$AppName.azurewebsites.net/"
Write-Host "Deployment complete: $url"
