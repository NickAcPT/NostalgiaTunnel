plugins {
    kotlin("jvm") version "1.5.0"
}

group = "io.github.nickacpt.nostalgiatunnel"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.github.Steveice10:MCProtocolLib:6499b21e4e")
    implementation(project(":NostalgiaPackets"))
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.11.2")
    implementation("net.kyori:adventure-text-serializer-legacy:4.8.1")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "15"
    }
}