plugins {
    java
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "de.goldmensch.chunkprotect"
version = "0.1.3"

java.sourceCompatibility = JavaVersion.VERSION_16
java.targetCompatibility = JavaVersion.VERSION_16

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://eldonexus.de/repository/maven-public")
    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    implementation(project(":api"))
    compileOnly("io.papermc.paper:paper-api:1.17.1-R0.1-SNAPSHOT")
    implementation("de.goldmensch", "SmartCommandDispatcher", "1.0.5-DEV")
    implementation("de.goldmensch.smartutils", "core", "1.1-DEV")
    implementation("de.goldmensch.smartutils", "minimessage-adapter", "1.1-DEV")
    implementation("com.fasterxml.jackson.dataformat", "jackson-dataformat-yaml", "2.11.1")
    implementation("com.jsoniter", "jsoniter", "0.9.19")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("com.google.code.gson:gson:2.8.7")
    testImplementation("com.destroystokyo.paper:paper-api:1.16.5-R0.1-SNAPSHOT")
}

tasks {
    getByName<Test>("test") {
        useJUnitPlatform()
    }

    processResources {
        from(sourceSets.main.get().resources.srcDirs) {
            filesMatching("plugin.yml") {
                expand("version" to version)
            }
        }
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }

    shadowJar {
        archiveBaseName.set("ChunkProtect")
        relocateAll("de.goldmensch.smartutils.core",
                "de.goldmensch.commanddispatcher",
                "de.goldmensch.smartutils.minimessage-adapter",
                "com.fasterxml.jackson",
                "com.jsoniter",
                "org.yaml.snakeyaml"
        )
        minimize {
            exclude(dependency("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.11.1"))
        }
    }
}

fun com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar.relocateAll(vararg names: String) {
    names.forEach { name ->
        relocate(name, "${rootProject.group}.${rootProject.name}.libs.$name")
    }
}