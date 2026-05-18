subprojects {
    val shadowImplementation by configurations.getting
    
    dependencies {
        if (this@subprojects.name != "core")
            shadowImplementation(project(":server:core"))
        shadowImplementation(project(":codec"))
        shadowImplementation(project(":codec:buffercodec"))
    }

    tasks {
        shadowJar {
            configurations = listOf(project.configurations.shadow.get())
        }
    }
}

tasks {
    clean {
        delete("$projectDir/../build")
    }
}
