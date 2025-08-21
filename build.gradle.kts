
plugins {
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "dev.demo"
version = "1.0.0"

repositories { mavenCentral() }

dependencies {
    implementation(platform("software.amazon.awssdk:bom:2.25.60"))
    implementation("software.amazon.awssdk:secretsmanager")
    implementation("software.amazon.awssdk:ssm")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

application {
    mainClass.set("dev.demo.App")
}
