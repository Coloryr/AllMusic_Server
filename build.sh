#!/bin/bash

# 项目数组定义
projects=(
    "forge_1_7_10"
    "forge_1_12_2"
    "forge_1_16_5"
    "forge_1_18_2"
    "forge_1_19_2"
    "forge_1_20"
    "fabric_1_16_5"
    "fabric_1_18_2"
    "fabric_1_19_2"
    "fabric_1_19_3"
    "fabric_1_20"
    "fabric_1_20_2"
    "fabric_1_20_6"
    "fabric_1_21"
    "fabric_1_21_4"
    "fabric_1_21_5"
    "fabric_1_21_6"
    "fabric_26_1"
    "neoforge_1_20_4"
    "neoforge_1_20_6"
    "neoforge_1_21"
    "neoforge_1_21_4"
    "neoforge_1_21_5"
    "neoforge_1_21_6"
    "folia"
    "server"
    "server_top"
)

# 执行预处理脚本
echo "正在执行预处理脚本: link.sh"
if ! ./link.sh; then
    echo "预处理脚本失败，退出编译流程"
    read -p "按回车键退出"
    exit 1
fi

while true; do
    clear
    echo "AllMusic服务器编译"
    echo "选择需要构建的版本："
    echo "----------------------------"

    # 显示菜单选项
    for i in "${!projects[@]}"; do
        printf "[%d] - %s\n" "$i" "${projects[$i]}"
    done

    echo "----------------------------"
    read -p "请输入要编译的项目编号 (0-$((${#projects[@]}-1))): " selection

    # 验证输入
    if ! [[ "$selection" =~ ^[0-9]+$ ]]; then
        echo "错误：请输入有效的数字"
        read -p "按回车键继续"
        continue
    fi

    if [ "$selection" -lt 0 ] || [ "$selection" -ge ${#projects[@]} ]; then
        echo "错误：输入超出范围 (0-$((${#projects[@]}-1)))"
        read -p "按回车键继续"
        continue
    fi

    selected_project="${projects[$selection]}"
    
    # 检查路径是否存在
    if [ ! -d "$selected_project" ]; then
        echo "错误：路径不存在 - $selected_project"
        read -p "按回车键继续"
        continue
    fi

    # 切换到项目目录
    cd "$selected_project" || {
        echo "无法进入目录: $selected_project"
        read -p "按回车键继续"
        continue
    }
    
    echo "当前工作目录："
    pwd
    echo "正在执行Gradle编译..."
    
    # 执行Gradle构建
    if ./gradlew build; then
        echo "编译成功！生成位置：build/libs"
    else
        echo "编译失败，请检查错误信息"
    fi

    read -p "按回车键返回菜单"
    cd ..  # 返回上级目录
done