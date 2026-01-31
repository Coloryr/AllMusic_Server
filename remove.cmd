@echo off

setlocal enabledelayedexpansion

set array=folia server_top server ^
fabric_1_16_5 fabric_1_20_1 fabric_1_21 fabric_1_21_6 fabric_1_21_11 fabric_26_1 ^
forge_1_7_10 forge_1_12_2 forge_1_16_5 forge_1_20_1 ^
neoforge_1_21 neoforge_1_21_6 neoforge_1_21_11

for %%i in (%array%) do (
    if exist "%%i\src\main\java\com\coloryr\allmusic\server\core" rmdir /s /q "%%i\src\main\java\com\coloryr\allmusic\server\core"
    if exist "%%i\src\main\java\com\coloryr\allmusic\codec" rmdir /s /q "%%i\src\main\java\com\coloryr\allmusic\codec"
    if exist "%%i\build" rmdir /s /q "%%i\build"
)

set array1=folia server_top ^
fabric_1_16_5 fabric_1_20_1 fabric_1_21 fabric_1_21_6 fabric_1_21_11 fabric_26_1 ^
forge_1_7_10 forge_1_12_2 forge_1_16_5 forge_1_20_1 ^
neoforge_1_21 neoforge_1_21_6 neoforge_1_21_11

for %%i in (%array1%) do (
    if exist "%%i\src\main\java\com\coloryr\allmusic\buffercodec" rmdir /s /q "%%i\src\main\java\com\coloryr\allmusic\buffercodec"
)

set array2=folia server_top server

for %%i in (%array2%) do (
    if exist "%%i\src\main\java\com\coloryr\allmusic\server\bstats" rmdir /s /q "%%i\src\main\java\com\coloryr\allmusic\server\bstats"
)

set array3=fabric_1_16_5 fabric_1_20_1 fabric_1_21 fabric_1_21_6 fabric_1_21_11 ^
neoforge_1_21 neoforge_1_21_6 neoforge_1_21_11

for %%i in (%array3%) do (
    if exist "onejar\%%i\build" rmdir /s /q "onejar\%%i\build"
    
    if exist "onejar\%%i\src\main\java\com\coloryr\allmusic\server" rmdir /s /q "onejar\%%i\src\main\java\com\coloryr\allmusic\server"
    if exist "onejar\%%i\src\main\java\com\coloryr\allmusic\client" rmdir /s /q "onejar\%%i\src\main\java\com\coloryr\allmusic\client"
    
    if exist "onejar\%%i\src\main\java\com\coloryr\allmusic\codec" rmdir /s /q "onejar\%%i\src\main\java\com\coloryr\allmusic\codec"
    if exist "onejar\%%i\src\main\java\com\coloryr\allmusic\buffercodec" rmdir /s /q "onejar\%%i\src\main\java\com\coloryr\allmusic\buffercodec"
    
    if exist "onejar\%%i\src\main\java\com\coloryr\allmusic\server\core" rmdir /s /q "onejar\%%i\src\main\java\com\coloryr\allmusic\server\core"
    if exist "onejar\%%i\src\main\java\com\coloryr\allmusic\client\core" rmdir /s /q "onejar\%%i\src\main\java\com\coloryr\allmusic\client\core"

    if exist "onejar\%%i\src\main\resources\com\coloryr\allmusic\client\core\player\decoder\mp3" rmdir /s /q "onejar\%%i\src\main\resources\com\coloryr\allmusic\client\core\player\decoder\mp3"
)

set array4=fabric_1_21 fabric_1_21_6 fabric_1_21_11 ^
neoforge_1_21 neoforge_1_21_6 neoforge_1_21_11

for %%i in (%array4%) do (
    if exist "%%i\src\main\java\com\coloryr\allmusic\comm" rmdir /s /q "%%i\src\main\java\com\coloryr\allmusic\comm"
    if exist "onejar\%%i\src\main\java\com\coloryr\allmusic\comm" rmdir /s /q "onejar\%%i\src\main\java\com\coloryr\allmusic\comm"
)

endlocal