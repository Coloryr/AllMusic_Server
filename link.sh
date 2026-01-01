#!/bin/bash

# 获取项目根目录绝对路径
ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# 创建 build/libs 目录
mkdir -p "$ROOT_DIR/build/libs"

# 定义数组
array=("folia" "server_top" "server" \
       "fabric_1_16_5" "fabric_1_20_1" "fabric_1_21" "fabric_1_21_6" "fabric_26_1" \
       "forge_1_7_10" "forge_1_12_2" "forge_1_16_5" "forge_1_20_1" \
       "neoforge_1_21" "neoforge_1_21_6")

# 为数组中的每个项目创建符号链接
for dir in "${array[@]}"; do
    if [ ! -d "$ROOT_DIR/$dir/src/main/java/com/coloryr/allmusic/server/core" ]; then
        ln -sf "$ROOT_DIR/core" "$ROOT_DIR/$dir/src/main/java/com/coloryr/allmusic/server/core"
    fi
    if [ ! -d "$ROOT_DIR/$dir/build" ]; then
        mkdir -p "$ROOT_DIR/$dir/build"
    fi
    if [ ! -d "$ROOT_DIR/$dir/build/libs" ]; then
        ln -sf "$ROOT_DIR/build/libs" "$ROOT_DIR/$dir/build/libs"
    fi
done

# 第二个数组
array1=("folia" "server_top" \
        "fabric_1_16_5" "fabric_1_20_1" "fabric_1_21" "fabric_1_21_6" "fabric_26_1" \
        "forge_1_7_10" "forge_1_12_2" "forge_1_16_5" "forge_1_20_1" \
        "neoforge_1_21" "neoforge_1_21_6")

# 为第二个数组创建符号链接
for dir in "${array1[@]}"; do
    if [ ! -d "$ROOT_DIR/$dir/src/main/java/com/coloryr/allmusic/server/codec" ]; then
        ln -sf "$ROOT_DIR/codec" "$ROOT_DIR/$dir/src/main/java/com/coloryr/allmusic/server/codec"
    fi
done

# 第三个数组
array2=("folia" "server_top" "server")

# 为第三个数组创建符号链接
for dir in "${array2[@]}"; do
    if [ ! -d "$ROOT_DIR/$dir/src/main/java/com/coloryr/allmusic/server/bstats" ]; then
        ln -sf "$ROOT_DIR/bstats" "$ROOT_DIR/$dir/src/main/java/com/coloryr/allmusic/server/bstats"
    fi
done

# 第四个数组
array3=("neoforge_1_21_6" "fabric_1_21_6" "forge_1_20_1" "fabric_1_20_1")

# 为第四个数组创建符号链接
for dir in "${array3[@]}"; do
    if [ ! -d "$ROOT_DIR/onejar/$dir/build" ]; then
        mkdir -p "$ROOT_DIR/onejar/$dir/build"
    fi
    if [ ! -d "$ROOT_DIR/onejar/$dir/src/main/java/com/coloryr/allmusic/client" ]; then
        mkdir -p "$ROOT_DIR/onejar/$dir/src/main/java/com/coloryr/allmusic/client"
    fi
    if [ ! -d "$ROOT_DIR/onejar/$dir/src/main/resources/com/coloryr/allmusic/client/core/player/decoder" ]; then
        mkdir -p "$ROOT_DIR/onejar/$dir/src/main/resources/com/coloryr/allmusic/client/core/player/decoder"
    fi

    if [ ! -d "$ROOT_DIR/onejar/$dir/build/libs" ]; then
        ln -sf "$ROOT_DIR/build/libs" "$ROOT_DIR/onejar/$dir/build/libs"
    fi
    if [ ! -d "$ROOT_DIR/onejar/$dir/src/main/java/com/coloryr/allmusic/server/core" ]; then
        ln -sf "$ROOT_DIR/core" "$ROOT_DIR/onejar/$dir/src/main/java/com/coloryr/allmusic/server/core"
    fi
    if [ ! -d "$ROOT_DIR/onejar/$dir/src/main/java/com/coloryr/allmusic/server/codec" ]; then
        ln -sf "$ROOT_DIR/codec" "$ROOT_DIR/onejar/$dir/src/main/java/com/coloryr/allmusic/server/codec"
    fi
    if [ ! -d "$ROOT_DIR/onejar/$dir/src/main/java/com/coloryr/allmusic/client/core" ]; then
        ln -sf "$ROOT_DIR/client/core" "$ROOT_DIR/onejar/$dir/src/main/java/com/coloryr/allmusic/client/core"
    fi
    if [ ! -d "$ROOT_DIR/onejar/$dir/src/main/resources/com/coloryr/allmusic/client/core/player/decoder/mp3" ]; then
        ln -sf "$ROOT_DIR/client/mp3" "$ROOT_DIR/onejar/$dir/src/main/resources/com/coloryr/allmusic/client/core/player/decoder/mp3"
    fi
done

echo "符号链接创建完成"
