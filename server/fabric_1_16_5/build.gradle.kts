plugins {
    id("fabric-loom") version Versions.fabricLoom
}

dependencies {
    minecraft("com.mojang:minecraft:1.16.5")
    mappings(loom.officialMojangMappings())
    modCompileOnly("net.fabricmc:fabric-loader:0.18.5")

    modCompileOnly("net.fabricmc.fabric-api:fabric-api:0.42.0+1.16")

    modImplementation(include("net.kyori:examination-api:1.3.0")!!)
    modImplementation(include("net.kyori:examination-string:1.3.0")!!)
    modImplementation(include("net.kyori:adventure-platform-api:4.0.0")!!)
    modImplementation(include("net.kyori:adventure-text-serializer-gson:4.9.3")!!)
    modImplementation(include("net.kyori:adventure-text-serializer-legacy:4.9.3")!!)
    modImplementation(include("net.kyori:adventure-text-serializer-plain:4.9.3")!!)
    modImplementation(include("net.kyori:adventure-text-minimessage:4.26.1")!!)
    modImplementation(include("net.kyori:adventure-api:4.26.1")!!)
    modImplementation(include("net.kyori:adventure-key:4.26.1")!!)
}

tasks {
    processResources {
        filesMatching("fabric.mod.json") {
            expand(
                "version" to project.version
            )
        }
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
