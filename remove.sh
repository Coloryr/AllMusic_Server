#!/bin/sh

# mkdir "build"
# mkdir ".gradle"

array="folia server_top server \
fabric_1_16_5 fabric_1_20_1 fabric_1_21 fabric_1_21_6 fabric_26_1 \
forge_1_7_10 forge_1_12_2 forge_1_16_5 forge_1_20_1 \
neoforge_1_21 neoforge_1_21_6"

for dir in $array; do
    rm "$dir/src/main/java/com/coloryr/allmusic/server/core"
    rm "$dir/src/main/java/com/coloryr/allmusic/server/codec"

    if [ -d "$dir/build" ]; then
        rm -rf "$dir/build"
    fi
    if [ -d "$dir/.gradle" ]; then
        rm -rf "$dir/.gradle"
    fi
    if [ -d "$dir/.idea" ]; then
        rm -rf "$dir/.idea"
    fi
done

array2="folia server_top server"

for dir in $array2; do
    rm -rf "$dir/src/main/java/com/coloryr/allmusic/server/bstats"
done