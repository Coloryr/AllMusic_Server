@echo off

mkdir "build"
mkdir ".gradle"

setlocal enabledelayedexpansion

set array=folia server_top server forge_1_20 forge_1_19_3 ^
forge_1_19_2 forge_1_18_2 forge_1_16_5 forge_1_12_2 forge_1_7_10 ^
fabric_1_20_2 fabric_1_20 fabric_1_19_3 fabric_1_19_2 fabric_1_18_2 ^
fabric_1_16_5 fabric_1_20_6 neoforge_1_20_4 neoforge_1_20_5

for %%i in (%array%) do (
    if exist "%%i\src\main\java\com\coloryr\allmusic\server\core" rmdir "%%i\src\main\java\com\coloryr\allmusic\server\core" "core"
    if exist "%%i\build" rmdir /s /q "%%i\build" "build"
    if exist "%%i\.gradle" rmdir /s /q "%%i\.gradle" ".gradle"
)

set array1=folia server_top forge_1_20 forge_1_19_3 ^
forge_1_19_2 forge_1_18_2 forge_1_16_5 forge_1_12_2 forge_1_7_10 ^
fabric_1_20_2 fabric_1_20 fabric_1_19_3 fabric_1_19_2 fabric_1_18_2 ^
fabric_1_16_5 fabric_1_20_6 neoforge_1_20_4 neoforge_1_20_5

for %%i in (%array1%) do (
    if exist "%%i\src\main\java\com\coloryr\allmusic\server\codec" rmdir "%%i\src\main\java\com\coloryr\allmusic\server\codec" "codec"
)

set array2=folia server_top server

for %%i in (%array2%) do (
    if exist "%%i\src\main\java\com\coloryr\allmusic\server\bstats" rmdir "%%i\src\main\java\com\coloryr\allmusic\server\bstats" "bstats"
)

endlocal