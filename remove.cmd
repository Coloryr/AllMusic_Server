@echo off

mkdir "build"
mkdir ".gradle"

setlocal enabledelayedexpansion

set array=folia server_top server forge_1_20 forge_1_19_3 ^
forge_1_19_2 forge_1_18_2 forge_1_16_5 forge_1_12_2 ^
fabric_1_20_2 fabric_1_20 fabric_1_19_3 fabric_1_19_2 fabric_1_18_2 ^
fabric_1_16_5 neoforge_1_20_4 neoforge_1_20_5

for %%i in (%array%) do (
    if exist "%%i\src\main\java\com\coloryr\allmusic\server\core" rmdir "%%i\src\main\java\com\coloryr\allmusic\server\core" "core"
    if exist "%%i\build" rmdir /s /q "%%i\build" "build"
    if exist "%%i\.gradle" rmdir /s /q "%%i\.gradle" ".gradle"
)

endlocal