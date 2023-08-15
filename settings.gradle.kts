rootProject.name = "otusJava"
include ("hw01-gradle")
include("hw02-generics")

pluginManagement {
    val dependencyManagement: String by settings
    val johnRengelmanShadow: String by settings
    val springframeworkBoot: String by settings

    plugins {
        id("io.spring.dependency-management") version dependencyManagement
        id("com.github.johnrengelman.shadow") version johnRengelmanShadow
        id("org.springframework.boot") version springframeworkBoot
    }
}
include("hw02-generics")
