@echo off

@REM mkdir "build"
@REM mkdir ".gradle"

setlocal enabledelayedexpansion

set array=folia server_top server forge_1_20_1 ^
forge_1_19_2 forge_1_18_2 forge_1_16_5 forge_1_12_2 forge_1_7_10 ^
fabric_1_20_2 fabric_1_20_1 fabric_1_19_3 fabric_1_19_2 fabric_1_18_2 ^
fabric_1_16_5 fabric_1_20_6 fabric_1_21 neoforge_1_20_4 neoforge_1_20_5 neoforge_1_21 ^
neoforge_1_21_4 fabric_1_21_4 fabric_1_21_5 neoforge_1_21_5 fabric_1_21_6 neoforge_1_21_6

for %%i in (%array%) do (
    if exist "%%i\src\main\java\com\coloryr\allmusic\server\core" rmdir "%%i\src\main\java\com\coloryr\allmusic\server\core"
    if exist "%%i\build" rmdir /s /q "%%i\build"
    if exist "%%i\.gradle" rmdir /s /q "%%i\.gradle"
    if exist "%%i\.idea" rmdir /s /q "%%i\.idea"
)

set array1=folia server_top forge_1_20_1 ^
forge_1_19_2 forge_1_18_2 forge_1_16_5 forge_1_12_2 forge_1_7_10 ^
fabric_1_20_2 fabric_1_20_1 fabric_1_19_3 fabric_1_19_2 fabric_1_18_2 ^
fabric_1_16_5 fabric_1_20_6 fabric_1_21 neoforge_1_20_4 neoforge_1_20_5 neoforge_1_21 ^
neoforge_1_21_4 fabric_1_21_4 fabric_1_21_5 neoforge_1_21_5 fabric_1_21_6 neoforge_1_21_6

for %%i in (%array1%) do (
    if exist "%%i\src\main\java\com\coloryr\allmusic\server\codec" rmdir "%%i\src\main\java\com\coloryr\allmusic\server\codec"
)

set array2=folia server_top server

for %%i in (%array2%) do (
    if exist "%%i\src\main\java\com\coloryr\allmusic\server\bstats" rmdir "%%i\src\main\java\com\coloryr\allmusic\server\bstats"
)

set array3=neoforge_1_21_6

for %%i in (%array3%) do (
    if exist "onejar\%%i\build" rmdir /s /q "onejar\%%i\build"
    if exist "onejar\%%i\.gradle" rmdir /s /q "onejar\%%i\.gradle"
    if exist "onejar\%%i\.idea" rmdir /s /q "onejar\%%i\.idea"

    if exist "onejar\%%i\src\main\java\com\coloryr\allmusic\server\core" rmdir "onejar\%%i\src\main\java\com\coloryr\allmusic\server\core"
    if exist "onejar\%%i\src\main\java\com\coloryr\allmusic\server\codec" rmdir "onejar\%%i\src\main\java\com\coloryr\allmusic\server\codec"
    if exist "onejar\%%i\src\main\java\com\coloryr\allmusic\client\core" rmdir "onejar\%%i\src\main\java\com\coloryr\allmusic\client\core"
    if exist "onejar\%%i\src\main\resources\com\coloryr\allmusic\client\core\player\decoder\mp3" rmdir "onejar\%%i\src\main\resources\com\coloryr\allmusic\client\core\player\decoder\mp3"
)

endlocal