import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val invoker: Configuration by configurations.creating


plugins {
    kotlin("jvm") version "1.7.21"
    kotlin("plugin.serialization") version "1.8.0"
    id("com.github.johnrengelman.shadow") version "4.0.4"
    application
    java
}


repositories {
    mavenCentral()
}


dependencies {
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.0")
    implementation("com.google.cloud:google-cloudevent-types:0.3.0")
    implementation("com.google.guava:guava:26.0-jre")
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.4")
    implementation("org.slf4j:slf4j-api:2.0.6")
    implementation("org.slf4j:slf4j-simple:2.0.6")

    implementation("com.google.cloud.functions:functions-framework-api:1.0.4")
    invoker("com.google.cloud.functions.invoker:java-function-invoker:1.1.0")


    testImplementation("com.google.guava:guava-testlib:31.1-jre")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:1.8.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.8.0")
    testImplementation ("com.google.truth:truth:1.1.3")
    testImplementation ("org.mockito:mockito-core:4.11.0")


}

application {
    mainClass.set("com.example.httpdeploy.AppKt")
    mainClassName = "com.example.httpdeploy.AppKt"
}

task<JavaExec>("runFunction") {
    mainClass.set("com.google.cloud.functions.invoker.runner.Invoker")
    classpath(invoker)
    inputs.files(configurations.runtimeClasspath, sourceSets["main"].output)
    args(
        "--target",
        project.findProperty("runFunction.target") ?: "com.example.httpdeploy.App",
        "--port",
        project.findProperty("runFunction.port") ?: 8080
    )
    doFirst {
        args("--classpath", files(configurations.runtimeClasspath, sourceSets["main"].output).asPath)
    }
}


tasks {
    named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
        archiveBaseName.set("shadow")
        mergeServiceFiles()
        manifest {
            attributes(mapOf("Main-Class" to "com.example.httpdeploy.App"))
        }
    }
}


tasks {
    build {
        dependsOn(shadowJar)
    }
}


tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}