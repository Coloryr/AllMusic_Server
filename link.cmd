@echo off

mkdir "build\libs"

setlocal enabledelayedexpansion

set array=folia server_top server ^
fabric_1_16_5 fabric_1_20_1 fabric_1_21 fabric_1_21_6 fabric_1_21_11 fabric_26_1 ^
forge_1_7_10 forge_1_12_2 forge_1_16_5 forge_1_20_1 ^
neoforge_1_21 neoforge_1_21_6 neoforge_1_21_11

for %%i in (%array%) do (
    if not exist "%%i\src\main\java\com\coloryr\allmusic\server\core" mklink /j "%%i\src\main\java\com\coloryr\allmusic\server\core" "core"
    if not exist "%%i\src\main\java\com\coloryr\allmusic\codec" mklink /j "%%i\src\main\java\com\coloryr\allmusic\codec" "client\codec"
    if not exist "%%i\build" mkdir "%%i\build" && mklink /j "%%i\build\libs" "build\libs"
)

set array1=folia server_top ^
fabric_1_16_5 fabric_1_20_1 fabric_1_21 fabric_1_21_6 fabric_1_21_11 fabric_26_1 ^
forge_1_7_10 forge_1_12_2 forge_1_16_5 forge_1_20_1 ^
neoforge_1_21 neoforge_1_21_6 neoforge_1_21_11

for %%i in (%array1%) do (
    if not exist "%%i\src\main\java\com\coloryr\allmusic\buffercodec" mklink /j "%%i\src\main\java\com\coloryr\allmusic\buffercodec" "client\buffercodec"
)

set array2=folia server_top server

for %%i in (%array2%) do (
    if not exist "%%i\src\main\java\com\coloryr\allmusic\server\bstats" mklink /j "%%i\src\main\java\com\coloryr\allmusic\server\bstats" "bstats"
)

set array3=fabric_1_16_5 fabric_1_20_1 fabric_1_21 fabric_1_21_6 fabric_1_21_11 ^
neoforge_1_21_6 ^
forge_1_20_1

for %%i in (%array3%) do (

    if not exist "onejar\%%i\build" mkdir "onejar\%%i\build"
    if not exist "onejar\%%i\src\main\java\com\coloryr\allmusic" mkdir "onejar\%%i\src\main\java\com\coloryr\allmusic"
    if not exist "onejar\%%i\src\main\resources\com\coloryr\allmusic\client\core\player\decoder" mkdir "onejar\%%i\src\main\resources\com\coloryr\allmusic\client\core\player\decoder"

    if not exist "onejar\%%i\build\libs" mklink /j "onejar\%%i\build\libs" "build\libs"

    if not exist "onejar\%%i\src\main\java\com\coloryr\allmusic\server" mklink /j "onejar\%%i\src\main\java\com\coloryr\allmusic\server" "%%i\src\main\java\com\coloryr\allmusic\server"
    if not exist "onejar\%%i\src\main\java\com\coloryr\allmusic\client" mklink /j "onejar\%%i\src\main\java\com\coloryr\allmusic\client" "client\%%i\src\main\java\com\coloryr\allmusic\client"

    if not exist "onejar\%%i\src\main\java\com\coloryr\allmusic\codec" mklink /j "onejar\%%i\src\main\java\com\coloryr\allmusic\codec" "client\codec"
    if not exist "onejar\%%i\src\main\java\com\coloryr\allmusic\buffercodec" mklink /j "onejar\%%i\src\main\java\com\coloryr\allmusic\buffercodec" "client\buffercodec"

    if not exist "onejar\%%i\src\main\java\com\coloryr\allmusic\server\core" mklink /j "onejar\%%i\src\main\java\com\coloryr\allmusic\server\core" "core"
    if not exist "onejar\%%i\src\main\java\com\coloryr\allmusic\client\core" mklink /j "onejar\%%i\src\main\java\com\coloryr\allmusic\client\core" "client\core"
    
    if not exist "onejar\%%i\src\main\resources\com\coloryr\allmusic\client\core\player\decoder\mp3" mklink /j "onejar\%%i\src\main\resources\com\coloryr\allmusic\client\core\player\decoder\mp3" "client\mp3"
)

set array4=fabric_1_21 fabric_1_21_6 fabric_1_21_11

for %%i in (%array4%) do (
    if not exist "%%i\src\main\java\com\coloryr\allmusic\comm" mklink /j "%%i\src\main\java\com\coloryr\allmusic\comm" "client\%%i\src\main\java\com\coloryr\allmusic\comm"
    if not exist "onejar\%%i\src\main\java\com\coloryr\allmusic\comm" mklink /j "onejar\%%i\src\main\java\com\coloryr\allmusic\comm" "client\%%i\src\main\java\com\coloryr\allmusic\comm"
)