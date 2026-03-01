
plugins {
    id("com.gtnewhorizons.gtnhconvention")
}

fun getVersionFromFile(): String {
    val versionFile = file("../version")
    return if (versionFile.exists()) {
        versionFile.readText().trim()
    } else {
        throw GradleException("Version file not found: ${versionFile.absolutePath}")
    }
}

version = getVersionFromFile()
