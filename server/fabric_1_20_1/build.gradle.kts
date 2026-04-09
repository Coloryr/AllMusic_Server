plugins {
    id("fabric-loom") version "1.15-SNAPSHOT"
}

java.sourceCompatibility = JavaVersion.VERSION_17
java.targetCompatibility = JavaVersion.VERSION_17

dependencies {
    minecraft("com.mojang:minecraft:1.20.1")
    mappings(loom.officialMojangMappings())
    modCompileOnly("net.fabricmc:fabric-loader:0.16.10")

    modCompileOnly("net.fabricmc.fabric-api:fabric-api:0.92.3+1.20.1")
    modCompileOnly("net.kyori:adventure-platform-fabric:5.9.0")

    shadowImplementation("net.kyori:adventure-text-minimessage:4.26.1")
    shadowImplementation("net.kyori:adventure-api:4.26.1")
    shadowImplementation("net.kyori:adventure-text-serializer-gson:4.8.1")
    shadowImplementation("net.kyori:adventure-text-serializer-legacy:4.8.1")
    shadowImplementation("net.kyori:adventure-text-serializer-plain:4.8.1")
    shadowImplementation("net.kyori:adventure-key:4.8.1")
}

tasks {
    processResources {
        filesMatching("fabric.mod.json") {
            expand(
                "version" to project.version
            )
        }
    }

    shadowJar {
        relocate("net.kyori", "com.coloryr.allmusic.libs.net.kyori")
        relocate("com.google.gson", "com.coloryr.allmusic.libs.com.google.gson")
    }

    remapJar {
        inputFile.set(shadowJar.get().archiveFile)

        archiveFileName.set("[fabric-1.20.1]AllMusic_Server-${project.version}.jar")
        destinationDirectory.set(file("${parent!!.projectDir}/target"))
    }

    build {
        dependsOn(remapJar)
    }
}
