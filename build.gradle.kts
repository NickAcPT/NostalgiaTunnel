plugins {
    kotlin("jvm") version "1.5.0"
}

group = "io.github.nickacpt"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.github.Steveice10:MCProtocolLib:6499b21e4e")
    implementation("org.bouncycastle:bcprov-jdk15to18:1.69")
    implementation("net.kyori:adventure-text-serializer-legacy:4.8.1")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.11.2")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "15"
    }
}