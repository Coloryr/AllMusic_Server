plugins {
    id("dev.architectury.loom") version Versions.architecturyLoom
//    id("architectury-plugin") version "3.5-SNAPSHOT"
}

java.sourceCompatibility = JavaVersion.VERSION_17
java.targetCompatibility = JavaVersion.VERSION_17

//architectury {
//    platformSetupLoomIde()
//    forge()
//}

repositories {
    maven("https://maven.minecraftforge.net/")
}

dependencies {
    minecraft("com.mojang:minecraft:1.20.1")
    mappings(loom.officialMojangMappings())
    forge("net.minecraftforge:forge:1.20.1-47.4.20")

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

        from(project.layout.buildDirectory.dir("tmp/emptyDirs")) {
            include("META-INF/versions/**")
        }
    }

    shadowJar {
        relocate("net.kyori", "com.coloryr.allmusic.libs.net.kyori")
        relocate("com.google.gson", "com.coloryr.allmusic.libs.com.google.gson")

        doFirst {
            val emptyRoot = project.layout.buildDirectory.dir("tmp/emptyDirs").get().asFile
            emptyRoot.mkdirs()
            val versionsDir = file("$emptyRoot/META-INF/versions")
            versionsDir.mkdirs()
        }
    }

    remapJar {
        inputFile.set(shadowJar.get().archiveFile)
        archiveFileName.set("[forge-1.20.1]AllMusic_Server-${project.version}.jar")
        destinationDirectory.set(file("${parent!!.projectDir}/../build"))
    }

    remapJar {
        inputFile.set(shadowJar.get().archiveFile)
        archiveFileName.set("[forge-1.20.1]AllMusic_Server-${project.version}.jar")
        destinationDirectory.set(file("${parent!!.projectDir}/../build"))

        doLast {
            val jarFile = destinationDirectory.get().file(archiveFileName.get()).asFile
            val tempDir = layout.buildDirectory.dir("tempInject").get().asFile
            tempDir.mkdirs()

            project.copy {
                from(zipTree(jarFile))
                into(tempDir)
            }

            val versionsDir = file("$tempDir/META-INF/versions")
            versionsDir.mkdirs()

            project.ant.withGroovyBuilder {
                "zip"("destfile" to jarFile.absolutePath) {
                    "fileset"("dir" to tempDir.absolutePath)
                }
            }

            tempDir.deleteRecursively()
        }
    }

    build {
        dependsOn(remapJar)
    }
}
