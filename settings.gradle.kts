rootProject.name = "AllMusic"

include(":codec")
project(":codec").projectDir = file("client/codec")
include(":codec:buffercodec")
project(":codec:buffercodec").projectDir = file("client/codec/buffercodec")

include(":client:core")
project(":client:core").projectDir = file("client/client/core")
include(":client:fabric_1_16_5")
project(":client:fabric_1_16_5").projectDir = file("client/client/fabric_1_16_5")
include(":client:fabric_1_20_1")
project(":client:fabric_1_20_1").projectDir = file("client/client/fabric_1_20_1")
include(":client:fabric_1_21")
project(":client:fabric_1_21").projectDir = file("client/client/fabric_1_21")
include(":client:fabric_1_21_6")
project(":client:fabric_1_21_6").projectDir = file("client/client/fabric_1_21_6")
include(":client:fabric_1_21_11")
project(":client:fabric_1_21_11").projectDir = file("client/client/fabric_1_21_11")
include(":client:fabric_26_1")
project(":client:fabric_26_1").projectDir = file("client/client/fabric_26_1")
include(":client:forge_1_7_10")
project(":client:forge_1_7_10").projectDir = file("client/client/forge_1_7_10")
include(":client:forge_1_12_2")
project(":client:forge_1_12_2").projectDir = file("client/client/forge_1_12_2")
include(":client:forge_1_16_5")
project(":client:forge_1_16_5").projectDir = file("client/client/forge_1_16_5")
include(":client:forge_1_20_1")
project(":client:forge_1_20_1").projectDir = file("client/client/forge_1_20_1")
include(":client:neoforge_1_21")
project(":client:neoforge_1_21").projectDir = file("client/client/neoforge_1_21")
include(":client:neoforge_1_21_6")
project(":client:neoforge_1_21_6").projectDir = file("client/client/neoforge_1_21_6")
include(":client:neoforge_1_21_11")
project(":client:neoforge_1_21_11").projectDir = file("client/client/neoforge_1_21_11")

include(":server")
include(":server:core")

include(":server:fabric_1_16_5")
include(":server:fabric_1_20_1")
include(":server:fabric_1_21")
include(":server:fabric_1_21_6")
include(":server:fabric_1_21_11")
//include(":server:fabric_26_1") // adventure未更新

include(":server:forge_1_7_10")
include(":server:forge_1_12_2")
include(":server:forge_1_16_5")
include(":server:forge_1_20_1")

include(":server:neoforge_1_21")
include(":server:neoforge_1_21_6")
include(":server:neoforge_1_21_11")

include(":server:spigot")
include(":server:paper")
include(":server:folia")
include(":server:paper")
include(":server:velocity")

include(":onejar")

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/")
        maven("https://maven.minecraftforge.net/")
        maven("https://maven.neoforged.net/releases/")
        maven("https://maven.architectury.dev/")
        maven("https://nexus.gtnewhorizons.com/repository/public/")
    }
}
