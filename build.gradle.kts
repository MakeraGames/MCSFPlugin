plugins {
    id("java")
    id("com.gradleup.shadow") version "8.3.6"
}

group = "gg.makera"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "com.gradleup.shadow")

    repositories {
        mavenCentral()
        maven {
            name = "ultimis-makera"
            url = uri("https://repo.ultimismc.com/repository/makera")
        }
    }

    dependencies {
        implementation("gg.makera:noteblock-api:0.1.0-SNAPSHOT")
        implementation("org.yaml:snakeyaml:2.4")
        compileOnly("org.projectlombok:lombok:1.18.38")
        annotationProcessor("org.projectlombok:lombok:1.18.38")
    }

}