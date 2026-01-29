plugins {
    id("java")
}

group = "org.astral"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(files("/libs/HytaleServer.jar"))
    compileOnly(files("/libs/proxy-1.0.2.jar"))
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}