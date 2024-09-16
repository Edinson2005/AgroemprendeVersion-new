// build.gradle.kts (Raíz del Proyecto)

plugins {
    id("com.android.application") version "8.0.2" apply false
    kotlin("android") version "1.8.10" apply false
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.0.2")
        classpath(kotlin("gradle-plugin", version = "1.8.10"))
    }
}

allprojects {

}
