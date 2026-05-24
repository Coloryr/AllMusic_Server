subprojects {
    val shadowImplementation by configurations.getting
    
    dependencies {
        shadowImplementation(project(":server"))
        shadowImplementation(project(":codec"))
        shadowImplementation(project(":codec:buffercodec"))
    }

    tasks {
        shadowJar {
            configurations = listOf(project.configurations.shadow.get())
        }
    }
}

dependencies {
    shadowImplementation(project(":codec"))
    shadowImplementation(project(":codec:buffercodec"))

    shadowImplementation("org.apache.httpcomponents.client5:httpclient5:${Versions.httpclient5}")
    shadowImplementation("org.apache.httpcomponents.core5:httpcore5:${Versions.httpcore5}")
    shadowImplementation("org.apache.httpcomponents.core5:httpcore5-h2:${Versions.httpcore5_h2}")

    compileOnly("com.google.code.gson:gson:${Versions.gson}")
    compileOnly("net.kyori:adventure-text-minimessage:${Versions.minimessage}")

//    testImplementation("com.google.code.gson:gson:${Versions.gson}")
}

tasks.register("buildServer") {
    group = "build"
    dependsOn(rootProject.subprojects.filter { it.path.startsWith(":server:") }
        .map { it.tasks.named("build") })
}

tasks {
    clean {
        delete("$projectDir/../build")
    }
}
