subprojects {
    val shadowImplementation: Configuration by configurations.creating
    configurations.shadow.get().extendsFrom(shadowImplementation)
    configurations.implementation.get().extendsFrom(shadowImplementation)

    dependencies {
        if (this@subprojects.name != "core")
            shadowImplementation(project(":server:core"))
        shadowImplementation(project(":codec"))
        shadowImplementation(project(":codec:buffercodec"))

        shadowImplementation("org.xerial:sqlite-jdbc:3.51.1.0")
    }

    tasks {
        shadowJar {
            configurations = listOf(project.configurations.shadow.get())
        }
    }
}

tasks {
    clean {
        delete("$projectDir/target")
    }
}