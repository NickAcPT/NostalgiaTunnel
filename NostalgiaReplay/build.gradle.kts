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
    implementation(project(":NostalgiaPackets"))
}