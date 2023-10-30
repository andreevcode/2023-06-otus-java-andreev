plugins {
    id ("java")
}

dependencies {
    implementation ("org.assertj:assertj-core")
    implementation ("ch.qos.logback:logback-classic")
    testImplementation ("org.junit.jupiter:junit-jupiter-api")
    testImplementation ("org.junit.jupiter:junit-jupiter-engine")
}