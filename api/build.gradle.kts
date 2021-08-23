plugins {
    java
}

group = "de.goldmensch.chunkprotect"
version = "0.1.3"

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://eldonexus.de/repository/maven-public")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.17.1-R0.1-SNAPSHOT")
    implementation("com.jsoniter", "jsoniter", "0.9.19")
    implementation("de.goldmensch.smartutils", "core", "1.1-DEV")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}