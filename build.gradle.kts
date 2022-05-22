import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.20"
    kotlin("plugin.serialization") version "1.6.10"
    id("org.jetbrains.compose") version "1.2.0-alpha01-dev683"
}

group = "me.user"
version = "1.0"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    implementation(compose.desktop.currentOs)
    @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
    implementation(compose.material3)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.21")

    implementation("br.com.devsrsouza.compose.icons.jetbrains:font-awesome:1.0.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

compose.desktop {
    application {
        mainClass = "de.enricoe.minigames.core.BenutzerverwaltungKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "compose"
            packageVersion = "1.0.0"
        }
    }
}