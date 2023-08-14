rootProject.name = "otusJava"
include ("hw01-gradle")

pluginManagement {
    val dependencyManagement: String by settings
    val johnRengelmanShadow: String by settings

    plugins {
        id("io.spring.dependency-management") version dependencyManagement
        id("com.github.johnrengelman.shadow") version johnRengelmanShadow
    }
}