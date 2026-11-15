# Sends several requests to the server AT THE SAME TIME (concurrently):
# the new task (TaskPrimeCount) together with some of the old tasks.
#
# Usage (from the project folder, while the server is running):
#   powershell -ExecutionPolicy Bypass -File .\test-concurrency.ps1

param([string]$Url = "http://localhost:8000")

$requests = @(
    "TaskPrimeCount&1,5000000",
    "TaskPrimeCount&1,4000000",
    "TaskPrimeCount&1,3000000",
    "TaskFiboRecursive&40",
    "TaskBubbleSort&9,4,7,1,8,2",
    "TaskFiboRecursive&35"
)

$outDir = Join-Path $env:TEMP "concurrency-test"
New-Item -ItemType Directory -Force $outDir | Out-Null

Write-Host "Sending $($requests.Count) requests at the same time..." -ForegroundColor Cyan
$timer = [Diagnostics.Stopwatch]::StartNew()

# Start every curl process WITHOUT waiting for the previous one to finish
$processes = @()
for ($i = 0; $i -lt $requests.Count; $i++) {
    $outFile = Join-Path $outDir "response$i.txt"
    $processes += Start-Process curl.exe `
        -ArgumentList "-s", "--http0.9", "-X", "POST", "-d", $requests[$i], $Url `
        -NoNewWindow -PassThru -RedirectStandardOutput $outFile
}

# Now wait until all of them have finished
$processes | Wait-Process
$timer.Stop()

Write-Host ""
Write-Host "Responses:" -ForegroundColor Cyan
for ($i = 0; $i -lt $requests.Count; $i++) {
    $response = (Get-Content (Join-Path $outDir "response$i.txt") -Raw)
    if ($response) { $response = $response.Trim() }
    Write-Host ("{0,-28} -> {1}" -f $requests[$i], $response)
}

Write-Host ""
$total = $timer.Elapsed.TotalSeconds
Write-Host ("Total wall-clock time for all requests: {0:N2}s" -f $total) -ForegroundColor Green
