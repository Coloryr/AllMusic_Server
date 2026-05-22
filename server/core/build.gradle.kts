dependencies {
    shadowImplementation("org.apache.httpcomponents.client5:httpclient5:${Versions.httpclient5}")
    shadowImplementation("org.apache.httpcomponents.core5:httpcore5:${Versions.httpcore5}")
    shadowImplementation("org.apache.httpcomponents.core5:httpcore5-h2:${Versions.httpcore5_h2}")

    implementation("com.google.code.gson:gson:${Versions.gson}")
    compileOnly("net.kyori:adventure-text-minimessage:${Versions.minimessage}")
}
