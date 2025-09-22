#!/bin/sh

# 创建 build/libs 目录
mkdir -p "build/libs"

# 定义模块数组
array="folia server_top server forge_1_20 forge_1_19_2 forge_1_18_2 forge_1_16_5 forge_1_12_2 forge_1_7_10 fabric_1_20_2 fabric_1_20 fabric_1_19_3 fabric_1_19_2 fabric_1_18_2 fabric_1_16_5 fabric_1_20_6 fabric_1_21 neoforge_1_20_4 neoforge_1_20_5 neoforge_1_21 neoforge_1_21_4 fabric_1_21_4 fabric_1_21_5 neoforge_1_21_5 fabric_1_21_6 neoforge_1_21_6"

# 处理核心链接和库链接
for module in $array; do
    # 创建 core 符号链接
    core_link="$module/src/main/java/com/coloryr/allmusic/server/core"
    if [ ! -e "$core_link" ]; then
        mkdir -p "$(dirname "$core_link")"
        ln -s "../../../../../../../../core" "$core_link"
    fi
    
    # 创建 build 目录和 libs 链接
    build_dir="$module/build"
    if [ ! -d "$build_dir" ]; then
        mkdir -p "$build_dir"
        ln -s "../../build/libs" "$build_dir/libs"
    fi
done

# 定义需要 codec 链接的模块
array1="folia server_top forge_1_20 forge_1_19_2 forge_1_18_2 forge_1_16_5 forge_1_12_2 forge_1_7_10 fabric_1_20_2 fabric_1_20 fabric_1_19_3 fabric_1_19_2 fabric_1_18_2 fabric_1_16_5 fabric_1_20_6 fabric_1_21 neoforge_1_20_4 neoforge_1_20_5 neoforge_1_21 neoforge_1_21_4 fabric_1_21_4 fabric_1_21_5 neoforge_1_21_5 fabric_1_21_6 neoforge_1_21_6"

# 创建 codec 符号链接
for module in $array1; do
    codec_link="$module/src/main/java/com/coloryr/allmusic/server/codec"
    if [ ! -e "$codec_link" ]; then
        mkdir -p "$(dirname "$codec_link")"
        ln -s "../../../../../../../../codec" "$codec_link"
    fi
done

# 定义需要 bstats 链接的模块
array2="folia server_top server"

# 创建 bstats 符号链接
for module in $array2; do
    bstats_link="$module/src/main/java/com/coloryr/allmusic/server/bstats"
    if [ ! -e "$bstats_link" ]; then
        mkdir -p "$(dirname "$bstats_link")"
        ln -s "../../../../../../../../bstats" "$bstats_link"
    fi
done

echo "符号链接创建完成"