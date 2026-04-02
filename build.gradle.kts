import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("com.gradleup.shadow") version "9.4.1" apply false
}

// 统一项目配置
allprojects {
    // 应用插件到子项目
    apply(plugin = "java")
    apply(plugin = "com.gradleup.shadow")

    group = "com.coloryr"
    version = "3.9.2"

    // 设置项目JDK版本
    java.sourceCompatibility = JavaVersion.VERSION_1_8
    java.targetCompatibility = JavaVersion.VERSION_1_8

    repositories {
        mavenCentral()
        maven("https://oss.sonatype.org/content/groups/public/")
        maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
        maven("https://repo.codemc.io/repository/maven-releases/")
        maven("https://repo.codemc.io/repository/maven-snapshots/")
        maven("https://jitpack.io/")
    }

    dependencies {
        compileOnly("io.netty:netty-all:4.1.109.Final")
    }

    tasks.withType<ShadowJar> {
        mutableListOf(
            "org.apache.hc.core5",
            "org.apache.hc.client5",
            "org.slf4j",
        ).forEach { relocate(it, "com.coloryr.allmusic.libs.$it") }
    }

    tasks.named("jar") {
        dependsOn("shadowJar")
    }

    afterEvaluate {
        if (plugins.hasPlugin("com.gtnewhorizons.retrofuturagradle")) return@afterEvaluate

        val rfgObfAttr = Attribute.of("com.gtnewhorizons.retrofuturagradle.obfuscation", String::class.java)
        val rfgDeobfAttr = Attribute.of("rfgDeobfuscatorTransformed", Boolean::class.javaObjectType)
        configurations.all {
            if (!isCanBeConsumed) return@all

            attributes {
                attribute(rfgObfAttr, "mcp")
                attribute(rfgDeobfAttr, true)
            }
        }
    }
}
