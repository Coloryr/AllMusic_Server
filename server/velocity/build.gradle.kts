repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://nexus.velocitypowered.com/repository/maven-public/")
}

dependencies {
    compileOnly("com.velocitypowered:velocity-api:3.0.0")
    annotationProcessor("com.velocitypowered:velocity-api:3.0.0")
}

tasks {
    processResources {
        filesMatching("velocity-plugin.json") {
            expand(
                "version" to project.version
            )
        }
    }

    shadowJar {
        archiveFileName.set("[velocity]AllMusic_Server-${project.version}.jar")
        destinationDirectory.set(file("${parent!!.projectDir}/target"))
    }
}