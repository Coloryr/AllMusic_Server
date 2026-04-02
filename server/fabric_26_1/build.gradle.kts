plugins {
    id("net.fabricmc.fabric-loom") version "1.15-SNAPSHOT"
}

java.sourceCompatibility = JavaVersion.VERSION_25
java.targetCompatibility = JavaVersion.VERSION_25

dependencies {
    minecraft("com.mojang:minecraft:26.1")
    implementation("net.fabricmc:fabric-loader:0.18.6")

    implementation("net.fabricmc.fabric-api:fabric-api:0.145.1+26.1")
    implementation("net.kyori:adventure-platform-fabric:6.9.0")

    shadow("net.kyori:adventure-text-minimessage:4.26.1")
    shadow("net.kyori:adventure-api:4.26.1")
    shadow("net.kyori:adventure-text-serializer-gson:4.8.1")
    shadow("net.kyori:adventure-text-serializer-legacy:4.8.1")
    shadow("net.kyori:adventure-text-serializer-plain:4.8.1")
    shadow("net.kyori:adventure-key:4.8.1")
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
        archiveFileName.set("[fabric-26.1]AllMusic_Server-${project.version}.jar")
        destinationDirectory.set(file("${parent!!.projectDir}/target"))

        relocate("net.kyori", "com.coloryr.allmusic.libs.net.kyori")
        relocate("com.google.gson", "com.coloryr.allmusic.libs.com.google.gson")
    }

    build {
        dependsOn(shadowJar)
    }
}
