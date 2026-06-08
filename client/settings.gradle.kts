rootProject.name = "AllMusic_Client"

include(":codec")

include(":client")
include(":client:fabric_1_16_5")
include(":client:fabric_1_20_1")
include(":client:fabric_1_21")
include(":client:fabric_1_21_6")
include(":client:fabric_1_21_11")
include(":client:fabric_26_1")
//include(":client:forge_1_7_10")
//include(":client:forge_1_12_2")
include(":client:forge_1_16_5")
include(":client:forge_1_20_1")
include(":client:neoforge_1_21")
include(":client:neoforge_1_21_6")
include(":client:neoforge_1_21_11")
include(":client:neoforge_26_1")

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
