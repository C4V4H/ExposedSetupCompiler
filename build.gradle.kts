plugins {
    kotlin("jvm") version "1.9.22"
}

group = "org.strong"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
dependencies {
    val exposedVersion = "0.46.0"

    testImplementation("org.jetbrains.kotlin:kotlin-test")

    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposedVersion")
    implementation("mysql:mysql-connector-java:8.0.28")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.5.31")
    implementation("com.google.code.gson:gson:2.7")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.16.0")
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("org.ehcache:ehcache:3.10.8")

    implementation("io.ktor:ktor-server-freemarker:2.2.2")
    implementation("io.ktor:ktor-client-core:2.0.2")
    implementation("io.ktor:ktor-client-cio:2.0.2")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("org.projectlombok:lombok:1.18.24")


}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(19)
}