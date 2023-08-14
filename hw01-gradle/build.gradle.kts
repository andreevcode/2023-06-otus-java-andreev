plugins {
    id ("java")
    id ("com.github.johnrengelman.shadow")
}

dependencies {
    implementation ("com.google.guava:guava")
}


tasks {
    shadowJar {
        archiveBaseName.set("gradleHelloOtus")
        archiveClassifier.set("0.1")
        archiveVersion.set("")
        manifest {
            attributes(mapOf("Main-Class" to "ru.otus.HelloOtus"))
        }
    }

    build {
        dependsOn(shadowJar)
    }

}
