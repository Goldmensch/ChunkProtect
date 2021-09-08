plugins {
    java
}

group = "de.goldmensch.chunkprotect"
version = "0.1.3"

java.sourceCompatibility = JavaVersion.VERSION_16
java.targetCompatibility = JavaVersion.VERSION_16

repositories {
    mavenCentral()
    maven("https://eldonexus.de/repository/maven-public")
    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.17.1-R0.1-SNAPSHOT")
    implementation("de.goldmensch.smartutils", "core", "1.1.1-DEV")
    implementation("com.fasterxml.jackson.core", "jackson-databind", "2.13.0-rc2")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}