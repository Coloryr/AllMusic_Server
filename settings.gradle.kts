rootProject.name = "AllMusic"

include(":codec")
include(":codec:buffercodec")

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
