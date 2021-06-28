plugins {
    kotlin("jvm") version "1.4.32"
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
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "15"
    }
}