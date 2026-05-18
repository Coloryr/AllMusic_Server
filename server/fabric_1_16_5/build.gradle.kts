plugins {
    id("fabric-loom") version Versions.fabricLoom
}

dependencies {
    minecraft("com.mojang:minecraft:1.16.5")
    mappings(loom.officialMojangMappings())
    modCompileOnly("net.fabricmc:fabric-loader:0.18.5")

    modCompileOnly("net.fabricmc.fabric-api:fabric-api:0.42.0+1.16")

    shadowImplementation("net.kyori:adventure-platform-api:4.0.0")
    shadowImplementation("net.kyori:adventure-text-minimessage:${Versions.minimessage}")
    shadowImplementation("net.kyori:adventure-api:${Versions.minimessage}")
    shadowImplementation("net.kyori:adventure-text-serializer-gson:${Versions.adventure}")
    shadowImplementation("net.kyori:adventure-text-serializer-legacy:${Versions.adventure}")
    shadowImplementation("net.kyori:adventure-text-serializer-plain:${Versions.adventure}")
    shadowImplementation("net.kyori:adventure-key:${Versions.adventure}")

    include("org.xerial:sqlite-jdbc:${Versions.sqlite}")
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
//        relocate("com.google.gson", "com.coloryr.allmusic.libs.com.google.gson")
    }

    remapJar {
        inputFile.set(shadowJar.get().archiveFile)

        archiveFileName.set("[fabric-1.16.5]AllMusic_Server-${project.version}.jar")
        destinationDirectory.set(file("${parent!!.projectDir}/../build"))
    }

    build {
        dependsOn(remapJar)
    }
}
