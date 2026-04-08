dependencies {
    shadowImplementation("org.apache.httpcomponents.client5:httpclient5:5.6")
    shadowImplementation("org.apache.httpcomponents.core5:httpcore5:5.4")
    shadowImplementation("org.apache.httpcomponents.core5:httpcore5-h2:5.4")

    compileOnly("com.google.code.gson:gson:2.11.0")
    compileOnly("net.kyori:adventure-text-minimessage:4.26.1")
}
