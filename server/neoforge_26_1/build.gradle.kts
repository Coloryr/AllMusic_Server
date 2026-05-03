plugins {
    id("dev.architectury.loom-no-remap") version "1.14-SNAPSHOT"
    id("architectury-plugin") version "3.5-SNAPSHOT"
}

java.sourceCompatibility = JavaVersion.VERSION_25
java.targetCompatibility = JavaVersion.VERSION_25

architectury {
  platformSetupLoomIde()
  neoForge()
}

repositories {
    maven("https://maven.neoforged.net/releases/")
}

dependencies {
    minecraft("com.mojang:minecraft:26.1")
    neoForge("net.neoforged:neoforge:26.1.0.19-beta")

    modImplementation("net.kyori:adventure-platform-neoforge:6.9.0")

    shadowImplementation("net.kyori:adventure-text-minimessage:4.26.1")
    shadowImplementation("net.kyori:adventure-api:4.26.1")
    shadowImplementation("net.kyori:adventure-text-serializer-gson:4.8.1")
    shadowImplementation("net.kyori:adventure-text-serializer-legacy:4.8.1")
    shadowImplementation("net.kyori:adventure-text-serializer-plain:4.8.1")
    shadowImplementation("net.kyori:adventure-key:4.8.1")
}

tasks {
    processResources {
        filesMatching("META-INF/mods.toml") {
            expand("version" to project.version)
        }
    }

    shadowJar {
        relocate("net.kyori", "com.coloryr.allmusic.libs.net.kyori")
        relocate("com.google.gson", "com.coloryr.allmusic.libs.com.google.gson")

        inputFile.set(shadowJar.get().archiveFile)
        archiveFileName.set("[neoforge-26.1]AllMusic_Server-${project.version}.jar")
        destinationDirectory.set(file("${parent!!.projectDir}/target"))
    }

//    remapJar {
//        inputFile.set(shadowJar.get().archiveFile)
//        archiveFileName.set("[neoforge-1.21.11]AllMusic_Server-${project.version}.jar")
//        destinationDirectory.set(file("${parent!!.projectDir}/target"))
//    }

    build {
        dependsOn(shadowJar)
    }
}
