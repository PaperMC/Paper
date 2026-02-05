plugins {
    java
    id("io.papermc.paperweight.paper-checkstyle")
}

val testData = sourceSets.create("testData")

dependencies {
    implementation("com.puppycrawl.tools:checkstyle:12.1.2")
    implementation("org.jspecify:jspecify:1.0.0")

    testCompileOnly("org.jetbrains:annotations:26.0.2")
    testImplementation(testData.output)

    testImplementation("org.junit.jupiter:junit-jupiter:5.13.3")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testData.implementationConfigurationName("org.jspecify:jspecify:1.0.0")

    checkstyle(project(":paper-checkstyle"))
}

tasks {
    test {
        useJUnitPlatform()
    }
}
