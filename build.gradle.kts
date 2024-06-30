plugins {
    id("application")
    kotlin("jvm") version "1.9.22"
    id("org.javamodularity.moduleplugin") version "1.8.12"
    id("org.openjfx.javafxplugin") version "0.1.0"
    id("org.beryx.jlink") version "3.0.1"
}

group = "ca.pragmaticcoding"
val archivesBaseName = "Blog"
version = "0.1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val junitVersion = "5.9.2"
println("Hello ${layout.buildDirectory}")


java {
    withSourcesJar()
}

kotlin {
    jvmToolchain(21)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "21"
    }
}

application {
    mainModule = "ca.pragmaticcoding.blog"
    mainClass = "ca.pragmaticcoding.blog.taskprogress.TaskProgress1Kt"
}

javafx {
    version = "21.0.2"
    modules = listOf("javafx.controls", "javafx.fxml")
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:${junitVersion}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${junitVersion}")
    implementation("org.kordamp.ikonli:ikonli-javafx:12.3.1")
}

tasks.test {
    useJUnitPlatform()
}

jlink {
    imageZip = project.file("${layout.buildDirectory}/distributions/app-${javafx.platform.classifier}.zip")
    options = listOf("--strip-debug", "--compress", "2", "--no-header-files", "--no-man-pages")
    launcher {
        name = "app"
    }
}

