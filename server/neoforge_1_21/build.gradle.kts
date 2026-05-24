plugins {
    id("dev.architectury.loom") version Versions.architecturyLoom
//    id("architectury-plugin") version "3.5-SNAPSHOT"
}

java.sourceCompatibility = JavaVersion.VERSION_21
java.targetCompatibility = JavaVersion.VERSION_21

//architectury {
//  platformSetupLoomIde()
//  neoForge()
//}

repositories {
    maven("https://maven.neoforged.net/releases/")
}

dependencies {
    minecraft("com.mojang:minecraft:1.21")
    mappings(loom.officialMojangMappings())
    neoForge("net.neoforged:neoforge:21.0.167")

    modImplementation("net.kyori:adventure-platform-neoforge:6.8.0")

    implementation(include("net.kyori:adventure-platform-neoforge:6.0.0")!!)
}

tasks {
    processResources {
        filesMatching("META-INF/mods.toml") {
            expand("version" to project.version)
        }
    }

    remapJar {
        inputFile.set(shadowJar.get().archiveFile)
        archiveFileName.set("[neoforge-1.21]AllMusic_Server-${project.version}.jar")
        destinationDirectory.set(file("${parent!!.projectDir}/../build"))
    }

    build {
        dependsOn(remapJar)
    }
}
