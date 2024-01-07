rootProject.name = "otusJava"
include("hw01-gradle")
include("hw02-generics")
include("hw03-annotations")
include("hw05-auto_logging")
include("hw08-json_handler")

pluginManagement {
    val dependencyManagement: String by settings
    val johnRengelmanShadow: String by settings
    val springframeworkBoot: String by settings
    val protobufVer: String by settings

    plugins {
        id("io.spring.dependency-management") version dependencyManagement
        id("com.github.johnrengelman.shadow") version johnRengelmanShadow
        id("org.springframework.boot") version springframeworkBoot
        id("com.google.protobuf") version protobufVer
    }
}
