group = "${parent?.group}"
version = "${parent?.version}"

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    implementation(project(":mcsf-plugin-common"))
    compileOnly("com.velocitypowered:velocity-api:3.4.0-SNAPSHOT")
    annotationProcessor("com.velocitypowered:velocity-api:3.4.0-SNAPSHOT")
}

tasks.compileJava {
    sourceCompatibility = "17"
    targetCompatibility = "17"
}