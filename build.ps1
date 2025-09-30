Clear-Host

# 项目数组定义
$projects = @(
    "forge_1_7_10",
    "forge_1_12_2",
    "forge_1_16_5",
    "forge_1_18_2",
    "forge_1_19_2",
    "forge_1_20_1",
    "fabric_1_16_5",
    "fabric_1_18_2",
    "fabric_1_19_2",
    "fabric_1_19_3",
    "fabric_1_20_1",
    "fabric_1_20_2",
    "fabric_1_20_6",
    "fabric_1_21",
    "fabric_1_21_4",
    "fabric_1_21_5",
    "fabric_1_21_6",
    "neoforge_1_20_4",
    "neoforge_1_20_6",
    "neoforge_1_21",
    "neoforge_1_21_4",
    "neoforge_1_21_5",
    "neoforge_1_21_6",
    "folia",
    "server",
    "server_top"
)

# 执行预处理脚本
Write-Host "正在执行预处理脚本: link.ps1"
& .\link.ps1
if (-not $?) {
    Write-Host "预处理脚本失败，退出编译流程"
    $null = Read-Host "按回车键退出"
    exit 1
}

while ($true) {
    Clear-Host
    Write-Host "AllMusic服务器编译"
    Write-Host "选择需要构建的版本："
    Write-Host "----------------------------"

    # 显示菜单选项
    for ($i = 0; $i -lt $projects.Length; $i++) {
        Write-Host ("[{0}] - {1}" -f $i, $projects[$i])
    }

    Write-Host "----------------------------"
    $selection = Read-Host "请输入要编译的项目编号 (0-$($projects.Length-1))"

    # 验证输入
    if (-not ($selection -match '^\d+$')) {
        Write-Host "错误：请输入有效的数字"
        $null = Read-Host "按任意键继续"
        continue
    }

    $index = [int]$selection
    if ($index -lt 0 -or $index -ge $projects.Length) {
        Write-Host "错误：输入超出范围 (0-$($projects.Length-1))"
        $null = Read-Host "按任意键继续"
        continue
    }

    $selectedProject = $projects[$index]
    
    # 检查路径是否存在
    if (-not (Test-Path -Path $selectedProject -PathType Container)) {
        Write-Host "错误：路径不存在 - $selectedProject"
        $null = Read-Host "按任意键继续"
        continue
    }

    # 切换到项目目录
    Set-Location -Path $selectedProject
    Write-Host "当前工作目录："
    Get-Location
    Write-Host "正在执行Gradle编译..."

    # 执行Gradle构建
    & .\gradlew build

    # 处理编译结果
    if ($?) {
        Write-Host "编译成功！生成位置：build\libs"
    } else {
        Write-Host "编译失败，请检查错误信息"
    }

    $null = Read-Host "按回车键返回菜单"
    Set-Location ..  # 返回上级目录
}