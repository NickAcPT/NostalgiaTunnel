plugins {
    kotlin("jvm")
}

group = "io.github.nickacpt.nostalgiatunnel"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.bouncycastle:bcprov-jdk15to18:1.69")
    implementation("com.github.steveice10:opennbt:1.4")
    implementation("net.kyori:adventure-text-serializer-legacy:4.8.1")
}