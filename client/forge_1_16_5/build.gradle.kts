plugins {
    id("dev.architectury.loom") version Versions.architecturyLoom
//    id("architectury-plugin") version "3.5-SNAPSHOT"
}

java.sourceCompatibility = JavaVersion.VERSION_17
java.targetCompatibility = JavaVersion.VERSION_17

//architectury {
//    compileOnly()
//    platformSetupLoomIde()
//    forge()
//}

repositories {
    maven("https://maven.minecraftforge.net/")
}

dependencies {
    minecraft("com.mojang:minecraft:1.16.5")
    mappings(loom.officialMojangMappings())
    forge("net.minecraftforge:forge:1.16.5-36.2.39")
}

tasks {
    processResources {
        filesMatching("META-INF/mods.toml") {
            expand("version" to project.version)
        }
    }

    remapJar {
        inputFile.set(shadowJar.get().archiveFile)
        archiveFileName.set("[forge-1.16.5]AllMusic_Client-${project.version}.jar")
        destinationDirectory.set(file("${parent!!.projectDir}/../build"))
    }

    build {
        dependsOn(remapJar)
    }
}
