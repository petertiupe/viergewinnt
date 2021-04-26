plugins {
    id("dev.fritz2.fritz2-gradle") version "0.9.2"
}

group "de.tiupe"
version "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    jvm()
    js(IR) {
        browser()
    }.binaries.executable()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("dev.fritz2:core:0.9.2")
                // see https://components.fritz2.dev/
                implementation("dev.fritz2:components:0.9.2")
            }
        }
        val jvmMain by getting {
            dependencies {
            }
        }
        val jsMain by getting {
            dependencies {
            }
        }
    }
}
