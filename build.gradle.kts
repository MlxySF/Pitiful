import com.github.jengelman.gradle.plugins.shadow.tasks.ConfigureShadowRelocation
import gg.essential.gradle.util.noServerRunConfigs

plugins {
    java
    kotlin("jvm") version "1.6.21"
    id("com.github.johnrengelman.shadow").version("7.1.2")
    id("gg.essential.loom").version("0.10.0.+")
    id("gg.essential.defaults").version("0.1.15")
}

// Utils ----------------------------------------------------------------
/** Gets property value for property key */
val String.prop: String get() = rootProject.ext.get(this).toString()
// ----------------------------------------------------------------------

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}
kotlin {
    jvmToolchain {
        check(this is JavaToolchainSpec)
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}


group = "de.nxll.pitiful"
version = "mod_version".prop

repositories {
    mavenCentral()
    maven("https://repo.spongepowered.org/maven/")
    maven("https://repo.essential.gg/repository/maven-public")
    maven("https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1")
}

@Suppress
dependencies {
    compileOnly("org.spongepowered:mixin:0.7.11-SNAPSHOT")
    compileOnly("gg.essential:essential-1.8.9-forge:2581")
    implementation("gg.essential:loader-launchwrapper:1.1.3")
    shadow("gg.essential:loader-launchwrapper:1.1.3")
}

// Minecraft --------------------------------------------------------------

loom {
    noServerRunConfigs()

    runs {
        getByName("client") {
            client()

            programArgs(
                "--tweakClass", "gg.essential.loader.stage0.EssentialSetupTweaker",
                "--mixin", "pitiful.mixins.json"
            )
        }

        create("clientDev") {
            inherit(getByName("client"))
            configName = "Minecraft Dev Client"

            property("mixin.debug", "true")
            property("pitiful.dev", "true")
        }
    }

    forge {
        mixinConfigs("pitiful.mixins.json")
    }

    @Suppress("UnstableApiUsage")
    mixin {
        defaultRefmapName.set("pitiful.refmap.json")
    }
}

// Fix resources
sourceSets.main {
    output.setResourcesDir(file("$buildDir/classes/java/main"))
}



tasks {
    // Replace placeholders
    processResources {
        inputs.property("version", "mod_version".prop)

        filesMatching("mcmod.info") {
            expand("version" to project.version)
        }
    }

    compileKotlin {
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs =
                listOf("-opt-in=kotlin.RequiresOptIn", "-Xjvm-default=all", "-Xrelease=8", "-Xbackend-threads=0")
            languageVersion = "1.6"
        }
        kotlinDaemonJvmArguments.set(
            listOf(
                "-Xmx2G",
                "-Dkotlin.enableCacheBuilding=true",
                "-Dkotlin.useParallelTasks=true",
                "-Dkotlin.enableFastIncremental=true",
                //"-Xbackend-threads=0"
            )
        )

    }

    // Disable jar task because remapJar unnecessarily calls it. We replace jar with shadowJar
    jar.get().enabled = false

    remapJar {
        this.input.set(shadowJar.get().archiveFile) // Use shadowJar output
//        archiveClassifier.set("")
    }

    val relocateShadowJar by creating(ConfigureShadowRelocation::class) {
        target = shadowJar.get()
        prefix = "${project.group}.libs"
    }

    shadowJar {
        configurations = listOf(project.configurations.shadow.get())
        archiveClassifier.set("clear")

        manifest.attributes(
            "TweakClass" to "de.nxll.pitiful.libs.gg.essential.loader.stage0.EssentialSetupTweaker"
        )

        dependsOn(relocateShadowJar)
        finalizedBy(remapJar)
    }

}