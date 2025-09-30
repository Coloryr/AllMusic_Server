# 创建 build\libs 目录（如果不存在）
$buildLibs = "build\libs"
if (-not (Test-Path -Path $buildLibs)) {
    New-Item -Path $buildLibs -ItemType Directory -Force | Out-Null
}

# 定义模块数组
$array = @(
    "folia", "server_top", "server", "forge_1_20_1",
    "forge_1_19_2", "forge_1_18_2", "forge_1_16_5", "forge_1_12_2", "forge_1_7_10",
    "fabric_1_20_2", "fabric_1_20_1", "fabric_1_19_3", "fabric_1_19_2", "fabric_1_18_2",
    "fabric_1_16_5", "fabric_1_20_6", "fabric_1_21", "neoforge_1_20_4", "neoforge_1_20_5", "neoforge_1_21",
    "neoforge_1_21_4", "fabric_1_21_4", "fabric_1_21_5", "neoforge_1_21_5", "fabric_1_21_6", "neoforge_1_21_6"
)

# 处理核心链接和库链接
foreach ($module in $array) {
    # 创建 core 符号链接
    $coreLink = "$module\src\main\java\com\coloryr\allmusic\server\core"
    if (-not (Test-Path -Path $coreLink)) {
        New-Item -Path $coreLink -ItemType Junction -Value "core" -Force | Out-Null
    }
    
    # 创建 build 目录和 libs 链接
    $buildDir = "$module\build"
    if (-not (Test-Path -Path $buildDir)) {
        New-Item -Path $buildDir -ItemType Directory -Force | Out-Null
        $libsLink = "$buildDir\libs"
        New-Item -Path $libsLink -ItemType Junction -Value "build\libs" -Force | Out-Null
    }
}

# 定义需要 codec 链接的模块
$array1 = @(
    "folia", "server_top", "forge_1_20_1",
    "forge_1_19_2", "forge_1_18_2", "forge_1_16_5", "forge_1_12_2", "forge_1_7_10",
    "fabric_1_20_2", "fabric_1_20_1", "fabric_1_19_3", "fabric_1_19_2", "fabric_1_18_2",
    "fabric_1_16_5", "fabric_1_20_6", "fabric_1_21", "neoforge_1_20_4", "neoforge_1_20_5", "neoforge_1_21",
    "neoforge_1_21_4", "fabric_1_21_4", "fabric_1_21_5", "neoforge_1_21_5", "fabric_1_21_6", "neoforge_1_21_6"
)

# 创建 codec 符号链接
foreach ($module in $array1) {
    $codecLink = "$module\src\main\java\com\coloryr\allmusic\server\codec"
    if (-not (Test-Path -Path $codecLink)) {
        New-Item -Path $codecLink -ItemType Junction -Value "codec" -Force | Out-Null
    }
}

# 定义需要 bstats 链接的模块
$array2 = @("folia", "server_top", "server")

# 创建 bstats 符号链接
foreach ($module in $array2) {
    $bstatsLink = "$module\src\main\java\com\coloryr\allmusic\server\bstats"
    if (-not (Test-Path -Path $bstatsLink)) {
        New-Item -Path $bstatsLink -ItemType Junction -Value "bstats" -Force | Out-Null
    }
}

Write-Host "符号链接创建完成"